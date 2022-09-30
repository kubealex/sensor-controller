package org.acme.service.impl;

import org.acme.model.SensorData;
import org.acme.service.IDataProducer;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataProducerAMQ implements IDataProducer{
    private final MeterRegis
    private static final Logger logger = LoggerFactory.getLogger("DataProducerAMQ");

    @Channel("sensor-data-in") Emitter<SensorData> sensorDataEmitter;
    public void sendData(SensorData data) {
        sensorDataEmitter.send(data);
        logger.debug("test");
    }
}
