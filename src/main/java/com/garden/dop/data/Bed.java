package com.garden.dop.data;

public record Bed (
        long id,
        long gardenId,
        String name,
        double length,
        double width,
        boolean lowLight
) {}
