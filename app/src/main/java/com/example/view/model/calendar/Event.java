package com.example.view.model.calendar;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private String event_id;
    private String title;
    private String category;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int travelTime;
    private String location;
    private String repetition;
    private boolean notificationEnabled;
    private String notes;
    private List<String> participants;

    public Event(String event_id, String title, String category, LocalDateTime startDateTime,
                 LocalDateTime endDateTime, int travelTime, String location, String repetition,
                 boolean notificationEnabled, String notes, List<String> participants) {
        this.event_id = event_id;
        this.title = title;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.travelTime = travelTime;
        this.location = location;
        this.repetition = repetition;
        this.notificationEnabled = notificationEnabled;
        this.notes = notes;
        this.participants = participants;
    }

    // Getters and Setters

    public String getId() {
        return event_id;
    }

    public void setId(String event_Id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}