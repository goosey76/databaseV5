package com.example.datenbankv5;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;  // Add this import


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datenbankv5.CloudComponent.RestApiService;
import com.example.datenbankv5.CloudComponent.TrustAllCertificates;
import com.example.datenbankv5.StorageComponent.TodoData;
import com.example.datenbankv5.ToDoComponent.AddTaskActivity;
import com.example.datenbankv5.ToDoComponent.core.Task;
import com.example.datenbankv5.ToDoComponent.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTodos;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private TodoData dbHelper; // Database helper


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestApiService.deleteUuid(this);
        RestApiService.generateUuid(this);


        // Initalisiere RecyclerView
        recyclerViewTodos = findViewById(R.id.recyclerViewTodos);
        recyclerViewTodos.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        recyclerViewTodos.setAdapter(taskAdapter);

        // initialize database helper
        dbHelper = new TodoData(this);

        // Exportiere die Datenbank nach jedem App-Start
        if (checkPermissions()) {
            dbHelper.exportDatabase();
        }

        // FloatingActionButton to add new tasks
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Delete all buttons
        Button buttonDeleteAll = findViewById(R.id.buttonDeleteAll);
        buttonDeleteAll.setOnClickListener(v -> deleteAllTasks());

    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return false;
        }
        return true;
    }

    private void loadTasks() {
        try {
            Log.d("MainActivity", "Starting to load tasks");
            taskList.clear();
            List<Task> tasks = dbHelper.getAllTasks();
            Log.d("MainActivity", "Loaded " + tasks.size() + " tasks");
            taskList.addAll(tasks);

            if (taskAdapter != null) {
                taskAdapter.notifyDataSetChanged();
                Log.d("MainActivity", "Notified adapter of data change");
            } else {
                Log.e("MainActivity", "TaskAdapter is null!");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error loading tasks", e);
            e.printStackTrace();
            Toast.makeText(this, "Fehler beim Laden der Aufgaben: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteAllTasks() {
        // Add confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Alle Einträge löschen")
                .setMessage("Möchten Sie wirklich alle Einträge löschen?")
                .setPositiveButton("Ja", (dialog, which) -> {
                    dbHelper.deleteAllTasks(); // You'll need to add this method
                    loadTasks();
                })
                .setNegativeButton("Nein", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload tasks when returning to MainActivity
        loadTasks();
    }

}
