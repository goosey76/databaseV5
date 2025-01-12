package com.example.datenbankv5.CalendarComponent.core;

/**
 * Enum, das verschiedene Stimmungen mit den entsprechenden Emojis darstellt.
 * Jede Stimmung wird durch ein Emoji repräsentiert, das in der Benutzeroberfläche angezeigt werden kann.
 */
public enum Mood {

    /** Die Stimmung ist fröhlich. */
    HAPPY("😊"),

    /** Die Stimmung ist neutral. */
    NEUTRAL("😐"),

    /** Die Stimmung ist traurig. */
    SAD("😢"),

    /** Die Stimmung ist aktiv. */
    ACTIVE("🏃‍♂️"),

    /** Die Stimmung ist ruhig. */
    CALM("😌"),

    /** Die Stimmung ist aufgeregt. */
    EXCITED("🤩"),

    /** Die Stimmung ist verwirrt. */
    CONFUSED("😕");

    /** Das Emoji, das die jeweilige Stimmung repräsentiert. */
    private final String emoji;

    /**
     * Konstruktor, der das Emoji für jede Stimmung festlegt.
     *
     * @param emoji Das Emoji, das der Stimmung zugeordnet wird
     */
    Mood(String emoji) {
        this.emoji = emoji;
    }

    /**
     * Gibt das Emoji zurück, das die Stimmung repräsentiert.
     *
     * @return Das Emoji der Stimmung
     */
    public String getEmoji() {
        return emoji;
    }
}


/*
Spinner moodSpinner = findViewById(R.id.moodSpinner);
ArrayAdapter<String> adapter = new ArrayAdapter<>(
    this,
    android.R.layout.simple_spinner_item,
    Arrays.stream(Mood.values()).map(Mood::toString).toArray(String[]::new)
);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
moodSpinner.setAdapter(adapter);
*/

