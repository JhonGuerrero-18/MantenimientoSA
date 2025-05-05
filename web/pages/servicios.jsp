<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Servicio"%>
<%
    // Verificación de sesión
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Servicios | Mantenimiento S.A.</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .table-responsive {
            overflow-x: auto;
        }
        .badge {
            font-size: 0.9em;
            padding: 0.5em 0.75em;
        }
        .badge-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .badge-success {
            background-color: #198754;
        }
        .badge-info {
            background-color: #0dcaf0;
            color: #212529;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <h1 class="text-center mb-4">Servicios de Mantenimiento</h1>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>
        
        <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success alert-dismissible fade show">
                Servicio registrado correctamente
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <div class="row">
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-body">
                        <h3 class="card-title">Nuevo Servicio</h3>
                        <form action="${pageContext.request.contextPath}/servicios" method="POST">
                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción</label>
                                <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="fecha" class="form-label">Fecha</label>
                                <input type="date" class="form-control" id="fecha" name="fecha" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Solicitar Servicio</button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <h3>Servicios Registrados</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Descripción</th>
                                <th>Fecha</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                            List<Servicio> servicios = (List<Servicio>) request.getAttribute("servicios");
                            if (servicios != null && !servicios.isEmpty()) {
                                for (Servicio servicio : servicios) { %>
                                    <tr>
                                        <td><%= servicio.getId() %></td>
                                        <td><%= servicio.getDescripcion() %></td>
                                        <td><%= servicio.getFecha() != null ? servicio.getFecha().toString() : "" %></td>
                                        <td>
                                            <span class="badge <%= 
                                                "Pendiente".equals(servicio.getEstado()) ? "bg-warning" : 
                                                "Completado".equals(servicio.getEstado()) ? "bg-success" : "bg-info" %>">
                                                <%= servicio.getEstado() %>
                                            </span>
                                        </td>
                                        <td>
                                            <% if ("Pendiente".equals(servicio.getEstado())) { %>
                                                <form action="${pageContext.request.contextPath}/servicios?action=complete&id=<%= servicio.getId() %>" 
                                                      method="POST" style="display: inline;">
                                                    <button type="submit" class="btn btn-sm btn-success">Completar</button>
                                                </form>
                                            <% } %>
                                        </td>
                                    </tr>
                                <% }
                            } else { %>
                                <tr>
                                    <td colspan="5" class="text-center">No hay servicios registrados</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Limpiar formulario después de enviar
        if (window.history.replaceState) {
            window.history.replaceState(null, null, window.location.href);
        }
    </script>
</body>
</html>