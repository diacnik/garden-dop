package com.garden.dop.data;

import java.time.LocalDate;

public record Plant (
        long id,
        String name,
        String genus,
        String species,
        int spreadRadius,
        int currentRadius,
        LocalDate datePlanted,
        LocalDate dateWatered,
        int daysDryDown,
        int daysToHarvest,
        int hardinessZone,
        int lifeSpan,
        boolean lowLight
) {}
