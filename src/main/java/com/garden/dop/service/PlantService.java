package com.garden.dop.service;

import com.garden.dop.data.Plant;

import java.time.LocalDate;

public class PlantService {

    // get all plants

    // get all plants in a garden

    // get all plants in a bed

    // create a plant
    public Plant createPlant(Plant plant){
        return new Plant(
                plant.id(),
                plant.name(),
                plant.genus(),
                plant.species(),
                plant.spreadRadius(),
                plant.currentRadius(),
                plant.datePlanted(),
                plant.dateWatered(),
                plant.daysDryDown(),
                plant.daysToHarvest(),
                plant.hardinessZone(),
                plant.lifeSpan(),
                plant.lowLight()
        );
    }

    // water a plant
    // will move someone else later
    public Plant waterPlant(Plant plant){
        return new Plant(
                plant.id(),
                plant.name(),
                plant.genus(),
                plant.species(),
                plant.spreadRadius(),
                plant.currentRadius(),
                plant.datePlanted(),
                LocalDate.now(),
                plant.daysDryDown(),
                plant.daysToHarvest(),
                plant.hardinessZone(),
                plant.lifeSpan(),
                plant.lowLight()
        );
    }

    // update a plant

    // delete a plant

}
