package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import models.Servicio;

@WebServlet("/servicios")
public class ServicioServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Simulación de datos - reemplazar con conexión a BD real
        List<Servicio> servicios = new ArrayList<>();
        
        Servicio s1 = new Servicio();
        s1.setId(1);
        s1.setDescripcion("Mantenimiento preventivo");
        s1.setEstado("Pendiente");
        
        Servicio s2 = new Servicio();
        s2.setId(2);
        s2.setDescripcion("Reparación de equipo");
        s2.setEstado("En proceso");
        
        servicios.add(s1);
        servicios.add(s2);
        
        request.setAttribute("servicios", servicios);
        request.getRequestDispatcher("/pages/servicios.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica para guardar nuevo servicio
        response.sendRedirect(request.getContextPath() + "/servicios?success=1");
    }
}