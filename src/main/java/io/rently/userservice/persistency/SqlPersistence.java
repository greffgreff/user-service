package io.rently.userservice.persistency;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.util.Broadcaster;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SqlPersistence implements IDatabaseContext {
    private static final MysqlDataSource dataSource = new MysqlDataSource();
    private static Connection cnn;

    public SqlPersistence(String user, String password, String server, String db) {
        setUser(user);
        setPassword(password);
        setServerName(server);
        setDatabaseName(db);
    }

    public static void setUser(String user) {
        dataSource.setUser(user);
    }

    public static void setPassword(String password) {
        dataSource.setPassword(password);
    }

    public static void setServerName(String server) {
        dataSource.setServerName(server);
    }

    public static void setDatabaseName(String db) {
        dataSource.setDatabaseName(db);
    }

    private static void createConnection() {
        try {
            cnn = dataSource.getConnection();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw Errors.DATABASE_CONNECTION_FAILED.getException();
        }
    }

    private static void terminateConnection() {
        try {
            cnn.close();
        }
        catch(Exception ignore) { }
    }

    public <T> T getById(Class<T> dto, String id) throws Exception {
        List<T> objList = get(dto, "id", id);
        if (objList.isEmpty()) return null;
        return objList.get(0);
    }

    @Override
    public <T> List<T> get(Class<T> dto, String field, String value) throws Exception {
        Constructor<T> dtoConstructor = dto.getConstructor();
        T persistentObj = dtoConstructor.newInstance();

        PersistentObject object = persistentObj.getClass().getDeclaredAnnotation(PersistentObject.class);
        if (object == null) {
            Broadcaster.error("Object passed to class is not annotated with PersistentObject");
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        createConnection();
        Statement statement = cnn.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM `" + object.name() + "` WHERE " + field + "='" + value +"'");
        HashMap<String, String> data = new HashMap<>();

        while(result.next()) {
            ResultSetMetaData meta = result.getMetaData();
            for(int i = 1; i<=meta.getColumnCount(); i++) {
                data.put(meta.getColumnName(i), result.getString(i));
            }
        }

        statement.close();
        terminateConnection();

        if (data.isEmpty()) return new ArrayList<>();
        return List.of(SqlMapper.mapResultSetToObject(persistentObj, data));
    }

    @Override
    public <T> void add(T obj) {
    }

    @Override
    public <T> void update(T obj) {
    }

    @Override
    public <T> void delete(T obj) {
    }
}
