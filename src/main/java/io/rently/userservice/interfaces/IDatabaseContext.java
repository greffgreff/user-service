package io.rently.userservice.interfaces;

import io.rently.userservice.annotations.PersistentField;

import java.util.ArrayList;

public interface IDatabaseContext {

    <T> T getById(Class<T> dto, String id);

    <T> ArrayList<T> get(Class<T> dto, PersistentField field, String value);

    <T> void add(T obj);

    <T> void update(T obj);

    <T> void delete(T obj);
}
