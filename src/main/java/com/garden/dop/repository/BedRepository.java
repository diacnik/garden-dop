package com.garden.dop.repository;

import com.garden.dop.data.Bed;
import io.agroal.api.AgroalDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A repository for stateless, functional data access operations for Bed records.
 */
public class BedRepository {

    public void persist(AgroalDataSource dataSource, Bed bed) {
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

        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> bindBedToPreparedStatement.accept(prepStmt, bed));
    }

    public Optional<Bed> findById(AgroalDataSource dataSource, long id) {
        String sql = """
                SELECT * FROM bed WHERE id = ?;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> prepStmt.setLong(1, id),
                resultSet -> resultSet.next() ? Optional.of(mapRowToBed.apply(resultSet)) : Optional.empty());
    }

    public List<Bed> findAll(AgroalDataSource dataSource) {
        String sql = """
                SELECT * FROM bed;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> {},
                resultSet -> {
                    List<Bed> beds = new ArrayList<>();
                    while (resultSet.next()) {
                        beds.add(mapRowToBed.apply(resultSet));
                    }
                    return beds;
                });
    }

    public void update(AgroalDataSource dataSource, Bed bed) {
        String sql = """
                UPDATE bed
                SET
                    garden_id = ?,
                    name = ?,
                    length = ?,
                    width = ?,
                    low_light= ?
                WHERE id = ?;
                """;

        JdbcPipeline.executeUpdate(dataSource, sql,
                prepStmt -> {
                    bindBedToPreparedStatement.accept(prepStmt, bed);
                    prepStmt.setLong(6, bed.id());
                });
    }

    public void delete(AgroalDataSource dataSource, long id) {
        String sql = """
                DELETE FROM bed WHERE id = ?;
                """;

        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> prepStmt.setLong(1, id));
    }

    private static final JdbcPipeline.JdbcFunction<ResultSet, Bed> mapRowToBed =
            resultSet ->  new Bed(
                    resultSet.getLong("id"),
                    resultSet.getLong("garden_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("length"),
                    resultSet.getDouble("width"),
                    resultSet.getBoolean("low_light")
            );

    private static final JdbcPipeline.JdbcBiConsumer<PreparedStatement, Bed> bindBedToPreparedStatement =
            (prepStmt, bed) -> {
                prepStmt.setLong(1, bed.gardenId());
                prepStmt.setString(2, bed.name());
                prepStmt.setDouble(3, bed.length());
                prepStmt.setDouble(4, bed.width());
                prepStmt.setBoolean(5, bed.lowLight());
            };
}
