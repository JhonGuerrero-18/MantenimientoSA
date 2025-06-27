package controllers;

import dao.ServicioDAO;
import models.Servicio;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/mis-servicios")
public class MisServiciosServlet extends HttpServlet {
    
    private ServicioDAO servicioDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            servicioDAO = new ServicioDAO();
            System.out.println("✅ MisServiciosServlet inicializado correctamente");
        } catch (Exception e) {
            System.out.println("❌ Error inicializando MisServiciosServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Verificar que sea cliente y esté logueado
        if (!"cliente".equals(userRole) || userId == null) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }
        
        try {
            String action = request.getParameter("action");
            System.out.println("🔍 MisServiciosServlet GET - Action: " + action + " | UserID: " + userId);
            
            if ("new".equals(action)) {
                // Mostrar formulario para crear servicio
                request.getRequestDispatcher("/pages/form_mi_servicio.jsp").forward(request, response);
                return;
            }
            
            // Obtener servicios del usuario logueado
            List<Servicio> misServicios = servicioDAO.obtenerPorUsuario(userId);
            System.out.println("📋 Servicios del usuario " + userId + ": " + (misServicios != null ? misServicios.size() : "null"));
            
            request.setAttribute("misServicios", misServicios);
            request.getRequestDispatcher("/pages/mis-servicios.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("❌ Error en MisServiciosServlet GET: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/pages/mis-servicios.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Verificar que sea cliente y esté logueado
        if (!"cliente".equals(userRole) || userId == null) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }
        
        System.out.println("📥 MisServiciosServlet POST REQUEST RECIBIDO para usuario: " + userId);
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // Crear nuevo servicio
            crearMiServicio(request, userId);
            
            // Redirigir a mis servicios
            response.sendRedirect(request.getContextPath() + "/mis-servicios");
            
        } catch (Exception e) {
            System.out.println("❌ Error en MisServiciosServlet POST: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mis-servicios?error=" + e.getMessage());
        }
    }
    
    private void crearMiServicio(HttpServletRequest request, int userId) throws Exception {
        
        String descripcion = request.getParameter("descripcion");
        String fechaStr = request.getParameter("fecha");
        
        System.out.println("=== CREANDO SERVICIO PARA CLIENTE ===");
        System.out.println("📝 Descripción: '" + descripcion + "'");
        System.out.println("👤 Usuario ID: " + userId);
        System.out.println("📅 Fecha: '" + fechaStr + "'");
        
        // Validaciones
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripción es obligatoria");
        }
        
        // Crear objeto servicio
        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setDescripcion(descripcion.trim());
        nuevoServicio.setId_usuario(userId); // Usar el ID del usuario logueado
        nuevoServicio.setEstado("Pendiente"); // Estado por defecto para clientes
        
        // Manejar fecha
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                java.sql.Date fecha = java.sql.Date.valueOf(fechaStr.trim());
                nuevoServicio.setFecha(fecha);
                System.out.println("📅 Fecha parseada: " + fecha);
            } catch (Exception e) {
                System.out.println("⚠️ Error en fecha, usando fecha actual: " + e.getMessage());
                nuevoServicio.setFecha(new java.sql.Date(System.currentTimeMillis()));
            }
        } else {
            nuevoServicio.setFecha(new java.sql.Date(System.currentTimeMillis()));
            System.out.println("📅 Usando fecha actual");
        }
        
        System.out.println("🔄 Objeto Servicio creado: " + nuevoServicio.toString());
        
        // Insertar en base de datos
        boolean exito = servicioDAO.insertar(nuevoServicio);
        
        if (exito) {
            System.out.println("✅ SERVICIO CREADO EXITOSAMENTE PARA CLIENTE");
        } else {
            System.out.println("❌ ERROR: NO SE PUDO CREAR EL SERVICIO");
            throw new Exception("No se pudo guardar el servicio en la base de datos");
        }
    }
}