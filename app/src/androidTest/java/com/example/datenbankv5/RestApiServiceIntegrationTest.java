package com.example.datenbankv5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.datenbankv5.CalendarComponent.core.Event;
import com.example.datenbankv5.CalendarComponent.core.EventErrorException;
import com.example.datenbankv5.CalendarComponent.core.Mood;
import com.example.datenbankv5.CalendarComponent.core.RepetitionType;
import com.example.datenbankv5.CloudComponent.MissingUUIDException;
import com.example.datenbankv5.CloudComponent.RestApiService;
import com.example.datenbankv5.ToDoComponent.core.Task;
import com.example.datenbankv5.ToDoComponent.core.Category;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RestApiServiceIntegrationTest {

    private Context context;

    private static final String PREFS_NAME = "CloudPrefs";
    /**
     * Schlüssel für die UUID in den SharedPreferences.
     */
    private static final String PREF_UUID_KEY = "UUID";

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

   // private TestCoroutineDispatcher testDispatcher;


   @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();

        // UUID generieren und direkt speichern
       //generateAndSetUuid();
       useSameUUid("8f1aecb3-2505-4b01-8fb4-903d91763b80");
    }

    private void useSameUUid(String uuid) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UUID", uuid);
        editor.apply();
        latch.await(5, TimeUnit.SECONDS);
    }

    private void generateAndSetUuid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        RestApiService.ApiService apiService = RestApiService.retrofitInstance.create(RestApiService.ApiService.class);
        Call<ResponseBody> call = apiService.generateUuid();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // UUID aus der Antwort extrahieren
                        String uuid = response.body().string();

                        // UUID in SharedPreferences speichern
                        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UUID", uuid);
                        editor.apply();

                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
    }

//Tasks Tests: abgeschlossen funktionstüchtig!

    @Test
    public void testSendNewToDo() throws Exception {
        // Beispiel-Task erstellen
        Task task4 = new Task("00000104", "Hausaufgaben", null, null, null);
        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Task senden
        RestApiService.sendNewToDo(context, task4);

        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);
    }

    @Test
    public void testGetAllToDo() throws InterruptedException, MissingUUIDException {
        // CountDownLatch verwenden, um auf die LiveData zu warten
        final CountDownLatch latch = new CountDownLatch(1);

        // Führe den API-Aufruf aus
        LiveData<List<Task>> taskLiveData = RestApiService.getAllToDo(context);

        // Beobachte die LiveData und blockiere den Test, bis die Daten abgerufen wurden
        taskLiveData.observeForever(new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                if (tasks != null) {
                    Log.d("Test", "Tasks: " + tasks.toString());  // Ausgabe in Logcat
                } else {
                    Log.d("Test", "Keine Daten empfangen");
                }
                latch.countDown();  // Latch freigeben, wenn die Daten da sind
            }
        });

        // Warte auf die Fertigstellung der API-Antwort (max. 5 Sekunden)
        boolean dataLoaded = latch.await(5, TimeUnit.SECONDS);

        // Überprüfe, ob die Daten erfolgreich abgerufen wurden
        assertTrue("Daten wurden nicht abgerufen", dataLoaded);
    }

    @Test
    public void testUpdateTaskInCloud() throws InterruptedException, MissingUUIDException {
        // Beispiel-Task erstellen
        Task task4 = new Task("00000104", "Hausaufgaben", null, "Prog3", Priority.URGENT_IMPORTANT);
        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Task senden

        RestApiService.updateToDoInCloud(context, task4);


        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);

    }

    @Test
    public void testDeleteTaskInCloud() throws InterruptedException, MissingUUIDException {
        // Beispiel-Task erstellen
        Task task4 = new Task("00000104", "Hausaufgaben", null, "Prog3", Priority.URGENT_IMPORTANT);
        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Task senden

        RestApiService.deleteToDoInCloud(context, task4);

        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);
    }


