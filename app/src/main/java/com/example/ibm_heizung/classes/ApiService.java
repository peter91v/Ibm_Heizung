package com.example.ibm_heizung.classes;

import com.example.ibm_heizung.classes.DataObjects.GPIOHead;
import com.example.ibm_heizung.classes.DataObjects.Sensor;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface  ApiService {
    @GET("getmessung")
        //
    Call<Map<String, Sensor>> getSensorDataFromServer();

    @GET("digitalio")
        //
    Call<Map<String, GPIOHead>> getGpioDataFromServer();
/*
    @POST("/your-endpoint")
    public default void receiveData(@Body YourDataClass data) {
        // Verarbeiten Sie die empfangenen Daten gemäß Ihren Anforderungen
        // Hier können Sie die Daten analysieren, in die Datenbank speichern usw.
        // Beispiel: Loggen der empfangenen Daten
        Log.d("RestService", "Empfangene Daten: " + data.toString());
    }
*/

}
