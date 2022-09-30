package org.acme;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.model.SensorData;
import org.acme.service.IDataProducer;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name= "Sensor Controller")
@Path("/api/v1")
public class GreetingResource {

    @Inject
    IDataProducer dataProducerAMQ;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/data")
    public String sendData(SensorData sensorData) {
        System.out.println("TEST");
        dataProducerAMQ.sendData(sensorData);
        return "OK";
    }
}