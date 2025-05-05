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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        
        try {
            // Manejar acciones (eliminar o editar)
            if (action != null && idParam != null) {
                int id = Integer.parseInt(idParam);
                
                if ("delete".equals(action)) {
                    eliminarTecnico(id, request);
                    request.getSession().setAttribute("success", "Técnico eliminado correctamente");
                    response.sendRedirect(request.getContextPath() + "/tecnicos");
                    return;
                } else if ("edit".equals(action)) {
                    Tecnico tecnico = obtenerTecnicoPorId(id, request);
                    request.setAttribute("tecnicoEdit", tecnico);
                }
            }
            
            // Obtener lista actualizada de técnicos
            List<Tecnico> tecnicos = obtenerTecnicosDesdeBD(request);
            request.setAttribute("tecnicos", tecnicos);
            
        } catch (SQLException | NumberFormatException e) {
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Redirigir a la vista
        request.getRequestDispatcher("/pages/tecnicos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String especialidad = request.getParameter("especialidad");
        String idParam = request.getParameter("id");
        
        try {
            if (idParam != null && !idParam.isEmpty()) {
                // Actualizar técnico existente
                int id = Integer.parseInt(idParam);
                actualizarTecnico(id, nombre, especialidad, request);
                request.getSession().setAttribute("success", "Técnico actualizado correctamente");
            } else {
                // Crear nuevo técnico
                crearTecnico(nombre, especialidad, request);
                request.getSession().setAttribute("success", "Técnico registrado correctamente");
            }
        } catch (SQLException | NumberFormatException e) {
            request.getSession().setAttribute("error", "Error al guardar técnico: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/tecnicos");
    }
    
    private List<Tecnico> obtenerTecnicosDesdeBD(HttpServletRequest request) throws SQLException {
        List<Tecnico> tecnicos = new ArrayList<>();
        String sql = "SELECT id_tecnico, nombre, especialidad FROM tecnico ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Tecnico tecnico = new Tecnico();
                tecnico.setId(rs.getInt("id_tecnico"));
                tecnico.setNombre(rs.getString("nombre"));
                tecnico.setEspecialidad(rs.getString("especialidad"));
                tecnicos.add(tecnico);
            }
        }
        return tecnicos;
    }
    
    private Tecnico obtenerTecnicoPorId(int id, HttpServletRequest request) throws SQLException {
        String sql = "SELECT id_tecnico, nombre, especialidad FROM tecnico WHERE id_tecnico = ?";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tecnico tecnico = new Tecnico();
                    tecnico.setId(rs.getInt("id_tecnico"));
                    tecnico.setNombre(rs.getString("nombre"));
                    tecnico.setEspecialidad(rs.getString("especialidad"));
                    return tecnico;
                }
            }
        }
        return null;
    }
    
    private void crearTecnico(String nombre, String especialidad, HttpServletRequest request) throws SQLException {
        String sql = "INSERT INTO tecnico (nombre, especialidad) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, especialidad);
            stmt.executeUpdate();
        }
    }
    
    private void actualizarTecnico(int id, String nombre, String especialidad, HttpServletRequest request) throws SQLException {
        String sql = "UPDATE tecnico SET nombre = ?, especialidad = ? WHERE id_tecnico = ?";
        
        try (Connection conn = DatabaseConnection.getConnection(request.getServletContext());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, especialidad);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }
    
    private void eliminarTecnico(int id, HttpServletRequest request) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection(request.getServletContext());
            conn.setAutoCommit(false);
            
            // 1. Eliminar asignaciones relacionadas
            String deleteAsignaciones = "DELETE FROM asignacion WHERE id_tecnico = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteAsignaciones)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            
            // 2. Eliminar el técnico
            String deleteTecnico = "DELETE FROM tecnico WHERE id_tecnico = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTecnico)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows == 0) {
                    conn.rollback();
                    throw new SQLException("No se encontró el técnico con ID: " + id);
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }
}