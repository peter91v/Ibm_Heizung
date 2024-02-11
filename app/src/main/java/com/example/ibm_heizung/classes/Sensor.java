package com.example.ibm_heizung.classes;

import com.google.gson.annotations.SerializedName;

public class Sensor {
    @SerializedName("id")
    private String id;

    @SerializedName("text")
    private String text;

    @SerializedName("status")
    private int status;

    @SerializedName("grad")
    private double grad;

    @SerializedName("class")
    private String classType;

    @SerializedName("loc")
    private String loc;

    @SerializedName("datum")
    private String datum;

    @SerializedName("zeit")
    private String zeit;

    @SerializedName("sid")
    private String sid;

    @SerializedName("code")
    private int code;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }

    public double getGrad() {
        return grad;
    }

    public String getClassType() {
        return classType;
    }

    public String getLoc() {
        return loc;
    }

    public String getDatum() {
        return datum;
    }

    public String getZeit() {
        return zeit;
    }

    public String getSid() {
        return sid;
    }
    public int getCode() {
        return code;
    }
    private String toString(int code) {
        return toString(code);
    }


}