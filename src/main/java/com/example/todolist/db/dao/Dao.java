package com.example.todolist.db.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    boolean save(T record);
    boolean update(T record);
    boolean delete (int id);
}
