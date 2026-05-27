package com.garden.dop.repository;

import com.garden.dop.data.PlantProfile;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlantProfileRepository implements Repository<PlantProfile> {

    @Inject
    private AgroalDataSource dataSource;

    public void persist(PlantProfile plantProfile) {
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

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParameters(prepStmt, plantProfile);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error persisting plant profile", ex);
        }
    }

    public Optional<PlantProfile> findById(long id) {
        String sql = """
                SELECT * FROM plant_profile WHERE id = ?;
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
            throw new RuntimeException("Error finding plant profile", ex);
        }
        return Optional.empty();
    }

    public List<PlantProfile> findAll() {
        String sql = """
                SELECT * FROM plant_profile;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<PlantProfile> plantProfiles = new ArrayList<>();
                while (resultSet.next()) {
                    plantProfiles.add(mapRowToPlant(resultSet));
                }
                return plantProfiles;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all plant profiles", ex);
        }
    }

    public void update(PlantProfile plantProfile) {
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

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            setPreparedStatementParametersWithId(prepStmt, plantProfile);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating plant profile", ex);
        }
    }
    public void delete(long id) {
        String sql = """
                DELETE FROM plant_profile WHERE id = ?;
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting plant profile", ex);
        }
    }

    private PlantProfile mapRowToPlant(ResultSet resultSet) throws SQLException {
        return new PlantProfile(
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

    private void setPreparedStatementParameters(PreparedStatement prepStmt, PlantProfile plantProfile) throws SQLException {
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
    }

    private void setPreparedStatementParametersWithId(PreparedStatement prepStmt, PlantProfile plantProfile) throws SQLException {
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
        prepStmt.setLong(11, plantProfile.id());
    }
}
