package com.example.datenbankv5.CalendarComponent.core;

import com.example.datenbankv5.ToDoComponent.core.Category;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public class Event {

    private String Event_ID;

    private String title;

    private Mood mood;

    private Category category;

    private Date beginnDate; //mit Datum und Uhrzeit

    private Date endDate; //mit Datum und Uhrzeit

    private Duration traveltime;

    private String locatation;

    private RepetitionType repetiton;

    private String description;

    private List<String> members;

    public Event(String event_ID, String title, Mood mood,
                 Category category, Date beginnDate, Date endDate,
                 Duration traveltime, String locatation, RepetitionType repetiton,
                 String description, List<String> members) throws EventErrorException {
        if (title != null) {
            if (beginnDate !=null && endDate != null) {
                Event_ID = event_ID;
                this.title = title;
                this.mood = mood;
                this.category = category;
                this.beginnDate = beginnDate;
                this.endDate = endDate;
                this.traveltime = traveltime;
                this.locatation = locatation;
                this.repetiton = repetiton;
                this.description = description;
                this.members = members;
            } else {
                throw new EventErrorException("Kein Start- oder Enddatum angegeben!");
            }
        } else {
            throw new EventErrorException("Title ist null!");
        }
    }

    //Setter und Getter

    public String getEvent_ID() {
        return Event_ID;
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

    public Date getBeginnDate() {
        return beginnDate;
    }

    public void setBeginnDate(Date beginnDate) {
        this.beginnDate = beginnDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Duration getTraveltime() {
        return traveltime;
    }

    public void setTraveltime(Duration traveltime) {
        this.traveltime = traveltime;
    }

    public String getLocatation() {
        return locatation;
    }

    public void setLocatation(String locatation) {
        this.locatation = locatation;
    }

    public RepetitionType getRepetiton() {
        return repetiton;
    }

    public void setRepetiton(RepetitionType repetiton) {
        this.repetiton = repetiton;
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
}