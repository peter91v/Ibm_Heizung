package com.example.ibm_heizung.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ibm_heizung.classes.DataObjects.GPIOHead;
import com.example.ibm_heizung.classes.DataObjects.Sensor;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService {
    private static final String DEFAULT_BASE_URL = "http://192.168.178.130:8080/@@ibm.jsonapi/";

    private ApiService apiService;
    private String baseUrl;

    public RestService(Context context) {
        determineBaseUrl(context);
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void determineBaseUrl(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            NetworkUtils nu = new NetworkUtils();
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                    baseUrl = "http://192.168.178.130:8080/@@ibm.jsonapi/"; // WLAN-Verbindung
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    baseUrl = "http://website.secondmed.at/@@ibm.jsonapi/"; // Mobile Datenverbindung
                }
            }
        }
        if (baseUrl == null) {
            Log.e("RestService", "No network connection available, using default URL");
            baseUrl = DEFAULT_BASE_URL;
        }
    }

    public void fetchDataFromServer(DataCallback callback) {
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
                Log.e("RestService", "Error fetching data: " + t.getMessage());
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
                Log.e("RestService", "Error fetching GPIO data: " + t.getMessage());
            }
        });
    }

    public interface DataCallback {
        void onDataReceived(Map<String, Sensor> result);
    }

    public interface GPIODownloadCallback {
        void onGpioDataReceived(Map<String, GPIOHead> gpioData);
    }
}
