<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario Usuario - MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .form-card {
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            border: none;
            overflow: hidden;
        }
        .form-header {
            background: linear-gradient(135deg, #dc3545, #c82333);
            color: white;
            padding: 1.5rem;
            text-align: center;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <%
        Usuario usuario = (Usuario) request.getAttribute("usuarioEdit");
        boolean esEdicion = (usuario != null);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card form-card">
                    <div class="form-header">
                        <h4><i class="fas fa-user-cog me-2"></i>
                            <% if (esEdicion) { %>
                                Editar Usuario
                            <% } else { %>
                                Agregar Nuevo Usuario
                            <% } %>
                        </h4>
                        <p class="mb-0">
                            <% if (esEdicion) { %>
                                Modificar datos del usuario existente
                            <% } else { %>
                                Crear un nuevo usuario en el sistema
                            <% } %>
                        </p>
                    </div>
                    <div class="card-body p-4">
                        
                        <!-- Mensajes de error -->
                        <% if (session.getAttribute("error") != null) { %>
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                <% session.removeAttribute("error"); %>
                            </div>
                        <% } %>
                        
                        <form action="${pageContext.request.contextPath}/usuarios" method="post">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id" value="<%= usuario.getId_usuario() %>">
                            <% } %>
                            
                            <div class="mb-4">
                                <label for="nombre" class="form-label">
                                    <i class="fas fa-user me-2"></i>Nombre Completo <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="nombre" name="nombre" 
                                       value="<%= esEdicion && usuario.getNombre() != null ? usuario.getNombre() : "" %>" 
                                       required placeholder="Ingresa el nombre completo">
                            </div>
                            
                            <div class="mb-4">
                                <label for="email" class="form-label">
                                    <i class="fas fa-envelope me-2"></i>Correo Electrónico <span class="text-danger">*</span>
                                </label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="<%= esEdicion && usuario.getEmail() != null ? usuario.getEmail() : "" %>" 
                                       required placeholder="usuario@ejemplo.com">
                                <div class="form-text">
                                    El email debe ser único en el sistema.
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <label for="password" class="form-label">
                                    <i class="fas fa-lock me-2"></i>Contraseña 
                                    <span class="text-danger">*</span>
                                    <% if (esEdicion) { %>
                                        <small class="text-muted">(Dejar vacío para mantener la actual)</small>
                                    <% } %>
                                </label>
                                <input type="password" class="form-control" id="password" name="password" 
                                       <% if (!esEdicion) { %>required<% } %> 
                                       placeholder="<% if (esEdicion) { %>Nueva contraseña (opcional)<% } else { %>Contraseña del usuario<% } %>">
                                <div class="form-text">
                                    <% if (esEdicion) { %>
                                        Solo ingresa una nueva contraseña si deseas cambiarla.
                                    <% } else { %>
                                        Mínimo 5 caracteres recomendado.
                                    <% } %>
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <label for="rol" class="form-label">
                                    <i class="fas fa-user-tag me-2"></i>Rol del Usuario <span class="text-danger">*</span>
                                </label>
                                <select class="form-control" id="rol" name="rol" required>
                                    <option value="">Seleccione un rol</option>
                                    <option value="admin" <% if (esEdicion && "admin".equals(usuario.getRol())) { %>selected<% } %>>
                                        👑 Administrador - Acceso completo
                                    </option>
                                    <option value="tecnico" <% if (esEdicion && "tecnico".equals(usuario.getRol())) { %>selected<% } %>>
                                        🔧 Técnico - Gestión de servicios
                                    </option>
                                    <option value="cliente" <% if (esEdicion && "cliente".equals(usuario.getRol())) { %>selected<% } %>>
                                        👤 Cliente - Solicitar servicios
                                    </option>
                                </select>
                                <div class="form-text">
                                    <strong>Admin:</strong> Puede gestionar todo el sistema | 
                                    <strong>Técnico:</strong> Puede gestionar servicios | 
                                    <strong>Cliente:</strong> Puede solicitar servicios
                                </div>
                            </div>
                            
                            <% if (esEdicion) { %>
                                <div class="mb-4">
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle me-2"></i>
                                        <strong>Usuario creado:</strong> 
                                        <% if (usuario.getFecha_creacion() != null) { %>
                                            <%= sdf.format(usuario.getFecha_creacion()) %>
                                        <% } else { %>
                                            N/A
                                        <% } %>
                                        <br>
                                        <strong>Último login:</strong> 
                                        <% if (usuario.getUltimo_login() != null) { %>
                                            <%= sdf.format(usuario.getUltimo_login()) %>
                                        <% } else { %>
                                            Nunca
                                        <% } %>
                                    </div>
                                </div>
                            <% } %>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>
                                    <% if (esEdicion) { %>
                                        Actualizar
                                    <% } else { %>
                                        Crear
                                    <% } %> Usuario
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación del formulario
        document.querySelector('form').addEventListener('submit', function(e) {
            const nombre = document.getElementById('nombre').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const rol = document.getElementById('rol').value;
            
            if (nombre.length < 3) {
                e.preventDefault();
                alert('El nombre debe tener al menos 3 caracteres');
                return;
            }
            
            if (!email.includes('@')) {
                e.preventDefault();
                alert('Ingresa un email válido');
                return;
            }
            
            <% if (!esEdicion) { %>
                if (password.length < 5) {
                    e.preventDefault();
                    alert('La contraseña debe tener al menos 5 caracteres');
                    return;
                }
            <% } %>
            
            if (!rol) {
                e.preventDefault();
                alert('Debes seleccionar un rol');
                return;
            }
        });
    </script>
</body>
</html>