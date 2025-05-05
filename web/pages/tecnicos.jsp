<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Tecnico"%>
<%
    // Validación de sesión
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
    <title>Técnicos | MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <h1 class="text-center mb-4">Gestión de Técnicos</h1>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success alert-dismissible fade show">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <div class="row">
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-body">
                        <h3 class="card-title">
                            <%= request.getAttribute("tecnicoEdit") != null ? "Editar" : "Registrar" %> Técnico
                        </h3>
                        <form action="${pageContext.request.contextPath}/tecnicos" method="POST">
                            <% if (request.getAttribute("tecnicoEdit") != null) { 
                                Tecnico tecnicoEdit = (Tecnico) request.getAttribute("tecnicoEdit");
                            %>
                                <input type="hidden" name="id" value="<%= tecnicoEdit.getId() %>">
                            <% } %>
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" 
                                       value="<%= request.getAttribute("tecnicoEdit") != null ? 
                                       ((Tecnico)request.getAttribute("tecnicoEdit")).getNombre() : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="especialidad" class="form-label">Especialidad</label>
                                <input type="text" class="form-control" id="especialidad" name="especialidad"
                                       value="<%= request.getAttribute("tecnicoEdit") != null ? 
                                       ((Tecnico)request.getAttribute("tecnicoEdit")).getEspecialidad() : "" %>" required>
                            </div>
                            <button type="submit" class="btn btn-primary">
                                <%= request.getAttribute("tecnicoEdit") != null ? "Actualizar" : "Guardar" %>
                            </button>
                            <% if (request.getAttribute("tecnicoEdit") != null) { %>
                                <a href="${pageContext.request.contextPath}/tecnicos" class="btn btn-secondary">Cancelar</a>
                            <% } %>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <h3>Listado de Técnicos</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Especialidad</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                            List<Tecnico> tecnicos = (List<Tecnico>) request.getAttribute("tecnicos");
                            if (tecnicos != null && !tecnicos.isEmpty()) {
                                for (Tecnico t : tecnicos) { %>
                                    <tr>
                                        <td><%= t.getId() %></td>
                                        <td><%= t.getNombre() %></td>
                                        <td><%= t.getEspecialidad() %></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/tecnicos?action=edit&id=<%= t.getId() %>" 
                                               class="btn btn-sm btn-warning">
                                                <i class="fas fa-edit"></i> Editar
                                            </a>
                                            <a href="${pageContext.request.contextPath}/tecnicos?action=delete&id=<%= t.getId() %>" 
                                               class="btn btn-sm btn-danger"
                                               onclick="return confirm('¿Estás seguro de eliminar al técnico <%= t.getNombre() %>? Esta acción no se puede deshacer.')">
                                                <i class="fas fa-trash-alt"></i> Eliminar
                                            </a>
                                        </td>
                                    </tr>
                                <% }
                            } else { %>
                                <tr>
                                    <td colspan="4" class="text-center">No hay técnicos registrados</td>
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
        // Limpiar parámetros de la URL después de acciones
        if (window.history.replaceState) {
            window.history.replaceState(null, null, window.location.pathname);
        }
        
        // Mostrar mensajes de éxito/error
        setTimeout(() => {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                alert.classList.remove('show');
                setTimeout(() => alert.remove(), 300);
            });
        }, 5000);
    </script>
</body>
</html>