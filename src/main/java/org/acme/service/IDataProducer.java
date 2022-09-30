package org.acme.service;

import org.acme.model.SensorData;

public interface IDataProducer {
    void sendData(SensorData data);
}
