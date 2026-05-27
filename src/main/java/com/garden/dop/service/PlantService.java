package com.garden.dop.service;

import com.garden.dop.data.PlantProfile;

import java.time.LocalDate;

public class PlantService {

    // get all plants

    // get all plants in a garden

    // get all plants in a bed

    // create a plant
    public PlantProfile createPlant(PlantProfile plantProfile){
        return new PlantProfile(
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
        );
    }

    // update a plant

    // delete a plant

}
