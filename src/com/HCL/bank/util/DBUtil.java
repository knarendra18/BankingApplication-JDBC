package com.HCL.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    public static Connection getDBConnection() {

        Connection connection = null;

        try {
            // 1. Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 2. Database connection details
            String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
            String username = "system";
            String password = "Your Passwword";

            // 3. Establish connection
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
