package org.acme.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import io.quarkus.logging.Log;

@ApplicationScoped
public class InfluxDBService {
    @ConfigProperty(name = "influxdb.url", defaultValue = "http://localhost:8086")
    String influxdbURL;
    @ConfigProperty(name = "influxdb.token")
    String influxdbToken;
    @ConfigProperty(name = "influxdb.bucket")
    String influxdbBucket;
    @ConfigProperty(name = "influxdb.organization")
    String influxdbOrg;
    private InfluxDBClient influxDBClient;

    public List<FluxRecord> queryInfluxDB(String query) throws InfluxDBServiceException {
        try {
            this.influxDBClient = InfluxDBClientFactory.create(influxdbURL, influxdbToken.toCharArray(),
                    "devconf-demo");
            Log.info("Connected to InfluxDB");
            QueryApi queryApi = influxDBClient.getQueryApi();
            List<FluxTable> tables = queryApi.query(query);
            List<FluxRecord> queryRecords = tables.get(0) .getRecords();
                for (FluxRecord fluxRecord : queryRecords) {
                    System.out.println(fluxRecord.getValueByKey("_value"));
                }
            influxDBClient.close();
            Log.info("Disconnected from InfluxDB");
            return queryRecords;
        } catch (InfluxException ie) {
            throw new InfluxDBServiceException("An error occurred while reading from InfluxDB: " + ie.getMessage());
        }
    }
}
