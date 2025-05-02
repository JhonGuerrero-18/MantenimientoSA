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
    <title>Servicios | Mantenimiento S.A.</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <h1 class="text-center mb-4">Servicios de Mantenimiento</h1>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">${error}</div>
        <% } %>
        
        <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">Servicio registrado correctamente</div>
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
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Descripción</th>
                            <th>Fecha</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        List<Servicio> servicios = (List<Servicio>) request.getAttribute("servicios");
                        if (servicios != null) {
                            for (Servicio servicio : servicios) { %>
                                <tr>
                                    <td><%= servicio.getId() %></td>
                                    <td><%= servicio.getDescripcion() %></td>
                                    <td><%= servicio.getFecha() %></td>
                                    <td><%= servicio.getEstado() %></td>
                                </tr>
                            <% }
                        } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>