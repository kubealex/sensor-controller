package org.acme.service;

import jakarta.inject.Inject;

import org.acme.model.AnomalyEvent;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.annotations.Broadcast;

public class DataSenderAMQ {

    @Broadcast
    @Inject
    @Channel("anomaly-data-out")
    Emitter<AnomalyEvent> anomalyEmitter;

    public void sendData(AnomalyEvent data) {
        anomalyEmitter.send(data);
    }
}
