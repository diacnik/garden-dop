package com.garden.dop.repository;

import com.garden.dop.data.Garden;
import io.agroal.api.AgroalDataSource;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GardenRepository implements AccountScopedRepository<Garden> {

    @Inject
    private AgroalDataSource dataSource;

    @Override
    public void persist(Garden garden) {
        String sql = """
                INSERT INTO garden (
                    account_id,
                    name,
                    indoors,
                    hardiness_zone,
                    is_public
                    ) VALUES (?,?,?,?,?);
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParameters(prepStmt, garden);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error persisting garden", ex);
        }
    }

    @Override
    public Optional<Garden> findById(long id) {
        String sql = """
                SELECT * FROM garden WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            prepStmt.setLong(1, id);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToGarden(resultSet));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding garden", ex);
        }

        return Optional.empty();
    }

    @Override
    public List<Garden> findAll() {
        String sql = """
                SELECT * FROM garden;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Garden> gardens = new ArrayList<>();
                while (resultSet.next()) {
                    gardens.add(mapRowToGarden(resultSet));
                }
                return gardens;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all gardens", ex);
        }
    }

    public List<Garden> findAllByAccountId(UUID accountId) {
        String sql = """
                SELECT * FROM garden WHERE account_id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setObject(1, accountId, java.sql.Types.OTHER);

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Garden> gardens = new ArrayList<>();
                while (resultSet.next()) {
                    gardens.add(mapRowToGarden(resultSet));
                }
                return gardens;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all gardens", ex);
        }
    }

    public Optional<Garden> findByIdAndAccountId(UUID accountId, long id) {
        String sql = """
                SELECT * FROM garden
                WHERE id = ?
                AND (account_id = ? OR is_public = true);
                """;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmt.setLong(1, id);
            prepStmt.setObject(2, accountId, java.sql.Types.OTHER);

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToGarden(resultSet));
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding garden", ex);
        }
        return Optional.empty();
    }

    @Override
    public void update(Garden garden) {
        String sql = """
                UPDATE garden
                SET
                    account_id = ?,
                    name = ?,
                    indoors = ?,
                    hardiness_zone = ?,
                    is_public = ?
                WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            setPreparedStatementParametersWithId(prepStmt, garden);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating garden", ex);
        }
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE FROM garden WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting garden", ex);
        }
    }

    private Garden mapRowToGarden(ResultSet resultSet)  throws SQLException {
        return new Garden(
                resultSet.getLong("id"),
                resultSet.getObject("account_id", java.util.UUID.class),
                resultSet.getString("name"),
                resultSet.getBoolean(("indoors")),
                resultSet.getInt("hardiness_zone"),
                resultSet.getBoolean("is_public")
        );
    }

    private void setPreparedStatementParameters(PreparedStatement prepStmt, Garden garden) throws SQLException {
        prepStmt.setObject(1, garden.accountId());
        prepStmt.setString(2, garden.name());
        prepStmt.setBoolean(3, garden.indoors());
        prepStmt.setInt(4, garden.hardinessZone());
        prepStmt.setBoolean(5, garden.isPublic());
    }

    private void setPreparedStatementParametersWithId(PreparedStatement prepStmt, Garden garden) throws SQLException {
        prepStmt.setObject(1, garden.accountId());
        prepStmt.setString(2, garden.name());
        prepStmt.setBoolean(3, garden.indoors());
        prepStmt.setInt(4, garden.hardinessZone());
        prepStmt.setBoolean(5, garden.isPublic());
        prepStmt.setLong(6, garden.id());
    }
}
