package com.garden.dop.resource;

import com.garden.dop.data.PlantProfile;
import com.garden.dop.workflow.PlantProfileWorkflow;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * An endpoint for PlantProfile records.
 */
@Path("/plantprofile")
public class PlantProfileResource {

    @Inject
    PlantProfileWorkflow plantProfileWorkflow;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlantProfile> getPlantProfiles() {
        return plantProfileWorkflow.getAllPlantProfiles();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlantProfile getPlantProfile(@PathParam("id") long id) {
        return plantProfileWorkflow.getPlantProfileById(id);
    }

    @POST
    public Response createPlantProfile(PlantProfile plantProfile) {
        plantProfileWorkflow.createPlantProfile(plantProfile);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response updatePlantProfile(PlantProfile plantProfile) {
        plantProfileWorkflow.updatePlantProfile(plantProfile);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlantProfile(@PathParam("id") long id) {
        plantProfileWorkflow.deletePlantProfileById(id);
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
