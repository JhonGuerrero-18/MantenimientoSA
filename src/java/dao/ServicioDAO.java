package dao;

import models.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    
    // URL CORREGIDA CON EL NOMBRE REAL DE LA BD
    private static final String URL = "jdbc:mysql://localhost:3306/mantenimiento_sa?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ DEBUG DAO: Driver MySQL cargado");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ DEBUG DAO: Error driver: " + e.getMessage());
            throw new SQLException("Driver MySQL no encontrado");
        }
        
        System.out.println("🔍 DEBUG DAO: Conectando a: " + URL);
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("✅ DEBUG DAO: Conexión MySQL exitosa!");
        return conn;
    }
    
    public List<Servicio> obtenerTodos() {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT id_servicio, descripcion, fecha, id_usuario, estado FROM servicio ORDER BY id_servicio DESC";
        
        System.out.println("========================================");
        System.out.println("🔍 DEBUG DAO: INICIANDO obtenerTodos()");
        System.out.println("🔍 DEBUG DAO: SQL: " + sql);
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("✅ DEBUG DAO: Query ejecutado, procesando resultados...");
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                
                int idServicio = rs.getInt("id_servicio");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha");
                int idUsuario = rs.getInt("id_usuario");
                String estado = rs.getString("estado");
                
                System.out.println("📄 DEBUG DAO: Fila " + contador + 
                    " - ID: " + idServicio + 
                    " | Desc: '" + descripcion + "'" +
                    " | Fecha: " + fecha +
                    " | Usuario: " + idUsuario +
                    " | Estado: '" + estado + "'");
                
                Servicio servicio = new Servicio();
                servicio.setId(idServicio);  // USAR setId en lugar de setD_servicio
                servicio.setDescripcion(descripcion);
                servicio.setFecha(fecha);
                servicio.setId_usuario(idUsuario);
                servicio.setEstado(estado);
                
                servicios.add(servicio);
            }
            
            System.out.println("✅ DEBUG DAO: Total servicios procesados: " + servicios.size());
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL: " + e.getMessage());
            System.out.println("❌ DEBUG DAO: SQLState: " + e.getSQLState());
            System.out.println("❌ DEBUG DAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        System.out.println("🔄 DEBUG DAO: Retornando lista con " + servicios.size() + " servicios");
        return servicios;
    }
    
    public Servicio obtenerPorId(int id) {
        String sql = "SELECT id_servicio, descripcion, fecha, id_usuario, estado FROM servicio WHERE id_servicio = ?";
        
        System.out.println("🔍 DEBUG DAO: Obteniendo servicio por ID: " + id);
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setFecha(rs.getDate("fecha"));
                servicio.setId_usuario(rs.getInt("id_usuario"));
                servicio.setEstado(rs.getString("estado"));
                
                System.out.println("✅ DEBUG DAO: Servicio encontrado: " + servicio.toString());
                return servicio;
            } else {
                System.out.println("⚠️ DEBUG DAO: No se encontró servicio con ID: " + id);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error al obtener por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean insertar(Servicio servicio) {
        String sql = "INSERT INTO servicio (descripcion, fecha, id_usuario, estado) VALUES (?, ?, ?, ?)";
        
        System.out.println("==========================================");
        System.out.println("📝 DEBUG DAO: INSERTANDO NUEVO SERVICIO");
        System.out.println("==========================================");
        System.out.println("   📝 Descripción: '" + servicio.getDescripcion() + "'");
        System.out.println("   📅 Fecha: " + servicio.getFecha());
        System.out.println("   👤 Usuario: " + servicio.getId_usuario());
        System.out.println("   📊 Estado: '" + servicio.getEstado() + "'");
        System.out.println("   📄 SQL: " + sql);
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // CONFIGURAR PARÁMETROS
            stmt.setString(1, servicio.getDescripcion());
            stmt.setDate(2, new java.sql.Date(servicio.getFecha().getTime()));
            stmt.setInt(3, servicio.getId_usuario());
            stmt.setString(4, servicio.getEstado());
            
            System.out.println("🔄 DEBUG DAO: Ejecutando INSERT...");
            
            // EJECUTAR INSERT
            int filasAfectadas = stmt.executeUpdate();
            
            System.out.println("📊 DEBUG DAO: Filas afectadas: " + filasAfectadas);
            
            // OBTENER ID GENERADO
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    System.out.println("🆔 DEBUG DAO: ID generado: " + idGenerado);
                    servicio.setId(idGenerado);  // Actualizar el objeto con el ID generado
                }
            }
            
            boolean exito = filasAfectadas > 0;
            
            if (exito) {
                System.out.println("✅ DEBUG DAO: SERVICIO INSERTADO EXITOSAMENTE");
            } else {
                System.out.println("❌ DEBUG DAO: NO SE INSERTÓ EL SERVICIO");
            }
            
            System.out.println("==========================================");
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: ERROR AL INSERTAR SERVICIO");
            System.out.println("❌ Mensaje: " + e.getMessage());
            System.out.println("❌ SQLState: " + e.getSQLState());
            System.out.println("❌ Error Code: " + e.getErrorCode());
            e.printStackTrace();
            System.out.println("==========================================");
            return false;
        }
    }
    
    public boolean actualizar(Servicio servicio) {
        String sql = "UPDATE servicio SET descripcion = ?, fecha = ?, id_usuario = ?, estado = ? WHERE id_servicio = ?";
        
        System.out.println("🔄 DEBUG DAO: Actualizando servicio ID: " + servicio.getId());
        System.out.println("   Nueva descripción: " + servicio.getDescripcion());
        System.out.println("   Nueva fecha: " + servicio.getFecha());
        System.out.println("   Nuevo usuario: " + servicio.getId_usuario());
        System.out.println("   Nuevo estado: " + servicio.getEstado());
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, servicio.getDescripcion());
            stmt.setDate(2, new java.sql.Date(servicio.getFecha().getTime()));
            stmt.setInt(3, servicio.getId_usuario());
            stmt.setString(4, servicio.getEstado());
            stmt.setInt(5, servicio.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("✅ DEBUG DAO: Servicio actualizado: " + exito + " (filas: " + filasAfectadas + ")");
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error al actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM servicio WHERE id_servicio = ?";
        
        System.out.println("🗑️ DEBUG DAO: Eliminando servicio ID: " + id);
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("✅ DEBUG DAO: Servicio eliminado: " + exito + " (filas: " + filasAfectadas + ")");
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error al eliminar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}