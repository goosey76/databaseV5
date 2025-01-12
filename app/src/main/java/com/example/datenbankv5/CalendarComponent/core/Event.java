package com.example.datenbankv5.CalendarComponent.core;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.datenbankv5.ToDoComponent.core.Category;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Diese Klasse repräsentiert ein Event mit verschiedenen Attributen wie Titel, Datum, Uhrzeit,
 * Stimmung, Kategorie, Wiederholung, Reisezeit und weiteren Details.
 */
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

    /**
     * Konstruktor zum Erstellen eines Events.
     *
     * @param eventID        Die ID des Events
     * @param title          Der Titel des Events
     * @param mood           Die Stimmung, die mit dem Event verbunden ist
     * @param category       Die Kategorie des Events
     * @param beginDate      Das Startdatum und die Uhrzeit des Events
     * @param endDate        Das Enddatum und die Uhrzeit des Events
     * @param travelTime     Die Zeit, die für die Anreise zum Event benötigt wird
     * @param location       Der Ort des Events
     * @param repetition     Der Wiederholungstyp des Events
     * @param description    Eine Beschreibung des Events
     * @param members        Eine Liste von Mitgliedern, die am Event teilnehmen
     * @throws EventErrorException Wenn einer der Parameter ungültig ist
     */
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

    /**
     * Gibt die ID des Events zurück.
     *
     * @return Die ID des Events
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Setzt die ID des Events.
     *
     * @param eventID Die neue ID des Events
     */
    public void setEventId(String eventID) {
        this.eventId = eventID;
    }

    /**
     * Gibt den Titel des Events zurück.
     *
     * @return Der Titel des Events
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Events.
     *
     * @param title Der neue Titel des Events
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gibt die Stimmung des Events zurück.
     *
     * @return Die Stimmung des Events
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * Setzt die Stimmung des Events.
     *
     * @param mood Die neue Stimmung des Events
     */
    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * Gibt die Kategorie des Events zurück.
     *
     * @return Die Kategorie des Events
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Setzt die Kategorie des Events.
     *
     * @param category Die neue Kategorie des Events
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gibt das Startdatum des Events zurück.
     *
     * @return Das Startdatum des Events
     */
    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    /**
     * Setzt das Startdatum des Events.
     *
     * @param beginDate Das neue Startdatum des Events
     */
    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * Gibt das Enddatum des Events zurück.
     *
     * @return Das Enddatum des Events
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Setzt das Enddatum des Events.
     *
     * @param endDate Das neue Enddatum des Events
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Gibt die Reisezeit zum Event zurück.
     *
     * @return Die Reisezeit des Events
     */
    public Duration getTravelTime() {
        return travelTime;
    }

    /**
     * Setzt die Reisezeit des Events.
     *
     * @param travelTime Die neue Reisezeit des Events
     */
    public void setTravelTime(Duration travelTime) {
        this.travelTime = travelTime;
    }

    /**
     * Gibt den Ort des Events zurück.
     *
     * @return Der Ort des Events
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setzt den Ort des Events.
     *
     * @param location Der neue Ort des Events
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gibt den Wiederholungstyp des Events zurück.
     *
     * @return Der Wiederholungstyp des Events
     */
    public RepetitionType getRepetition() {
        return repetition;
    }

    /**
     * Setzt den Wiederholungstyp des Events.
     *
     * @param repetition Der neue Wiederholungstyp des Events
     */
    public void setRepetition(RepetitionType repetition) {
        this.repetition = repetition;
    }

    /**
     * Gibt die Beschreibung des Events zurück.
     *
     * @return Die Beschreibung des Events
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung des Events.
     *
     * @param description Die neue Beschreibung des Events
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt die Liste der Mitglieder des Events zurück.
     *
     * @return Die Liste der Mitglieder des Events
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * Setzt die Mitglieder des Events.
     *
     * @param members Die neue Liste der Mitglieder des Events
     */
    public void setMembers(List<String> members) {
        this.members = members;
    }

    /**
     * Gibt eine String-Darstellung des Events zurück, die alle wichtigen Attribute enthält.
     *
     * @return Eine String-Darstellung des Events
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {

        if (travelTime == null) {
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
