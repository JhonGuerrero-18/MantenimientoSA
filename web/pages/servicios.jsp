<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.ServicioDAO" %>
<%@ page import="models.Servicio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
// AGREGADO: Variables para control de roles
String userRole = (String) session.getAttribute("userRole");
boolean canEdit = "admin".equals(userRole) || "tecnico".equals(userRole);
boolean canDelete = "admin".equals(userRole);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Servicios - MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<style>
    .servicios-card {
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        border: none;
        overflow: hidden;
    }
    .servicios-header {
        background-color: #0d6efd;
        color: white;
        padding: 1.5rem;
        text-align: center;
    }
    .servicios-body {
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
    /* Arreglar hover de navbar - que NO se vea como botón */
    .navbar .nav-link {
        border-radius: 0 !important;
        padding: 0.5rem 1rem !important;
        border: none !important;
        background: none !important;
        text-decoration: none !important;
    }
    .navbar .nav-link:hover {
        color: #e3f2fd !important;
        background: none !important;
        border: none !important;
        text-decoration: none !important;
        box-shadow: none !important;
    }
    .navbar .nav-link:focus {
        background: none !important;
        border: none !important;
        box-shadow: none !important;
    }
</style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-10 col-md-12">
                <div class="card servicios-card">
                    <div class="servicios-header">
                        <h2><i class="fas fa-tools me-2"></i>Gestión de Servicios</h2>
                    </div>
                    <div class="card-body servicios-body">
                        <!-- Información del Sistema -->
                        <div class="mb-3">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Modo JSP Tradicional:</strong> Esta página carga datos directamente desde el servidor.
                                <a href="${pageContext.request.contextPath}/servicios.jsp" class="btn btn-sm btn-outline-primary ms-2">
                                    <i class="fas fa-rocket me-1"></i>Ver Versión API
                                </a>
                            </div>
                        </div>

                        <!-- Botones de Control - MODIFICADO CON ROLES -->
                        <div class="mb-4">
                            <% if (canEdit) { %>
                                <a href="${pageContext.request.contextPath}/servicios?action=new" class="btn btn-custom">
                                    <i class="fas fa-plus me-2"></i>Agregar Nuevo Servicio
                                </a>
                            <% } %>
                            <a href="${pageContext.request.contextPath}/servicios" class="btn btn-info ms-2">
                                <i class="fas fa-refresh me-2"></i>Recargar
                            </a>
                        </div>

                        <!-- Tabla de Servicios -->
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Descripción</th>
                                        <th>Fecha</th>
                                        <th>Usuario</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        // Obtener servicios desde el servlet
                                        List<Servicio> servicios = (List<Servicio>) request.getAttribute("servicios");
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        
                                        if (servicios != null && !servicios.isEmpty()) {
                                            for (Servicio servicio : servicios) {
                                    %>
                                        <tr>
                                            <td><%= servicio.getD_servicio() %></td>
                                            <td><%= servicio.getDescripcion() %></td>
                                            <td><%= servicio.getFecha() != null ? sdf.format(servicio.getFecha()) : "N/A" %></td>
                                            <td>Usuario #<%= servicio.getId_usuario() %></td>
                                            <td>
                                                <% 
                                                    String estado = servicio.getEstado();
                                                    String badgeClass = "secondary";
                                                    if ("Completado".equals(estado)) {
                                                        badgeClass = "success";
                                                    } else if ("En Proceso".equals(estado)) {
                                                        badgeClass = "warning";
                                                    } else if ("Pendiente".equals(estado)) {
                                                        badgeClass = "primary";
                                                    }
                                                %>
                                                <span class="badge bg-<%= badgeClass %>"><%= estado %></span>
                                            </td>
                                            <!-- COLUMNA DE ACCIONES MODIFICADA CON ROLES -->
                                            <td>
                                                <% if (canEdit) { %>
                                                    <a href="${pageContext.request.contextPath}/servicios?action=edit&id=<%= servicio.getD_servicio() %>" 
                                                       class="btn btn-sm btn-warning">
                                                        <i class="fas fa-edit"></i> Editar
                                                    </a>
                                                <% } %>
                                                <% if (canDelete) { %>
                                                    <a href="${pageContext.request.contextPath}/servicios?action=delete&id=<%= servicio.getD_servicio() %>" 
                                                       class="btn btn-sm btn-danger ms-1"
                                                       onclick="return confirm('¿Está seguro de eliminar este servicio?')">
                                                        <i class="fas fa-trash"></i> Eliminar
                                                    </a>
                                                <% } %>
                                                <% if (!canEdit && !canDelete) { %>
                                                    <span class="text-muted small">Sin permisos</span>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <%
                                            }
                                        } else {
                                    %>
                                        <tr>
                                            <td colspan="6" class="text-center text-muted">
                                                <i class="fas fa-info-circle me-2"></i>
                                                No hay servicios registrados. 
                                                <% if (canEdit) { %>
                                                    <a href="${pageContext.request.contextPath}/servicios?action=new">Crear el primero</a>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>

                        <!-- Información adicional -->
                        <div class="mt-4">
                            <div class="alert alert-light">
                                <h6><i class="fas fa-lightbulb me-2"></i>Información:</h6>
                                <ul class="mb-0">
                                    <li>Esta página muestra todos los servicios de mantenimiento registrados</li>
                                    <% if (canEdit) { %>
                                        <li>Puede crear, editar servicios usando los botones correspondientes</li>
                                    <% } %>
                                    <% if (canDelete) { %>
                                        <li>Puede eliminar servicios (solo administradores)</li>
                                    <% } %>
                                    <li>Los estados indican el progreso de cada servicio</li>
                                    <% if (userRole != null) { %>
                                        <li><strong>Su rol:</strong> <span class="badge bg-primary"><%= userRole %></span></li>
                                    <% } %>
                                </ul>
                            </div>
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