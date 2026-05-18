package com.garden.dop.repository;

import com.garden.dop.data.Plant;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AccountScopedPlantRepository implements AccountScopedRepository<Plant> {

    @Inject
    private AgroalDataSource dataSource;

    public void persist(Plant plant) {
        String sql = """
                INSERT INTO plants (
                    name,
                    genus,
                    species,
                    spread_radius,
                    date_planted,
                    date_watered,
                    days_dry_down,
                    days_to_harvest,
                    hardiness_zone,
                    years_life_span,
                    low_light)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParameters(prepStmt, plant);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error persisting plant", ex);
        }
    }
    public Optional<Plant> findById(long id) {
        String sql = """
                SELECT * FROM plants WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, id);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToPlant(resultSet));
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding plant", ex);
        }
        return Optional.empty();
    }
    public List<Plant> findAll() {
        String sql = """
                SELECT * FROM plant;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Plant> plants = new ArrayList<>();
                while (resultSet.next()) {
                    plants.add(mapRowToPlant(resultSet));
                }
                return plants;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants", ex);
        }
    }
    public List<Plant> findAllByAccountId(UUID accountId) {
        String sql = """
                SELECT
                    p.id,
                    p.name,
                    p.genus,
                    p.species,
                    p.spread_radius,
                    p.date_planted,
                    p.date_watered,
                    p.days_dry_down,,
                    p.hardiness_zone,
                    p.life_span_years,
                    p.low_light
                FROM plant_garden pg
                    LEFT JOIN garden g ON pg.garden_id = g.id
                    LEFT JOIN plant p ON pg.plant_id = p.id
                WHERE g.account_id = ?;
        """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setObject(1, accountId);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Plant> plants = new ArrayList<>();
                while (resultSet.next()) {
                    plants.add(mapRowToPlant(resultSet));
                }
                return plants;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants for user", ex);
        }
    }

    public List<Plant> findAllByBedId(UUID accountId, long bedId) {
        String sql = """
                SELECT
                    p.id,
                    p.name,
                    p.genus,
                    p.species,
                    p.spread_radius,
                    p.date_planted,
                    p.date_watered,
                    p.days_dry_down,,
                    p.hardiness_zone,
                    p.life_span_years,
                    p.low_light
                FROM plant_bed pb
                    INNER JOIN bed b ON pb.bed_id = b.id
                    INNER JOIN plant p ON pb.plant_id = p.id
                    INNER JOIN garden g ON b.garden_id = g.id
                WHERE b.id = ?
                AND (g.account_id = ? OR g.is_public = true);
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, bedId);
            prepStmt.setObject(2, accountId);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Plant> plants = new ArrayList<>();
                while (resultSet.next()) {
                    plants.add(mapRowToPlant(resultSet));
                }
                return plants;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants for user", ex);
        }
    }

    public List<Plant> findAllByGardenId(UUID accountId, long gardenId) {
        String sql = """
                SELECT
                    p.id,
                    p.name,
                    p.genus,
                    p.species,
                    p.spread_radius,
                    p.date_planted,
                    p.date_watered,
                    p.days_dry_down,,
                    p.hardiness_zone,
                    p.life_span_years,
                    p.low_light
                FROM plant_garden pg
                    INNER JOIN garden g ON pg.garden_id = g.id
                    INNER JOIN plant p ON pg.plant_id = p.id
                WHERE g.id = ?
                AND (g.account_id = ? OR g.is_public = true);
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, gardenId);
            prepStmt.setObject(2, accountId);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Plant> plants = new ArrayList<>();
                while (resultSet.next()) {
                    plants.add(mapRowToPlant(resultSet));
                }
                return plants;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plants for user", ex);
        }
    }

    public void update(Plant plant) {
        String sql = """
                UPDATE plant SET (
                name = ?,
                genus = ?,
                species = ?,
                spread_radius = ?,
                date_planted = ?,
                date_watered = ?,
                days_dry_down = ?,
                days_to_harvest = ?,
                hardiness_zone = ?,
                years_life_span = ?,
                low_light = ?
                WHERE id = ?;
        """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParametersWithId(prepStmt, plant);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating plant", ex);
        }
    }
    public void delete(long id) {
        String sql = """
                DELETE FROM plant WHERE id = ?;
        """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting plant", ex);
        }
    }

    private Plant mapRowToPlant(ResultSet resultSet) throws SQLException {
        return new Plant(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("genus"),
                resultSet.getString("species"),
                resultSet.getInt("spread_radius"),
                resultSet.getDate("date_planted").toLocalDate(),
                resultSet.getDate("date_watered").toLocalDate(),
                resultSet.getInt("days_dry_down"),
                resultSet.getInt("days_to_harvest"),
                resultSet.getInt("hardiness_zone"),
                resultSet.getInt("life_span_years"),
                resultSet.getBoolean("low_light")
        );
    }

    private void setPreparedStatementParameters(PreparedStatement prepStmt, Plant plant) throws SQLException {
        prepStmt.setString(1, plant.name());
        prepStmt.setString(2, plant.genus());
        prepStmt.setString(3, plant.species());
        prepStmt.setInt(4, plant.spreadRadius());
        prepStmt.setDate(5, Date.valueOf(plant.datePlanted()));
        prepStmt.setDate(6, Date.valueOf(plant.dateWatered()));
        prepStmt.setInt(7, plant.daysDryDown());
        prepStmt.setInt(8, plant.daysToHarvest());
        prepStmt.setInt(9, plant.hardinessZone());
        prepStmt.setInt(10, plant.lifeSpan());
        prepStmt.setBoolean(11, plant.lowLight());
    }

    private void setPreparedStatementParametersWithId(PreparedStatement prepStmt, Plant plant) throws SQLException {
        prepStmt.setString(1, plant.name());
        prepStmt.setString(2, plant.genus());
        prepStmt.setString(3, plant.species());
        prepStmt.setInt(4, plant.spreadRadius());
        prepStmt.setDate(5, Date.valueOf(plant.datePlanted()));
        prepStmt.setDate(6, Date.valueOf(plant.dateWatered()));
        prepStmt.setInt(7, plant.daysDryDown());
        prepStmt.setInt(8, plant.daysToHarvest());
        prepStmt.setInt(9, plant.hardinessZone());
        prepStmt.setInt(10, plant.lifeSpan());
        prepStmt.setBoolean(11, plant.lowLight());
        prepStmt.setLong(12, plant.id());
    }
}
