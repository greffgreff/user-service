package io.rently.userservice.interfaces;

import java.util.List;

public interface IDatabaseContext {

    <T> T getById(Class<T> dto, String id);

    <T> List<T> get(Class<T> dto, String field, String value);

    <T> void add(T obj);

    <T> void update(T obj);

    <T> void delete(T obj);
}
