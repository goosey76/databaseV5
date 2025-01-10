package com.example.datenbankv5.ToDoComponent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datenbankv5.R;
import com.example.datenbankv5.StorageComponent.TodoData;
import com.example.datenbankv5.ToDoComponent.core.Category;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import com.example.datenbankv5.ToDoComponent.core.Task;

public class AddTaskActivity extends AppCompatActivity {

    private TodoData dbHelper; // Database Helper
    private int selectedPriority = -1; // Default invalid priority
    private Category selectedCategory; // Default invalid category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new TodoData(this);

        // Initialize input fields and button
        EditText editTextTask = findViewById(R.id.editTextTask);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        Button buttonSaveTask = findViewById(R.id.buttonSaveTask);
        Spinner spinnerPriority = findViewById(R.id.spinner_priority);
        Spinner spinnerCategory = findViewById(R.id.spinner_category);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle category selection
                selectedCategory = Category.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null; // Reset to no selection
            }
        });


        // Set spinner listener
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority = position + 1; // Store priority value as int
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPriority = -1; // Reset to invalid priority
            }
        });

        // Set button click listener
        buttonSaveTask.setOnClickListener(v -> {
            // Read input values
            String task = editTextTask.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            // Validate input
            if (task.isEmpty()) {
                Toast.makeText(this, "Task name is required", Toast.LENGTH_SHORT).show();
                editTextTask.requestFocus();
                return;
            }

            if (selectedPriority == -1) {
                Toast.makeText(this, "Please select a priority", Toast.LENGTH_SHORT).show();
                spinnerPriority.requestFocus();
                return;
            }

            if (selectedCategory == null) {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
                spinnerCategory.requestFocus();
                return;
            }

            // Convert selectedPriority to Priority enum
            Priority priority;
            try {
            priority = Priority.fromValue(selectedPriority);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid priority selected", Toast.LENGTH_SHORT).show();
                return;
            }

            // Unique String
            String formatId = dbHelper.getNextId();

            // Create Task Object
            Task createdTask = new Task(formatId, task, selectedCategory, description, priority);

            // Insert task into the database
            dbHelper.insertTask(createdTask);

            // Finish the activity and return to the previous screen
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
