package com.example.ibm_heizung.classes.DataObjects;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class GPIOHead {
    @SerializedName("titel")
    private String title;

    @SerializedName("available")
    private boolean available;

    @SerializedName("input")
    private Map<String, GPIOState> input;

    @SerializedName("output")
    private Map<String, GPIOState> output;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Map<String, GPIOState> getInput() {
        return input;
    }

    public void setInput(Map<String, GPIOState> input) {
        this.input = input;
    }

    public Map<String, GPIOState> getOutput() {
        return output;
    }

    public void setOutput(Map<String, GPIOState> output) {
        this.output = output;
    }
}
