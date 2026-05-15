package com.garden.dop.data;

import java.util.Date;

public sealed interface Plant permits Tree, Annual, Perennial, HousePlant {}
record Tree(
        long id,
        String name,
        String genus,
        String species,
        int trunkSpace
) implements Plant {}
record Annual(
        long id,
        String name,
        String genus,
        String species,
        int spreadRadius,
        int currentRadius,
        Date datePlanted,
        Date lastWatered,
        int daysDryDown,
        int daysToHarvest
) implements Plant {}
record Perennial(
        long id,
        String name,
        String genus,
        String species,
        int spreadRadius,
        int currentRadius,
        Date datePlanted,
        Date lastWatered,
        int daysDryDown,
        int hardinessZone,
        int lifeSpan
) implements Plant {}
record HousePlant(
        long id,
        String name,
        String genus,
        String species,
        int radius,
        Date lastWatered,
        int daysDryDown,
        boolean lowLight
) implements Plant {}

