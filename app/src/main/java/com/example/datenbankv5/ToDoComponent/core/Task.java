package com.example.datenbankv5.ToDoComponent.core;

import android.icu.util.ULocale;

/**
 * Repräsentiert eine Aufgabe mit den relevanten Details wie ID, Titel, Beschreibung, Kategorie und Priorität.
 * Diese Klasse bietet Methoden zum Abrufen und Setzen der verschiedenen Eigenschaften einer Aufgabe.
 */
public class Task {

    private String id;
    private String task;
    private Category category;
    private String description;
    private Priority priority;

    /**
     * Konstruktor, um eine neue Aufgabe zu erstellen.
     *
     * @param id          Die eindeutige ID der Aufgabe.
     * @param task        Der Titel der Aufgabe.
     * @param category    Die Kategorie der Aufgabe.
     * @param description Eine detaillierte Beschreibung der Aufgabe.
     * @param priority    Die Priorität der Aufgabe.
     */
    public Task(String id, String task, Category category, String description, Priority priority) {
        this.id = id;
        this.task = task;
        this.category = category;
        this.description = description;
        this.priority = priority; //
    }

    /**
     * Gibt die ID der Aufgabe zurück.
     *
     * @return Die ID der Aufgabe.
     */
    public String getId() {
        return id;
    }

    /**
     * Gibt den Titel der Aufgabe zurück.
     *
     * @return Der Titel der Aufgabe.
     */
    public String getTask() {
        return task;
    }

    /**
     * Setzt den Titel der Aufgabe.
     *
     * @param task Der neue Titel der Aufgabe.
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Gibt die Kategorie der Aufgabe zurück.
     *
     * @return Die Kategorie der Aufgabe.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Gibt die Beschreibung der Aufgabe zurück.
     *
     * @return Die Beschreibung der Aufgabe.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gibt die Priorität der Aufgabe zurück.
     *
     * @return Die Priorität der Aufgabe.
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Hilfsmethode, um die Details der Kategorie der Aufgabe zu erhalten.
     *
     * @return Die Details der Kategorie oder eine Nachricht, wenn keine Kategorie angegeben wurde.
     */
    public String getCategoryDetails() {
        return category != null ? category.getDetails() : "No category specified";
    }

    /**
     * Hilfsmethode, um den Wert der Priorität der Aufgabe zu erhalten.
     *
     * @return Der Wert der Priorität oder -1, wenn keine Priorität angegeben wurde.
     */
    public int getPriorityDetails() {
        return priority != null ? priority.getValue() : -1;
    }

    /**
     * Gibt eine stringbasierte Darstellung der Aufgabe zurück.
     *
     * @return Eine String-Darstellung der Aufgabe.
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}
