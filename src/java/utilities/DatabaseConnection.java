package utilities;

import java.sql.*;
import javax.servlet.ServletContext;

public class DatabaseConnection {
    public static Connection getConnection(ServletContext context) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mantenimiento_sa?useSSL=false";
        String user = "root";
        String password = "";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado");
        }
    }
}