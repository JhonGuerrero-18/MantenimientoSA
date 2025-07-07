<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios - MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .usuarios-card {
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            border: none;
            overflow: hidden;
        }
        .usuarios-header {
            background: linear-gradient(135deg, #dc3545, #c82333);
            color: white;
            padding: 1.5rem;
            text-align: center;
        }
        .usuarios-body {
            padding: 2rem;
        }
        .btn-custom {
            background: linear-gradient(135deg, #28a745, #20c997);
            border: none;
            color: white;
        }
        .btn-custom:hover {
            background: linear-gradient(135deg, #20c997, #28a745);
            color: white;
        }
        .table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }
        .badge-admin { background-color: #dc3545; }
        .badge-tecnico { background-color: #007bff; }
        .badge-cliente { background-color: #28a745; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-12">
                <div class="card usuarios-card">
                    <div class="usuarios-header">
                        <h2><i class="fas fa-users me-2"></i>Gestión de Usuarios</h2>
                        <p class="mb-0">Administrar todos los usuarios del sistema</p>
                    </div>
                    <div class="card-body usuarios-body">
                        
                        <!-- Mensajes de estado -->
                        <% if (session.getAttribute("success") != null) { %>
                            <div class="alert alert-success alert-dismissible fade show">
                                <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                <% session.removeAttribute("success"); %>
                            </div>
                        <% } %>
                        
                        <% if (session.getAttribute("error") != null) { %>
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                <% session.removeAttribute("error"); %>
                            </div>
                        <% } %>

                        <!-- Información del Sistema -->
                        <div class="mb-3">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Panel de Administración:</strong> Gestión completa de usuarios del sistema.
                                <strong>Admin:</strong> <%= session.getAttribute("userName") %>
                            </div>
                        </div>

                        <!-- Botones de Control -->
                        <div class="mb-4">
                            <a href="${pageContext.request.contextPath}/usuarios?action=new" class="btn btn-custom">
                                <i class="fas fa-plus me-2"></i>Agregar Nuevo Usuario
                            </a>
                            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-info ms-2">
                                <i class="fas fa-refresh me-2"></i>Recargar
                            </a>
                        </div>

                        <!-- Resumen de usuarios -->
                        <%
                            List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                            int admins = 0, tecnicos = 0, clientes = 0;
                            
                            if (usuarios != null) {
                                for (Usuario u : usuarios) {
                                    if ("admin".equals(u.getRol())) admins++;
                                    else if ("tecnico".equals(u.getRol())) tecnicos++;
                                    else if ("cliente".equals(u.getRol())) clientes++;
                                }
                            }
                        %>
                        
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="card text-center border-primary">
                                    <div class="card-body">
                                        <h5 class="card-title text-primary">Total Usuarios</h5>
                                        <h2 class="text-primary"><%= usuarios != null ? usuarios.size() : 0 %></h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center border-danger">
                                    <div class="card-body">
                                        <h5 class="card-title text-danger">Administradores</h5>
                                        <h2 class="text-danger"><%= admins %></h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center border-info">
                                    <div class="card-body">
                                        <h5 class="card-title text-info">Técnicos</h5>
                                        <h2 class="text-info"><%= tecnicos %></h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center border-success">
                                    <div class="card-body">
                                        <h5 class="card-title text-success">Clientes</h5>
                                        <h2 class="text-success"><%= clientes %></h2>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Tabla de Usuarios -->
                        <h4 class="mb-3">👥 Lista de Usuarios</h4>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre</th>
                                        <th>Email</th>
                                        <th>Rol</th>
                                        <th>Fecha Registro</th>
                                        <th>Último Login</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                                        
                                        if (usuarios != null && !usuarios.isEmpty()) {
                                            for (Usuario usuario : usuarios) {
                                    %>
                                        <tr>
                                            <td><%= usuario.getId_usuario() %></td>
                                            <td><%= usuario.getNombre() %></td>
                                            <td><%= usuario.getEmail() %></td>
                                            <td>
                                                <% 
                                                    String rol = usuario.getRol();
                                                    String badgeClass = "badge-cliente";
                                                    if ("admin".equals(rol)) {
                                                        badgeClass = "badge-admin";
                                                    } else if ("tecnico".equals(rol)) {
                                                        badgeClass = "badge-tecnico";
                                                    }
                                                %>
                                                <span class="badge <%= badgeClass %>"><%= rol %></span>
                                            </td>
                                            <td><%= usuario.getFecha_creacion() != null ? sdfDate.format(usuario.getFecha_creacion()) : "N/A" %></td>
                                            <td><%= usuario.getUltimo_login() != null ? sdf.format(usuario.getUltimo_login()) : "Nunca" %></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/usuarios?action=edit&id=<%= usuario.getId_usuario() %>" 
                                                   class="btn btn-sm btn-warning" title="Editar">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <% 
                                                    // No permitir eliminar al usuario actual
                                                    Integer currentUserId = (Integer) session.getAttribute("userId");
                                                    if (currentUserId == null || currentUserId != usuario.getId_usuario()) {
                                                %>
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=delete&id=<%= usuario.getId_usuario() %>" 
                                                       class="btn btn-sm btn-danger" title="Eliminar"
                                                       onclick="return confirm('¿Estás seguro de eliminar al usuario <%= usuario.getNombre() %>? Esta acción no se puede deshacer.')">
                                                        <i class="fas fa-trash-alt"></i>
                                                    </a>
                                                <% } else { %>
                                                    <button class="btn btn-sm btn-secondary" disabled title="No puedes eliminarte a ti mismo">
                                                        <i class="fas fa-user-shield"></i>
                                                    </button>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <%  }
                                        } else { %>
                                        <tr>
                                            <td colspan="7" class="text-center">No hay usuarios registrados</td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>