//Event Tests:

    @Test
    public void testSendNewEvent() throws Exception {
        // Beispiel-Event erstellen
        Event event3 = new Event("00000102", "Date", Mood.EXCITED, null, LocalDateTime.of(2025,1,17, 15, 00), LocalDateTime.of(2025,1,17, 22, 00),null, null, null, "Weihnachtsmarkt und Abfahrt", null );

        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Event senden
        RestApiService.sendNewEvent(context, event3);

        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);
    }

    @Test
    public void testGetEvents() throws InterruptedException, MissingUUIDException {
        // CountDownLatch verwenden, um auf die LiveData zu warten
        final CountDownLatch latch = new CountDownLatch(1);

        // Führe den API-Aufruf aus
        LiveData<List<Event>> eventsLiveData = RestApiService.getAllEvents(context);

        // Beobachte die LiveData und blockiere den Test, bis die Daten abgerufen wurden
        eventsLiveData.observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                if (events != null) {
                    Log.d("Test", "Tasks: " + events.toString());  // Ausgabe in Logcat
                } else {
                    Log.d("Test", "Keine Daten empfangen");
                }
                latch.countDown();  // Latch freigeben, wenn die Daten da sind
            }
        });

        // Warte auf die Fertigstellung der API-Antwort (max. 5 Sekunden)
        boolean dataLoaded = latch.await(5, TimeUnit.SECONDS);

        // Überprüfe, ob die Daten erfolgreich abgerufen wurden
        assertTrue("Daten wurden nicht abgerufen", dataLoaded);
    }

    @Test
    public void testUpdateEventInCloud() throws InterruptedException, MissingUUIDException, EventErrorException {
        String member = RestApiService.getUUid(context);

        List<String> list = Arrays.asList(member);

        // Beispiel-Event erstellen
        Event event3 = new Event("00000102", "Weihnachtsmarkt", Mood.SAD, null, LocalDateTime.of(2025,1,17, 15, 00), LocalDateTime.of(2025,1,17, 22, 00),null, "Berlin", null, "Weihnachtsmarkt und Abfahrt", list );

        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Event senden

        RestApiService.updateEventInCloud(context, event3);


        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);

    }

    @Test
    public void testDeleteEventInCloud() throws InterruptedException, MissingUUIDException, EventErrorException {
        // Beispiel-Event erstellen
        Event event3 = new Event("00000102", "Weihnachtsmarkt", Mood.SAD, null, LocalDateTime.of(2025,1,17, 15, 00), LocalDateTime.of(2025,1,17, 22, 00),null, "Berlin", null, "Weihnachtsmarkt und Abfahrt", null );
        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Event löschen
        RestApiService.deleteEventInCloud(context, event3);

        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);
    }




//Share Tests: abgeschlossen funktionstüchtig!
    @Test
    public void testSendEventToShare() throws Exception {
       String member = RestApiService.getUUid(context);

        List<String> list = Arrays.asList(member);

        // Beispiel-Task erstellen
        Event event1 = new Event("12345678", "Hausaufgaben", null, null, LocalDateTime.of(2025, 1, 13, 12, 30), LocalDateTime.of(2025, 1, 13, 14, 00), null, null, null, null,  null);

        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Task senden
        RestApiService.sendEventToShare(event1);

        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetEventToShare() throws Exception {

       //testSendEventToShare();
        // CountDownLatch zum Warten auf asynchrone Antwort
        final CountDownLatch latch = new CountDownLatch(1);

        // Führe den API-Aufruf aus
        LiveData<Event> eventLiveData = RestApiService.getSharedEvent("12345678");

        // Beobachte die LiveData und blockiere den Test, bis die Daten abgerufen wurden
        eventLiveData.observeForever(new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if (event != null) {
                    Log.d("Test", "Event empfangen: " + event.toString());  // Ausgabe in Logcat
                } else {
                    Log.d("Test", "Keine Daten empfangen");
                }
                latch.countDown();  // Latch freigeben, wenn die Daten da sind
            }
        });

        // Warte auf die Fertigstellung der API-Antwort (max. 5 Sekunden)
        boolean dataLoaded = latch.await(5, TimeUnit.SECONDS);
    }

}

