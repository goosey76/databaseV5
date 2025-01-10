package com.example.datenbankv5.CloudComponent.ParsingAndSerializer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationDeserializer implements JsonDeserializer<Duration> {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Deserialisieren von Millisekunden (String) zu Duration
        return Duration.ofMillis(Long.parseLong(json.getAsString()));
    }
}
