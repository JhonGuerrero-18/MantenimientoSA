package dao;

import java.sql.*;

public class TestConexion {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/mantenimientosa";
        String USER = "root";
        String PASSWORD = "";
        
        System.out.println("=== PROBANDO CONEXIÓN A BD ===");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver cargado");
            
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión establecida");
            
            // Probar consulta directa
            String sql = "SELECT COUNT(*) as total FROM servicio";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✅ Total servicios en BD: " + total);
            }
            
            // Probar consulta completa
            sql = "SELECT id_servicio, descripcion, fecha, id_usuario, estado FROM servicio LIMIT 3";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            System.out.println("=== PRIMEROS 3 SERVICIOS ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_servicio") + 
                                 ", Desc: " + rs.getString("descripcion") +
                                 ", Fecha: " + rs.getDate("fecha") +
                                 ", Usuario: " + rs.getInt("id_usuario") +
                                 ", Estado: " + rs.getString("estado"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}   