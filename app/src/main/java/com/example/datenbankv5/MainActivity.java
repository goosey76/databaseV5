package com.example.datenbankv5;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // RecyclerViews for different priority categories
    private RecyclerView recyclerViewUrgentImportant;
    private RecyclerView recyclerViewNotUrgentImportant;
    private RecyclerView recyclerViewUrgentNotImportant;
    private RecyclerView recyclerViewNotUrgentNotImportant;

    // TaskAdapter instances
    private TaskAdapter adapterUrgentImportant;
    private TaskAdapter adapterNotUrgentImportant;
    private TaskAdapter adapterUrgentNotImportant;
    private TaskAdapter adapterNotUrgentNotImportant;

    // Lists to hold tasks for each priority category
    private List<Task> listUrgentImportant = new ArrayList<>();
    private List<Task> listNotUrgentImportant = new ArrayList<>();
    private List<Task> listUrgentNotImportant = new ArrayList<>();
    private List<Task> listNotUrgentNotImportant = new ArrayList<>();

    private TodoDatabaseHelper dbHelper; // Database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TodoDatabaseHelper(this);

        // Initialize RecyclerViews
        recyclerViewUrgentImportant = findViewById(R.id.recyclerViewUrgentImportant);
        recyclerViewNotUrgentImportant = findViewById(R.id.recyclerViewNotUrgentImportant);
        recyclerViewUrgentNotImportant = findViewById(R.id.recyclerViewUrgentNotImportant);
        recyclerViewNotUrgentNotImportant = findViewById(R.id.recyclerViewNotUrgentNotImportant);

        // Set up RecyclerViews and Adapters
        setupRecyclerView(recyclerViewUrgentImportant, listUrgentImportant);
        setupRecyclerView(recyclerViewNotUrgentImportant, listNotUrgentImportant);
        setupRecyclerView(recyclerViewUrgentNotImportant, listUrgentNotImportant);
        setupRecyclerView(recyclerViewNotUrgentNotImportant, listNotUrgentNotImportant);

        // FloatingActionButton to add new tasks
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload tasks when returning to MainActivity
        loadTasks();
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Task> taskList) {
        TaskAdapter adapter = new TaskAdapter(this, taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Assign the adapter to its respective field
        if (recyclerView == recyclerViewUrgentImportant) adapterUrgentImportant = adapter;
        if (recyclerView == recyclerViewNotUrgentImportant) adapterNotUrgentImportant = adapter;
        if (recyclerView == recyclerViewUrgentNotImportant) adapterUrgentNotImportant = adapter;
        if (recyclerView == recyclerViewNotUrgentNotImportant)
            adapterNotUrgentNotImportant = adapter;
    }

    private void loadTasks() {
        // Clear lists to avoid duplicate entries
        listUrgentImportant.clear();
        listNotUrgentImportant.clear();
        listUrgentNotImportant.clear();
        listNotUrgentNotImportant.clear();

        // Fetch tasks from the database
        List<Task> allTasks = dbHelper.getAllTasks();

        // Add tasks to appropriate lists based on priority
        for (Task task : allTasks) {
            switch (task.getPriority()) {
                case URGENT_IMPORTANT:
                    listUrgentImportant.add(task);
                    break;
                case NOT_URGENT_IMPORTANT:
                    listNotUrgentImportant.add(task);
                    break;
                case URGENT_NOT_IMPORTANT:
                    listUrgentNotImportant.add(task);
                    break;
                case NOT_URGENT_NOT_IMPORTANT:
                    listNotUrgentNotImportant.add(task);
                    break;
            }
        }

        // Notify the adapters to refresh the RecyclerViews
        notifyAdapters();
    }

    private void notifyAdapters() {
        if (adapterUrgentImportant != null) adapterUrgentImportant.notifyDataSetChanged();
        if (adapterNotUrgentImportant != null) adapterNotUrgentImportant.notifyDataSetChanged();
        if (adapterUrgentNotImportant != null) adapterUrgentNotImportant.notifyDataSetChanged();
        if (adapterNotUrgentNotImportant != null)
            adapterNotUrgentNotImportant.notifyDataSetChanged();
    }
}