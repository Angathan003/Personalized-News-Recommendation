package org.example.personalizednewsrecommendation.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12748920";
    private static final String USER = "sql12748920";
    private static final String PASSWORD = "uBBZknHp1V";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
