package com.example.ibm_heizung.classes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService extends Service {
    private static final String BASE_URL = "http://192.168.178.130:8080/@@ibm.jsonapi/";

    private final ApiService apiService;

    public RestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void fetchDataFromServer(DataCallback callback) {
        final Map<String, Sensor> dataMap = new HashMap<>();
        Call<Map<String, Sensor>> call = apiService.getSensorDataFromServer();
        call.enqueue(new Callback<Map<String, Sensor>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Sensor>> call, @NonNull Response<Map<String, Sensor>> response) {
                if (response.isSuccessful()) {
                    Map<String, Sensor> dataMap = response.body();
                    callback.onDataReceived(dataMap);
                } else {
                    Log.e("RestService", "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Sensor>> call, @NonNull Throwable t) {
                Log.e("RestService", "Fehler beim Abrufen der Daten: " + t.getMessage());
            }
        });
    }

    public void fetchGpioStateFromServer(GPIODownloadCallback callback) {
        Call<Map<String, GPIOHead>> call = apiService.getGpioDataFromServer();
        call.enqueue(new Callback<Map<String, GPIOHead>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, GPIOHead>> call, @NonNull Response<Map<String, GPIOHead>> response) {
                if (response.isSuccessful()) {
                    Map<String, GPIOHead> dataMap = response.body();
                    callback.onGpioDataReceived(dataMap);
                } else {
                    Log.e("RestService", "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, GPIOHead>> call, @NonNull Throwable t) {
                Log.e("RestService", "Fehler beim Abrufen der GPIO-Daten: " + t.getMessage());
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public interface DataCallback {
        void onDataReceived(Map<String, Sensor> result);
    }

    public interface GPIODownloadCallback {
        void onGpioDataReceived(Map<String, GPIOHead> gpioData);
    }
}