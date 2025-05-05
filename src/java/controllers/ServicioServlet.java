package controllers;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import models.Servicio;
import utilities.DatabaseConnection;

@WebServlet("/servicios")
public class ServicioServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            List<Servicio> servicios = obtenerServiciosDesdeBD(request);
            request.setAttribute("servicios", servicios);
            
            request.getRequestDispatcher("/pages/servicios.jsp").forward(request, response);
            
        } catch (SQLException e) {
            request.setAttribute("error", "Error al cargar servicios: " + e.getMessage());
            request.getRequestDispatcher("/pages/servicios.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String descripcion = request.getParameter("descripcion");
        String fecha = request.getParameter("fecha");
        HttpSession session = request.getSession();
        String emailUsuario = (String) session.getAttribute("user");
        
        try {
            // Obtener ID del usuario
            int idUsuario = obtenerIdUsuario(emailUsuario, request);
            
            if (idUsuario == 0) {
                throw new SQLException("Usuario no encontrado");
            }
            
            // Crear nuevo servicio
            crearServicio(descripcion, fecha, idUsuario, request);
            
            // Redirigir con mensaje de éxito
            session.setAttribute("success", "Servicio creado correctamente");
            response.sendRedirect(request.getContextPath() + "/servicios");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Error al crear servicio: " + e.getMessage());
            request.getRequestDispatcher("/pages/servicios.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

    private List<Servicio> obtenerServiciosDesdeBD(HttpServletRequest request) throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT id_servicio, descripcion, fecha, estado FROM servicio ORDER BY fecha DESC";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setFecha(rs.getDate("fecha"));
                servicio.setEstado(rs.getString("estado"));
                servicios.add(servicio);
            }
        }
        return servicios;
    }
    
    private int obtenerIdUsuario(String email, HttpServletRequest request) throws SQLException {
        String sql = "SELECT id_usuario FROM usuario WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("id_usuario") : 0;
            }
        }
    }
    
    private void crearServicio(String descripcion, String fecha, int idUsuario, HttpServletRequest request) 
            throws SQLException {
        String sql = "INSERT INTO servicio (descripcion, fecha, estado, id_usuario) VALUES (?, ?, 'Pendiente', ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, descripcion);
            stmt.setString(2, fecha);
            stmt.setInt(3, idUsuario);
            stmt.executeUpdate();
        }
    }
}