package com.example.datenbankv5;

import com.example.datenbankv5.CalendarComponent.core.Event;
import com.example.datenbankv5.CalendarComponent.core.EventErrorException;
import com.example.datenbankv5.CalendarComponent.core.Mood;
import com.example.datenbankv5.CalendarComponent.core.RepetitionType;
import com.example.datenbankv5.CloudComponent.RestApiService;
import com.example.datenbankv5.ToDoComponent.core.Category;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import com.example.datenbankv5.ToDoComponent.core.Task;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.util.Date;


public class RestApiServiceTest {

    public RestApiServiceTest() throws EventErrorException {
    }

    private Task task1 = new Task("00000100", "Hausaufgaben", Category.UNIVERSITY, "Prog3 erledigen",Priority.NOT_URGENT_NOT_IMPORTANT);

    private Task task2 = new Task("00000101", "Programmieren", Category.UNIVERSITY, "Backend fertigstellen",Priority.URGENT_IMPORTANT);

    private Task task3 = new Task("00000102", "Einkaufen", Category.HOUSEHOLD, "Eier, Brot, Wasser",Priority.URGENT_NOT_IMPORTANT);

    private Task task4 = new Task("00000103", "Meeting vorbereiten", Category.WORK, "C446",Priority.NOT_URGENT_IMPORTANT);

    private Task task5 = new Task("00000104", "Dusche sauber machen", Category.HOUSEHOLD, "",Priority.NOT_URGENT_NOT_IMPORTANT);


    private Event event1 = new Event("00000100", "Meeting", Mood.NEUTRAL, Category.WORK, new Date(2025,1,13, 12, 30), new Date(2025,1,13, 14, 00), Duration.ofMinutes(90), "C646", null, "Meeting about our App", null );

    private Event event2 = new Event("00000101", "Fußball", Mood.HAPPY, null, new Date(2025,1,11, 17, 30), new Date(2025,1,11, 20, 00), Duration.ofMinutes(15), "SEP", RepetitionType.WEEKLY, null, null );

    private Event event3 = new Event("00000102", "Date", Mood.EXCITED, null, new Date(2025,1,17, 15, 00), new Date(2025,1,17, 22, 00),null, null, null, "Weihnachtsmarkt und Abfahrt", null );

    private Event event4 = new Event("00000103", "Arzttermin", Mood.SAD, null, new Date(2025,2,2, 8, 25), new Date(2025,2,2, 8, 45), Duration.ofMinutes(5), "Ärztehaus II", null, "nüchtern!", null );

    private Event event5 = new Event("00000104", "Vorlesung", Mood.CALM, Category.UNIVERSITY, new Date(2025,1,13, 12, 15), new Date(2025,1,13, 13, 45), Duration.ofMinutes(90), "C446", RepetitionType.WEEKLY, "Prog3", null );




}
