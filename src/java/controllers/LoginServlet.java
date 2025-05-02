package controllers;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Por favor ingrese email y contraseña");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }
        
        // Consulta modificada para usar 'usuario' y 'contraseña'
        String sql = "SELECT id_usuario, nombre, email FROM usuario WHERE email = ? AND contraseña = ?";
        
        try (Connection conn = utilities.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", rs.getString("nombre"));
                    session.setAttribute("userEmail", rs.getString("email"));
                    session.setAttribute("userId", rs.getInt("id_usuario"));
                    
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                } else {
                    request.setAttribute("error", "Email o contraseña incorrectos");
                    request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        }
    }
}