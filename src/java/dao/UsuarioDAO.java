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
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
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
                usuarios.add(usuario);
            }
            
            System.out.println("✅ Usuarios encontrados: " + usuarios.size());
            
        } catch (SQLException e) {
            System.out.println("❌ Error al consultar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }
    
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
                
                // Actualizar ultimo_login
                actualizarUltimoLogin(usuario.getId_usuario());
                
                System.out.println("✅ Login exitoso para: " + usuario.getNombre());
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
    
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, email, contraseña, fecha_creacion) VALUES (?, ?, ?, NOW())";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("📝 Registrando usuario: " + usuario.getEmail());
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContraseña());
            
            boolean exito = stmt.executeUpdate() > 0;
            System.out.println("✅ Usuario registrado: " + exito);
            return exito;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
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
}