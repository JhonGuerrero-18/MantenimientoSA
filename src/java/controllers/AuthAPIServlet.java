package controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthAPIServlet extends HttpServlet {
    
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
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        String pathInfo = request.getPathInfo();
        System.out.println("📡 Auth GET pathInfo: '" + pathInfo + "'");
        System.out.println("📡 Auth GET requestURI: '" + request.getRequestURI() + "'");
        
        if (pathInfo == null || "/usuarios".equals(pathInfo)) {
            // Respuesta simulada con tus usuarios reales
            String jsonResponse = "{" +
                "\"success\": true," +
                "\"message\": \"Usuarios obtenidos\"," +
                "\"data\": [" +
                    "{\"id_usuario\": 1, \"nombre\": \"Jhon Guerrero\", \"email\": \"jhonhairis@hotmail.com\"}," +
                    "{\"id_usuario\": 2, \"nombre\": \"Ana Gómez\", \"email\": \"ana@example.com\"}," +
                    "{\"id_usuario\": 3, \"nombre\": \"juan\", \"email\": \"jomehaz@hotmail.com\"}" +
                "]" +
            "}";
            response.getWriter().write(jsonResponse);
        } else {
            String errorResponse = "{\"success\":false,\"message\":\"Endpoint GET no encontrado: " + pathInfo + "\"}";
            response.getWriter().write(errorResponse);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        String pathInfo = request.getPathInfo();
        System.out.println("📥 Auth POST pathInfo: '" + pathInfo + "'");
        System.out.println("📥 Auth POST requestURI: '" + request.getRequestURI() + "'");
        
        try {
            // Leer JSON del request
            java.io.InputStream inputStream = request.getInputStream();
            java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8");
            scanner.useDelimiter("\\A");
            
            String jsonString = "";
            if (scanner.hasNext()) {
                jsonString = scanner.next();
            }
            scanner.close();
            
            System.out.println("📝 JSON recibido: " + jsonString);
            
            if ("/login".equals(pathInfo)) {
                // Simular login exitoso (más tarde conectaremos a BD)
                String loginResponse = "{" +
                    "\"success\": true," +
                    "\"message\": \"Login exitoso\"," +
                    "\"data\": {" +
                        "\"id\": 1," +
                        "\"nombre\": \"Jhon Guerrero\"," +
                        "\"email\": \"jhonhairis@hotmail.com\"" +
                    "}" +
                "}";
                response.getWriter().write(loginResponse);
                System.out.println("✅ Login simulado exitoso para pathInfo: " + pathInfo);
                
            } else if ("/register".equals(pathInfo)) {
                // Simular registro exitoso
                String registerResponse = "{" +
                    "\"success\": true," +
                    "\"message\": \"Usuario registrado exitosamente\"" +
                "}";
                response.getWriter().write(registerResponse);
                System.out.println("✅ Registro simulado exitoso para pathInfo: " + pathInfo);
                
            } else {
                String errorResponse = "{\"success\":false,\"message\":\"Endpoint POST no encontrado: " + pathInfo + "\"}";
                response.getWriter().write(errorResponse);
                System.out.println("❌ Endpoint POST no encontrado: " + pathInfo);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error en Auth POST: " + e.getMessage());
            e.printStackTrace();
            String errorResponse = "{\"success\":false,\"message\":\"Error interno: " + e.getMessage() + "\"}";
            response.getWriter().write(errorResponse);
        }
    }
}