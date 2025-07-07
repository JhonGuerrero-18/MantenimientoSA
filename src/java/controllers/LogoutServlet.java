package controllers;  // Paquete obligatorio (debe estar en controllers/)

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebServlet("/logout")  // Anotación que lo convierte en Servlet
public class LogoutServlet extends HttpServlet {  // Extiende de HttpServlet
    
    // Método para manejar solicitudes GET (ej: al hacer clic en "Cerrar Sesión")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtener la sesión actual (si existe)
        HttpSession session = request.getSession(false);
        
        // 2. Invalidar la sesión (elimina todos los datos)
        if (session != null) {
            session.invalidate();  // Destruye la sesión
        }
        
        // 3. Redirigir al login
        response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
    }
}