package com.example.ibm_heizung.ui.sensors;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibm_heizung.MainActivity;
import com.example.ibm_heizung.R;
import com.example.ibm_heizung.SensorListActivity;
import com.example.ibm_heizung.classes.Sensor;
import com.example.ibm_heizung.placeholder.PlaceholderContent;

import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Sensors.
 */
public class SensorFragment extends Fragment {

    // Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // Parameter
    private int mColumnCount = 1;

    public SensorFragment() {
    }

    // Parameter initialization
    @SuppressWarnings("unused")
    public static SensorFragment newInstance(int columnCount) {
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_list, container, false);

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.sensorlist);

        // Check if the RecyclerView exists
        if (recyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            List<Sensor> sensorList = mainActivity.getSensorList();
            // Replace PlaceholderContent.ITEMS with your actual data map from SensorListActivity
            // Here, assuming you have a member variable sensorList in SensorListActivity
            SensorRecyclerViewAdapter adapter = new SensorRecyclerViewAdapter(context, sensorList);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
