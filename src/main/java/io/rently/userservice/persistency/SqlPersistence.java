package io.rently.userservice.persistency;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Controller
public class SqlPersistence<T> implements IDatabaseContext<T> {
    private static final MysqlDataSource dataSource = new MysqlDataSource();
    private static Connection cnn;

    @Autowired
    public SqlPersistence() {
        this.setUser("dbi433816")
            .setPassword("admin")
            .setServerName("studmysql01.fhict.local")
            .setDatabaseName("dbi433816");
    }

    public SqlPersistence<T> setUser(String user) {
        dataSource.setUser(user);
        return this;
    }

    public SqlPersistence<T> setPassword(String password) {
        dataSource.setPassword(password);
        return this;
    }

    public SqlPersistence<T> setServerName(String server) {
        dataSource.setServerName(server);
        return this;
    }

    public SqlPersistence<T> setDatabaseName(String db) {
        dataSource.setDatabaseName(db);
        return this;
    }

    public void initConnection() {
        throw Errors.DATABASE_CONNECTION_FAILED.getException();
//
//        try {
//            cnn = dataSource.getConnection();
//        }
//        catch (SQLException ex) {
//            System.out.println("SERVER ERROR");
//            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to establish connection to database: " + ex.getMessage());
//        }
    }

    @Override
    public ArrayList<T> get() {
        //        Statement stmt = cnn.createStatement();
        //        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
        //        System.out.println(rs.findColumn("name"));
        return null;
    }

    @Override
    public void add(T obj) {
        try {
            Statement sql = cnn.createStatement();
        }
        catch(SQLException ex) {
//            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Override
    public void update(T obj) {

    }

    @Override
    public void delete(T obj) {

    }
}
