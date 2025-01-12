package com.example.view.interfaces;

import com.example.view.model.todo.Task;
import com.example.view.model.calendar.Event;

import java.util.List;

public interface TodoEventInterface {

    // Task-related methods
    List<Task> getTasks();
    void deleteAllTasks();

    // Event-related methods
    void createEventFromTask(Task task);
    void addEvent(Event event);
    void deleteEvent(String eventId);
}
