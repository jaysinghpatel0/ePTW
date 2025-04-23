package com.example.eptw.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MSSQLConnection {
    private static final String URL = "jdbc:jtds:sqlserver://49.50.100.105:5263/GrasimChemicals";
    private static final String USER = "GrasimChemicals";
    private static final String PASSWORD = "qk#4R021a";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
