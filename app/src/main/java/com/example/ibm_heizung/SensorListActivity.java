package com.example.ibm_heizung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.ibm_heizung.classes.RestService;
import com.example.ibm_heizung.classes.Sensor;
import com.example.ibm_heizung.ui.sensors.SensorRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSensors;
    private SensorRecyclerViewAdapter sensorAdapter;
    private RestService restService;
    private Map<String, Sensor> dataMap;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "MyAppPrefs";
    private static final String SENSOR_DATA_KEY = "SensorData";

    public List<Sensor> getSensorList() {
        return sensorList;
    }
    private List<Sensor> sensorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sensor_list);

        recyclerViewSensors = findViewById(R.id.sensorlist);
        recyclerViewSensors.setLayoutManager(new LinearLayoutManager(this));

        restService = new RestService();
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // Laden der gespeicherten Daten
        loadData();
        fetchData();
    }

    private void fetchData() {
        restService.fetchDataFromServer(new RestService.DataCallback() {
            @Override
            public void onDataReceived(Map<String, Sensor> result) {
                dataMap = result;
                if (!dataMap.isEmpty()) {
                    sensorList = new ArrayList<>(dataMap.values());
                    updateSensorList();
                    Toast.makeText(SensorListActivity.this, "Sensor data loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SensorListActivity.this, "Failed to load sensor data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SENSOR_DATA_KEY, "");
        Type type = new TypeToken<HashMap<String, Sensor>>(){}.getType();
        dataMap = gson.fromJson(json, type);
        if (dataMap != null) {
            sensorList = new ArrayList<>(dataMap.values());
        }
    }

    private void updateSensorList() {

        sensorAdapter = new SensorRecyclerViewAdapter((Context) this, sensorList);
        recyclerViewSensors.setAdapter(sensorAdapter);
    }
}
