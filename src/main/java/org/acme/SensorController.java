package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.model.SensorData;
import org.acme.service.IDataProducer;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.logging.Log;
import io.vertx.core.json.Json;

@Tag(name = "Sensor Controller")
@ApplicationScoped
@Path("/api/v1")
public class SensorController {
    @Inject
    IDataProducer dataProducerAMQ;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/data")
    public Response sendData(SensorData sensorData) {
        Log.info("Received sensor data from device");
        dataProducerAMQ.sendData(sensorData);
        Log.info(Json.encode(sensorData));
        return Response.ok().build();
    }
}