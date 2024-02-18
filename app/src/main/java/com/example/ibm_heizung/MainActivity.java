package com.example.ibm_heizung;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ibm_heizung.classes.RestService;
import com.example.ibm_heizung.classes.DataObjects.Sensor;
import com.example.ibm_heizung.classes.ViewDataController;
import com.example.ibm_heizung.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Map<String, Sensor> dataMap;
    private RestService restService;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "MyAppPrefs";
    private static final String SENSOR_DATA_KEY = "SensorData";
    private static final long EXPIRATION_TIME_MS = 1 * 60 * 1000; // 1 Minute in Millisekunden

    private List<Sensor> sensorList;
    private ViewDataController viewDataController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_sensors, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        restService = new RestService(this);
        viewDataController = new ViewDataController();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        checkDataValidityAndLoadIfNeeded();
    }

    public void checkDataValidityAndLoadIfNeeded() {
        long lastUpdateTimeMillis = sharedPreferences.getLong("last_update_time", 0);
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceMillis = currentTimeMillis - lastUpdateTimeMillis;

        if (lastUpdateTimeMillis == 0 || timeDifferenceMillis > EXPIRATION_TIME_MS) {
            fetchData();
        } else {
            loadData();
        }
//        updateFragment();
    }

//    private void updateFragment() {
//        SensorFragment fragment = (SensorFragment) getSupportFragmentManager().findFragmentById(R.id.sensorlist);
//        if (fragment != null) {
//            fragment.updateAdapterData(sensorList);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean isScrollingUp() {
        View child = binding.navView.getChildAt(0);
        return child != null && child.getScrollY() > 0;
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SENSOR_DATA_KEY, "");
        Type type = new TypeToken<HashMap<String, Sensor>>() {
        }.getType();
        dataMap = gson.fromJson(json, type);
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        viewDataController.setDataMap(dataMap);
        sensorList = new ArrayList<>(dataMap.values());
        viewDataController.updateSensorViews(MainActivity.this);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_update_time", System.currentTimeMillis());
        Gson gson = new Gson();
        String json = gson.toJson(dataMap);
        editor.putString(SENSOR_DATA_KEY, json);
        editor.apply();
    }
    public void filterSensorList(List<Sensor> sensorList) {
        Iterator<Sensor> iterator = sensorList.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            if (sensor == null || sensor.getZeit().isEmpty() || sensor.getCode() == 0) {
                iterator.remove();
            }
        }
    }
    private void fetchData() {
        restService.fetchDataFromServer(new RestService.DataCallback() {
            @Override
            public void onDataReceived(Map<String, Sensor> result) {
                runOnUiThread(() -> {
                    dataMap = result;
                    if (!dataMap.isEmpty()) {
                        saveData();
                        viewDataController.setDataMap(dataMap);
                        sensorList = new ArrayList<>(dataMap.values());
                        viewDataController.updateSensorViews(MainActivity.this);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), R.string.sensordata_Loaded, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.sensordata_not_Loaded, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isScrollingUp()) {
                checkDataValidityAndLoadIfNeeded();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public List<Sensor> getSensorList() {
        checkDataValidityAndLoadIfNeeded();
//        filterSensorList(sensorList);
        return sensorList;
    }
}




















