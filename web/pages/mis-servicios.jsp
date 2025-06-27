<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Servicio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Servicios - MantenimientoSA</title>
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
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
            padding: 1.5rem;
            text-align: center;
        }
        .servicios-body {
            padding: 2rem;
        }
        .btn-custom {
            background: linear-gradient(135deg, #007bff, #0056b3);
            border: none;
            color: white;
        }
        .btn-custom:hover {
            background: linear-gradient(135deg, #0056b3, #007bff);
            color: white;
        }
        .table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }
        .servicio-card {
            border-left: 4px solid #28a745;
            margin-bottom: 1rem;
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
                        <h2><i class="fas fa-clipboard-list me-2"></i>Mis Servicios de Mantenimiento</h2>
                        <p class="mb-0">Gestiona tus solicitudes de servicio</p>
                    </div>
                    <div class="card-body servicios-body">
                        
                        <!-- Información del Cliente -->
                        <div class="mb-3">
                            <div class="alert alert-success">
                                <i class="fas fa-user-circle me-2"></i>
                                <strong>Bienvenido:</strong> <%= session.getAttribute("userName") %>
                                <span class="badge bg-light text-dark ms-2">Cliente</span>
                            </div>
                        </div>

                        <!-- Botón para crear nuevo servicio -->
                        <div class="mb-4">
                            <a href="${pageContext.request.contextPath}/mis-servicios?action=new" class="btn btn-custom">
                                <i class="fas fa-plus me-2"></i>Solicitar Nuevo Servicio
                            </a>
                            <a href="${pageContext.request.contextPath}/mis-servicios" class="btn btn-info ms-2">
                                <i class="fas fa-refresh me-2"></i>Actualizar
                            </a>
                        </div>

                        <!-- Resumen de servicios -->
                        <%
                            List<Servicio> misServicios = (List<Servicio>) request.getAttribute("misServicios");
                            int pendientes = 0, enProceso = 0, completados = 0;
                            
                            if (misServicios != null) {
                                for (Servicio s : misServicios) {
                                    if ("Pendiente".equals(s.getEstado())) pendientes++;
                                    else if ("En Proceso".equals(s.getEstado())) enProceso++;
                                    else if ("Completado".equals(s.getEstado())) completados++;
                                }
                            }
                        %>
                        
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="card text-center border-primary">
                                    <div class="card-body">
                                        <h5 class="card-title text-primary">Total</h5>
                                        <h2 class="text-primary"><%= misServicios != null ? misServicios.size() : 0 %></h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center border-warning">
                                    <div class="card-body">
                                        <h5 class="card-title text-warning">Pendientes</h5>
                                        <h2 class="text-warning"><%= pendientes %></h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center border-info">
                                    <div class="card-body">
                                        <h5 class="card-title text-info">En Proceso</h5>
                                        <h2 class="text-info"><%= enProceso %></h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center border-success">
                                    <div class="card-body">
                                        <h5 class="card-title text-success">Completados</h5>
                                        <h2 class="text-success"><%= completados %></h2>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Lista de servicios -->
                        <h4 class="mb-3">📋 Historial de Servicios</h4>
                        
                        <%
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            
                            if (misServicios != null && !misServicios.isEmpty()) {
                                for (Servicio servicio : misServicios) {
                        %>
                            <div class="card servicio-card">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col-md-1">
                                            <h5 class="text-muted mb-0">#<%= servicio.getId() %></h5>
                                        </div>
                                        <div class="col-md-5">
                                            <h6 class="mb-1"><%= servicio.getDescripcion() %></h6>
                                            <small class="text-muted">
                                                <i class="fas fa-calendar me-1"></i>
                                                <%= servicio.getFecha() != null ? sdf.format(servicio.getFecha()) : "Sin fecha" %>
                                            </small>
                                        </div>
                                        <div class="col-md-3">
                                            <% 
                                                String estado = servicio.getEstado();
                                                String badgeClass = "secondary";
                                                String icon = "fas fa-clock";
                                                
                                                if ("Completado".equals(estado)) {
                                                    badgeClass = "success";
                                                    icon = "fas fa-check-circle";
                                                } else if ("En Proceso".equals(estado)) {
                                                    badgeClass = "warning";
                                                    icon = "fas fa-cog fa-spin";
                                                } else if ("Pendiente".equals(estado)) {
                                                    badgeClass = "primary";
                                                    icon = "fas fa-clock";
                                                }
                                            %>
                                            <span class="badge bg-<%= badgeClass %> p-2">
                                                <i class="<%= icon %> me-1"></i><%= estado %>
                                            </span>
                                        </div>
                                        <div class="col-md-3 text-end">
                                            <% if ("Pendiente".equals(estado)) { %>
                                                <small class="text-muted">Esperando asignación</small>
                                            <% } else if ("En Proceso".equals(estado)) { %>
                                                <small class="text-info">Técnico trabajando</small>
                                            <% } else if ("Completado".equals(estado)) { %>
                                                <small class="text-success">Servicio finalizado</small>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <%
                                }
                            } else {
                        %>
                            <div class="text-center py-5">
                                <i class="fas fa-clipboard-list text-muted" style="font-size: 4rem;"></i>
                                <h4 class="text-muted mt-3">No tienes servicios registrados</h4>
                                <p class="text-muted">¡Solicita tu primer servicio de mantenimiento!</p>
                                <a href="${pageContext.request.contextPath}/mis-servicios?action=new" class="btn btn-custom">
                                    <i class="fas fa-plus me-2"></i>Solicitar Servicio
                                </a>
                            </div>
                        <%
                            }
                        %>

                        <!-- Información adicional -->
                        <div class="mt-4">
                            <div class="alert alert-light">
                                <h6><i class="fas fa-info-circle me-2"></i>¿Cómo funciona?</h6>
                                <ul class="mb-0">
                                    <li><strong>Solicita:</strong> Crea una nueva solicitud de servicio</li>
                                    <li><strong>Seguimiento:</strong> Monitorea el estado de tus servicios</li>
                                    <li><strong>Estados:</strong> 
                                        <span class="badge bg-primary">Pendiente</span> → 
                                        <span class="badge bg-warning">En Proceso</span> → 
                                        <span class="badge bg-success">Completado</span>
                                    </li>
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