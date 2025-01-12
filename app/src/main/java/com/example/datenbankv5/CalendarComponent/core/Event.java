package com.example.datenbankv5.CalendarComponent.core;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.datenbankv5.ToDoComponent.core.Category;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Event {

    private String eventId;
    private String title;
    private Mood mood;
    private Category category;
    private LocalDateTime beginDate; // Mit Datum und Uhrzeit
    private LocalDateTime endDate; // Mit Datum und Uhrzeit
    private Duration travelTime;
    private String location;
    private RepetitionType repetition;
    private String description;
    private List<String> members;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Event(String eventID, String title, Mood mood,
                 Category category, LocalDateTime beginDate, LocalDateTime endDate,
                 Duration travelTime, String location, RepetitionType repetition,
                 String description, List<String> members) throws EventErrorException {

        if (title == null) {
            throw new EventErrorException("Title ist null!");
        }

        if (beginDate == null || endDate == null) {
            throw new EventErrorException("Kein Start- oder Enddatum angegeben!");
        }

        if (endDate.isBefore(beginDate)) {
            throw new EventErrorException("Enddatum liegt vor dem Startdatum!");
        }

        this.eventId = eventID;
        this.title = title;
        this.mood = mood;
        this.category = category;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.travelTime = travelTime;
        this.location = location;
        this.repetition = repetition;
        this.description = description;
        this.members = members;
    }

    // Getter und Setter (falls benötigt)

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventID) {
        this.eventId = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Duration getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Duration travelTime) {
        this.travelTime = travelTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public RepetitionType getRepetition() {
        return repetition;
    }

    public void setRepetition(RepetitionType repetition) {
        this.repetition = repetition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {

        if (travelTime== null) {
            travelTime = Duration.ofMinutes(0);
        }
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", mood='" + mood + '\'' +
                ", category='" + category + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", travelTime='" + travelTime.toMinutes() + '\'' +
                ", location='" + location + '\'' +
                ", repetition='" + repetition + '\'' +
                ", description='" + description + '\'' +
                ", members=" + members +
                '}';
    }
}
