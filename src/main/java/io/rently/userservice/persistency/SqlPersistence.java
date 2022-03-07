package io.rently.userservice.persistency;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public <T> T getById(Class<T> dto, String id) throws Exception {
        createConnection();
        Statement statement = cnn.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM `_rently_users` WHERE id='" + id +"'");
        HashMap<String, String> data = new HashMap<>();

        while(result.next()) {
            ResultSetMetaData meta = result.getMetaData();
            for(int i = 1; i<=meta.getColumnCount(); i++) {
                data.put(meta.getColumnName(i), result.getString(i));
            }
        }

        statement.close();
        terminateConnection();
        return SqlMapper.mapResultSetToObject(dto, data);
    }

    @Override
    public <T> ArrayList<T> get(Class<T> dto, PersistentField field, String value) {
        return null;
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
