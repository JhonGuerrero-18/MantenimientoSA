package dao;

import models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mantenimiento_sa?useSSL=false";
        String user = "root";
        String password = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
    // MÉTODO ORIGINAL - Login de usuario
    public Usuario login(String email, String contraseña) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND contraseña = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("🔐 Intentando login: " + email);
            
            stmt.setString(1, email);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                usuario.setUltimo_login(rs.getTimestamp("ultimo_login"));
                usuario.setRol(rs.getString("rol"));
                
                // Actualizar ultimo_login
                actualizarUltimoLogin(usuario.getId_usuario());
                
                System.out.println("✅ Login exitoso para: " + usuario.getNombre() + " (Rol: " + usuario.getRol() + ")");
                return usuario;
            } else {
                System.out.println("❌ Credenciales incorrectas para: " + email);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error en login: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // MÉTODO ORIGINAL - Registrar usuario
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, email, contraseña, rol, fecha_creacion) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("📝 Registrando usuario: " + usuario.getEmail());
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getRol() != null ? usuario.getRol() : "cliente");
            
            boolean exito = stmt.executeUpdate() > 0;
            System.out.println("✅ Usuario registrado: " + exito);
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // NUEVO - Listar todos los usuarios
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY id_usuario DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("📋 Consultando usuarios existentes...");
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                usuario.setUltimo_login(rs.getTimestamp("ultimo_login"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
            
            System.out.println("✅ Usuarios encontrados: " + usuarios.size());
            
        } catch (SQLException e) {
            System.out.println("❌ Error al consultar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }
    
    // NUEVO - Obtener usuario por ID
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                usuario.setUltimo_login(rs.getTimestamp("ultimo_login"));
                usuario.setRol(rs.getString("rol"));
                
                System.out.println("✅ Usuario encontrado: " + usuario.getNombre());
                return usuario;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // NUEVO - Verificar si existe email
    public boolean existeEmail(String email) {
        String sql = "SELECT id_usuario FROM usuario WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.out.println("❌ Error al verificar email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // NUEVO - Actualizar usuario
    public boolean actualizar(Usuario usuario) {
        String sql;
        
        // Si hay contraseña nueva, actualizarla también
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            sql = "UPDATE usuario SET nombre = ?, email = ?, contraseña = ?, rol = ? WHERE id_usuario = ?";
        } else {
            sql = "UPDATE usuario SET nombre = ?, email = ?, rol = ? WHERE id_usuario = ?";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("📝 Actualizando usuario: " + usuario.getEmail());
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            
            if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
                stmt.setString(3, usuario.getContraseña());
                stmt.setString(4, usuario.getRol());
                stmt.setInt(5, usuario.getId_usuario());
            } else {
                stmt.setString(3, usuario.getRol());
                stmt.setInt(4, usuario.getId_usuario());
            }
            
            boolean exito = stmt.executeUpdate() > 0;
            System.out.println("✅ Usuario actualizado: " + exito);
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // NUEVO - Eliminar usuario
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("🗑️ Eliminando usuario ID: " + id);
            
            stmt.setInt(1, id);
            boolean exito = stmt.executeUpdate() > 0;
            System.out.println("✅ Usuario eliminado: " + exito);
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // NUEVO - Activar/Desactivar usuario (opcional - requiere campo 'activo' en BD)
    public boolean toggleActivo(int id) {
        // Nota: Esto requeriría agregar un campo 'activo' a la tabla usuario
        // Por ahora, solo retornamos true como placeholder
        System.out.println("🔄 Toggle activo para usuario ID: " + id);
        return true;
    }
    
    // MÉTODO AUXILIAR - Actualizar último login
    private void actualizarUltimoLogin(int id_usuario) {
        String sql = "UPDATE usuario SET ultimo_login = NOW() WHERE id_usuario = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id_usuario);
            stmt.executeUpdate();
            System.out.println("✅ Ultimo_login actualizado para usuario: " + id_usuario);
            
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar ultimo_login: " + e.getMessage());
        }
    }
    
    // MÉTODO AUXILIAR - Verificar conexión a base de datos
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexión a base de datos exitosa");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
            return false;
        }
    }
}