package com.garden.dop.service;

import com.garden.dop.data.PlantProfile;
import com.garden.dop.repository.PlantProfileRepository;
import jakarta.inject.Inject;

public class PlantProfileService {

    @Inject
    private PlantProfileRepository plantProfileRepository;

    // get all profiles


    // create a plant
    public void createPlantProfile(PlantProfile plantProfile){
        plantProfileRepository.persist(
                new PlantProfile(
                        plantProfile.id(),
                        plantProfile.name(),
                        plantProfile.family(),
                        plantProfile.genus(),
                        plantProfile.species(),
                        plantProfile.spreadRadius(),
                        plantProfile.daysDryDown(),
                        plantProfile.daysToHarvest(),
                        plantProfile.hardinessZone(),
                        plantProfile.lifeSpan(),
                        plantProfile.lowLight()
                )
        );
    }

    // update a plant

    // delete a plant

}
