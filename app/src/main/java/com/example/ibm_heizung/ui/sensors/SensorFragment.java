package com.example.ibm_heizung.ui.sensors;

import android.annotation.SuppressLint;
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
import com.example.ibm_heizung.SensorListActivity;
import com.example.ibm_heizung.R;
import com.example.ibm_heizung.classes.Sensor;
import com.example.ibm_heizung.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class SensorFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public SensorFragment() {
    }
    private RecyclerView recyclerView;
    private SensorRecyclerViewAdapter adapter;

    private FragmentHomeBinding binding;
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
        recyclerView = view.findViewById(R.id.sensorlist);

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
            adapter = new SensorRecyclerViewAdapter(getContext(), sensorList);
            recyclerView.setAdapter(adapter);
            // LayoutManager einstellen
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Überprüfen, ob die MainActivity ist und casten
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            // Daten neu laden
            mainActivity.checkDataValidityAndLoadIfNeeded();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapterData(List<Sensor> sensorList) {
        if (adapter != null) {
        adapter.setData(sensorList);
        adapter.notifyDataSetChanged();
    }
    }
}
