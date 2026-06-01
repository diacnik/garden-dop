package com.garden.dop.repository;

import com.garden.dop.data.PlantProfile;

import io.agroal.api.AgroalDataSource;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A repository for stateless, functional data access operations for PlantProfile records.
 */
public final class PlantProfileRepository {

    // DB stateless(maybe?) functions
    public static void persist (AgroalDataSource dataSource, PlantProfile plantProfile) {
        String sql = """
                INSERT INTO plant_profile (
                    name,
                    family,
                    genus,
                    species,
                    spread_radius,
                    days_dry_down,
                    days_to_harvest,
                    hardiness_zone,
                    years_life_span,
                    low_light)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> bindPlantProfileToPreparedStatement.accept(prepStmt, plantProfile));
    }

    public static Optional<PlantProfile> findById(AgroalDataSource dataSource, long id) {
        String sql = """
                SELECT * FROM plant_profile WHERE id = ?;
                """;
        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> prepStmt.setLong(1, id),
                resultSet -> resultSet.next() ? Optional.of(mapRowToPlantProfile.apply(resultSet)) : Optional.empty());
    }

    public static List<PlantProfile> findAll(AgroalDataSource dataSource) {
        String sql = """
                SELECT * FROM plant_profile;
                """;
        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> {},
                resultSet -> {
                    List<PlantProfile> plantProfiles = new ArrayList<>();
                    while (resultSet.next()) {
                        plantProfiles.add(mapRowToPlantProfile.apply(resultSet));
                    }
                    return plantProfiles;
                });
    }

    public static void update(AgroalDataSource dataSource, PlantProfile plantProfile) {
        String sql = """
                UPDATE plant_profile
                SET
                    name = ?,
                    family = ?,
                    genus = ?,
                    species = ?,
                    spread_radius = ?,
                    days_dry_down = ?,
                    days_to_harvest = ?,
                    hardiness_zone = ?,
                    years_life_span = ?,
                    low_light = ?
                WHERE id = ?;
                """;
        JdbcPipeline.executeUpdate(dataSource, sql,
                prepStmt -> {
                    bindPlantProfileToPreparedStatement.accept(prepStmt, plantProfile);
                    prepStmt.setLong(11, plantProfile.id());
        });
    }

    public static void delete(AgroalDataSource dataSource, long id) {
        String sql = """
                DELETE FROM plant_profile WHERE id = ?;
                """;
        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> prepStmt.setLong(1, id));
    }

    // Helper functions, don't know where to put these
    private static final JdbcPipeline.JdbcFunction<ResultSet, PlantProfile> mapRowToPlantProfile =
            resultSet -> new PlantProfile(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("family"),
                    resultSet.getString("genus"),
                    resultSet.getString("species"),
                    resultSet.getInt("spread_radius"),
                    resultSet.getInt("days_dry_down"),
                    resultSet.getInt("days_to_harvest"),
                    resultSet.getInt("hardiness_zone"),
                    resultSet.getInt("life_span_years"),
                    resultSet.getBoolean("low_light")
            );

    private static final JdbcPipeline.JdbcBiConsumer<PreparedStatement, PlantProfile> bindPlantProfileToPreparedStatement =
            (prepStmt, plantProfile) -> {
                prepStmt.setString(1, plantProfile.name());
                prepStmt.setString(2, plantProfile.family());
                prepStmt.setString(3, plantProfile.genus());
                prepStmt.setString(4, plantProfile.species());
                prepStmt.setInt(5, plantProfile.spreadRadius());
                prepStmt.setInt(6, plantProfile.daysDryDown());
                prepStmt.setInt(7, plantProfile.daysToHarvest());
                prepStmt.setInt(8, plantProfile.hardinessZone());
                prepStmt.setInt(9, plantProfile.lifeSpan());
                prepStmt.setBoolean(10, plantProfile.lowLight());
            };
}
