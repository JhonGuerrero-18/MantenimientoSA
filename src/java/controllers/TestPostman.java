package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

//@WebServlet("/test-postman")
public class TestPostman extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Test Postman</title></head><body>");
        out.println("<h1>🔧 TEST POSTMAN DEBUG</h1>");
        
        // Debug completo
        out.println("<h2>📡 Información de la Petición:</h2>");
        out.println("<p><strong>Method:</strong> " + request.getMethod() + "</p>");
        out.println("<p><strong>Content-Type:</strong> " + request.getContentType() + "</p>");
        out.println("<p><strong>Content-Length:</strong> " + request.getContentLength() + "</p>");
        out.println("<p><strong>Character Encoding:</strong> " + request.getCharacterEncoding() + "</p>");
        
        // Headers
        out.println("<h3>📋 Headers:</h3>");
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            out.println("<p><strong>" + headerName + ":</strong> " + headerValue + "</p>");
        }
        
        // Leer cuerpo
        out.println("<h3>📄 Cuerpo de la Petición:</h3>");
        
        try {
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            
            request.setCharacterEncoding("UTF-8");
            
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }
            
            String jsonString = jsonBuffer.toString();
            
            out.println("<p><strong>Contenido RAW:</strong></p>");
            out.println("<pre style='background: #f0f0f0; padding: 10px; border: 1px solid #ccc;'>");
            out.println("'" + jsonString + "'");
            out.println("</pre>");
            
            out.println("<p><strong>Length:</strong> " + jsonString.length() + "</p>");
            out.println("<p><strong>Is Empty:</strong> " + jsonString.isEmpty() + "</p>");
            
            if (!jsonString.isEmpty()) {
                out.println("<p style='color: green;'><strong>✅ JSON RECIBIDO CORRECTAMENTE</strong></p>");
                
                // Intentar parsear
                try {
                    com.google.gson.JsonObject jsonObj = com.google.gson.JsonParser.parseString(jsonString).getAsJsonObject();
                    out.println("<p style='color: green;'><strong>✅ JSON PARSEADO CORRECTAMENTE</strong></p>");
                    out.println("<p><strong>Campos:</strong> " + jsonObj.keySet() + "</p>");
                } catch (Exception e) {
                    out.println("<p style='color: red;'><strong>❌ ERROR AL PARSEAR:</strong> " + e.getMessage() + "</p>");
                }
            } else {
                out.println("<p style='color: red;'><strong>❌ JSON VACÍO</strong></p>");
                out.println("<h3>🔧 Pasos para solucionar:</h3>");
                out.println("<ol>");
                out.println("<li>En Postman, asegúrate de seleccionar <strong>POST</strong></li>");
                out.println("<li>Ve a la pestaña <strong>Body</strong></li>");
                out.println("<li>Selecciona <strong>raw</strong></li>");
                out.println("<li>En el dropdown, selecciona <strong>JSON</strong> (no Text)</li>");
                out.println("<li>Pega el JSON en el área de texto</li>");
                out.println("<li>Verifica que el header Content-Type sea application/json</li>");
                out.println("</ol>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color: red;'><strong>❌ ERROR:</strong> " + e.getMessage() + "</p>");
        }
        
        out.println("<hr>");
        out.println("<p><a href='javascript:history.back()'>← Volver</a></p>");
        out.println("</body></html>");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Test Postman</title></head><body>");
        out.println("<h1>🔧 Test Postman - Instrucciones</h1>");
        out.println("<h2>📮 Configuración en Postman:</h2>");
        out.println("<ol>");
        out.println("<li><strong>Método:</strong> POST</li>");
        out.println("<li><strong>URL:</strong> http://localhost:8080/MantenimientoSA/test-postman</li>");
        out.println("<li><strong>Headers:</strong> Content-Type: application/json</li>");
        out.println("<li><strong>Body:</strong> raw + JSON</li>");
        out.println("<li><strong>JSON:</strong></li>");
        out.println("</ol>");
        
        out.println("<pre style='background: #f0f0f0; padding: 10px; border: 1px solid #ccc;'>");
        out.println("{");
        out.println("    \"descripcion\": \"Test desde Postman\",");
        out.println("    \"fecha\": \"2025-05-28\",");
        out.println("    \"id_usuario\": 1,");
        out.println("    \"estado\": \"Pendiente\"");
        out.println("}");
        out.println("</pre>");
        
        out.println("<p><strong>Después de configurar, envía la petición POST y verás el debug aquí.</strong></p>");
        out.println("</body></html>");
    }
}