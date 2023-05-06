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

    @ConfigProperty(name = "sensor.threshold", defaultValue = "29.0")
    Double sensorThreshold;
    @ConfigProperty(name = "sensor.device-id", defaultValue = "ext-01")
    String deviceID;
    @ConfigProperty(name = "sensor.location", defaultValue = "fridge-room-c1-outdoor")
    String location;

    @Inject
    DataSenderAMQ dataSenderAMQ;
    @Inject
    InfluxDBService influxDBService;

    private static Boolean alertSent = false;

    @Scheduled(every = "20s")
    public void handleData() throws InfluxDBServiceException {
        Log.info("Received sensor data from device");
        anomalyDetection();
        Log.info("Trying again in 20s");
    }

    public Boolean anomalyDetection() throws InfluxDBServiceException {
        List<FluxRecord> internalTemperature = influxDBService
                .queryInfluxDB(InfluxQueries.queryBuilder("temperature", deviceID, location));

        Double deltaTemperature = 0.0;
        if (internalTemperature.size() <= 1)
            deltaTemperature = (Double) internalTemperature.get(0).getValue() - sensorThreshold;
        else
            deltaTemperature = (Double) internalTemperature.get(1).getValue() - sensorThreshold;
        if (deltaTemperature > 2.0 || deltaTemperature < -2.0) {
            Log.error("Anomaly detected: temperature is above threshold");
            if (!alertSent) {
                Log.warn("ALERT SENT");
                AnomalyEvent anomalyEvent = new AnomalyEvent(location, deviceID, deltaTemperature,
                        "temperatureAnomaly");
                Log.info(Json.encode(anomalyEvent));
                dataSenderAMQ.sendData(anomalyEvent);
                alertSent = true;
            }
            return true;
        }
        alertSent = false;
        return false;
    }
}