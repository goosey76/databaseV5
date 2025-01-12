package com.example.view.control.calendar;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.view.interfaces.TodoEventInterface;
import com.example.view.model.calendar.Event;
import com.example.view.model.repository.EventRepository;
import com.example.view.model.repository.TodoDatabaseHelper;
import com.example.view.model.todo.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends ViewModel implements TodoEventInterface {
    private final MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>();
    private final EventRepository eventRepository;
    private final TodoDatabaseHelper todoDatabaseHelper;

    public EventViewModel(Context context) {
        eventRepository = new EventRepository();
        todoDatabaseHelper = new TodoDatabaseHelper(context);
        eventsLiveData.setValue(new ArrayList<>());
    }

    public LiveData<List<Event>> getEventsLiveData() {
        return eventsLiveData;
    }

    public void loadEventsForDate(LocalDateTime date) {
        List<Event> eventsForDate = eventRepository.getEventsForDate(date);
        Log.d("EventViewModel", "Requested date: " + date + ", Events found: " + eventsForDate.size());
        eventsLiveData.setValue(eventsForDate);
    }

    @Override
    public List<Task> getTasks() {
        try {
            return todoDatabaseHelper.getAllTasksSortedByPriority();
        } catch (Exception e) {
            Log.e("EventViewModel", "Error fetching tasks", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteAllTasks() {
        try {
            todoDatabaseHelper.deleteAllTasks();
            Log.d("EventViewModel", "All tasks deleted successfully.");
        } catch (Exception e) {
            Log.e("EventViewModel", "Error deleting tasks", e);
        }
    }

    @Override
    public void createEventFromTask(Task task) {
        try {
            Event event = new Event(
                    task.getId(),
                    task.getTask(),
                    task.getCategory().getName(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1),
                    task.getCategory().getTravelTime(),
                    task.getCategory().getLocation(),
                    null,
                    true,
                    task.getDescription(),
                    new ArrayList<>()
            );
            addEvent(event);
        } catch (Exception e) {
            Log.e("EventViewModel", "Error creating event from task", e);
        }
    }

    @Override
    public void addEvent(Event event) {
        try {
            eventRepository.insertEvent(event);
            loadEventsForDate(event.getStartDateTime());
        } catch (Exception e) {
            Log.e("EventViewModel", "Error adding event", e);
        }
    }

    @Override
    public void deleteEvent(String eventId) {
        if (eventId != null && !eventId.isEmpty()) {
            try {
                eventRepository.deleteEvent(eventId);
                Log.d("EventViewModel", "Event deleted with ID: " + eventId);
            } catch (Exception e) {
                Log.e("EventViewModel", "Error deleting event", e);
            }
        }
    }
}
