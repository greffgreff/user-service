package io.rently.userservice.persistency;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.rently.userservice.errors.HttpException;
import io.rently.userservice.interfaces.IDatabaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;

//@Service
public class SqlPersistence<T> implements IDatabaseContext<T> {
    private static final MysqlDataSource dataSource = new MysqlDataSource();
    private static final Connection cnn;

    static {
        dataSource.setUser("dbi433816");
        dataSource.setPassword("admin");
        dataSource.setServerName("studmysql01.fhict.local");
        dataSource.setDatabaseName("dbi433816");
        try {
            cnn = dataSource.getConnection();
        }
        catch (Exception exception) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not connect to server");
        }
    }

//    @Autowired
    public SqlPersistence() {
//        Statement stmt = cnn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
//        System.out.println(rs.findColumn("name"));
    }

    @Override
    public ArrayList<T> get() {
        return null;
    }

    @Override
    public void add(T obj) {

    }

    @Override
    public void remove(T obj) {

    }

    @Override
    public void update(T obj) {

    }

    @Override
    public void delete(T obj) {

    }
}
