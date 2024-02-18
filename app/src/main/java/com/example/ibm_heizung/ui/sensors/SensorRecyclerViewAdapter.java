package com.example.ibm_heizung.ui.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibm_heizung.R;
import com.example.ibm_heizung.classes.DataObjects.Sensor;
import com.example.ibm_heizung.classes.DataObjects.SensorList;

import java.util.List;

public class SensorRecyclerViewAdapter extends RecyclerView.Adapter<SensorRecyclerViewAdapter.ViewHolder> {

    private SensorList sensorList;
//    private Context context;

    public SensorRecyclerViewAdapter(List<Sensor> sensorList) {
//        this.context = context;
        this.sensorList = new SensorList();
        this.sensorList.setSensorList(sensorList);
        this.sensorList.filterSensorList();
        this.sensorList.sortByProperty("code", true);
        Log.d("SensorRecyclerViewAdapter", "Created"); // Log-Ausgabe hinzufügen

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_sensor, parent, false);
        Log.d("SensorRecyclerViewAdapter", "ViewHolder"); // Log-Ausgabe hinzufügen
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sensor sensor = sensorList.getSensorByPosition(position);
        Log.d("SensorRecyclerViewAdapter", "onBindViewHolder"); // Log-Ausgabe hinzufügen
        if (sensor != null && sensor.isValid()) {
            holder.bind(sensor);
        }
    }

    @Override
    public int getItemCount() {
        return sensorList.getSize();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Sensor> sensorList) {
        this.sensorList.setSensorList(sensorList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSensorId;
        TextView txtText;
        TextView txtZeit;
        TextView txtGrad;

        ViewHolder(View itemView) {
            super(itemView);
            txtSensorId = itemView.findViewById(R.id.txtSensorId);
            txtText = itemView.findViewById(R.id.txtText);
            txtZeit = itemView.findViewById(R.id.txtZeit);
            txtGrad = itemView.findViewById(R.id.txtSensorValue);
        }

        void bind(Sensor sensor) {
            txtSensorId.setText(String.valueOf(sensor.getCode()));
            txtText.setText(sensor.getText());
            txtZeit.setText(sensor.getZeit());
            txtGrad.setText(String.valueOf(sensor.getGrad()));

            int backgroundColor = getBackgroundColor(getAdapterPosition());
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), backgroundColor));
        }

        private int getBackgroundColor(int position) {
            return position % 2 == 0 ? com.google.android.material.R.color.material_dynamic_primary10 : R.color.bluegray800;
        }
    }
}
