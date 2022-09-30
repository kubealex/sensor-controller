package org.acme;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.model.SensorData;
import org.acme.service.IDataProducer;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.logging.Log;

@Tag(name= "Sensor Controller")
@Path("/api/v1")
public class SensorController {
    @Inject
    IDataProducer dataProducerAMQ;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/data")
    public String sendData(SensorData sensorData) {
        Log.info("Test");
        dataProducerAMQ.sendData(sensorData);
        return "OK";
    }
}