package com.example.datenbankv5.CalendarComponent.core;

/**
 * Enum, das verschiedene Wiederholungsarten für Ereignisse darstellt.
 * Jede Wiederholungsart hat ein Intervall in Tagen, das angibt, wie oft das Ereignis wiederholt wird.
 */
public enum RepetitionType {

    /** Das Ereignis wiederholt sich täglich. */
    DAILY(1),           // Alle 1 Tag

    /** Das Ereignis wiederholt sich alle 2 Tage. */
    EVERY_2_DAYS(2),    // Alle 2 Tage

    /** Das Ereignis wiederholt sich wöchentlich (alle 7 Tage). */
    WEEKLY(7),          // Alle 7 Tage

    /** Das Ereignis wiederholt sich alle 2 Wochen (alle 14 Tage). */
    EVERY_2_WEEKS(14),  // Alle 14 Tage

    /** Das Ereignis wiederholt sich alle 4 Wochen (alle 28 Tage). */
    EVERY_4_WEEKS(28);  // Alle 28 Tage

    /** Das Intervall in Tagen, das die Wiederholung definiert. */
    private final int intervalInDays;

    /**
     * Konstruktor, der das Intervall in Tagen für jede Wiederholungsart festlegt.
     *
     * @param intervalInDays Das Intervall in Tagen, das die Wiederholung definiert
     */
    RepetitionType(int intervalInDays) {
        this.intervalInDays = intervalInDays;
    }

    /**
     * Gibt das Intervall in Tagen zurück, das für diese Wiederholungsart definiert ist.
     *
     * @return Das Intervall in Tagen für die Wiederholungsart
     */
    public int getIntervalInDays() {
        return intervalInDays;
    }
}
