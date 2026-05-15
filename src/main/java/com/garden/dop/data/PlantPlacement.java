package com.garden.dop.data;

public record PlantPlacement(
        long id,
        long bedId,
        long plantId,
        int spreadRadius
) {
}
