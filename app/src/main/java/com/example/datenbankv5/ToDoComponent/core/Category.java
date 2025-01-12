package com.example.datenbankv5.ToDoComponent.core;

/**
 * Enum zur Darstellung von Kategorien für Aufgaben. Jede Kategorie hat einen Namen, einen Standort,
 * eine mögliche Reisezeit und eine mögliche Dauer.
 */
public enum Category {

    WORK("Arbeit", "Office", 30, -1),              // Kategorie Arbeit
    UNIVERSITY("Uni", "HTW Berlin", 90, 90),        // Kategorie Uni
    HOUSEHOLD("Haushalt", "Home", -1, -1);          // Kategorie Haushalt

    private String name;
    private String location;
    private int travelTime; // in Minuten, -1 falls nicht angegeben
    private int duration;   // in Minuten, -1 falls nicht angegeben

    /**
     * Konstruktor für die Kategorie-Enum.
     *
     * @param name Der Name der Kategorie.
     * @param location Der Standort der Kategorie.
     * @param travelTime Die Reisezeit zur Kategorie in Minuten (oder -1, falls nicht angegeben).
     * @param duration Die Dauer der Aufgabe in der Kategorie in Minuten (oder -1, falls nicht angegeben).
     */
    Category(String name, String location, int travelTime, int duration) {
        this.name = name;
        this.location = location;
        this.travelTime = travelTime;
        this.duration = duration;
    }

    // Getter-Methoden

    /**
     * Gibt den Namen der Kategorie zurück.
     *
     * @return Der Name der Kategorie.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den Standort der Kategorie zurück.
     *
     * @return Der Standort der Kategorie.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gibt die Reisezeit zur Kategorie zurück.
     *
     * @return Die Reisezeit in Minuten (oder -1, falls nicht angegeben).
     */
    public int getTravelTime() {
        return travelTime;
    }

    /**
     * Gibt die Dauer der Aufgabe in der Kategorie zurück.
     *
     * @return Die Dauer in Minuten (oder -1, falls nicht angegeben).
     */
    public int getDuration() {
        return duration;
    }

    // Setter-Methoden

    /**
     * Setzt den Standort der Kategorie.
     *
     * @param location Der neue Standort der Kategorie.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Setzt die Reisezeit zur Kategorie.
     *
     * @param travelTime Die neue Reisezeit in Minuten.
     */
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    /**
     * Setzt die Dauer der Aufgabe in der Kategorie.
     *
     * @param duration Die neue Dauer in Minuten.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gibt die Details der Kategorie als formatierten String zurück.
     *
     * @return Ein String, der alle relevanten Details der Kategorie beschreibt.
     */
    public String getDetails() {
        return String.format(
                "Name: %s, Location: %s, Travel Time: %s, Duration: %s",
                name,
                location,
                (travelTime == -1 ? "Not specified" : travelTime + " minutes"),
                (duration == -1 ? "Not specified" : duration + " minutes")
        );
    }
}
