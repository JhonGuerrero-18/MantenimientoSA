package utilities;

import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        testDatabaseConnection();
    }

    public static void testDatabaseConnection() {
        System.out.println("=== Prueba de Conexión a Base de Datos ===");
        
        // Configuración manual para pruebas (ajusta estos valores)
        String host = "localhost";
        String dbName = "mantenimiento_sa";
        String user = "root";
        String password = ""; // Tu contraseña si existe
        
        String url = "jdbc:mysql://" + host + ":3306/" + dbName + 
                    "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
        
        try {
            // 1. Cargar el driver explícitamente
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("✔ Driver MySQL cargado correctamente");
            
            // 2. Establecer conexión
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                System.out.println("✔ Conexión establecida con éxito");
                
                // 3. Probar consultas a todas las tablas esenciales
                testTable(conn, "usuario");
                testTable(conn, "tecnico");
                testTable(conn, "servicio");
                testTable(conn, "asignacion");
                
                // 4. Verificar campo 'estado' en servicio
                verifyColumn(conn, "servicio", "estado");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✖ Error: Driver MySQL no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✖ Error de conexión a la base de datos:");
            System.err.println("URL: " + url);
            System.err.println("Usuario: " + user);
            e.printStackTrace();
            
            // Mensajes adicionales para diagnóstico
            diagnoseConnectionError(e);
        }
    }

    private static void testTable(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.printf("✔ Tabla %-10s %3d registros encontrados%n", 
                                tableName + ":", rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.printf("✖ Error consultando tabla %s: %s%n", 
                            tableName, e.getMessage());
            throw e;
        }
    }
    
    private static void verifyColumn(Connection conn, String table, String column) throws SQLException {
        String sql = "SELECT " + column + " FROM " + table + " LIMIT 1";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.printf("✔ Columna %-15s existe en %s%n", 
                                column + ":", table);
            }
        } catch (SQLException e) {
            System.err.printf("✖ Error: Columna %s no existe en %s%n", 
                            column, table);
            throw e;
        }
    }
    
    private static void diagnoseConnectionError(SQLException e) {
        System.err.println("\n=== Diagnóstico del Error ===");
        
        // Códigos de error comunes de MySQL
        if (e.getErrorCode() == 0) {
            System.err.println("Posible causa: Servidor MySQL no está corriendo");
        } else if (e.getErrorCode() == 1045) {
            System.err.println("Posible causa: Usuario/contraseña incorrectos");
        } else if (e.getErrorCode() == 1049) {
            System.err.println("Posible causa: Base de datos no existe");
        } else if (e.getErrorCode() == 2002) {
            System.err.println("Posible causa: No se puede conectar al servidor MySQL");
        } else if (e.getErrorCode() == 1146) {
            System.err.println("Posible causa: Tabla no existe en la base de datos");
        }
        
        System.err.println("Código de error SQL: " + e.getErrorCode());
        System.err.println("Estado SQL: " + e.getSQLState());
        System.err.println("Mensaje: " + e.getMessage());
    }
}