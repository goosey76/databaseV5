package com.example.datenbankv5.StorageComponent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.datenbankv5.CloudComponent.MissingUUIDException;
import com.example.datenbankv5.ToDoComponent.core.Category;
import com.example.datenbankv5.CloudComponent.RestApiService;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import com.example.datenbankv5.ToDoComponent.core.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


/**
 * Diese Klasse bietet eine SQLite-Datenbank-Implementierung zur Verwaltung von Aufgaben.
 * Sie stellt CRUD-Operationen, die Möglichkeit zum Export der Datenbank und die Integration mit Cloud-Diensten bereit.
 */
public class TodoData extends SQLiteOpenHelper {

    /**
     * Name der Datenbankdatei.
     */
    private static final String DATABASE_NAME = "tasks.db";

    /**
     * Version der Datenbank.
     */
    private static final int DATABASE_VERSION = 9;

    /**
     * Name der Tabelle zur Speicherung von Aufgaben.
     */
    private static final String TABLE_NAME = "todos";

    // Spaltennamen der Tabelle
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";

    private final Context context;
    private String databasePath;

    /**
     * Konstruktor für TodoData.
     *
     * @param context Der Anwendungskontext.
     */
    public TodoData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }


    /**
     * Exportiert die Datenbank in eine externe Datei.
     */
    public void exportDatabase() {
        try {
            File currentDB = new File(databasePath);
            File backupDB = new File(context.getExternalFilesDir(null), DATABASE_NAME);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.d("DatabaseHelper", "Database exported to: " + backupDB.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error exporting database", e);
        }
    }

    /**
     * Erstellt die Datenbanktabelle, falls sie nicht existiert.
     *
     * @param db Die SQLite-Datenbankinstanz.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TASK + " TEXT NOT NULL, " +
                COLUMN_CATEGORY + " TEXT, " + // Neue Kategorie-Spalte
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRIORITY + " INTEGER)";
        db.execSQL(createTable);
        Log.d("DatabaseHelper", "Tabelle erstellt: " + TABLE_NAME);
    }


    /**
     * Löscht alle Aufgaben aus der Datenbank und entfernt sie auch aus der Cloud.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> columnIds = new ArrayList<>();

        try {
            // Abfrage, um alle COLUMN_IDs zu erhalten
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID}, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    columnIds.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                }
                cursor.close();
            }

            // Logge die COLUMN_IDs (optional, zu Debug-Zwecken)
            Log.d("DatabaseHelper", "Saved COLUMN_IDs: " + columnIds);

            // Lösche alle Einträge in der Tabelle
            db.delete(TABLE_NAME, null, null);
            Log.d("DatabaseHelper", "All tasks deleted");

            new Thread(() -> {
                for (int a = 0; a < columnIds.size(); a++) {
                    // Lösche alle Einträge in der Cloud
                    try {
                        RestApiService.deleteToDoInCloud(context, columnIds.get(a));
                    } catch (MissingUUIDException e) {
                        Log.e("DatabaseHelper", "Error deleting task in cloud", e);
                    }

                    // Timeout von 1 Sekunde
                    try {
                        Thread.sleep(3000); // 1000 Millisekunden = 1 Sekunde
                    } catch (InterruptedException e) {
                        Log.e("Timeout", "Thread wurde unterbrochen", e);
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting tasks", e);
        } finally {
            db.close();
        }
    }


    /**
     * Generiert die nächste eindeutige Aufgaben-ID basierend auf der aktuellen maximalen ID.
     *
     * @return Die nächste eindeutige ID als String.
     */
    public String getNextId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                    " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);

            if (cursor != null && cursor.moveToFirst()) {
                String lastId = cursor.getString(0);
                int nextNumericId = Integer.parseInt(lastId) + 1;
                return String.format("%08d", nextNumericId);
            }
            return String.format("%08d", 100); // Start with 100 if no entries exist
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    /**
     * Fügt eine Aufgabe in die Datenbank ein und synchronisiert sie mit der Cloud.
     *
     * @param taskToStore Die einzufügende Aufgabe.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertTask(Task taskToStore) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, taskToStore.getId());
            values.put(COLUMN_TASK, taskToStore.getTask());
            values.put(COLUMN_CATEGORY, taskToStore.getCategory().getName());
            values.put(COLUMN_DESCRIPTION, taskToStore.getDescription());
            values.put(COLUMN_PRIORITY, taskToStore.getPriority().getValue());

            long result = db.insert(TABLE_NAME, null, values);
            if (result == -1) {
                Log.e("DatabaseHelper", "Failed to insert task");
            } else {
                Log.d("DatabaseHelper", "Task inserted successfully");

                try {
                    //Speichert in Cloud ab
                    RestApiService.sendNewToDo(context, taskToStore);
                } catch (MissingUUIDException e) {
                    Log.e("DatabaseHelper", "Error saving task in cloud", e);
                }
            }
        } finally {
            db.close();
        }
    }

    /**
     * Ruft alle Aufgaben aus der Datenbank ab.
     *
     * @return Eine Liste der in der Datenbank gespeicherten Aufgaben.
     */
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>(); // List to hold tasks
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to select all tasks from the database
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            Log.d("DatabaseHelper", "Number of tasks in database: " + (cursor != null ? cursor.getCount() : 0));

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract values from the database
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK));
                    String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    int priorityValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY));

                    Log.d("DatabaseHelper", "Loading task: " + taskName + ", Category: " + categoryName);

                    // Convert category name to enum
                    Category category = null;
                    for (Category c : Category.values()) {
                        if (c.getName().equals(categoryName)) {
                            category = c;
                            break;
                        }
                    }

                    // Convert priority integer value to Priority enum
                    Priority priority = Priority.fromValue(priorityValue);

                    // Create a Task object and add it to the list
                    Task task = new Task(id, taskName, category, description, priority);
                    taskList.add(task);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching tasks", e);
        } finally {
            // Close cursor and database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return taskList; // Return the list of tasks
    }

    /**
     * Aktualisiert die Datenbank auf eine neue Version, indem die vorhandene Tabelle gelöscht und neu erstellt wird.
     *
     * @param db         Die SQLite-Datenbankinstanz.
     * @param oldVersion Die alte Versionsnummer der Datenbank.
     * @param newVersion Die neue Versionsnummer der Datenbank.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Log.d("DatabaseHelper", "Database upgraded from version " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            //create the table
            onCreate(db);
        }
    }

}
