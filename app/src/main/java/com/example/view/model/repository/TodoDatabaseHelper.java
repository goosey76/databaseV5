package com.example.view.model.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.view.model.todo.Category;
import com.example.view.model.todo.Priority;
import com.example.view.model.todo.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 10;

    // Table names
    private static final String TABLE_TODOS = "todos";
    private static final String TABLE_EVENTS = "events";

    // Columns for the Todo table
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_EVENT_ID = "event_id";

    // Columns for the Event table
    private static final String COLUMN_EVENT_TITLE = "title";
    private static final String COLUMN_EVENT_START_TIME = "start_time";
    private static final String COLUMN_EVENT_END_TIME = "end_time";
    private static final String COLUMN_EVENT_LOCATION = "location";
    private static final String COLUMN_EVENT_TRAVEL_TIME = "travel_time";
    private static final String COLUMN_EVENT_NOTIFICATION = "notification";
    private static final String COLUMN_EVENT_REPETITION = "repetition";
    private static final String COLUMN_EVENT_TODO_ID = "todo_id";

    private final Context context;

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void exportDatabase() {
        try {
            File currentDB = context.getDatabasePath(DATABASE_NAME);
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
        // Create the Todo table
        String createTodoTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TODOS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TASK + " TEXT NOT NULL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRIORITY + " INTEGER, " +
                COLUMN_EVENT_ID + " TEXT" +
                ")";
        db.execSQL(createTodoTable);

        // Create the Event table
        String createEventTable = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_EVENT_TITLE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_EVENT_START_TIME + " TEXT, " +
                COLUMN_EVENT_END_TIME + " TEXT, " +
                COLUMN_EVENT_LOCATION + " TEXT, " +
                COLUMN_EVENT_TRAVEL_TIME + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_EVENT_NOTIFICATION + " INTEGER, " +
                COLUMN_EVENT_REPETITION + " TEXT, " +
                COLUMN_EVENT_TODO_ID + " TEXT)";
        db.execSQL(createEventTable);

        Log.d("DatabaseHelper", "Tables created successfully.");
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_TODOS, null, null);
            db.delete(TABLE_EVENTS, null, null);
            Log.d("DatabaseHelper", "All tasks and events deleted.");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting tasks and events", e);
        } finally {
            db.close();
        }
    }

    public String getNextId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_TODOS + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);

            if (cursor != null && cursor.moveToFirst()) {
                String lastId = cursor.getString(0);
                int nextNumericId = Integer.parseInt(lastId) + 1;
                return String.format("%08d", nextNumericId);
            }
            return String.format("%08d", 100);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error generating next ID", e);
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public void insertTaskWithEvent(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Create a unique event ID
            String eventId = UUID.randomUUID().toString();

            // Insert into Event table
            ContentValues eventValues = new ContentValues();
            eventValues.put("_id", eventId);
            eventValues.put("title", task.getTask());
            eventValues.put("category", task.getCategory().getName());
            eventValues.put("start_time", LocalDateTime.now().toString());
            eventValues.put("end_time", LocalDateTime.now().plusHours(1).toString());
            eventValues.put("location", task.getCategory().getLocation());
            eventValues.put("travel_time", task.getCategory().getTravelTime());
            eventValues.put("description", task.getDescription());
            eventValues.put("notification", 1);
            eventValues.put("repetition", "Keine");
            eventValues.put("todo_id", task.getId());

            long eventInsertResult = db.insert(EVENT_TABLE_NAME, null, eventValues);
            if (eventInsertResult == -1) {
                Log.e("DatabaseHelper", "Failed to insert event");
            } else {
                Log.d("DatabaseHelper", "Event inserted with ID: " + eventId);
            }

            // Insert into Todo table
            ContentValues todoValues = new ContentValues();
            todoValues.put("_id", task.getId());
            todoValues.put("task", task.getTask());
            todoValues.put("category", task.getCategory().getName());
            todoValues.put("description", task.getDescription());
            todoValues.put("priority", task.getPriority().getValue());
            todoValues.put("event_id", eventId);

            long todoInsertResult = db.insert(TABLE_NAME, null, todoValues);
            if (todoInsertResult == -1) {
                Log.e("DatabaseHelper", "Failed to insert task");
            } else {
                Log.d("DatabaseHelper", "Task inserted with ID: " + task.getId());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting Task and Event.", e);
        } finally {
            db.close();
        }
    }


    public List<Task> getAllTasksSortedByPriority() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TODOS + " ORDER BY " + COLUMN_PRIORITY + " ASC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK));
                    String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    int priorityValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY));

                    Category category = Category.fromName(categoryName);
                    Priority priority = Priority.fromValue(priorityValue);

                    taskList.add(new Task(id, taskName, category, description, priority));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching tasks", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return taskList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            onCreate(db);
        }
    }
}
