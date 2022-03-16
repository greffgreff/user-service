package io.rently.userservice.persistency;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.util.Broadcaster;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SqlPersistence implements IDatabaseContext {
    private static final MysqlDataSource dataSource = new MysqlDataSource();
    private static Connection cnn;

    public SqlPersistence() { }

    public void setUser(String user) {
        dataSource.setUser(user);
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
    }

    public void setServerName(String server) {
        dataSource.setServerName(server);
    }

    public void setDatabaseName(String db) {
        dataSource.setDatabaseName(db);
    }

    private void createConnection() {
        try {
            cnn = dataSource.getConnection();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw Errors.DATABASE_CONNECTION_FAILED.getException();
        }
    }

    private void terminateConnection() {
        try {
            cnn.close();
        }
        catch(Exception ignore) { }
    }

    @Override
    public <T> T getById(Class<T> dto, String id) {
        Constructor<T> dtoConstructor = null;
        try {
            dtoConstructor = dto.getConstructor();
            T persistentObj = dtoConstructor.newInstance();
            List<T> objList = get(dto, getKeyName(persistentObj), id);
            if (objList.isEmpty()) return null;
            return objList.get(0);
        } catch (Exception ex) {
            Broadcaster.error("Error occurred while fetching dto by key", ex);
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
    }

    @Override
    public <T> List<T> get(Class<T> dto, String field, String value) {
        try {
            Constructor<T> dtoConstructor = dto.getConstructor();
            T persistentObj = dtoConstructor.newInstance();

            createConnection();
            Statement statement = cnn.createStatement();
            String sql = "SELECT * FROM `" + getTableName(persistentObj) + "` WHERE " + field + "='" + value +"'";
            Broadcaster.debug(sql);
            ResultSet result = statement.executeQuery(sql);
            HashMap<String, String> data = new HashMap<>();

            while(result.next()) {
                ResultSetMetaData meta = result.getMetaData();
                for(int i = 1; i<=meta.getColumnCount(); i++) { // iterate through row count
                    data.put(meta.getColumnName(i), result.getString(i));
                }
            }

            statement.close();
            terminateConnection();

            if (data.isEmpty()) return new ArrayList<>();
            return List.of(SqlMapper.mapResultSetToObject(persistentObj, data));
        }
        catch(Exception ex) {
            Broadcaster.error("Error occurred while fetching dto", ex);
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
    }

    @Override
    public <T> void add(T obj) {
        TreeMap<String, String> persistentFields = getFields(obj);

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Map.Entry<String, String> keyPair: persistentFields.entrySet()) {
            if (keyPair.getValue().length() > 100) {
                Broadcaster.info("Payload too long ("+ keyPair.getValue().length()+"): " + keyPair.getValue());
                throw Errors.PAYLOAD_TOO_LARGE.getException();
            }
            columns.append("`").append(keyPair.getKey()).append("`");
            values.append("'").append(keyPair.getValue()).append("'");

            if (!Objects.equals(keyPair.getKey(), persistentFields.lastKey())) {
                columns.append(",");
                values.append(",");
            }
        }

        try {
            createConnection();
            Statement statement = cnn.createStatement();
            String sql = "INSERT INTO `"+ getTableName(obj) + "`("+ columns +") VALUES(" + values +")";
            Broadcaster.debug(sql);
            statement.executeUpdate(sql);
            terminateConnection();
        }
        catch(Exception ex) {
            Broadcaster.error("Error occurred while inserting dto to database", ex);
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
    }

    @Override
    public <T> void update(T obj) {
        this.delete(obj);
        this.add(obj);
    }

    @Override
    public <T> void delete(T obj) {
        try {
            createConnection();
            Statement statement = cnn.createStatement();
            String sql = "DELETE FROM `" + getTableName(obj) + "` WHERE " + getKeyName(obj) + "='" + getKeyFieldValue(obj) + "'";
            Broadcaster.debug(sql);
            statement.executeUpdate(sql);
            terminateConnection();
        }
        catch(Exception ex) {
            Broadcaster.error("Error occurred while removing dto from database", ex);
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
    }

    private <T> Object getKeyFieldValue(T obj) {
        for (Field field: obj.getClass().getDeclaredFields()) {
            if (field.getName().equals(getKeyName(obj))) {
                try {
                    field.setAccessible(true);
                    return field.get(obj);
                } catch (IllegalAccessException e) {
                    Broadcaster.error("Could not find value of key.", e);
                    throw Errors.INTERNAL_SERVER_ERROR.getException();
                }
            }
        }
        Broadcaster.error("Object passed does not contain a field annotated with PersistentKeyField");
        throw Errors.INTERNAL_SERVER_ERROR.getException();
    }

    private <T> String getTableName(T obj) {
        PersistentObject persistentObject = obj.getClass().getDeclaredAnnotation(PersistentObject.class);
        if (persistentObject == null) {
            Broadcaster.error("Object passed is not annotated field with PersistentObject");
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
        return persistentObject.name();
    }

    private <T> TreeMap<String, String> getFields(T obj) {
        TreeMap<String, String> persistentFields = new TreeMap<>();
        for (Field field: obj.getClass().getDeclaredFields()) {
            PersistentField persistentField = field.getDeclaredAnnotation(PersistentField.class);
            if (persistentField != null) {
                try {
                    field.setAccessible(true);
                    persistentFields.put(persistentField.name(), field.get(obj).toString());
                } catch (Exception ex) {
                    Broadcaster.error("Error occurred while fetching field: " + ex.getMessage(), ex);
                }
            }
        }
        return persistentFields;
    }

    private <T> String getKeyName(T obj) {
        for (Field field: obj.getClass().getDeclaredFields()) {
            PersistentField persistentField = field.getAnnotation(PersistentField.class);
            if (persistentField != null) {
                return persistentField.name();
            }
        }
        Broadcaster.error("Object passed does not contain annotated field with PersistentKeyField");
        throw Errors.INTERNAL_SERVER_ERROR.getException();
    }
}
