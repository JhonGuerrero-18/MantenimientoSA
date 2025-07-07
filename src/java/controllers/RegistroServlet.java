package controllers;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import utilities.DatabaseConnection;

//@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            // Verificar si el email ya existe
            if (existeUsuario(email, request)) {
                if (isJsonRequest(request)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"success\":false,\"message\":\"El email ya está registrado\"}");
                } else {
                    request.setAttribute("error", "El email ya está registrado");
                    request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
                }
                return;
            }
            
            // Registrar nuevo usuario
            registrarUsuario(nombre, email, password, request);
            
            if (isJsonRequest(request)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"success\":true,\"message\":\"Usuario registrado exitosamente\"}");
            } else {
                request.getSession().setAttribute("success", "Registro exitoso. Por favor inicie sesión.");
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            }
            
        } catch (SQLException e) {
            if (isJsonRequest(request)) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\":false,\"message\":\"Error al registrar usuario: " + e.getMessage() + "\"}");
            } else {
                request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
                request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
            }
            e.printStackTrace();
        }
    }

    private boolean existeUsuario(String email, HttpServletRequest request) throws SQLException {
        String sql = "SELECT id_usuario FROM usuario WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private void registrarUsuario(String nombre, String email, String password, HttpServletRequest request) 
            throws SQLException {
        // Query ajustada a tu estructura de BD (sin fecha_creacion manual)
        String sql = "INSERT INTO usuario (nombre, email, contraseña) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();
        }
    }
    
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        String accept = request.getHeader("Accept");
        
        return (contentType != null && contentType.contains("application/json")) ||
               (accept != null && accept.contains("application/json")) ||
               "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}