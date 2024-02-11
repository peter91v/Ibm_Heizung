package com.example.ibm_heizung.classes;

import com.google.gson.annotations.SerializedName;

public class GPIOState {
    @SerializedName("id")
    private String id;

    @SerializedName("text")
    private String text;

    @SerializedName("status")
    private boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}