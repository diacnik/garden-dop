package com.garden.dop.data;

import java.time.LocalDate;

public record Planting(
        long id,
        long bedId,
        long plantId,
        String nickname,
        LocalDate datePlanted,
        LocalDate dateWatered
) {}
