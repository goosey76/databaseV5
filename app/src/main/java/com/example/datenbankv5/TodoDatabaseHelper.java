package com.example.datenbankv5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_NAME = "todos";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRIORITY + " INTEGER)";
        db.execSQL(createTable);
        Log.d("DatabaseHelper", "Tabelle erstellt: " + TABLE_NAME);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS tasks");
            //create the table
            onCreate(db);
            Log.d("DatabaseHelper", "Database upgraded from version " + oldVersion + " to " + newVersion);
        }
    }

    public void insertTask(String task, String description, Priority priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK, task);
            values.put(COLUMN_DESCRIPTION, description);
            values.put(COLUMN_PRIORITY, priority.getValue());

            long result = db.insert(TABLE_NAME, null, values);

            if (result == -1) {
                Log.e("DatabaseHelper", "Failed to insert task into the database.");
            } else {
                Log.d("DatabaseHelper", "Task inserted successfully with ID: " + result);
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
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract values from the database
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    int priorityValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY));

                    // Convert priority integer value to Priority enum
                    Priority priority = Priority.fromValue(priorityValue);

                    // Create a Task object and add it to the list
                    Task task = new Task(id, taskName, description, priority);
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


}
