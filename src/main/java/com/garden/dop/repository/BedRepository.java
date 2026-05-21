package com.garden.dop.repository;

import com.garden.dop.data.Bed;
import io.agroal.api.AgroalDataSource;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BedRepository implements Repository<Bed> {

    @Inject
    private AgroalDataSource dataSource;

    @Override
    public void persist(Bed bed) {
        String sql = """
                INSERT INTO bed (
                    id,
                    garden_id,
                    name,
                    length,
                    width,
                    low_light
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            setPreparedStatementParameters(prepStmt, bed);
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error persisting bed", ex);
        }

    }

    @Override
    public Optional<Bed> findById(long id) {
        String sql = """
                SELECT * FROM bed WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            prepStmt.setLong(1, id);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToBed(resultSet));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding bed", ex);
        }

        return Optional.empty();
    }

    @Override
    public List<Bed> findAll() {
        String sql = """
                SELECT * FROM bed;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                List<Bed> beds = new ArrayList<>();
                while (resultSet.next()) {
                    beds.add(mapRowToBed(resultSet));
                }
                return beds;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding all bed", ex);
        }
    }

    @Override
    public void update(Bed bed) {
        String sql = """
                UPDATE bed
                SET
                    garden_id,
                    name,
                    length,
                    width,
                    low_light
                WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            setPreparedStatementParametersWithId(prepStmt, bed);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating bed", ex);
        }
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE FROM bed WHERE id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting bed", ex);
        }

    }

    private Bed mapRowToBed(ResultSet resultSet) throws SQLException {
        return new Bed(
                resultSet.getLong("id"),
                resultSet.getLong("garden_id"),
                resultSet.getString("name"),
                resultSet.getDouble("length"),
                resultSet.getDouble("width"),
                resultSet.getBoolean("low_light")
        );
    }

    private void setPreparedStatementParameters(PreparedStatement prepStmt, Bed bed) throws SQLException {
        prepStmt.setLong(1, bed.gardenId());
        prepStmt.setString(2, bed.name());
        prepStmt.setDouble(3, bed.length());
        prepStmt.setDouble(4, bed.width());
        prepStmt.setBoolean(5, bed.lowLight());
    }

    private void setPreparedStatementParametersWithId(PreparedStatement prepStmt, Bed bed) throws SQLException {
        prepStmt.setLong(1, bed.gardenId());
        prepStmt.setString(2, bed.name());
        prepStmt.setDouble(3, bed.length());
        prepStmt.setDouble(4, bed.width());
        prepStmt.setBoolean(5, bed.lowLight());
        prepStmt.setLong(6, bed.id());
    }
}
