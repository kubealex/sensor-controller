package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnomalyEvent {
    @JsonProperty("sensor_location")
    private String location;
    @JsonProperty("sensor_id")
    private String deviceID;
    @JsonProperty("sensor_temperature_delta")
    private Double temperatureDelta;
    @JsonProperty("sensor_event_type")
    private String eventType;

    public AnomalyEvent(String location, String deviceID, Double temperatureDelta, String eventType){
        this.location = location;
        this.deviceID = deviceID;
        this.temperatureDelta = temperatureDelta;
        this.eventType = eventType;
    }
}
