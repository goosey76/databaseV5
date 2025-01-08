package com.example.datenbankv5.CloudComponent;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.datenbankv5.CalendarComponent.core.Event;
import com.example.datenbankv5.ToDoComponent.core.Task;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public class RestApiService {

    // Basis-URL der API
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    // Name und Schlüssel für SharedPreferences
    private static final String PREFS_NAME = "CloudPrefs";
    private static final String PREF_UUID_KEY = "UUID";

    //Hilfsmethode zum Testen der UUID Registrierung im Backend
    // Methode zum Löschen der gespeicherten UUID
    public static void deleteUuid(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Prüfen, ob eine UUID existiert
        String existingUuid = sharedPreferences.getString(PREF_UUID_KEY, null);
        if (existingUuid != null) {
            // UUID löschen
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(PREF_UUID_KEY);
            editor.apply();

            Log.d("CloudService", "UUID successfully deleted: " + existingUuid);
        } else {
            Log.d("CloudService", "No UUID found to delete.");
        }
    }



    // Retrofit-Instanz
    private static final Retrofit retrofitInstance = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON-Converter
            .build();

    // API-Schnittstelle
    private interface ApiService {

        //UUID GENERATOR
        @GET("generate") // Endpunkt: BASE_URL/generate
        Call<ResponseBody> generateUuid(); // GET-Anfrage für UUID
            //Implementierung: Check

        //TASKS API
        @POST("tasks") // Endpunkt: BASE_URL/tasks
        Call<ResponseBody> sendNewToDo(@Body Task task, @Query("param") String uuid); // POST
            //Implementierung: Check

        @GET("tasks") // Endpunkt: BASE_URL/tasks
        Call<ResponseBody> getAllToDo(@Query("param") String uuid); //GET-Anfrage für alle ToDos in der Cloud

        @PUT("tasks") // Endpunkt: BASE_URL/tasks
        Call<ResponseBody> sendUpdatedTodo(@Body Task task, @Query("param") String uuid); //PUT-Anfrage, um existierenden ToDos zu aktualisieren

        @DELETE("tasks") // Endpunkt: BASE_URL/tasks
        Call<ResponseBody> deleteToDoInCloud(@Body String idOfDeletedTask, @Query("param") String uuid); //DELETE-Anfrage, um ToDos aus der Cloud zu löschen, basierend auf ihrer ID

        //CALENDAR API
        @POST("calendar") // Endpunkt: BASE_URL/calendar
        Call<ResponseBody> sendNewEvent(@Body Event event, @Query("param") String uuid); // Query-Parameter hinzufügen
            //Implementierung: Check

        @GET("calendar") // Endpunkt: BASE_URL/calendar
        Call<ResponseBody> getAllEvents(@Query("param") String uuid); //GET-Anfrage für alle Events in der Cloud

        @PUT("calendar") // BASE_URL/calendar
        Call<ResponseBody> sendUpdatedEvent(@Body Event event, @Query("param") String uuid); //PUT-Anfrage, um existierende Events zu aktualisieren

        @DELETE("calendar") // Endpunkt: BASE_URL/calendar
        Call<ResponseBody> deleteEventInCloud(@Body String idOfDeletedEvent, @Query("param") String uuid); //DELETE-Anfrage, um ToDos aus der Cloud zu löschen, basierend auf ihrer ID

        //SHARE API
        @POST("share") // Endpunkt: BASE_URL/share
        Call<ResponseBody> sendEventToShare(@Body Event event); //POST-Anfrage, um Event in öffentliche Datenbank zum teilen zu speichern
            //Implementierung: Check
        @GET("share") // Endpunkt: BASE_URL/share
        Call<ResponseBody> getSharedEvent(@Body String idOfSharedEvent); //GET-Anfrage, um Event aus der öffentlichen Datenbank zu holen, basierend auf der ID des Events

    }

//GENERATE IMPLEMENTIERUNG
    // GET-Methode zum Abrufen der UUID vom /generate-Endpunkt
    public static void generateUuid(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String existingUuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        // Prüfen, ob bereits eine UUID existiert
        if (existingUuid != null) {
            Log.d("CloudService", "UUID already exists: " + existingUuid);
            return;
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.generateUuid();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // UUID aus der Antwort extrahieren
                        String uuid = response.body().string();

                        // UUID speichern
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PREF_UUID_KEY, uuid);
                        editor.apply();

                        Log.d("CloudService", "UUID successfully retrieved and saved: " + uuid);
                    } catch (IOException e) {
                        Log.d("CloudService", "Error parsing UUID: " + e.getMessage());
                    }
                } else {
                    Log.d("CloudService", "Error generating UUID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("CloudService", "Failed to generate UUID: " + t.getMessage());
            }
        });
    }


