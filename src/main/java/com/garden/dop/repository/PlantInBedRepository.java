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

public class PlantInBedRepository implements AccountScopedRepository<PlantInBed> {

    @Inject
    private AgroalDataSource dataSource;

    @Override
    public List findAllByAccountId(UUID accountId) {
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

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setObject(1, accountId);

            try (ResultSet resultset = prepStmt.executeQuery()) {
                List<PlantInBed> plantInBeds = new ArrayList<>();
                while (resultset.next()) {
                    plantInBeds.add(mapRowToPlantInBed(resultset));
                }
                return plantInBeds;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants in beds based on account id", ex);
        }
    }

    public List findAllPlantsInBed(UUID accountId, Bed bed) {
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

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, bed.id());
            prepStmt.setObject(2, accountId);

            try (ResultSet resultset = prepStmt.executeQuery()) {
                List<PlantInBed> plantInBeds = new ArrayList<>();
                while (resultset.next()) {
                    plantInBeds.add(mapRowToPlantInBed(resultset));
                }
                return plantInBeds;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants in bed", ex);
        }
    }

    @Override
    public void persist(PlantInBed plantInBed) {
        String sql = """
                INSERT INTO bed_plant (
                bed_id,
                plant_profile_id,
                nickname,
                date_planted,
                date_watered
                ) VALUES (?,?,?,?,?);
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParameters(prepStmt, plantInBed);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error persisting bed plant", ex);
        }
    }

    @Override
    public Optional findById(long id) {
        String sql = """
                SELECT * FROM bed_plant WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, id);
            try (ResultSet resultset = prepStmt.executeQuery()) {
                if (resultset.next()) {
                    return Optional.of(mapRowToPlantInBed(resultset));
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding bed plant", ex);
        }
        return Optional.empty();
    }

    @Override
    public List findAll() {
        String sql = """
                SELECT * FROM bed_plant;
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            try (ResultSet resultset = prepStmt.executeQuery()) {
                List<PlantInBed> plantInBeds = new ArrayList<>();
                while (resultset.next()) {
                    plantInBeds.add(mapRowToPlantInBed(resultset));
                }
                return plantInBeds;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants in beds", ex);
        }
    }

    @Override
    public void update(PlantInBed plantInBed) {
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

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParametersWithId(prepStmt, plantInBed);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating bed plant", ex);
        }
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE FROM bed_plant WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting bed plant", ex);
        }
    }

    private PlantInBed mapRowToPlantInBed(ResultSet resultSet) throws SQLException {
        return new PlantInBed(
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
    }

    private void setPreparedStatementParameters(PreparedStatement prepStmt, PlantInBed plantInBed) throws SQLException {
        prepStmt.setLong(1, plantInBed.bedId());
        prepStmt.setLong(2, plantInBed.plantProfileId());
        prepStmt.setString(3, plantInBed.nickname());
        prepStmt.setDate(4, Date.valueOf(plantInBed.datePlanted()));
        prepStmt.setDate(5, Date.valueOf(plantInBed.dateWatered()));
    }

    private void setPreparedStatementParametersWithId(PreparedStatement prepStmt, PlantInBed plantInBed) throws SQLException {
        prepStmt.setLong(1, plantInBed.bedId());
        prepStmt.setLong(2, plantInBed.plantProfileId());
        prepStmt.setString(3, plantInBed.nickname());
        prepStmt.setDate(4, Date.valueOf(plantInBed.datePlanted()));
        prepStmt.setDate(5, Date.valueOf(plantInBed.dateWatered()));
        prepStmt.setLong(6, plantInBed.id());
    }
}
