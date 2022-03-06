package io.rently.userservice.persistency;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.util.ArrayList;

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

    private static void open() throws ResponseStatusException {
        try {
            cnn = dataSource.getConnection();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw Errors.DATABASE_CONNECTION_FAILED.getException();
        }
    }

    private static void close() {
        try {
            cnn.close();
        }
        catch(Exception ignore) { }
    } // placeholder method

    @Override
    public ArrayList<Object> get() {
        //        Statement stmt = cnn.createStatement();
        //        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
        //        System.out.println(rs.findColumn("name"));
        return null;
    }

    @Override
    public void add(Object obj) {}

    @Override
    public void update(Object obj) {

    }

    @Override
    public void delete(Object obj) {

    }
}
