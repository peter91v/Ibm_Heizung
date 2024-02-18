package com.example.ibm_heizung;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibm_heizung.classes.RestService;
import com.example.ibm_heizung.classes.DataObjects.Sensor;
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
    private RestService restService;
    private Map<String, Sensor> dataMap;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "MyAppPrefs";
    private static final String SENSOR_DATA_KEY = "SensorData";

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    private List<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sensor_list);

        recyclerViewSensors = findViewById(R.id.sensorlist);
        recyclerViewSensors.setLayoutManager(new LinearLayoutManager(this));

        restService = new RestService(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // Laden der gespeicherten Daten
        loadData();
        fetchData();
    }

    private void fetchData() {
        restService.fetchDataFromServer(new RestService.DataCallback() {
            @Override
            public void onDataReceived(Map<String, Sensor> result) {
                // Update UI on the main UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataMap = result;
                        if (dataMap != null && !dataMap.isEmpty()) {
                            sensorList = new ArrayList<>(dataMap.values());
                            updateSensorList();
                            Toast.makeText(getApplicationContext(), R.string.sensordata_Loaded, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.sensordata_not_Loaded, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SENSOR_DATA_KEY, "");
        Type type = new TypeToken<HashMap<String, Sensor>>() {}.getType();
        dataMap = gson.fromJson(json, type);
        if (dataMap != null) {
            sensorList = new ArrayList<>(dataMap.values());
        }
    }

    public void updateSensorList() {
        SensorRecyclerViewAdapter sensorAdapter = new SensorRecyclerViewAdapter(this, sensorList);
        recyclerViewSensors.setAdapter(sensorAdapter);
    }
}
