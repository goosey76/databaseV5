package com.example.datenbankv5.CalendarComponent.core;

/**
 * Diese Ausnahme wird ausgelöst, wenn bei der Verarbeitung von Event-Daten ein Fehler auftritt.
 * Sie dient dazu, spezifische Fehler in Bezug auf die Erstellung oder Verarbeitung von Events zu kennzeichnen.
 */
public class EventErrorException extends Exception {

    /**
     * Konstruktor zum Erstellen einer neuen EventErrorException mit einer spezifischen Fehlermeldung.
     *
     * @param errorString Die Fehlermeldung, die die Ausnahme beschreibt
     */
    public EventErrorException(String errorString) {
        super(errorString);
    }
}
