<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Asignaciones - MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .asignaciones-card {
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            border: none;
            overflow: hidden;
        }
        .asignaciones-header {
            background: linear-gradient(135deg, #17a2b8, #138496);
            color: white;
            padding: 1.5rem;
            text-align: center;
        }
        .asignaciones-body {
            padding: 2rem;
        }
        .table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }
        .debug-info {
            background-color: #f8f9fa;
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1rem;
            font-family: monospace;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-12">
                <div class="card asignaciones-card">
                    <div class="asignaciones-header">
                        <h2><i class="fas fa-tasks me-2"></i>Gestión de Asignaciones</h2>
                        <p class="mb-0">Asignar servicios a técnicos disponibles</p>
                    </div>
                    <div class="card-body asignaciones-body">
                        
                        <!-- Mensajes de estado -->
                        <% if (session.getAttribute("success") != null) { %>
                            <div class="alert alert-success alert-dismissible fade show">
                                <i class="fas fa-check-circle me-2"></i><%= session.getAttribute("success") %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                <% session.removeAttribute("success"); %>
                            </div>
                        <% } %>
                        
                        <% if (session.getAttribute("error") != null) { %>
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-circle me-2"></i><%= session.getAttribute("error") %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                <% session.removeAttribute("error"); %>
                            </div>
                        <% } %>

                        <!-- Información del Sistema -->
                        <div class="mb-3">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Panel de Asignaciones:</strong> Gestión de servicios y técnicos.
                                <strong>Admin:</strong> <%= session.getAttribute("userName") %>
                            </div>
                        </div>

                        <!-- Debug Info -->
                        <div class="debug-info">
                            <strong>📊 Estado de los datos:</strong><br>
                            <%
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> todosLosServicios = (List<Map<String, Object>>) request.getAttribute("todosLosServicios");
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> todosLosTecnicos = (List<Map<String, Object>>) request.getAttribute("todosLosTecnicos");
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> serviciosSinAsignar = (List<Map<String, Object>>) request.getAttribute("serviciosSinAsignar");
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> asignaciones = (List<Map<String, Object>>) request.getAttribute("asignaciones");
                            %>
                            Servicios disponibles: <%= todosLosServicios != null ? todosLosServicios.size() : "0" %> | 
                            Técnicos disponibles: <%= todosLosTecnicos != null ? todosLosTecnicos.size() : "0" %> | 
                            Servicios sin asignar: <%= serviciosSinAsignar != null ? serviciosSinAsignar.size() : "0" %> | 
                            Asignaciones actuales: <%= asignaciones != null ? asignaciones.size() : "0" %>
                        </div>

                        <!-- Botones de Control -->
                        <div class="mb-4">
                            <a href="${pageContext.request.contextPath}/asignaciones" class="btn btn-info">
                                <i class="fas fa-refresh me-2"></i>Recargar
                            </a>
                            <button type="button" class="btn btn-success ms-2" data-bs-toggle="modal" data-bs-target="#nuevaAsignacionModal">
                                <i class="fas fa-plus me-2"></i>Nueva Asignación
                            </button>
                        </div>

                        <!-- Contenido principal -->
                        <div class="row">
                            <!-- ASIGNACIONES ACTUALES - TABLA CON EDICIÓN -->
                            <div class="col-md-8">
                                <h3><i class="fas fa-clipboard-list me-2"></i>Asignaciones Actuales</h3>
                                <%
                                    if (asignaciones != null && !asignaciones.isEmpty()) {
                                %>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Técnico</th>
                                                    <th>Servicio</th>
                                                    <th>Estado</th>
                                                    <th>Fecha</th>
                                                    <th>Acciones</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    for (Map<String, Object> asignacion : asignaciones) {
                                                %>
                                                    <tr>
                                                        <!-- ID -->
                                                        <td><%= asignacion.get("id_asignacion") %></td>
                                                        
                                                        <!-- TÉCNICO -->
                                                        <td>
                                                            <i class="fas fa-user-cog me-1"></i>
                                                            <% 
                                                                Object nombreTecnico = asignacion.get("nombre_tecnico");
                                                                if (nombreTecnico != null) {
                                                                    out.print(nombreTecnico);
                                                                    Object especialidad = asignacion.get("tecnico_especialidad");
                                                                    if (especialidad != null) {
                                                                        out.print("<br><small class='text-muted'>" + especialidad + "</small>");
                                                                    }
                                                                } else {
                                                                    out.print("<span class='text-muted'>Sin técnico</span>");
                                                                }
                                                            %>
                                                        </td>
                                                        
                                                        <!-- SERVICIO -->
                                                        <td>
                                                            <% 
                                                                Object descripcionServicio = asignacion.get("descripcion_servicio");
                                                                if (descripcionServicio != null) {
                                                                    out.print("<small class='text-muted'>" + descripcionServicio + "</small>");
                                                                } else {
                                                                    out.print("<span class='text-muted'>Sin descripción</span>");
                                                                }
                                                            %>
                                                        </td>
                                                        
                                                        <!-- ESTADO -->
                                                        <td>
                                                            <% 
                                                                Object estadoServicio = asignacion.get("estado_servicio");
                                                                if (estadoServicio != null) {
                                                                    String estado = estadoServicio.toString();
                                                                    String badgeClass = "bg-primary";
                                                                    if ("pendiente".equalsIgnoreCase(estado)) {
                                                                        badgeClass = "bg-warning";
                                                                    } else if ("completado".equalsIgnoreCase(estado)) {
                                                                        badgeClass = "bg-success";
                                                                    } else if ("en_proceso".equalsIgnoreCase(estado)) {
                                                                        badgeClass = "bg-info";
                                                                    }
                                                                    out.print("<span class='badge " + badgeClass + "'>" + estado + "</span>");
                                                                } else {
                                                                    out.print("<span class='badge bg-secondary'>Sin estado</span>");
                                                                }
                                                            %>
                                                        </td>
                                                        
                                                        <!-- FECHA -->
                                                        <td>
                                                            <% 
                                                                Object fechaAsignacion = asignacion.get("fecha_asignacion");
                                                                if (fechaAsignacion != null) {
                                                                    out.print("<small>" + fechaAsignacion + "</small>");
                                                                } else {
                                                                    out.print("<small class='text-muted'>Sin fecha</small>");
                                                                }
                                                            %>
                                                        </td>
                                                        
                                                        <!-- ACCIONES - CON EDITAR -->
                                                        <td>
                                                            <button type="button" class="btn btn-sm btn-primary me-1" 
                                                                    onclick="editarAsignacion(<%= asignacion.get("id_asignacion") %>, <%= asignacion.get("id_servicio") %>, <%= asignacion.get("id_tecnico") %>)"
                                                                    data-bs-toggle="modal" data-bs-target="#editarAsignacionModal">
                                                                <i class="fas fa-edit me-1"></i>Editar
                                                            </button>
                                                            <a href="?action=delete&id=<%= asignacion.get("id_asignacion") %>" 
                                                               class="btn btn-sm btn-danger"
                                                               onclick="return confirm('¿Eliminar asignación?')">
                                                                <i class="fas fa-trash me-1"></i>Eliminar
                                                            </a>
                                                        </td>
                                                    </tr>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                <%
                                    } else {
                                %>
                                    <div class="alert alert-warning">
                                        <i class="fas fa-info-circle me-2"></i>
                                        No hay asignaciones registradas. ¡Crea la primera asignación!
                                    </div>
                                <%
                                    }
                                %>
                            </div>
                            
                            <!-- SERVICIOS SIN ASIGNAR -->
                            <div class="col-md-4">
                                <h3><i class="fas fa-exclamation-triangle me-2"></i>Servicios Sin Asignar</h3>
                                <%
                                    if (serviciosSinAsignar != null && !serviciosSinAsignar.isEmpty()) {
                                %>
                                    <div class="table-responsive">
                                        <table class="table table-sm table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Descripción</th>
                                                    <th>Cliente</th>
                                                    <th>Acción</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    for (Map<String, Object> servicio : serviciosSinAsignar) {
                                                %>
                                                    <tr>
                                                        <td><%= servicio.get("id_servicio") %></td>
                                                        <td>
                                                            <small><%= servicio.get("descripcion") %></small>
                                                        </td>
                                                        <td>
                                                            <small class="text-muted"><%= servicio.get("cliente") %></small>
                                                        </td>
                                                        <td>
                                                            <button type="button" class="btn btn-sm btn-success" 
                                                                    onclick="asignarRapido(<%= servicio.get("id_servicio") %>, '<%= servicio.get("descripcion") %>')"
                                                                    data-bs-toggle="modal" data-bs-target="#nuevaAsignacionModal">
                                                                <i class="fas fa-plus"></i>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                <%
                                    } else {
                                %>
                                    <div class="alert alert-success">
                                        <i class="fas fa-check-circle me-2"></i>
                                        ¡Todos los servicios están asignados!
                                    </div>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- MODAL NUEVA ASIGNACIÓN -->
    <div class="modal fade" id="nuevaAsignacionModal" tabindex="-1" aria-labelledby="nuevaAsignacionModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="nuevaAsignacionModalLabel">
                        <i class="fas fa-plus-circle me-2"></i>Nueva Asignación
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/asignaciones" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="servicioSelect" class="form-label">
                                <i class="fas fa-clipboard-list me-2"></i>Seleccionar Servicio
                            </label>
                            <select class="form-select" id="servicioSelect" name="servicioId" required>
                                <option value="">-- Seleccione un servicio --</option>
                                <%
                                    if (todosLosServicios != null) {
                                        for (Map<String, Object> servicio : todosLosServicios) {
                                %>
                                    <option value="<%= servicio.get("id_servicio") %>">
                                        [#<%= servicio.get("id_servicio") %>] <%= servicio.get("descripcion") %> 
                                        - Cliente: <%= servicio.get("cliente") %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="tecnicoSelect" class="form-label">
                                <i class="fas fa-user-cog me-2"></i>Seleccionar Técnico
                            </label>
                            <select class="form-select" id="tecnicoSelect" name="tecnicoId" required>
                                <option value="">-- Seleccione un técnico --</option>
                                <%
                                    if (todosLosTecnicos != null) {
                                        for (Map<String, Object> tecnico : todosLosTecnicos) {
                                %>
                                    <option value="<%= tecnico.get("id") %>">
                                        <%= tecnico.get("nombre_completo") %> 
                                        - <%= tecnico.get("especialidad") %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancelar
                        </button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-2"></i>Crear Asignación
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- MODAL EDITAR ASIGNACIÓN -->
    <div class="modal fade" id="editarAsignacionModal" tabindex="-1" aria-labelledby="editarAsignacionModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editarAsignacionModalLabel">
                        <i class="fas fa-edit me-2"></i>Editar Asignación
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/asignaciones" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="editAsignacionId" name="asignacionId">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="editServicioSelect" class="form-label">
                                <i class="fas fa-clipboard-list me-2"></i>Seleccionar Servicio
                            </label>
                            <select class="form-select" id="editServicioSelect" name="servicioId" required>
                                <option value="">-- Seleccione un servicio --</option>
                                <%
                                    if (todosLosServicios != null) {
                                        for (Map<String, Object> servicio : todosLosServicios) {
                                %>
                                    <option value="<%= servicio.get("id_servicio") %>">
                                        [#<%= servicio.get("id_servicio") %>] <%= servicio.get("descripcion") %> 
                                        - Cliente: <%= servicio.get("cliente") %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="editTecnicoSelect" class="form-label">
                                <i class="fas fa-user-cog me-2"></i>Seleccionar Técnico
                            </label>
                            <select class="form-select" id="editTecnicoSelect" name="tecnicoId" required>
                                <option value="">-- Seleccione un técnico --</option>
                                <%
                                    if (todosLosTecnicos != null) {
                                        for (Map<String, Object> tecnico : todosLosTecnicos) {
                                %>
                                    <option value="<%= tecnico.get("id") %>">
                                        <%= tecnico.get("nombre_completo") %> 
                                        - <%= tecnico.get("especialidad") %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancelar
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-2"></i>Actualizar Asignación
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function asignarRapido(servicioId, descripcion) {
            document.getElementById('servicioSelect').value = servicioId;
        }
        
        function editarAsignacion(asignacionId, servicioId, tecnicoId) {
            document.getElementById('editAsignacionId').value = asignacionId;
            document.getElementById('editServicioSelect').value = servicioId;
            document.getElementById('editTecnicoSelect').value = tecnicoId;
        }
    </script>
</body>
</html>