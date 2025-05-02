package controllers;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import models.Tecnico;
import utilities.DatabaseConnection;

@WebServlet("/tecnicos")
public class TecnicoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Siempre obtener los técnicos frescos desde la base de datos
            List<Tecnico> tecnicos = obtenerTecnicosDesdeBD();
            request.setAttribute("tecnicos", tecnicos);
            
            // Manejar mensajes de éxito/error
            manejarMensajes(request);
            
        } catch (SQLException e) {
            request.setAttribute("error", "Error al cargar técnicos: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/pages/tecnicos.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String especialidad = request.getParameter("especialidad");
        
        try {
            guardarTecnicoEnBD(nombre, especialidad);
            request.getSession().setAttribute("success", "Técnico registrado correctamente");
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "Error al guardar técnico: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/tecnicos");
    }
    
    private List<Tecnico> obtenerTecnicosDesdeBD() throws SQLException {
        List<Tecnico> tecnicos = new ArrayList<>();
        String sql = "SELECT id_tecnico, nombre, especialidad FROM tecnico ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tecnicos.add(new Tecnico(
                    rs.getInt("id_tecnico"),
                    rs.getString("nombre"),
                    rs.getString("especialidad")
                ));
            }
        }
        return tecnicos;
    }
    
    private void guardarTecnicoEnBD(String nombre, String especialidad) throws SQLException {
        String sql = "INSERT INTO tecnico (nombre, especialidad) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, especialidad);
            stmt.executeUpdate();
        }
    }
    
    private void manejarMensajes(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        if (session.getAttribute("success") != null) {
            request.setAttribute("success", session.getAttribute("success"));
            session.removeAttribute("success");
        }
        
        if (session.getAttribute("error") != null) {
            request.setAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
    }
}