package org.acme.service.impl;

import javax.xml.crypto.Data;

import org.acme.model.SensorData;
import org.acme.service.IDataProducer;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import io.quarkus.logging.Log;
public class DataProducerAMQ implements IDataProducer{

    private Counter sampleCounter;
    private final MeterRegistry sentSamples;

    @Channel("sensor-data-in") Emitter<SensorData> sensorDataEmitter;

    DataProducerAMQ(MeterRegistry sentSamples) {
        this.sentSamples=sentSamples;
        sampleCounter = this.sentSamples.counter("amq.samples.sent");
    }
    public void sendData(SensorData data) {
        sampleCounter.increment();
        sensorDataEmitter.send(data);
        Log.info("test");
    }
}
