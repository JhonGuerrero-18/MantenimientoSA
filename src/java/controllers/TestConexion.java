package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/test-conexion")
public class TestConexion extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Test Conexión BD</title></head><body>");
        out.println("<h1>🔧 TEST DE CONEXIÓN A BASE DE DATOS</h1>");
        
        // CREDENCIALES CORREGIDAS
        String url = "jdbc:mysql://localhost:3306/mantenimiento_sa";
        String usuario = "root";
        String password = "";
        
        out.println("<h2>📡 Intentando conectar...</h2>");
        out.println("<p><strong>URL:</strong> " + url + "</p>");
        out.println("<p><strong>Usuario:</strong> " + usuario + "</p>");
        out.println("<p><strong>Password:</strong> " + (password.isEmpty() ? "VACÍO" : "CONFIGURADO") + "</p>");
        
        try {
            // 1. Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            out.println("<p>✅ <strong>Driver MySQL cargado</strong></p>");
            
            // 2. Conectar
            Connection conn = DriverManager.getConnection(url, usuario, password);
            out.println("<p>✅ <strong>CONEXIÓN EXITOSA!</strong></p>");
            
            // 3. Probar query
            String sql = "SELECT COUNT(*) as total FROM servicio";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                out.println("<p>✅ <strong>SERVICIOS EN BD: " + total + "</strong></p>");
            }
            
            // 4. Mostrar algunos servicios
            sql = "SELECT id_servicio, descripcion, estado FROM servicio LIMIT 10";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            out.println("<h3>📄 Servicios encontrados:</h3>");
            out.println("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            out.println("<tr style='background-color: #f0f0f0;'><th>ID</th><th>Descripción</th><th>Estado</th></tr>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td style='padding: 8px;'>" + rs.getInt("id_servicio") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("descripcion") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("estado") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            
            // Cerrar conexión
            rs.close();
            stmt.close();
            conn.close();
            out.println("<p>✅ <strong>CONEXIÓN CERRADA CORRECTAMENTE</strong></p>");
            
        } catch (ClassNotFoundException e) {
            out.println("<p>❌ <strong>ERROR DRIVER:</strong> " + e.getMessage() + "</p>");
            
        } catch (SQLException e) {
            out.println("<p>❌ <strong>ERROR SQL:</strong> " + e.getMessage() + "</p>");
            out.println("<p>❌ <strong>SQLState:</strong> " + e.getSQLState() + "</p>");
            out.println("<p>❌ <strong>Error Code:</strong> " + e.getErrorCode() + "</p>");
            
        } catch (Exception e) {
            out.println("<p>❌ <strong>ERROR GENERAL:</strong> " + e.getMessage() + "</p>");
        }
        
        out.println("<hr>");
        out.println("<p><a href='" + request.getContextPath() + "/servicios'>← Ir a Servicios JSP</a></p>");
        out.println("<p><a href='" + request.getContextPath() + "/api/servicios'>← Probar API REST</a></p>");
        out.println("</body></html>");
    }
}