//TASKS IMPLEMENTIERUNG
    //POST an BASE_URL/tasks sendet
    public static void sendNewToDo(Context context, Task taskToStore) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.sendNewToDo(taskToStore, uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "Task successfully sent");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Failed to send task: " + t.getMessage());
            }
        });
    }

    //GET von BASE_URL/tasks TODO Rückgabe
    public static List<Task> getAllToDo(Context context) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.getAllToDo(uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //TODO Antwort der Cloud richtig speichern und irgendwie zurückgeben und TASKs richtig einlesen (Problem sind die setzung der Enums)

                    /*
                    try {
                        String responseBody = response.body().string();
                        List<Task> tasks = parseTasksFromJson(responseBody);
                        callback.onSuccess(tasks);
                    } catch (IOException e) {
                        callback.onError(e.getMessage());
                    }

                    // Callback-Interface
                        public interface ToDoCallback {
                            void onSuccess(List<Task> tasks);
                            void onError(String errorMessage);
                        }


                    //Verwendung

                getAllToDo(context, new ToDoCallback() {
                    @Override
                    public void onSuccess(List<Task> tasks) {
                        // Hier kannst du die Liste verwenden
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Fehler behandeln
                    }
                });
                     */


                    Log.d("CloudService", "All Tasks successfully retrieved and saved");
                } else {
                    Log.d("CloudService", "Error retrieving all Tasks: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("CloudService", "Failed to retrieve all Tasks: " + t.getMessage());
            }
        });
        //TODO
        return java.util.Collections.emptyList();
    }

    //PUT von BASE_URL/tasks
    public static void updateToDoInCloud(Context context, Task updatedTask) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.sendUpdatedTodo(updatedTask, uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "updated Task successfully sent");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error updating Task in Cloud " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Error updating Task in Cloud " + t.getMessage());
            }
        });
    }

    //DELETE von BASE_URL/tasks
    public static void deleteToDoInCloud(Context context, Task taskToDelete) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.deleteToDoInCloud(taskToDelete.getId(), uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "deleted Task successfully");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error deleting Task in Cloud " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Error deleting Task in Cloud " + t.getMessage());
            }
        });
    }



//CALENDER IMPLEMENTIERUNG
    //POST an BASE_URL/calendar sendet
    public static void sendNewEvent(Context context, Event eventToStore) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            return;
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.sendNewEvent(eventToStore, uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "Event successfully sent");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error beim POSTen des Events: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Failed to send task: " + t.getMessage());
            }
        });
    }

    //GET von BASE_URL/calendar TODO Rückgabe
    public static List<Task> getAllEvents(Context context) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.getAllEvents(uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //TODO Antwort der Cloud richtig speichern und irgendwie zurückgeben und TASKs richtig einlesen (Problem sind die setzung der Enums)

                    /*
                    try {
                        String responseBody = response.body().string();
                        List<Task> tasks = parseTasksFromJson(responseBody);
                        callback.onSuccess(tasks);
                    } catch (IOException e) {
                        callback.onError(e.getMessage());
                    }

                    // Callback-Interface
                        public interface ToDoCallback {
                            void onSuccess(List<Task> tasks);
                            void onError(String errorMessage);
                        }


                    //Verwendung

                getAllToDo(context, new ToDoCallback() {
                    @Override
                    public void onSuccess(List<Task> tasks) {
                        // Hier kannst du die Liste verwenden
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Fehler behandeln
                    }
                });
                     */


                    Log.d("CloudService", "All Tasks successfully retrieved and saved");
                } else {
                    Log.d("CloudService", "Error retrieving all Tasks: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("CloudService", "Failed to retrieve all Tasks: " + t.getMessage());
            }
        });
        return java.util.Collections.emptyList();
    }

    //PUT von BASE_URL/tasks
    public static void updateEventInCloud(Context context, Event updatedEvent) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.sendUpdatedEvent(updatedEvent, uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "updated Event successfully sent");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error updating Event in Cloud " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Error updating Event in Cloud " + t.getMessage());
            }
        });
    }

    //DELETE von BASE_URL/calendar
    public static void deleteEventInCloud(Context context, Event eventToDelete) throws MissingUUIDException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(PREF_UUID_KEY, null);

        if (uuid == null) {
            Log.d("CloudService", "UUID not found. Generate UUID first.");
            throw new MissingUUIDException("keine Bekannte UUID: Kein Cloudaufruf möglich");
        }

        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.deleteEventInCloud(eventToDelete.getEvent_ID(), uuid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "deleted Event successfully");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error deleting Event in Cloud " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Error deleting Event in Cloud " + t.getMessage());
            }
        });
    }


//SHARE IMPLEMENTIERUNG
    //POST an BASE_URL/share sendet
    public static void sendEventToShare(Event eventToShare) {
        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.sendEventToShare(eventToShare);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Erfolg
                    Log.d("CloudService", "Event successfully sent to " + BASE_URL + "share");
                } else {
                    // Fehler
                    Log.d("CloudService", "Error beim POSTen des Events: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Fehler bei der Kommunikation
                Log.d("CloudService", "Failed to send task: " + t.getMessage());
            }
        });
    }

    //GET von BASE_URL/share TODO Rückgabe
    public static Event getSharedEvent(String idOfSharedEvent) {
        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.getSharedEvent(idOfSharedEvent);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                   //TODO Event aus des Body erstellen und zurückgeben

                    Log.d("CloudService", "Shared Event successfully retrieved");
                } else {
                    Log.d("CloudService", "Error retrieving SharedEvent: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("CloudService", "Error retrieving SharedEvent: " + t.getMessage());
            }
        });
        //TODO
        return null;
    }
}
