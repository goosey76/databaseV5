package com.example.datenbankv5.CloudComponent.ParsingAndSerializer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Duration;

/**
 * Ein benutzerdefinierter Deserializer für die Klasse {@link Duration}, der JSON-Daten
 * aus einem String mit Millisekunden in ein {@link Duration}-Objekt konvertiert.
 */
public class DurationDeserializer implements JsonDeserializer<Duration> {

    /**
     * Deserialisiert einen JSON-String, der eine Dauer in Millisekunden repräsentiert,
     * und wandelt ihn in ein {@link Duration}-Objekt um.
     *
     * @param json     Das JSON-Element, das deserialisiert wird.
     * @param typeOfT  Der Typ des Objekts, der deserialisiert wird.
     * @param context  Der Deserialisierungs-Kontext.
     * @return Ein {@link Duration}-Objekt, das die Dauer in Millisekunden repräsentiert.
     * @throws JsonParseException Wenn das JSON-Element nicht korrekt analysiert werden kann.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Deserialisieren von Millisekunden (String) zu Duration
        return Duration.ofMillis(Long.parseLong(json.getAsString()));
    }
}
