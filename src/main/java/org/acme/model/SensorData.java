package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorData {
    @JsonProperty("sensor_location")
    private String location;
    @JsonProperty("sensor_data")
    private Double temperature;
    @JsonProperty("sensor_id")
    private String deviceID;

    public SensorData(String location, Double temperature, String deviceID) {
        this.location = location;
        this.temperature = temperature;
        this.deviceID = deviceID;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public String getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
