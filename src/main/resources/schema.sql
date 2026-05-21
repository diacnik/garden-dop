CREATE TABLE account (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    auth_id VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE plant (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    family VARCHAR(255),
    genus VARCHAR(255),
    species VARCHAR(255),
    spread_radius INT,
    days_dry_down INT,
    days_to_harvest INT,
    hardiness_zone INT,
    life_span_years INT,
    low_light BOOLEAN
);

CREATE TABLE garden (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES account(id)
        ON DELETE CASCADE,
    name VARCHAR(255),
    indoors BOOLEAN,
    hardiness_zone INT,
    is_public BOOLEAN
);


CREATE TABLE bed (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    garden_id BIGINT NOT NULL REFERENCES garden(id)
        ON DELETE CASCADE,
    name VARCHAR(255),
    length INT,
    width INT,
    low_light BOOLEAN
);

CREATE TABLE plant_bed (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bed_id BIGINT NOT NULL REFERENCES bed(id)
        ON DELETE CASCADE,
    plant_id BIGINT NOT NULL REFERENCES plant(id)
        ON DELETE CASCADE,
    nickname VARCHAR(255),
    date_planted DATE,
    date_watered DATE
);

CREATE TABLE plant_garden (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    garden_id BIGINT NOT NULL REFERENCES garden(id)
       ON DELETE CASCADE,
    plant_id BIGINT NOT NULL REFERENCES plant(id)
       ON DELETE CASCADE,
    UNIQUE (garden_id, plant_id)
);