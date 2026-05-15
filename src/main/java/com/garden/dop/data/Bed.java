package com.garden.dop.data;

public sealed interface Bed permits GardenBed, Room {}
record GardenBed(
        long id,
        long yardId,
        String name,
        double length,
        double width
) implements Bed {}
record Room(
        long id,
        long houseId,
        int spaces,
        boolean lowLight
) implements Bed {}
