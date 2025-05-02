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
        
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Verificar si el email ya existe
            String checkSql = "SELECT * FROM usuario WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                request.setAttribute("error", "El email ya está registrado");
                request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
                return;
            }
            
            // Registrar nuevo usuario
            String insertSql = "INSERT INTO usuario (nombre, email, contraseña) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, nombre);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);
            
            int rowsAffected = insertStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?registro=exitoso");
            } else {
                request.setAttribute("error", "Error al registrar usuario");
                request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("/pages/registro.jsp").forward(request, response);
        }
    }
}