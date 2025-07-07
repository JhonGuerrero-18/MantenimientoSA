package controllers;

import dao.AsignacionDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AsignacionServlet extends HttpServlet {
    
    private AsignacionDAO asignacionDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            System.out.println("🔄 Inicializando AsignacionServlet...");
            asignacionDAO = new AsignacionDAO();
            System.out.println("✅ AsignacionServlet inicializado correctamente");
        } catch (Exception e) {
            System.out.println("❌ Error inicializando AsignacionServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }
        
        try {
            if (asignacionDAO == null) {
                asignacionDAO = new AsignacionDAO();
            }
            
            // DEBUG: Mostrar qué datos tenemos en la base de datos
            System.out.println("🔍 === DEBUG ASIGNACIONES ===");
            asignacionDAO.debugDatos();
            
            String action = request.getParameter("action");
            System.out.println("🔍 AsignacionServlet GET - Action: " + action);
            
            if ("delete".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    boolean eliminado = asignacionDAO.eliminarAsignacion(id);
                    session.setAttribute(eliminado ? "success" : "error", 
                        eliminado ? "Asignación eliminada correctamente" : "Error al eliminar asignación");
                }
                response.sendRedirect(request.getContextPath() + "/asignaciones");
                return;
            }
            
            // Obtener TODOS los datos
            List<Map<String, Object>> asignaciones = asignacionDAO.listarAsignacionesCompletas();
            List<Map<String, Object>> todosLosServicios = asignacionDAO.listarTodosLosServicios();
            List<Map<String, Object>> serviciosSinAsignar = asignacionDAO.listarServiciosSinAsignar();
            List<Map<String, Object>> todosLosTecnicos = asignacionDAO.listarTodosLosTecnicos();
            
            System.out.println("📋 === RESULTADOS FINALES ===");
            System.out.println("   - Asignaciones actuales: " + (asignaciones != null ? asignaciones.size() : "null"));
            System.out.println("   - Todos los servicios: " + (todosLosServicios != null ? todosLosServicios.size() : "null"));
            System.out.println("   - Servicios sin asignar: " + (serviciosSinAsignar != null ? serviciosSinAsignar.size() : "null"));
            System.out.println("   - Todos los técnicos: " + (todosLosTecnicos != null ? todosLosTecnicos.size() : "null"));
            
            // Mostrar detalles si hay datos
            if (todosLosServicios != null && !todosLosServicios.isEmpty()) {
                System.out.println("🎯 SERVICIOS QUE SE MOSTRARÁN EN DROPDOWN:");
                for (Map<String, Object> servicio : todosLosServicios) {
                    System.out.println("   - [" + servicio.get("id_servicio") + "] " + 
                            servicio.get("descripcion") + " (" + servicio.get("cliente") + ")");
                }
            }
            
            if (todosLosTecnicos != null && !todosLosTecnicos.isEmpty()) {
                System.out.println("🎯 TÉCNICOS QUE SE MOSTRARÁN EN DROPDOWN:");
                for (Map<String, Object> tecnico : todosLosTecnicos) {
                    System.out.println("   - [" + tecnico.get("id") + "] " + tecnico.get("nombre_completo"));
                }
            }
            
            // Pasar datos al JSP
            request.setAttribute("asignaciones", asignaciones);
            request.setAttribute("todosLosServicios", todosLosServicios);
            request.setAttribute("serviciosSinAsignar", serviciosSinAsignar);
            request.setAttribute("todosLosTecnicos", todosLosTecnicos);
            
            request.getRequestDispatcher("/pages/asignaciones.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("❌ Error en AsignacionServlet GET: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/pages/asignaciones.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            String action = request.getParameter("action");
            System.out.println("📥 AsignacionServlet POST - Action: " + action);
            
            if ("asignar".equals(action)) {
                String tecnicoIdStr = request.getParameter("tecnico_id");
                String servicioIdStr = request.getParameter("servicio_id");
                
                System.out.println("📝 Datos recibidos:");
                System.out.println("   - Técnico ID: " + tecnicoIdStr);
                System.out.println("   - Servicio ID: " + servicioIdStr);
                
                if (tecnicoIdStr != null && servicioIdStr != null && 
                    !tecnicoIdStr.isEmpty() && !servicioIdStr.isEmpty()) {
                    
                    int tecnicoId = Integer.parseInt(tecnicoIdStr);
                    int servicioId = Integer.parseInt(servicioIdStr);
                    
                    boolean exito = asignacionDAO.asignar(tecnicoId, servicioId);
                    
                    if (exito) {
                        session.setAttribute("success", "¡Servicio asignado correctamente al técnico!");
                        System.out.println("✅ Asignación exitosa: Servicio " + servicioId + " → Técnico " + tecnicoId);
                    } else {
                        session.setAttribute("error", "Error al crear la asignación");
                        System.out.println("❌ Falló la asignación");
                    }
                } else {
                    session.setAttribute("error", "Por favor seleccione un técnico y un servicio");
                    System.out.println("❌ Datos incompletos - Técnico: " + tecnicoIdStr + ", Servicio: " + servicioIdStr);
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/asignaciones");
            
        } catch (Exception e) {
            System.out.println("❌ Error en AsignacionServlet POST: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/asignaciones");
        }
    }
}