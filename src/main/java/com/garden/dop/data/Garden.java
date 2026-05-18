package com.garden.dop.data;

import java.util.UUID;

public record Garden(
        long id,
        UUID accountId,
        String name,
        boolean indoors,
        int hardinessZone,
        boolean isPublic
) {}
