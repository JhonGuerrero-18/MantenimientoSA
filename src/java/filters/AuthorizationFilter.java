package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        String userRole = null;
        
        if (session != null) {
            userRole = (String) session.getAttribute("userRole");
        }
        
        // Rutas que requieren admin
        if (uri.contains("/tecnicos") && !"admin".equals(userRole)) {
            if (session != null && session.getAttribute("user") != null) {
                response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
                return;
            }
        }
        
        // Rutas que requieren admin o tecnico
        if (uri.contains("/servicios") && !("admin".equals(userRole) || "tecnico".equals(userRole))) {
            if (session != null && session.getAttribute("user") != null) {
                response.sendRedirect(request.getContextPath() + "/acceso-denegado.jsp");
                return;
            }
        }
        
        chain.doFilter(req, res);
    }
    
    @Override
    public void destroy() {
        // Limpieza del filtro
    }
}