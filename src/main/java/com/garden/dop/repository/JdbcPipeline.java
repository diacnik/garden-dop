package com.garden.dop.repository;

import io.agroal.api.AgroalDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional utility for executing JDBC operations.
 */
public interface JdbcPipeline {
    // Functional interfaces
    @FunctionalInterface
    interface JdbcConsumer<T> {
        void accept(T t) throws SQLException;
    }

    @FunctionalInterface
    interface JdbcBiConsumer<T, U> {
        void accept(T t, U u) throws SQLException;
    }

    @FunctionalInterface
    interface JdbcFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    // Higher order functions
    static <T> T executeQuery(AgroalDataSource dataSource, String sql, JdbcConsumer<PreparedStatement> prepStmtConsumer, JdbcFunction<ResultSet, T> resultSetMapper) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmtConsumer.accept(prepStmt);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                return resultSetMapper.apply(resultSet);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error executing query", ex);
        }
    }

    static int executeUpdate(AgroalDataSource dataSource, String sql, JdbcConsumer<PreparedStatement> prepStmtConsumer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmtConsumer.accept(prepStmt);
            return prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error executing query", ex);
        }
    }
}
