package org.acme;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.acme.model.AnomalyEvent;
import org.acme.model.InfluxQueries;
import org.acme.service.DataSenderAMQ;
import org.acme.service.InfluxDBService;
import org.acme.service.InfluxDBServiceException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.influxdb.query.FluxRecord;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.json.Json;

@ApplicationScoped
public class SensorController {

    @ConfigProperty(name = "sensor.threshold", defaultValue = "30.0")
    Double sensorThreshold;
    @ConfigProperty(name = "sensor.device-id", defaultValue = "ext-01")
    String deviceID;
    @ConfigProperty(name = "sensor.location", defaultValue = "fridge-room-c1-outdoor")
    String location;

    @Inject
    DataSenderAMQ dataSenderAMQ;
    @Inject
    InfluxDBService influxDBService;

    private Double actualValue = 0.0;
    private Double deltaTemperature = 0.0;

    @Scheduled(every = "20s")
    public void handleData() throws InfluxDBServiceException {
        Log.info("Received sensor data from device");
        anomalyDetection();
        Log.info("Trying again in 20s");
    }

    public Boolean anomalyDetection() throws InfluxDBServiceException {
        List<FluxRecord> externalTemperature = influxDBService
                .queryInfluxDB(InfluxQueries.queryBuilder("temperature", deviceID, location));
        Integer sampleIndex = externalTemperature.size() - 1;
        deltaTemperature = (Double) externalTemperature.get(sampleIndex).getValue() - sensorThreshold;
        actualValue = (Double) externalTemperature.get(sampleIndex).getValue();

        if (Math.abs(deltaTemperature) > 2.0) {
            Log.error("Anomaly detected: temperature is above threshold");
            sensorThreshold = actualValue;
            Log.warn("ALERT SENT");
            AnomalyEvent anomalyEvent = new AnomalyEvent(location, deviceID, deltaTemperature,
                    "temperatureAnomaly");
            Log.info(Json.encode(anomalyEvent));
            dataSenderAMQ.sendData(anomalyEvent);
            return true;
        }
        return false;
    }
}