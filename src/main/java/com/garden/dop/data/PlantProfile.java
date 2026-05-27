package com.garden.dop.data;

public record PlantProfile(
        long id,
        String name,
        String family,
        String genus,
        String species,
        int spreadRadius,
        int daysDryDown,
        int daysToHarvest,
        int hardinessZone,
        int lifeSpan,
        boolean lowLight
) {}
