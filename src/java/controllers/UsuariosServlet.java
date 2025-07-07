package controllers;

import dao.UsuarioDAO;
import models.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class UsuariosServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            usuarioDAO = new UsuarioDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        try {
            String action = request.getParameter("action");
            
            if ("new".equals(action)) {
                request.getRequestDispatcher("/pages/form_usuario.jsp").forward(request, response);
                return;
            } else if ("edit".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    Usuario usuario = usuarioDAO.obtenerPorId(id);
                    if (usuario != null) {
                        request.setAttribute("usuarioEdit", usuario);
                    }
                }
                request.getRequestDispatcher("/pages/form_usuario.jsp").forward(request, response);
                return;
            } else if ("delete".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    boolean eliminado = usuarioDAO.eliminar(id);
                    if (eliminado) {
                        request.getSession().setAttribute("success", "Usuario eliminado correctamente");
                    } else {
                        request.getSession().setAttribute("error", "Error al eliminar usuario");
                    }
                }
                response.sendRedirect(request.getContextPath() + "/usuarios");
                return;
            }
            
            List<Usuario> usuarios = usuarioDAO.listar();
            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher("/pages/usuarios.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/pages/usuarios.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            String action = request.getParameter("action");
            
            if ("update".equals(action)) {
                actualizarUsuario(request);
                request.getSession().setAttribute("success", "Usuario actualizado correctamente");
            } else {
                crearUsuario(request);
                request.getSession().setAttribute("success", "Usuario creado correctamente");
            }
            
            response.sendRedirect(request.getContextPath() + "/usuarios");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/usuarios");
        }
    }
    
    private void crearUsuario(HttpServletRequest request) throws Exception {
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria");
        }
        if (rol == null || rol.trim().isEmpty()) {
            throw new Exception("El rol es obligatorio");
        }
        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre.trim());
        nuevoUsuario.setEmail(email.trim());
        nuevoUsuario.setContraseña(password.trim());
        nuevoUsuario.setRol(rol.trim());
        
        boolean exito = usuarioDAO.registrar(nuevoUsuario);
        
        if (!exito) {
            throw new Exception("No se pudo crear el usuario en la base de datos");
        }
    }
    
    private void actualizarUsuario(HttpServletRequest request) throws Exception {
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            throw new Exception("ID de usuario requerido");
        }
        
        int id = Integer.parseInt(idStr.trim());
        
        Usuario usuario = new Usuario();
        usuario.setId_usuario(id);
        usuario.setNombre(nombre.trim());
        usuario.setEmail(email.trim());
        usuario.setRol(rol.trim());
        
        if (password != null && !password.trim().isEmpty()) {
            usuario.setContraseña(password.trim());
        }
        
        boolean exito = usuarioDAO.actualizar(usuario);
        
        if (!exito) {
            throw new Exception("No se pudo actualizar el usuario");
        }
    }
}