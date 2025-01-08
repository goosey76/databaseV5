package com.example.datenbankv5.CalendarComponent.core;

public enum Mood {
    HAPPY("😊"),
    NEUTRAL("😐"),
    SAD("😢"),
    ACTIVE("🏃‍♂️"),
    CALM("😌"),
    EXCITED("🤩"),
    CONFUSED("😕");

    private final String emoji;

    // Konstruktor
    Mood(String emoji) {
        this.emoji = emoji;
    }

    // Getter für das Emoji
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

