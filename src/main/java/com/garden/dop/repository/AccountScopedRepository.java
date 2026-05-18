package com.garden.dop.repository;


import java.util.List;
import java.util.UUID;

public interface AccountScopedRepository<T> extends Repository<T> {
    public List<T> findAllByAccountId(UUID accountId);
}
