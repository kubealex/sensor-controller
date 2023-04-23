package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.model.InfluxQueries;
import org.acme.service.InfluxDBService;
import org.acme.service.InfluxDBServiceException;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class SensorController {

    @Inject
    InfluxDBService influxDBService;

    @Scheduled(every = "20s")
    public void handleData() throws InfluxDBServiceException {
        String query = InfluxQueries.queryBuilder("temperature", "sensor-t1", "fridge-room-c1-indoor");
        Log.info("Received sensor data from device");
        influxDBService.queryInfluxDB(query);
        Log.info("Trying again in 20s");
    }
}