package com.garden.dop.repository;

import com.garden.dop.data.Bed;
import com.garden.dop.data.PlantInBed;
import com.garden.dop.data.PlantProfile;
import io.agroal.api.AgroalDataSource;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A repository for stateless, functional data access operations for PlantInBed records.
 */
public class PlantInBedRepository {

    @Inject
    private AgroalDataSource dataSource;

    public List<PlantInBed> findAllByAccountId(AgroalDataSource dataSource, UUID accountId) {
        String sql = """
                SELECT
                    bp.id,
                    bp.bed_id,
                    bp.plant_profile_id,
                    bp.nickname,
                    bp.date_planted,
                    bp.date_watered,
                    pp.id,
                    pp.name,
                    pp.family,
                    pp.genus,
                    pp.species,
                    pp.spread_radius,
                    pp.days_dry_down,
                    pp.days_to_harvest,
                    pp.hardiness_zone,
                    pp.life_span_years,
                    pp.low_light
                FROM bed_plant bp
                    INNER JOIN plant_profile pp ON bp.plant_profile_id = pp.id
                    INNER JOIN bed b ON bp.bed_id = b.id
                    INNER JOIN garden g ON b.garden_id = g.garden_id
                WHERE g.account_id = ?;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> prepStmt.setObject(1, accountId),
                resultSet -> {
                    List<PlantInBed> plantInBeds = new ArrayList<>();
                    while (resultSet.next()) {
                        plantInBeds.add(mapRowToPlantInBed.apply(resultSet));
                    }
                    return plantInBeds;
                });
    }

    public List findAllPlantsInBed(AgroalDataSource dataSource, UUID accountId, Bed bed) {
        String sql = """
                SELECT
                    bp.id,
                    bp.bed_id,
                    bp.plant_profile_id,
                    bp.nickname,
                    bp.date_planted,
                    bp.date_watered,
                    pp.id,
                    pp.name,
                    pp.family,
                    pp.genus,
                    pp.species,
                    pp.spread_radius,
                    pp.days_dry_down,
                    pp.days_to_harvest,
                    pp.hardiness_zone,
                    pp.life_span_years,
                    pp.low_light
                FROM bed_plant bp
                    INNER JOIN plant_profile pp ON bp.plant_profile_id = pp.id
                    INNER JOIN bed b ON bp.bed_id = b.id
                    INNER JOIN garden g ON b.garden_id = g.garden_id
                WHERE bp.id = ?
                    AND (g.account_id = ? OR g.is_public = true);
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> {
                    prepStmt.setLong(1, bed.id());
                    prepStmt.setObject(2, accountId);
                },
                resultSet -> {
                    List<PlantInBed> plantInBeds = new ArrayList<>();
                    while (resultSet.next()) {
                        plantInBeds.add(mapRowToPlantInBed.apply(resultSet));
                    }
                    return plantInBeds;
                });
    }

    public void persist(AgroalDataSource dataSource, PlantInBed plantInBed) {
        String sql = """
                INSERT INTO bed_plant (
                bed_id,
                plant_profile_id,
                nickname,
                date_planted,
                date_watered
                ) VALUES (?,?,?,?,?);
                """;

        JdbcPipeline.executeUpdate(dataSource,sql, prepStmt -> bindPlantInBedToPreparedStatement.accept(prepStmt, plantInBed));
    }

    public Optional findById(long id) {
        String sql = """
                SELECT * FROM bed_plant WHERE id = ?;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> prepStmt.setLong(1, id),
                resultSet -> resultSet.next() ? Optional.of(mapRowToPlantInBed.apply(resultSet)) : Optional.empty());
    }

    public List<PlantInBed> findAll() {
        String sql = """
                SELECT * FROM bed_plant;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> {},
                resultSet -> {
                    List<PlantInBed> plantInBeds = new ArrayList<>();
                    while (resultSet.next()) {
                        plantInBeds.add(mapRowToPlantInBed.apply(resultSet));
                    }
                    return plantInBeds;
                });
    }

    public void update(AgroalDataSource dataSource, PlantInBed plantInBed) {
        String sql = """
                UPDATE bed_plant
                SET
                    bed_id = ?,
                    plant_profile_id = ?,
                    nickname = ?,
                    date_planted = ?,
                    date_watered = ?
                WHERE id = ?;
                """;

        JdbcPipeline.executeUpdate(dataSource, sql,
                prepStmt -> {
                    bindPlantInBedToPreparedStatement.accept(prepStmt, plantInBed);
                    prepStmt.setLong(1, plantInBed.id());
        });
    }

    public void delete(AgroalDataSource dataSource, long id) {
        String sql = """
                DELETE FROM bed_plant WHERE id = ?;
                """;

        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> prepStmt.setLong(1, id));
    }



    private static final JdbcPipeline.JdbcFunction<ResultSet, PlantInBed> mapRowToPlantInBed =
        resultSet -> new PlantInBed(
                resultSet.getLong("bp.id"),
                resultSet.getLong("bp.bed_id"),
                resultSet.getLong("bp.plant_profile_id"),
                resultSet.getString("bp.nickname"),
                resultSet.getDate("bp.date_planted").toLocalDate(),
                resultSet.getDate("bp.date_watered").toLocalDate(),
                new PlantProfile(
                        resultSet.getLong("pp.id"),
                        resultSet.getString("pp.name"),
                        resultSet.getString("pp.family"),
                        resultSet.getString("pp.genus"),
                        resultSet.getString("pp.species"),
                        resultSet.getInt("pp.spread_radius"),
                        resultSet.getInt("pp.days_dry_down"),
                        resultSet.getInt("pp.days_to_harvest"),
                        resultSet.getInt("hardiness_zone"),
                        resultSet.getInt("life_span_years"),
                        resultSet.getBoolean("low_light")
                )
        );

    private static final JdbcPipeline.JdbcBiConsumer<PreparedStatement, PlantInBed> bindPlantInBedToPreparedStatement =
            (prepStmt, plantInBed) -> {
                prepStmt.setLong(1, plantInBed.bedId());
                prepStmt.setLong(2, plantInBed.plantProfileId());
                prepStmt.setString(3, plantInBed.nickname());
                prepStmt.setDate(4, Date.valueOf(plantInBed.datePlanted()));
                prepStmt.setDate(5, Date.valueOf(plantInBed.dateWatered()));
            };
}
