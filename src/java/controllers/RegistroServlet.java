package controllers;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import utilities.DatabaseConnection;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            // Verificar si el email ya existe
            if (existeUsuario(email, request)) {
                request.setAttribute("error", "El email ya está registrado");
                request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
                return;
            }
            
            // Registrar nuevo usuario
            registrarUsuario(nombre, email, password, request);
            
            // Redirigir a login con mensaje de éxito
            request.getSession().setAttribute("success", "Registro exitoso. Por favor inicie sesión.");
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
            request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
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
        String sql = "INSERT INTO usuario (nombre, email, contraseña) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();
        }
    }
}