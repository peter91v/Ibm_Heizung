package com.example.ibm_heizung.classes;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getmessung")
        //
    Call<Map<String, Sensor>> getSensorDataFromServer();

    @GET("digitalio")
        //
    Call<Map<String, GPIOHead>> getGpioDataFromServer();
}
