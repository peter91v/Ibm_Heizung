package com.example.ibm_heizung.classes.DataObjects;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SensorList {
    private List<Sensor> sensorList;

    public SensorList() {
        this.sensorList = new ArrayList<>();
    }

    public void addSensor(Sensor sensor) {
        sensorList.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        sensorList.remove(sensor);
    }

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    public void filterSensorList() {
        Iterator<Sensor> iterator = sensorList.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            if (sensor == null || sensor.getZeit().isEmpty() || sensor.getCode() == 0) {
                iterator.remove();
            }
        }
    }

    public void sortByProperty(String propertyName, boolean ascending) {
        Comparator<Sensor> comparator = new Comparator<Sensor>() {
            @Override
            public int compare(Sensor sensor1, Sensor sensor2) {
                try {
                    Field field = Sensor.class.getDeclaredField(propertyName);
                    field.setAccessible(true);

                    Comparable value1 = (Comparable) field.get(sensor1);
                    Comparable value2 = (Comparable) field.get(sensor2);

                    int result = value1.compareTo(value2);
                    return ascending ? result : -result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };

        Collections.sort(sensorList, comparator);
    }
    public Sensor getSensorById(String id) {
        for (Sensor sensor : sensorList) {
            if (sensor.getId().equals(id)) {
                return sensor;
            }
        }
        return null;
    }

    public Sensor getSensorByPosition(int position) {
        if (position >= 0 && position < sensorList.size()) {
            return sensorList.get(position);
        }
        return null;
    }

    public int getSize() {
        return sensorList.size();
    }
}
