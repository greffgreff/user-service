package io.rently.userservice.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseContext {

    <T> T getById(Class<T> dto, String id) throws Exception;

    <T> List<T> get(Class<T> dto, String field, String value) throws Exception;

    <T> void add(T obj) throws Exception;

    <T> void update(T obj) throws Exception;

    <T> void delete(T obj) throws Exception;
}
