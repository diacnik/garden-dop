package com.garden.dop.workflow;

import com.garden.dop.data.PlantProfile;
import com.garden.dop.repository.PlantProfileRepository;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * A bean for handling the PlantProfile workflow, acting as the imperative shell of the functional service and repository layers.
 */
@ApplicationScoped
public class PlantProfileWorkflow {

    @Inject
    AgroalDataSource dataSource;

    public void createPlantProfile(PlantProfile plantProfileInput) {
        PlantProfileRepository.persist(dataSource, plantProfileInput);
    }

    public List<PlantProfile> getAllPlantProfiles() {
        return PlantProfileRepository.findAll(dataSource);
    }

    public PlantProfile getPlantProfileById(long id) {
        return PlantProfileRepository.findById(dataSource, id)
                .orElseThrow(() -> new NotFoundException("PlantProfile with id " + id + " not found"));
    }

    public void updatePlantProfile(PlantProfile plantProfileInput) {
        PlantProfileRepository.update(dataSource, plantProfileInput);
    }

    public void deletePlantProfileById(long id) {
        PlantProfileRepository.delete(dataSource, id);
    }
}
