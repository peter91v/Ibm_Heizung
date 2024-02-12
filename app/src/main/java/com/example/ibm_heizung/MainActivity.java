package com.example.ibm_heizung;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ibm_heizung.classes.RestService;
import com.example.ibm_heizung.classes.Sensor;
import com.example.ibm_heizung.classes.ViewDataController;
import com.example.ibm_heizung.databinding.ActivityMainBinding;
import com.example.ibm_heizung.ui.sensors.SensorFragment;
import com.example.ibm_heizung.ui.sensors.SensorRecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final long EXPIRATION_TIME_MS = 1 * 60 * 1000; // 2 Minuten in Millisekunden

    private List<Sensor> sensorList;
    private ViewDataController viewDataController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_sensors, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        restService = new RestService();
        viewDataController = new ViewDataController();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // Laden der gespeicherten Daten
        loadData();
        if (dataMap.isEmpty()) {
            checkDataValidityAndLoadIfNeeded();
        }
        binding.appBarMain.fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hier wird die Methode aufgerufen
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isScrollingUp()) {
                    fetchData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 3000);
    }

    private void fetchData() {
        restService.fetchDataFromServer(new RestService.DataCallback() {
            @Override
            public void onDataReceived(Map<String, Sensor> result) {
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
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_update_time", System.currentTimeMillis());
        Gson gson = new Gson();
        String json = gson.toJson(dataMap);
        editor.putString(SENSOR_DATA_KEY, json);
        editor.apply();
    }


    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SENSOR_DATA_KEY, "");
        Type type = new TypeToken<HashMap<String, Sensor>>(){}.getType();
        dataMap = gson.fromJson(json, type);
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        viewDataController.setDataMap(dataMap);
        viewDataController.updateSensorViews(MainActivity.this);
    }

    private boolean isScrollingUp() {
        View child = binding.navView.getChildAt(0);
        if (child != null) {
            return child.getScrollY() > 0;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDataValidityAndLoadIfNeeded();
        updateFragment();
    }

    private void updateFragment() {
        SensorFragment fragment = (SensorFragment) getSupportFragmentManager().findFragmentById(R.id.sensorlist);
        if (fragment != null) {
            fragment.updateAdapterData(sensorList);
        }
    }
    public void checkDataValidityAndLoadIfNeeded() {
        long lastUpdateTimeMillis = sharedPreferences.getLong("last_update_time", 0);
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceMillis = currentTimeMillis - lastUpdateTimeMillis;

        if (lastUpdateTimeMillis == 0 || timeDifferenceMillis > EXPIRATION_TIME_MS) {
            fetchData();
            updateFragment();
        } else {
            loadData();
            updateFragment();
        }
    }
    public List<Sensor> getSensorList() {
        checkDataValidityAndLoadIfNeeded();
        return sensorList;
    }
}
