package io.rently.userservice.interfaces;

import java.util.ArrayList;

public interface IDatabaseContext<T> {

    ArrayList<T> get();

    void add(T obj);

    void update(T obj);

    void delete(T obj);

    void initConnection();
}
