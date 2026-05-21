package com.garden.dop.repository;

import com.garden.dop.data.Bed;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountScopedBedRepository implements AccountScopedRepository<Bed> {
    @Override
    public List<Bed> findAllByAccountId(UUID accountId) {
        return List.of();
    }

    @Override
    public void persist(Bed record) {

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
    public void update(Bed record) {

    }

    @Override
    public void delete(long id) {

    }
}
