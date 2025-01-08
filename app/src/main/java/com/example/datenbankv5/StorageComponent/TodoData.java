package com.example.datenbankv5.StorageComponent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

public class TodoData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 9;
    private static final String TABLE_NAME = "todos";
    private final Context context;
    private String databasePath;

    // Die Tabelle
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";

    public TodoData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

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

    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, null, null);
            Log.d("DatabaseHelper", "All tasks deleted");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting tasks", e);
        } finally {
            db.close();
        }
    }

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
                    //TODO behandelung, wenn keine UUID vorhaben ist
                }
            }
        } finally {
            db.close();
        }
    }

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
