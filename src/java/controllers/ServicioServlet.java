package controllers;

import dao.ServicioDAO;
import models.Servicio;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ServicioServlet extends HttpServlet {
    
    private ServicioDAO servicioDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            servicioDAO = new ServicioDAO();
            System.out.println("✅ ServicioServlet inicializado correctamente");
        } catch (Exception e) {
            System.out.println("❌ Error inicializando ServicioServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // VALIDACIÓN DE ROL AGREGADA
        String userRole = (String) request.getSession().getAttribute("userRole");
        if (!("admin".equals(userRole) || "tecnico".equals(userRole))) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }
        
        try {
            String action = request.getParameter("action");
            System.out.println("🔍 ServicioServlet GET - Action: " + action);
            
            if ("new".equals(action)) {
                request.getRequestDispatcher("/pages/form_servicio.jsp").forward(request, response);
                return;
            } else if ("edit".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    Servicio servicio = servicioDAO.obtenerPorId(id);
                    request.setAttribute("servicio", servicio);
                }
                request.getRequestDispatcher("/pages/form_servicio.jsp").forward(request, response);
                return;
            } else if ("delete".equals(action)) {
                // Solo admin puede eliminar
                if (!"admin".equals(userRole)) {
                    response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
                    return;
                }
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    boolean eliminado = servicioDAO.eliminar(id);
                    System.out.println("🗑️ Servicio eliminado: " + eliminado);
                }
                response.sendRedirect(request.getContextPath() + "/servicios");
                return;
            }
            
            // Obtener todos los servicios
            List<Servicio> servicios = servicioDAO.obtenerTodos();
            System.out.println("📋 Servicios encontrados: " + (servicios != null ? servicios.size() : "null"));
            request.setAttribute("servicios", servicios);
            request.getRequestDispatcher("/pages/servicios.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("❌ Error en ServicioServlet GET: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/pages/servicios.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("📥 ServicioServlet POST REQUEST RECIBIDO");
        
        // VALIDACIÓN DE ROL AGREGADA
        String userRole = (String) request.getSession().getAttribute("userRole");
        if (!("admin".equals(userRole) || "tecnico".equals(userRole))) {
            response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
            return;
        }
        
        // CONFIGURAR ENCODING PARA CARACTERES ESPECIALES
        request.setCharacterEncoding("UTF-8");
        
        try {
            String action = request.getParameter("action");
            System.out.println("🔍 Action: " + action);
            
            if ("update".equals(action)) {
                // Actualizar servicio existente
                actualizarServicio(request);
            } else {
                // Crear nuevo servicio
                crearServicio(request);
            }
            
            // Redirigir SIEMPRE a la lista
            response.sendRedirect(request.getContextPath() + "/servicios");
            
        } catch (Exception e) {
            System.out.println("❌ Error en ServicioServlet POST: " + e.getMessage());
            e.printStackTrace();
            
            // En caso de error, también redirigir
            response.sendRedirect(request.getContextPath() + "/servicios?error=" + e.getMessage());
        }
    }
    
    private void crearServicio(HttpServletRequest request) throws Exception {
        
        // OBTENER PARÁMETROS
        String descripcion = request.getParameter("descripcion");
        String idUsuarioStr = request.getParameter("id_usuario");
        String fechaStr = request.getParameter("fecha");
        String estado = request.getParameter("estado");
        
        System.out.println("=== CREANDO NUEVO SERVICIO ===");
        System.out.println("📝 Descripción: '" + descripcion + "'");
        System.out.println("👤 ID Usuario: '" + idUsuarioStr + "'");
        System.out.println("📅 Fecha: '" + fechaStr + "'");
        System.out.println("📊 Estado: '" + estado + "'");
        
        // VALIDACIONES BÁSICAS
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripción es obligatoria");
        }
        
        if (idUsuarioStr == null || idUsuarioStr.trim().isEmpty()) {
            throw new Exception("El ID de usuario es obligatorio");
        }
        
        if (estado == null || estado.trim().isEmpty()) {
            throw new Exception("El estado es obligatorio");
        }
        
        // CREAR OBJETO SERVICIO
        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setDescripcion(descripcion.trim());
        nuevoServicio.setId_usuario(Integer.parseInt(idUsuarioStr.trim()));
        nuevoServicio.setEstado(estado.trim());
        
        // MANEJAR FECHA
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                java.sql.Date fecha = java.sql.Date.valueOf(fechaStr.trim());
                nuevoServicio.setFecha(fecha);
                System.out.println("📅 Fecha parseada: " + fecha);
            } catch (Exception e) {
                System.out.println("⚠️ Error en fecha proporcionada, usando fecha actual: " + e.getMessage());
                nuevoServicio.setFecha(new java.sql.Date(System.currentTimeMillis()));
            }
        } else {
            // Si no hay fecha, usar fecha actual
            java.sql.Date fechaActual = new java.sql.Date(System.currentTimeMillis());
            nuevoServicio.setFecha(fechaActual);
            System.out.println("📅 Usando fecha actual: " + fechaActual);
        }
        
        System.out.println("🔄 Objeto Servicio creado: " + nuevoServicio.toString());
        
        // INSERTAR EN BASE DE DATOS
        boolean exito = servicioDAO.insertar(nuevoServicio);
        
        if (exito) {
            System.out.println("✅ SERVICIO CREADO EXITOSAMENTE EN BD");
        } else {
            System.out.println("❌ ERROR: NO SE PUDO CREAR EL SERVICIO");
            throw new Exception("No se pudo guardar el servicio en la base de datos");
        }
    }
    
    private void actualizarServicio(HttpServletRequest request) throws Exception {
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        
        System.out.println("🔄 Actualizando servicio ID: " + id);
        
        Servicio servicio = servicioDAO.obtenerPorId(id);
        if (servicio != null) {
            servicio.setDescripcion(request.getParameter("descripcion"));
            servicio.setId_usuario(Integer.parseInt(request.getParameter("id_usuario")));
            servicio.setEstado(request.getParameter("estado"));
            
            // Manejar fecha si se proporciona
            String fechaStr = request.getParameter("fecha");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                java.sql.Date fecha = java.sql.Date.valueOf(fechaStr);
                servicio.setFecha(fecha);
            }
            
            boolean actualizado = servicioDAO.actualizar(servicio);
            System.out.println("✅ Servicio actualizado: " + actualizado);
        }
    }
}