package com.example.datenbankv5.ToDoComponent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datenbankv5.R;
import com.example.datenbankv5.StorageComponent.TodoData;
import com.example.datenbankv5.ToDoComponent.core.Category;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import com.example.datenbankv5.ToDoComponent.core.Task;

/**
 * Aktivität zum Hinzufügen einer neuen Aufgabe.
 * Ermöglicht das Erstellen einer Aufgabe mit einem Titel, einer Beschreibung,
 * einer Priorität und einer Kategorie und speichert diese in der Datenbank.
 */
public class AddTaskActivity extends AppCompatActivity {

    private TodoData dbHelper; // Database Helper
    private int selectedPriority = -1; // Standardmäßig ungültige Priorität
    private Category selectedCategory; // Standardmäßig ungültige Kategorie

    /**
     * Wird beim Erstellen der Aktivität aufgerufen.
     * Initialisiert die Eingabefelder und den Button und setzt Listener für Spinner.
     *
     * @param savedInstanceState Das gespeicherte Instanz-Staat, falls vorhanden.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new TodoData(this);

        // Initialisiert Eingabefelder und den Button
        EditText editTextTask = findViewById(R.id.editTextTask);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        Button buttonSaveTask = findViewById(R.id.buttonSaveTask);
        Spinner spinnerPriority = findViewById(R.id.spinner_priority);
        Spinner spinnerCategory = findViewById(R.id.spinner_category);

        // Spinner für Kategorie
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Kategorie auswählen
                selectedCategory = Category.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null; // Zurücksetzen, wenn nichts ausgewählt wurde
            }
        });

        // Spinner für Priorität
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority = position + 1; // Speichern der Priorität als int
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPriority = -1; // Zurücksetzen, wenn nichts ausgewählt wurde
            }
        });

        // Setzt den Klick-Listener für den "Speichern"-Button
        buttonSaveTask.setOnClickListener(v -> {
            // Liest die Eingabewerte
            String task = editTextTask.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            // Validiert die Eingaben
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

            // Konvertiert die ausgewählte Priorität in das Priority-Enum
            Priority priority;
            try {
                priority = Priority.fromValue(selectedPriority);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid priority selected", Toast.LENGTH_SHORT).show();
                return;
            }

            // Einzigartige ID für die Aufgabe generieren
            String formatId = dbHelper.getNextId();

            // Erstellt ein Task-Objekt
            Task createdTask = new Task(formatId, task, selectedCategory, description, priority);

            // Fügt die Aufgabe in die Datenbank ein
            dbHelper.insertTask(createdTask);

            // Zeigt eine Erfolgsmeldung und beendet die Aktivität
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
