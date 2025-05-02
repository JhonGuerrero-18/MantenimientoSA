package utilities;  // Asegúrate de que coincida con tu paquete

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("¡Conexión exitosa a la base de datos!");
            
            // Prueba consulta a la tabla usuario
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM usuario");
            
            if (rs.next()) {
                System.out.println("Número de usuarios registrados: " + rs.getInt("total"));
            }
            
            // Consulta adicional para listar usuarios
            System.out.println("\nListado de usuarios:");
            ResultSet usuarios = stmt.executeQuery("SELECT id_usuario, nombre, email FROM usuario");
            while (usuarios.next()) {
                System.out.println(
                    "ID: " + usuarios.getInt("id_usuario") + 
                    " | Nombre: " + usuarios.getString("nombre") + 
                    " | Email: " + usuarios.getString("email")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error en la conexión:");
            e.printStackTrace();
        }
    }
}