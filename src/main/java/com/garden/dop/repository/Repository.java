package com.garden.dop.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T>{
    void persist(T record);
    Optional<T> findById(long id);
    List<T> findAll();
    void update(T record);
    void delete(long id);
}
