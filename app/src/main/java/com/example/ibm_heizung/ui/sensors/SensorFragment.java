package com.example.ibm_heizung.ui.sensors;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ibm_heizung.MainActivity;
import com.example.ibm_heizung.R;
import com.example.ibm_heizung.classes.DataObjects.Sensor;
import com.example.ibm_heizung.ui.sensors.SensorRecyclerViewAdapter;

import java.util.List;

public class SensorFragment extends Fragment{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public SensorFragment() {
    }

    private RecyclerView recyclerView;
    private SensorRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
private List<Sensor> sensorList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_list, container, false);

        recyclerView = view.findViewById(R.id.sensorlist);
//        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout2);
//        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        Context context = inflater.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        MainActivity mainActivity = (MainActivity) requireActivity();
        sensorList = mainActivity.getSensorList();
        adapter = new SensorRecyclerViewAdapter(sensorList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        Log.d(TAG, "refreshData() called");

//        Context context = requireContext();
////        MainActivity mainActivity = (MainActivity) requireActivity();
////        List<Sensor> updatedSensorList = mainActivity.getSensorList();
//        if (updatedSensorList != null) {
//            adapter = new SensorRecyclerViewAdapter(updatedSensorList);
//            adapter.setData(updatedSensorList);
//            recyclerView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
//
//        swipeRefreshLayout.setRefreshing(false);
    }

}
