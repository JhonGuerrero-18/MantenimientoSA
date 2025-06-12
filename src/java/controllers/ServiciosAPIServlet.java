package controllers;

import dao.ServicioDAO;
import models.Servicio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class ServiciosAPIServlet extends HttpServlet {
    
    private ServicioDAO servicioDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        System.out.println("=====================================");
        System.out.println("🚀 INICIALIZANDO ServiciosAPIServlet");
        System.out.println("=====================================");
        
        try {
            servicioDAO = new ServicioDAO();
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            System.out.println("✅ SERVLET: ServicioDAO y Gson inicializados");
            
            // Probar conexión
            List<Servicio> pruebaServicios = servicioDAO.obtenerTodos();
            System.out.println("🔍 SERVLET: Prueba BD - Servicios encontrados: " + 
                (pruebaServicios != null ? pruebaServicios.size() : "NULL"));
                
        } catch (Exception e) {
            System.out.println("❌ SERVLET: Error al inicializar: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=====================================");
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // CORS preflight
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("📥 GET REQUEST RECIBIDO");
        System.out.println("   URI: " + request.getRequestURI());
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // OBTENER TODOS
                System.out.println("🔍 Obteniendo TODOS los servicios...");
                
                List<Servicio> servicios = servicioDAO.obtenerTodos();
                System.out.println("🔍 DAO retornó: " + (servicios != null ? servicios.size() + " servicios" : "NULL"));
                
                String jsonResponse = "{\"success\":true,\"data\":" + gson.toJson(servicios) + "}";
                response.getWriter().write(jsonResponse);
                
            } else {
                // OBTENER POR ID
                String[] parts = pathInfo.split("/");
                if (parts.length >= 2) {
                    try {
                        int id = Integer.parseInt(parts[1]);
                        Servicio servicio = servicioDAO.obtenerPorId(id);
                        
                        if (servicio != null) {
                            String jsonResponse = "{\"success\":true,\"data\":" + gson.toJson(servicio) + "}";
                            response.getWriter().write(jsonResponse);
                        } else {
                            response.getWriter().write("{\"success\":false,\"message\":\"Servicio no encontrado\"}");
                        }
                    } catch (NumberFormatException e) {
                        response.getWriter().write("{\"success\":false,\"message\":\"ID inválido\"}");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR EN doGet: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Error del servidor\"}");
        }
        
        System.out.println("✅ GET REQUEST COMPLETADO");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("📥 POST REQUEST RECIBIDO");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            // Leer JSON del request
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
            
            String jsonString = jsonBuffer.toString();
            System.out.println("📝 JSON recibido: " + jsonString);
            
            if (jsonString.isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Body JSON requerido\"}");
                return;
            }
            
            // Convertir JSON a objeto Servicio
            Servicio nuevoServicio = gson.fromJson(jsonString, Servicio.class);
            
            System.out.println("🔄 Convirtiendo a objeto Servicio:");
            System.out.println("   Descripción: " + nuevoServicio.getDescripcion());
            System.out.println("   Fecha: " + nuevoServicio.getFecha());
            System.out.println("   Usuario: " + nuevoServicio.getId_usuario());
            System.out.println("   Estado: " + nuevoServicio.getEstado());
            
            // Guardar en BD
            boolean exito = servicioDAO.insertar(nuevoServicio);
            
            if (exito) {
                System.out.println("✅ Servicio creado exitosamente");
                response.getWriter().write("{\"success\":true,\"message\":\"Servicio creado exitosamente\"}");
            } else {
                System.out.println("❌ Error al crear servicio");
                response.getWriter().write("{\"success\":false,\"message\":\"Error al crear servicio\"}");
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR EN doPost: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Error del servidor: " + e.getMessage() + "\"}");
        }
        
        System.out.println("✅ POST REQUEST COMPLETADO");
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("📝 PUT REQUEST RECIBIDO");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            String pathInfo = request.getPathInfo();
            System.out.println("PUT pathInfo: " + pathInfo);
            
            if (pathInfo != null && pathInfo.length() > 1) {
                String[] parts = pathInfo.split("/");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[1]);
                    System.out.println("Actualizando servicio ID: " + id);
                    
                    // Leer JSON del request
                    BufferedReader reader = request.getReader();
                    StringBuilder jsonBuffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuffer.append(line);
                    }
                    
                    String jsonString = jsonBuffer.toString();
                    System.out.println("JSON recibido: " + jsonString);
                    
                    // Convertir JSON a objeto
                    Servicio servicioActualizado = gson.fromJson(jsonString, Servicio.class);
                    servicioActualizado.setD_servicio(id); // Asegurar que tenga el ID correcto
                    
                    // Actualizar en BD
                    boolean exito = servicioDAO.actualizar(servicioActualizado);
                    
                    if (exito) {
                        System.out.println("✅ Servicio actualizado exitosamente");
                        response.getWriter().write("{\"success\":true,\"message\":\"Servicio actualizado exitosamente\"}");
                    } else {
                        System.out.println("❌ Error al actualizar servicio");
                        response.getWriter().write("{\"success\":false,\"message\":\"Error al actualizar servicio\"}");
                    }
                } else {
                    response.getWriter().write("{\"success\":false,\"message\":\"ID de servicio requerido\"}");
                }
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"ID de servicio requerido en la URL\"}");
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR EN doPut: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Error del servidor: " + e.getMessage() + "\"}");
        }
        
        System.out.println("✅ PUT REQUEST COMPLETADO");
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("🗑️ DELETE REQUEST RECIBIDO");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            String pathInfo = request.getPathInfo();
            System.out.println("DELETE pathInfo: " + pathInfo);
            
            if (pathInfo != null && pathInfo.length() > 1) {
                String[] parts = pathInfo.split("/");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[1]);
                    System.out.println("Eliminando servicio ID: " + id);
                    
                    // Verificar que el servicio existe
                    Servicio servicio = servicioDAO.obtenerPorId(id);
                    if (servicio == null) {
                        response.getWriter().write("{\"success\":false,\"message\":\"Servicio no encontrado\"}");
                        return;
                    }
                    
                    // Eliminar de BD
                    boolean exito = servicioDAO.eliminar(id);
                    
                    if (exito) {
                        System.out.println("✅ Servicio eliminado exitosamente");
                        response.getWriter().write("{\"success\":true,\"message\":\"Servicio eliminado exitosamente\"}");
                    } else {
                        System.out.println("❌ Error al eliminar servicio");
                        response.getWriter().write("{\"success\":false,\"message\":\"Error al eliminar servicio\"}");
                    }
                } else {
                    response.getWriter().write("{\"success\":false,\"message\":\"ID de servicio requerido\"}");
                }
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"ID de servicio requerido en la URL\"}");
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR EN doDelete: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Error del servidor: " + e.getMessage() + "\"}");
        }
        
        System.out.println("✅ DELETE REQUEST COMPLETADO");
    }
}