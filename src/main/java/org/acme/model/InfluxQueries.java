package org.acme.model;

public class InfluxQueries {
    public static String queryBuilder(String measurement, String deviceID, String location) {
        String query = "from(bucket: \"devconf-demo\") |> range(start: -5m) |> filter(fn: (r) => r[\"_measurement\"] == \"SensorData\") |> filter(fn: (r) => r[\"_field\"] == \"" + measurement + "\") |> filter(fn: (r) => r[\"deviceID\"] == \"" + deviceID + "\") |> filter(fn: (r) => r[\"location\"] == \"" + location + "\") |> aggregateWindow(every: 5m, fn: mean, createEmpty: false) |> yield(name: \"mean\")";
        return query;
    }
}
