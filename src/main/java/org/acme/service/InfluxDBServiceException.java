package org.acme.service;

public class InfluxDBServiceException extends Exception {
    public InfluxDBServiceException(String message) {
        super(message);
    }
}
