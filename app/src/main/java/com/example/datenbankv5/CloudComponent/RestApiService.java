package com.example.datenbankv5.CloudComponent;

import android.util.Log;

import com.example.datenbankv5.ToDoComponent.core.Task;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class RestApiService {

    // Basis-URL der API
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    // Retrofit-Instanz
    private static final Retrofit retrofitInstance = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON-Converter
            .build();

    // API-Schnittstelle
    private interface ApiService {
        @POST("tasks") // Endpunkt: https://example.com/api/tasks
        Call<ResponseBody> sendNewToDo(@Body Task task); // Sendet Task-Daten als JSON
    }

    // Methode, die einen boolean zurückgibt
    public static void sendNewToDo(Task taskToStore) {
        ApiService apiService = retrofitInstance.create(ApiService.class);
        Call<ResponseBody> call = apiService.sendNewToDo(taskToStore);

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
        /*
        try {
            // Synchronen API-Aufruf durchführen
            Response<Void> response = call.execute();
            return response.isSuccessful(); // true bei 2xx, false bei anderen Statuscodes
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Fehlerfall
        }

         */
    }
}
