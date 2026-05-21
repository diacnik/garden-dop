package com.garden.dop.repository;

import com.garden.dop.data.Plant;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlantRepository implements Repository<Plant> {

    @Inject
    private AgroalDataSource dataSource;

    public void persist(Plant plant) {
        String sql = """
                INSERT INTO plants (
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
                SELECT * FROM plant WHERE id = ?;
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

    public void update(Plant plant) {
        String sql = """
                UPDATE plant
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
    }

    private void setPreparedStatementParameters(PreparedStatement prepStmt, Plant plant) throws SQLException {
        prepStmt.setString(1, plant.name());
        prepStmt.setString(2, plant.family());
        prepStmt.setString(3, plant.genus());
        prepStmt.setString(4, plant.species());
        prepStmt.setInt(5, plant.spreadRadius());
        prepStmt.setInt(6, plant.daysDryDown());
        prepStmt.setInt(7, plant.daysToHarvest());
        prepStmt.setInt(8, plant.hardinessZone());
        prepStmt.setInt(9, plant.lifeSpan());
        prepStmt.setBoolean(10, plant.lowLight());
    }

    private void setPreparedStatementParametersWithId(PreparedStatement prepStmt, Plant plant) throws SQLException {
        prepStmt.setString(1, plant.name());
        prepStmt.setString(2, plant.family());
        prepStmt.setString(3, plant.genus());
        prepStmt.setString(4, plant.species());
        prepStmt.setInt(5, plant.spreadRadius());
        prepStmt.setInt(6, plant.daysDryDown());
        prepStmt.setInt(7, plant.daysToHarvest());
        prepStmt.setInt(8, plant.hardinessZone());
        prepStmt.setInt(9, plant.lifeSpan());
        prepStmt.setBoolean(10, plant.lowLight());
        prepStmt.setLong(11, plant.id());
    }
}
