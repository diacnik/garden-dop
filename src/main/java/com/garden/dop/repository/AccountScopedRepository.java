package com.garden.dop.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountScopedRepository<T> extends Repository<T> {
    public List<T> findAllByAccountId(UUID accountId);

    /*public Optional<T> findByIdAndAccountId(UUID accountId, long id);
    public void updateByIdAndAccountId(UUID accountId, long id);
    public void deleteByIdAndAccountId(UUID accountId, long id);*/

}
