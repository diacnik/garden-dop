package com.garden.dop.repository;

import com.garden.dop.data.Bed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BedRepository implements Repository<Bed> {

    @Override
    public void persist(Bed bed) {

    }

    @Override
    public Optional<Bed> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Bed> findAll() {
        return List.of();
    }

    @Override
    public void update(Bed bed) {

    }

    @Override
    public void delete(long id) {

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

    private void setPreparedStatement(PreparedStatement prepStmt, Bed bed) throws SQLException {
        prepStmt.setLong(1, bed.gardenId());
        prepStmt.setString(2, bed.name());
        prepStmt.setDouble(3, bed.length());
        prepStmt.setDouble(4, bed.width());
        prepStmt.setBoolean(5, bed.lowLight());
    }
}
