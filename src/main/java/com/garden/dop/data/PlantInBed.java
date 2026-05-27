package com.garden.dop.data;

import java.time.LocalDate;

public record PlantInBed (
        long id,
        long bedId,
        long plantProfileId,
        String nickname,
        LocalDate datePlanted,
        LocalDate dateWatered,
        PlantProfile plantProfile
) {}
