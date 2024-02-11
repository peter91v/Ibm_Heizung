package com.example.ibm_heizung.ui.sensors;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ibm_heizung.R;
import com.example.ibm_heizung.classes.Sensor;

import java.util.ArrayList;
import java.util.List;

public class SensorRecyclerViewAdapter extends RecyclerView.Adapter<SensorRecyclerViewAdapter.ViewHolder> {

    private List<Sensor> sensorList;
    private Context context;

    public SensorRecyclerViewAdapter(Context context, List<Sensor> sensorList) {
        this.context = context;
        if (sensorList != null) {
            this.sensorList = new ArrayList<>(sensorList);
        } else {
            this.sensorList = new ArrayList<>(); // Stelle sicher, dass die Liste initialisiert wird, auch wenn sie null ist
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_sensor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (sensorList != null && position < sensorList.size()) {
            Sensor sensor = sensorList.get(position);

            // Ändern Sie die Hintergrundfarbe basierend auf der Position
            if (position % 2 == 0) {
                // Gerade Positionen
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.material_dynamic_primary10));
            } else {
                // Ungerade Positionen
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.material_dynamic_secondary10));
            }
            if (sensor != null) {
                holder.txtSensorId.setText(String.valueOf(sensor.getCode()));
                holder.txtText.setText(String.valueOf(sensor.getText()));
                holder.txtZeit.setText(sensor.getZeit());
                holder.txtGrad.setText(String.valueOf(sensor.getGrad()));
            } else {
                // Wenn die Sensor-Instanz null ist, setzen Sie die Ansichten auf einen Standardwert oder leeren Text.
                holder.txtSensorId.setText("");
                holder.txtText.setText("");
                holder.txtZeit.setText("");
                holder.txtGrad.setText("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
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
    }
}
