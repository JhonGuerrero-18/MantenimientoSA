package controllers;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import utilities.DatabaseConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar para manejar tanto formularios como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            Connection conn = DatabaseConnection.getConnection(getServletContext());
            // Query ajustada para incluir rol
            String sql = "SELECT id_usuario, nombre, rol FROM usuario WHERE email = ? AND contraseña = ?"; // MODIFICADO
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Login exitoso
                HttpSession session = request.getSession();
                session.setAttribute("user", email);
                session.setAttribute("userId", rs.getInt("id_usuario"));
                session.setAttribute("userName", rs.getString("nombre"));
                session.setAttribute("userRole", rs.getString("rol")); // AGREGADO
                
                // Actualizar último login
                updateLastLogin(rs.getInt("id_usuario"), conn);
                
                // Si viene de React, enviar JSON
                if (isJsonRequest(request)) {
                    response.getWriter().write("{\"success\":true,\"message\":\"Login exitoso\",\"user\":\"" + email + "\",\"role\":\"" + rs.getString("rol") + "\"}"); // MODIFICADO
                } else {
                    // Si viene de JSP, redirigir normalmente
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
                
            } else {
                // Credenciales incorrectas
                if (isJsonRequest(request)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"success\":false,\"message\":\"Credenciales incorrectas\"}");
                } else {
                    request.setAttribute("error", "Credenciales incorrectas");
                    request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
                }
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            if (isJsonRequest(request)) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\":false,\"message\":\"Error de conexión: " + e.getMessage() + "\"}");
            } else {
                request.setAttribute("error", "Error de conexión: " + e.getMessage());
                request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            }
            e.printStackTrace();
        }
    }
    
    /**
     * Actualizar último login del usuario
     */
    private void updateLastLogin(int userId, Connection conn) throws SQLException {
        String sql = "UPDATE usuario SET ultimo_login = CURRENT_TIMESTAMP WHERE id_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Detectar si la request viene de React (JSON) o JSP (form)
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        String accept = request.getHeader("Accept");
        
        return (contentType != null && contentType.contains("application/json")) ||
               (accept != null && accept.contains("application/json")) ||
               "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}