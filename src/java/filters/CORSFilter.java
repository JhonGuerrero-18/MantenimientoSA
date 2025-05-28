package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro CORS para permitir conexiones desde React
 * Necesario para la comunicación frontend-backend
 * 
 * @author Tu Nombre
 * @version 1.0
 * @since 2024
 */
@WebFilter("/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔧 CORSFilter inicializado correctamente");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        // Log de requests para debug
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String origin = request.getHeader("Origin");
        
        System.out.println("🌐 CORS Request: " + method + " " + uri + " from " + origin);
        
        // Configurar headers CORS
        if (origin != null && (
            origin.contains("localhost:3000") || 
            origin.contains("127.0.0.1:3000") ||
            origin.contains("localhost:5173") || 
            origin.contains("127.0.0.1:5173")
        )) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            System.out.println("✅ CORS: Origen permitido - " + origin);
        }
        
        // Headers CORS necesarios
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", 
            "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // Manejar requests OPTIONS (preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            System.out.println("✅ CORS: Request OPTIONS manejado");
            return;
        }
        
        // Continuar con la cadena de filtros
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        System.out.println("🔧 CORSFilter destruido");
    }
}