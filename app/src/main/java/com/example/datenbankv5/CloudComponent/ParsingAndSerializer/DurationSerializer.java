package com.example.datenbankv5.CloudComponent.ParsingAndSerializer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Duration;

public class DurationSerializer implements JsonSerializer<Duration> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        // Serialisieren als Millisekunden (String)
        return context.serialize(Long.toString(duration.toMillis()));
    }
}
