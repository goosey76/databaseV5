package com.example.datenbankv5.CloudComponent.ParsingAndSerializer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Duration;

/**
 * Ein benutzerdefinierter Serializer für die Klasse {@link Duration}, der Java-Duration-Objekte
 * in JSON-Strings umwandelt, die die Dauer in Millisekunden repräsentieren.
 */
public class DurationSerializer implements JsonSerializer<Duration> {

    /**
     * Serialisiert ein {@link Duration}-Objekt in einen JSON-String, der die Dauer
     * in Millisekunden enthält.
     *
     * @param duration   Das {@link Duration}-Objekt, das serialisiert werden soll.
     * @param typeOfSrc  Der Typ des Quellobjekts.
     * @param context    Der Serialisierungs-Kontext.
     * @return Ein {@link JsonElement}, das die Dauer in Millisekunden als String enthält.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        // Serialisieren als Millisekunden (String)
        return context.serialize(Long.toString(duration.toMillis()));
    }
}
