package com.example.datenbankv5.CloudComponent.ParsingAndSerializer;

import android.os.Build;
import android.util.Log;

import com.example.datenbankv5.CalendarComponent.core.Event;
import com.example.datenbankv5.CalendarComponent.core.EventErrorException;
import com.example.datenbankv5.CalendarComponent.core.Mood;
import com.example.datenbankv5.CalendarComponent.core.RepetitionType;
import com.example.datenbankv5.ToDoComponent.core.Category;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import com.example.datenbankv5.ToDoComponent.core.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResponseParser {

    public static Event parseEvent(String jsonResponse) throws EventErrorException {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // JSON-String in ein JSONObject umwandeln
                JSONObject json = new JSONObject(jsonResponse);

                // Felder aus dem JSON extrahieren
                String eventId = json.getString("eventId");
                String title = json.getString("title");

                // Mood und Category konvertieren
                Mood mood = json.has("mood") && !json.isNull("mood")
                        ? Mood.valueOf(json.getString("mood").toUpperCase())
                        : null;
                Category category = json.has("category") && !json.isNull("category")
                        ? Category.valueOf(json.getString("category").toUpperCase())
                        : null;

                // Datum und Uhrzeit parsen (ISO 8601-Format)
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                LocalDateTime beginDate = json.has("beginDate") && !json.isNull("beginDate")
                        ? LocalDateTime.parse(json.getString("beginDate"), formatter)
                        : null;

                LocalDateTime endDate = json.has("endDate") && !json.isNull("endDate")
                        ? LocalDateTime.parse(json.getString("endDate"), formatter)
                        : null;


                // RepetitionType konvertieren
                RepetitionType repetition = json.has("repetition") && !json.isNull("repetition")
                        ? RepetitionType.valueOf(json.getString("repetition").toUpperCase())
                        : null;

                // Dauer konvertieren
                Duration travelTime = json.has("travelTime") && !json.isNull("travelTime")
                        ? Duration.parse(json.getString("travelTime"))
                        : null;


                // Mitglieder extrahieren
                List<String> members = new ArrayList<>();
                if (json.has("members") && !json.isNull("members")) {
                    JSONArray membersArray = json.getJSONArray("members");
                    for (int i = 0; i < membersArray.length(); i++) {
                        members.add(membersArray.getString(i));
                    }
                }

                // Optionales Feld "description"
                String description = json.has("description") && !json.isNull("description")
                        ? json.getString("description")
                        : null;

                // Optionales Feld "location"
                String location = json.has("location") && !json.isNull("location")
                        ? json.getString("location")
                        : null;

                // Event-Objekt erstellen
                return new Event(eventId, title, mood, category, beginDate, endDate, travelTime,
                        location, repetition, description, members);
            } else {
                throw new EventErrorException("Fehler beim Parsen des Events: Build.VERSION.SDK_INT < Build.Version_codes.0");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Event> parseEventList(String jsonResponse) throws EventErrorException {
        List<Event> events = new ArrayList<>();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // JSON-Array aus der Antwort extrahieren
                JSONArray jsonArray = new JSONArray(jsonResponse);

                // Jeden Eintrag im Array durchlaufen
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);

                    // Felder aus dem JSON-Objekt extrahieren
                    String eventId = json.getString("eventId");
                    String title = json.getString("title");

                    // Mood und Category konvertieren
                    Mood mood = json.has("mood") && !json.isNull("mood")
                            ? Mood.valueOf(json.getString("mood").toUpperCase())
                            : null;
                    Category category = json.has("category") && !json.isNull("category")
                            ? Category.valueOf(json.getString("category").toUpperCase())
                            : null;

                    // Datum und Uhrzeit parsen (ISO 8601-Format)
                    LocalDateTime beginDate = json.has("beginDate") && !json.isNull("beginDate")
                            ? LocalDateTime.parse(json.getString("beginDate"), formatter)
                            : null;
                    LocalDateTime endDate = json.has("endDate") && !json.isNull("endDate")
                            ? LocalDateTime.parse(json.getString("endDate"), formatter)
                            : null;

                    // RepetitionType konvertieren
                    RepetitionType repetition = json.has("repetition") && !json.isNull("repetition")
                            ? RepetitionType.valueOf(json.getString("repetition").toUpperCase())
                            : null;

                    // Dauer konvertieren
                    Duration travelTime = json.has("travelTime") && !json.isNull("travelTime")
                            ? Duration.ofMillis(Long.parseLong(json.getString("travelTime")))
                            : null;

                    // Mitglieder extrahieren
                    List<String> members = new ArrayList<>();
                    if (json.has("members") && !json.isNull("members")) {
                        JSONArray membersArray = json.getJSONArray("members");
                        for (int j = 0; j < membersArray.length(); j++) {
                            members.add(membersArray.getString(j));
                        }
                    }

                    // Optionales Feld "description"
                    String description = json.has("description") && !json.isNull("description")
                            ? json.getString("description")
                            : null;

                    // Optionales Feld "location"
                    String location = json.has("location") && !json.isNull("location")
                            ? json.getString("location")
                            : null;

                    // Event-Objekt erstellen und zur Liste hinzufügen
                    Event event = new Event(eventId, title, mood, category, beginDate, endDate,
                            travelTime, location, repetition, description, members);
                    events.add(event);
                }
            }else {
                    throw new EventErrorException("Fehler beim Parsen des Events: Build.VERSION.SDK_INT < Build.Version_codes.0");
                }
        } catch (Exception e) {
            // Fehlerbehandlung
            throw new EventErrorException("Fehler beim Parsen der Event-Liste: " + e.getMessage());
        }
        return events;
    }

    public static List<Task> parseTaskList(String jsonResponse) throws Exception {
        List<Task> tasks = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                // Extrahiere die Felder aus dem JSON-Objekt
                String id = json.getString("id");
                String taskName = json.getString("task");
                Category category = json.has("category") && !json.isNull("category")
                        ? Category.valueOf(json.getString("category").toUpperCase())
                        : null;
                String description = json.has("description") && !json.isNull("description")
                        ? json.getString("description")
                        : null;
                Priority priority = json.has("priority") && !json.isNull("priority")
                        ? Priority.valueOf(json.getString("priority"))
                        : null;

                // Erstelle ein Task-Objekt und füge es zur Liste hinzu
                Task task = new Task(id, taskName, category, description, priority);
                tasks.add(task);
            }
        } catch (Exception e) {
            throw new Exception("Error parsing Task list: " + e.getMessage());
        }
        return tasks;
    }
}
