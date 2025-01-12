package com.example.datenbankv5.ToDoComponent.core;

/**
 * Enum zur Darstellung der Priorität einer Aufgabe. Die Priorität wird durch einen numerischen Wert definiert,
 * der sowohl die Dringlichkeit als auch die Wichtigkeit einer Aufgabe widerspiegelt.
 */
public enum Priority {
    URGENT_IMPORTANT(1),               // Wichtig & Dringend
    NOT_URGENT_IMPORTANT(2),           // Wichtig & Nicht Dringend
    URGENT_NOT_IMPORTANT(3),           // Nicht Wichtig & Dringend
    NOT_URGENT_NOT_IMPORTANT(4);       // Nicht Wichtig & Nicht Dringend

    private final int value;

    /**
     * Konstruktor für die Prioritäten-Enum. Setzt den numerischen Wert der Priorität.
     *
     * @param value Der numerische Wert der Priorität.
     */
    Priority(int value) {
        this.value = value;
    }

    /**
     * Gibt den numerischen Wert der Priorität zurück.
     *
     * @return Der numerische Wert der Priorität.
     */
    public int getValue() {
        return value;
    }

    /**
     * Liefert die Priorität basierend auf einem numerischen Wert zurück.
     *
     * @param value Der numerische Wert der Priorität.
     * @return Die zugehörige Priorität.
     * @throws IllegalArgumentException Wenn der numerische Wert keiner gültigen Priorität entspricht.
     */
    public static Priority fromValue(int value) {
        for (Priority priority : Priority.values()) {
            if (priority.getValue() == value) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
