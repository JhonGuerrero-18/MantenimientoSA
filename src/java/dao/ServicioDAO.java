package dao;

import models.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    
    private static final String URL = "jdbc:mysql://localhost:3306/mantenimiento_sa";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Método para obtener conexión
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: No se pudo cargar el driver de MySQL", e);
        }
    }
    
    // Obtener todos los servicios
    public List<Servicio> obtenerTodos() {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT id_servicio, descripcion, fecha, id_usuario, estado FROM servicio ORDER BY id_servicio DESC";
        
        System.out.println("========================================");
        System.out.println("🔍 DEBUG DAO: OBTENIENDO TODOS LOS SERVICIOS");
        System.out.println("🔍 DEBUG DAO: SQL: " + sql);
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("✅ DEBUG DAO: Conexión establecida, ejecutando query...");
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                
                int id = rs.getInt("id_servicio");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha");
                int idUsuario = rs.getInt("id_usuario");
                String estado = rs.getString("estado");
                
                System.out.println("📄 DEBUG DAO: Fila " + contador + 
                    " - ID: " + id + 
                    " | Desc: '" + descripcion + "'" +
                    " | Fecha: " + fecha +
                    " | Usuario: " + idUsuario +
                    " | Estado: '" + estado + "'");
                
                Servicio servicio = new Servicio();
                servicio.setD_servicio(id);
                servicio.setDescripcion(descripcion);
                servicio.setFecha(fecha);
                servicio.setId_usuario(idUsuario);
                servicio.setEstado(estado);
                
                servicios.add(servicio);
            }
            
            System.out.println("✅ DEBUG DAO: Total servicios encontrados: " + servicios.size());
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL: " + e.getMessage());
            System.out.println("❌ DEBUG DAO: SQLState: " + e.getSQLState());
            System.out.println("❌ DEBUG DAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        System.out.println("🔄 DEBUG DAO: Retornando lista con " + servicios.size() + " servicios");
        return servicios;
    }
    
    // Obtener servicio por ID
    public Servicio obtenerPorId(int id) {
        String sql = "SELECT id_servicio, descripcion, fecha, id_usuario, estado FROM servicio WHERE id_servicio = ?";
        
        System.out.println("========================================");
        System.out.println("🔍 DEBUG DAO: OBTENIENDO SERVICIO POR ID: " + id);
        System.out.println("🔍 DEBUG DAO: SQL: " + sql);
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int idServicio = rs.getInt("id_servicio");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha");
                int idUsuario = rs.getInt("id_usuario");
                String estado = rs.getString("estado");
                
                System.out.println("✅ DEBUG DAO: Servicio encontrado - ID: " + idServicio + 
                    " | Desc: '" + descripcion + "'" +
                    " | Fecha: " + fecha +
                    " | Usuario: " + idUsuario +
                    " | Estado: '" + estado + "'");
                
                Servicio servicio = new Servicio();
                servicio.setD_servicio(idServicio);
                servicio.setDescripcion(descripcion);
                servicio.setFecha(fecha);
                servicio.setId_usuario(idUsuario);
                servicio.setEstado(estado);
                
                return servicio;
            } else {
                System.out.println("❌ DEBUG DAO: No se encontró servicio con ID: " + id);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Insertar nuevo servicio
    public boolean insertar(Servicio servicio) {
        String sql = "INSERT INTO servicio (descripcion, fecha, id_usuario, estado) VALUES (?, ?, ?, ?)";
        
        System.out.println("========================================");
        System.out.println("📝 DEBUG DAO: INSERTANDO NUEVO SERVICIO");
        System.out.println("📝 DEBUG DAO: SQL: " + sql);
        System.out.println("📝 DEBUG DAO: Descripción: '" + servicio.getDescripcion() + "'");
        System.out.println("📝 DEBUG DAO: Fecha: " + servicio.getFecha());
        System.out.println("📝 DEBUG DAO: Usuario ID: " + servicio.getId_usuario());
        System.out.println("📝 DEBUG DAO: Estado: '" + servicio.getEstado() + "'");
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, servicio.getDescripcion());
            // CORREGIDO: Conversión de java.util.Date a java.sql.Date
            if (servicio.getFecha() != null) {
                stmt.setDate(2, new java.sql.Date(servicio.getFecha().getTime()));
            } else {
                stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            }
            stmt.setInt(3, servicio.getId_usuario());
            stmt.setString(4, servicio.getEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("📊 DEBUG DAO: Filas afectadas: " + filasAfectadas);
            System.out.println(exito ? "✅ DEBUG DAO: INSERCIÓN EXITOSA" : "❌ DEBUG DAO: INSERCIÓN FALLIDA");
            
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL en inserción: " + e.getMessage());
            System.out.println("❌ DEBUG DAO: SQLState: " + e.getSQLState());
            System.out.println("❌ DEBUG DAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }
    
    // Actualizar servicio
    public boolean actualizar(Servicio servicio) {
        String sql = "UPDATE servicio SET descripcion = ?, fecha = ?, id_usuario = ?, estado = ? WHERE id_servicio = ?";
        
        System.out.println("========================================");
        System.out.println("🔄 DEBUG DAO: ACTUALIZANDO SERVICIO ID: " + servicio.getD_servicio());
        System.out.println("🔄 DEBUG DAO: SQL: " + sql);
        System.out.println("🔄 DEBUG DAO: Descripción: '" + servicio.getDescripcion() + "'");
        System.out.println("🔄 DEBUG DAO: Fecha: " + servicio.getFecha());
        System.out.println("🔄 DEBUG DAO: Usuario ID: " + servicio.getId_usuario());
        System.out.println("🔄 DEBUG DAO: Estado: '" + servicio.getEstado() + "'");
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, servicio.getDescripcion());
            // CORREGIDO: Conversión de java.util.Date a java.sql.Date
            if (servicio.getFecha() != null) {
                stmt.setDate(2, new java.sql.Date(servicio.getFecha().getTime()));
            } else {
                stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            }
            stmt.setInt(3, servicio.getId_usuario());
            stmt.setString(4, servicio.getEstado());
            stmt.setInt(5, servicio.getD_servicio());
            
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("📊 DEBUG DAO: Filas afectadas: " + filasAfectadas);
            System.out.println(exito ? "✅ DEBUG DAO: ACTUALIZACIÓN EXITOSA" : "❌ DEBUG DAO: ACTUALIZACIÓN FALLIDA");
            
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL en actualización: " + e.getMessage());
            System.out.println("❌ DEBUG DAO: SQLState: " + e.getSQLState());
            System.out.println("❌ DEBUG DAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }
    
    // Eliminar servicio
    public boolean eliminar(int id) {
        String sql = "DELETE FROM servicio WHERE id_servicio = ?";
        
        System.out.println("========================================");
        System.out.println("🗑️ DEBUG DAO: ELIMINANDO SERVICIO ID: " + id);
        System.out.println("🗑️ DEBUG DAO: SQL: " + sql);
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("📊 DEBUG DAO: Filas afectadas: " + filasAfectadas);
            System.out.println(exito ? "✅ DEBUG DAO: ELIMINACIÓN EXITOSA" : "❌ DEBUG DAO: ELIMINACIÓN FALLIDA");
            
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL en eliminación: " + e.getMessage());
            System.out.println("❌ DEBUG DAO: SQLState: " + e.getSQLState());
            System.out.println("❌ DEBUG DAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }
    
    // NUEVO MÉTODO: Obtener servicios por usuario
    public List<Servicio> obtenerPorUsuario(int idUsuario) {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT id_servicio, descripcion, fecha, id_usuario, estado FROM servicio WHERE id_usuario = ? ORDER BY id_servicio DESC";
        
        System.out.println("========================================");
        System.out.println("🔍 DEBUG DAO: OBTENIENDO SERVICIOS POR USUARIO: " + idUsuario);
        System.out.println("🔍 DEBUG DAO: SQL: " + sql);
        System.out.println("========================================");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("✅ DEBUG DAO: Query ejecutado, procesando resultados...");
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                
                int idServicio = rs.getInt("id_servicio");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha");
                int idUsuarioServicio = rs.getInt("id_usuario");
                String estado = rs.getString("estado");
                
                System.out.println("📄 DEBUG DAO: Fila " + contador + 
                    " - ID: " + idServicio + 
                    " | Desc: '" + descripcion + "'" +
                    " | Fecha: " + fecha +
                    " | Usuario: " + idUsuarioServicio +
                    " | Estado: '" + estado + "'");
                
                Servicio servicio = new Servicio();
                servicio.setD_servicio(idServicio);
                servicio.setDescripcion(descripcion);
                servicio.setFecha(fecha);
                servicio.setId_usuario(idUsuarioServicio);
                servicio.setEstado(estado);
                
                servicios.add(servicio);
            }
            
            System.out.println("✅ DEBUG DAO: Total servicios del usuario " + idUsuario + ": " + servicios.size());
            
        } catch (SQLException e) {
            System.out.println("❌ DEBUG DAO: Error SQL: " + e.getMessage());
            System.out.println("❌ DEBUG DAO: SQLState: " + e.getSQLState());
            System.out.println("❌ DEBUG DAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        System.out.println("🔄 DEBUG DAO: Retornando lista con " + servicios.size() + " servicios del usuario");
        return servicios;
    }
}