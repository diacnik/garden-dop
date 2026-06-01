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

/**
 * A repository for stateless, functional data access operations for Garden records.
 */
public class GardenRepository {

    public static void persist(AgroalDataSource dataSource, Garden garden) {
        String sql = """
                INSERT INTO garden (
                    account_id,
                    name,
                    indoors,
                    hardiness_zone,
                    is_public
                    ) VALUES (?,?,?,?,?);
                """;

        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> bindGardenToPreparedStatement.accept(prepStmt, garden));
    }

    public static Optional<Garden> findById(AgroalDataSource dataSource, long id) {
        String sql = """
                SELECT * FROM garden WHERE id = ?;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> prepStmt.setLong(1, id),
                resultSet -> resultSet.next() ? Optional.of(mapRowToGarden.apply(resultSet)) : Optional.empty());
    }

    public static List<Garden> findAll(AgroalDataSource dataSource) {
        String sql = """
                SELECT * FROM garden;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> {},
                resultSet -> {
                    List<Garden> gardens = new ArrayList<>();
                    while (resultSet.next()) {
                        gardens.add(mapRowToGarden.apply(resultSet));
                    }
                    return gardens;
                });
    }

    public List<Garden> findAllByAccountId(AgroalDataSource dataSource, UUID accountId) {
        String sql = """
                SELECT * FROM garden WHERE account_id = ?;
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> prepStmt.setObject(1, accountId),
                resultSet -> {
                    List<Garden> gardens = new ArrayList<>();
                    while (resultSet.next()) {
                        gardens.add(mapRowToGarden.apply(resultSet));
                    }
                    return gardens;
                });
    }

    public static Optional<Garden> findByIdAndAccountId(AgroalDataSource dataSource, UUID accountId, long id) {
        String sql = """
                SELECT * FROM garden
                WHERE id = ?
                AND (account_id = ? OR is_public = true);
                """;

        return JdbcPipeline.executeQuery(dataSource, sql,
                prepStmt -> {
            prepStmt.setLong(1, id);
            prepStmt.setObject(2, accountId);
                },
                resultSet -> resultSet.next() ? Optional.of(mapRowToGarden.apply(resultSet)) : Optional.empty());
    }

    public static void update(AgroalDataSource dataSource, Garden garden) {
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

        JdbcPipeline.executeUpdate(dataSource, sql,
                prepStmt -> {
                    bindGardenToPreparedStatement.accept(prepStmt, garden);
                    prepStmt.setLong(6, garden.id());
                });
    }

    public static void delete(AgroalDataSource dataSource, long id) {
        String sql = """
                DELETE FROM garden WHERE id = ?;
                """;
        JdbcPipeline.executeUpdate(dataSource, sql, prepStmt -> prepStmt.setLong(1, id));
    }

    private static final JdbcPipeline.JdbcFunction<ResultSet, Garden> mapRowToGarden =
            resultSet ->  new Garden(
                    resultSet.getLong("id"),
                    resultSet.getObject("account_id", java.util.UUID.class),
                    resultSet.getString("name"),
                    resultSet.getBoolean("indoors"),
                    resultSet.getInt("hardiness_zone"),
                    resultSet.getBoolean("is_public")
            );

    private static final JdbcPipeline.JdbcBiConsumer<PreparedStatement, Garden> bindGardenToPreparedStatement =
            (prepStmt, garden) -> {
                prepStmt.setObject(1, garden.accountId());
                prepStmt.setString(2, garden.name());
                prepStmt.setBoolean(3, garden.indoors());
                prepStmt.setInt(4, garden.hardinessZone());
                prepStmt.setBoolean(5, garden.isPublic());
            };
}
