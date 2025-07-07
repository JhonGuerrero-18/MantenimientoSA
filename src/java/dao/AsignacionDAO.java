package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsignacionDAO {
    
    private static final String URL = "jdbc:mysql://localhost:3306/mantenimiento_sa";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
    // Método para listar TODOS los servicios (CORREGIDO)
    public List<Map<String, Object>> listarTodosLosServicios() {
        List<Map<String, Object>> servicios = new ArrayList<>();
        String sql = "SELECT " +
                "s.id_servicio, " +
                "s.descripcion, " +
                "s.estado, " +
                "s.fecha, " +
                "u.nombre " +
                "FROM servicio s " +
                "INNER JOIN usuario u ON s.id_usuario = u.id_usuario " +
                "ORDER BY s.fecha DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> servicio = new HashMap<>();
                servicio.put("id_servicio", rs.getInt("id_servicio"));
                servicio.put("descripcion", rs.getString("descripcion"));
                servicio.put("estado", rs.getString("estado"));
                servicio.put("fecha_solicitud", rs.getDate("fecha"));
                servicio.put("cliente", rs.getString("nombre"));
                servicios.add(servicio);
            }
            
            System.out.println("✅ TODOS los servicios obtenidos: " + servicios.size());
            for (Map<String, Object> s : servicios) {
                System.out.println("   - [ID:" + s.get("id_servicio") + "] " + 
                        s.get("descripcion") + " | Estado: " + s.get("estado") + 
                        " | Cliente: " + s.get("cliente"));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al listar servicios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return servicios;
    }
    
    // Método para listar TODOS los técnicos (CORREGIDO)
    public List<Map<String, Object>> listarTodosLosTecnicos() {
        List<Map<String, Object>> tecnicos = new ArrayList<>();
        String sql = "SELECT id_tecnico, nombre, especialidad " +
                "FROM tecnico " +
                "ORDER BY nombre";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> tecnico = new HashMap<>();
                tecnico.put("id", rs.getInt("id_tecnico"));
                tecnico.put("nombre", rs.getString("nombre"));
                tecnico.put("especialidad", rs.getString("especialidad"));
                tecnico.put("nombre_completo", rs.getString("nombre"));
                tecnicos.add(tecnico);
            }
            
            System.out.println("✅ TODOS los técnicos encontrados: " + tecnicos.size());
            for (Map<String, Object> t : tecnicos) {
                System.out.println("   - [ID:" + t.get("id") + "] " + 
                        t.get("nombre_completo") + " | Especialidad: " + t.get("especialidad"));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al listar técnicos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tecnicos;
    }
    
    // Listar asignaciones actuales - CORREGIDO PARA COINCIDIR CON JSP
    public List<Map<String, Object>> listarAsignacionesCompletas() {
        List<Map<String, Object>> asignaciones = new ArrayList<>();
        String sql = "SELECT " +
                "a.id_asignacion, " +
                "a.id_servicio, " +
                "a.id_tecnico, " +
                "s.descripcion as servicio_descripcion, " +
                "s.estado as servicio_estado, " +
                "s.fecha as servicio_fecha, " +
                "t.nombre as tecnico_nombre, " +
                "t.especialidad as tecnico_especialidad, " +
                "u.nombre as cliente_nombre " +
                "FROM asignacion a " +
                "INNER JOIN servicio s ON a.id_servicio = s.id_servicio " +
                "INNER JOIN tecnico t ON a.id_tecnico = t.id_tecnico " +
                "INNER JOIN usuario u ON s.id_usuario = u.id_usuario " +
                "ORDER BY a.id_asignacion DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> asignacion = new HashMap<>();
                asignacion.put("id_asignacion", rs.getInt("id_asignacion"));
                asignacion.put("id_servicio", rs.getInt("id_servicio"));
                asignacion.put("id_tecnico", rs.getInt("id_tecnico"));
                
                // NOMBRES CORREGIDOS PARA COINCIDIR CON JSP:
                asignacion.put("nombre_tecnico", rs.getString("tecnico_nombre"));
                asignacion.put("descripcion_servicio", rs.getString("servicio_descripcion"));
                asignacion.put("estado_servicio", rs.getString("servicio_estado"));
                asignacion.put("fecha_asignacion", rs.getDate("servicio_fecha"));
                
                // CAMPOS ADICIONALES:
                asignacion.put("tecnico_especialidad", rs.getString("tecnico_especialidad"));
                asignacion.put("cliente_nombre", rs.getString("cliente_nombre"));
                
                asignaciones.add(asignacion);
                
                System.out.println("✅ Asignación procesada: " +
                    "ID=" + rs.getInt("id_asignacion") +
                    ", Técnico=" + rs.getString("tecnico_nombre") +
                    ", Servicio=" + rs.getString("servicio_descripcion"));
            }
            
            System.out.println("📋 Total asignaciones encontradas: " + asignaciones.size());
            
        } catch (SQLException e) {
            System.out.println("❌ Error al listar asignaciones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return asignaciones;
    }
    
    // Listar servicios SIN asignar
    public List<Map<String, Object>> listarServiciosSinAsignar() {
        List<Map<String, Object>> servicios = new ArrayList<>();
        String sql = "SELECT " +
                "s.id_servicio, " +
                "s.descripcion, " +
                "s.estado, " +
                "s.fecha, " +
                "u.nombre as cliente_nombre " +
                "FROM servicio s " +
                "INNER JOIN usuario u ON s.id_usuario = u.id_usuario " +
                "LEFT JOIN asignacion a ON s.id_servicio = a.id_servicio " +
                "WHERE a.id_servicio IS NULL " +
                "ORDER BY s.fecha DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> servicio = new HashMap<>();
                servicio.put("id_servicio", rs.getInt("id_servicio"));
                servicio.put("descripcion", rs.getString("descripcion"));
                servicio.put("estado", rs.getString("estado"));
                servicio.put("fecha_solicitud", rs.getDate("fecha"));
                servicio.put("cliente", rs.getString("cliente_nombre"));
                servicios.add(servicio);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al listar servicios sin asignar: " + e.getMessage());
            e.printStackTrace();
        }
        
        return servicios;
    }
    
    // Crear nueva asignación
    public boolean crearAsignacion(int idServicio, int idTecnico) {
        String sql = "INSERT INTO asignacion (id_servicio, id_tecnico) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idServicio);
            stmt.setInt(2, idTecnico);
            
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("✅ Asignación creada: " + exito + " (Servicio: " + idServicio + ", Técnico: " + idTecnico + ")");
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al crear asignación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // MÉTODO REQUERIDO POR AsignacionServlet
    public boolean asignar(int idTecnico, int idServicio) {
        System.out.println("🔗 Creando asignación: Técnico " + idTecnico + " → Servicio " + idServicio);
        return crearAsignacion(idServicio, idTecnico);
    }
    
    // Eliminar asignación
    public boolean eliminarAsignacion(int idAsignacion) {
        String sql = "DELETE FROM asignacion WHERE id_asignacion = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAsignacion);
            int filasAfectadas = stmt.executeUpdate();
            boolean exito = filasAfectadas > 0;
            
            System.out.println("✅ Asignación eliminada: " + exito + " (ID: " + idAsignacion + ")");
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar asignación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Método para debug de datos
    public void debugDatos() {
        System.out.println("🔍 === DEBUG DATOS BD ===");
        
        try (Connection conn = getConnection()) {
            // Contar servicios
            String sql = "SELECT COUNT(*) FROM servicio";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("   - Servicios en BD: " + rs.getInt(1));
            }
            
            // Contar técnicos
            sql = "SELECT COUNT(*) FROM tecnico";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("   - Técnicos en BD: " + rs.getInt(1));
            }
            
            // Contar asignaciones
            sql = "SELECT COUNT(*) FROM asignacion";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("   - Asignaciones en BD: " + rs.getInt(1));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error en debug: " + e.getMessage());
        }
    }
}