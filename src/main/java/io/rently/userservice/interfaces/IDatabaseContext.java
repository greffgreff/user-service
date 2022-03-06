package io.rently.userservice.interfaces;

import io.rently.userservice.annotations.PersistentField;

import java.util.ArrayList;

public interface IDatabaseContext<T> {

    T getById(String id);

    ArrayList<T> get(PersistentField field, String value);

    void add(T obj);

    void update(T obj);

    void delete(T obj);
}
