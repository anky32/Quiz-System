package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/quizapp"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "password"; 

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Ensure MySQL Connector JAR is added.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection testConnection = DatabaseConnector.connect();
        if (testConnection != null) {
            System.out.println("Connection Test Successful!");
        } else {
            System.out.println("Connection Test Failed.");
        }
    }
}
