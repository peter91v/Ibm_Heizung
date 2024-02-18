package com.example.ibm_heizung.classes;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.example.ibm_heizung.MainActivity;
import com.example.ibm_heizung.R;
import com.example.ibm_heizung.classes.DataObjects.Sensor;

import java.util.HashMap;
import java.util.Map;

public class ViewDataController {
    private Map<String, Sensor> dataMap;

    public ViewDataController(Map<String, Sensor> dataMap) {
        this.dataMap = dataMap;
    }

    public ViewDataController() {
        Map<String, Sensor> dataMap = new HashMap<>();
    }

    public Map<String, Sensor> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Sensor> dataMap) {
        this.dataMap = dataMap;
    }

    @SuppressLint("SetTextI18n")
    public void updateSensorViews(MainActivity mainActivity) {
        // Hier werden die TextViews basierend auf den Sensor-IDs aktualisiert
        if (dataMap != null && !dataMap.isEmpty()) {
            for (Map.Entry<String, Sensor> entry : dataMap.entrySet()) {
                String sensorId = entry.getKey();
                Sensor sensor = entry.getValue();
                // Hier wird der entsprechende TextView basierend auf der Sensor-ID gesucht und aktualisiert
                TextView textView = mainActivity.findViewById(getTextViewIdForSensor(sensorId));
                if (textView != null) {
                    textView.setText(Double.toString(sensor.getGrad()));
                }
            }
        }
    }

    private int getTextViewIdForSensor(String sensorId) {
        switch (sensorId) {
            case "201":
                return R.id.lblTempUGGangBoden;
            case "202":
                return R.id.lblTempUGHobby;
            case "203":
                return R.id.lblTempUGWohnzimmer;
            case "301":
                return R.id.lblTempEGKuecheBoden;
            case "302":
                return R.id.lblTempEGGangBoden;
            case "303":
                return R.id.lblTempEGWohnzimmer;
            case "304":
                return R.id.lblTempEGSchlafzimmer;
            case "305":
                return R.id.lblTempEGGang;
            case "401":
                return R.id.lblTempOGKueche;
            case "402":
                return R.id.lblTempOGWohnzimmer;
            case "403":
                return R.id.lblTempOGBuero;
            case "404":
                return R.id.lblTempOGSchlafzimmer;
            case "405":
                return R.id.lblTempOGGang;
            case "410":
                return R.id.lblTempOGBadBoden;
            case "501":
                return R.id.lblTempSued;
            case "502":
                return R.id.lblTempNord;
            case "510":
                return R.id.lblTempDach;
            case "601":
                return R.id.lblTempSolar;
            // Fügen Sie weitere Cases für weitere Sensor-IDs hinzu, falls benötigt
            default:
                return 0; // Wenn keine passende TextView-ID gefunden wird
        }
    }
}
