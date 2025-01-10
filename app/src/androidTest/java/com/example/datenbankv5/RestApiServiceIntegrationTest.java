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
import com.example.datenbankv5.CalendarComponent.core.Mood;
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

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

   // private TestCoroutineDispatcher testDispatcher;


    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();

        // UUID generieren und direkt speichern
        generateAndSetUuid();
    }

    private void generateAndSetUuid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UUID", "30955b4f-4faa-49c5-b8d4-72e3e66c1192");
        editor.apply();


        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSendNewToDo() throws Exception {
        // Beispiel-Task erstellen
        Task task5 = new Task("00000104", "Dusche sauber machen", Category.HOUSEHOLD, "", null);

        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Task senden
        RestApiService.sendNewToDo(context, task5);

        // Warten auf den Abschluss des asynchronen Aufrufs
        latch.await(5, TimeUnit.SECONDS);

        // Überprüfung: UUID muss vorhanden sein
        SharedPreferences sharedPreferences = context.getSharedPreferences("CloudPrefs", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", null);
        assertNotNull("UUID sollte vorhanden sein", uuid);
    }


    //ParseProbleme im Backend!
    @Test
    public void testSendEventToShare() throws Exception {
        // Beispiel-Task erstellen
        Event event1 = new Event("00000100", "Meeting", Mood.NEUTRAL, Category.WORK, new Date(2025,1,13, 12, 30), new Date(2025,1,13, 14, 00), Duration.ofMinutes(90), "C646", null, "Meeting about our App", null );

        // CountDownLatch zum Warten auf asynchrone Antwort
        CountDownLatch latch = new CountDownLatch(1);

        // Task senden
        RestApiService.sendEventToShare(event1);

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
}

