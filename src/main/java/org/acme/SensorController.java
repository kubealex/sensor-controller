package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.service.InfluxDBService;
import org.acme.service.InfluxDBServiceException;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class SensorController {

    @Inject
    InfluxDBService influxDBService;

    @Scheduled(every = "20s")
    public void generateData() throws InfluxDBServiceException {
        String query = "from(bucket: \"devconf-demo\") |> range(start: -10m) |> filter(fn: (r) => r[\"_measurement\"] == \"SensorData\") |> filter(fn: (r) => r[\"_field\"] == \"temperature\") |> aggregateWindow(every: 10m, fn: mean, createEmpty: false) |> yield(name: \"mean\")";
        Log.info("Received sensor data from device");
        influxDBService.queryInfluxDB(query);
        Log.info("Trying again in 20s");
    }
}