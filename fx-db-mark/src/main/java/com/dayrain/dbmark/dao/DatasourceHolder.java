package com.dayrain.dbmark.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatasourceHolder {
    private static DataSource dataSource;
    private static Connection connection;
    static {
        HikariConfig hikariConfig = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(hikariConfig);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
