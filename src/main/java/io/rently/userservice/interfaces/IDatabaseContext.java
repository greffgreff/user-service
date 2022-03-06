package io.rently.userservice.interfaces;

import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDatabaseContext {

    ArrayList<Object> get();

    void add(Object obj);

    void update(Object obj);

    void delete(Object obj);
}
