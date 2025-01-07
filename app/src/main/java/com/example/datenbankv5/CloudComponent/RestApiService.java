package com.example.datenbankv5.CloudComponent;

import android.util.Log;

import com.example.datenbankv5.ToDoComponent.core.Task;

import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RestApiService {

    // Basis-URL der API
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    // Variable für die UUID, die nach der Antwort vom /generate-Endpunkt gespeichert wird
    private static String MY_UUID = "";

    // Retrofit-Instanz
    private static final Retrofit retrofitInstance = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON-Converter
            .build();

    // API-Schnittstelle
    private interface ApiService {
        @POST("tasks") // Endpunkt: http://10.0.2.2:8080/api/tasks
        Call<ResponseBody> sendNewToDo(@Body Task task, @Query("param") String uuid); // Query-Parameter hinzufügen

        @GET("generate") // Endpunkt für das Generieren der UUID
        Call<ResponseBody> generateUuid(); // GET-Anfrage für UUID
    }

    // Methode zum Abrufen der UUID vom /generate-Endpunkt
    public static void generateUuid() {
        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.generateUuid();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Hier wird die UUID aus der Antwort extrahiert, die angenommen wird, dass sie als String zurückgegeben wird
                        String uuid = response.body().string();
                        MY_UUID = uuid; // Die UUID wird in der Instanzvariablen gespeichert
                        Log.d("CloudService", "UUID successfully retrieved: " + MY_UUID);
                    } catch (Exception e) {
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

    // Methode, die den API-Aufruf mit einem Parameter durchführt
    public static void sendNewToDo(Task taskToStore) {
        ApiService apiService = retrofitInstance.create(ApiService.class);

        // Die UUID aus der Instanzvariablen verwenden
        Call<ResponseBody> call = apiService.sendNewToDo(taskToStore, MY_UUID);

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
}
