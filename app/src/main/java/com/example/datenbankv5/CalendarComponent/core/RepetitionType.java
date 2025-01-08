package com.example.datenbankv5.CalendarComponent.core;

public enum RepetitionType {
    DAILY(1),           // Alle 1 Tag
    EVERY_2_DAYS(2),    // Alle 2 Tage
    WEEKLY(7),          // Alle 7 Tage
    EVERY_2_WEEKS(14),  // Alle 14 Tage
    EVERY_4_WEEKS(28);  // Alle 28 Tage

    private final int intervalInDays;

    RepetitionType(int intervalInDays) {
        this.intervalInDays = intervalInDays;
    }

    public int getIntervalInDays() {
        return intervalInDays;
    }
}
