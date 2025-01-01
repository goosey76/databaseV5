package com.example.datenbankv5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private TodoDatabaseHelper dbHelper; // Database Helper
    private int selectedPriority = -1; // Default invalid priority

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new TodoDatabaseHelper(this);

        // Initialize input fields and button
        EditText editTextTask = findViewById(R.id.editTextTask);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        Button buttonSaveTask = findViewById(R.id.buttonSaveTask);
        Spinner spinnerPriority = findViewById(R.id.spinner_priority);

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

            // Convert selectedPriority to Priority enum
            Priority priority;
            try {
                priority = Priority.fromValue(selectedPriority);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid priority selected", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert task into the database
            dbHelper.insertTask(task, description, priority);

            // Finish the activity and return to the previous screen
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
