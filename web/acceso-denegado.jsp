<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso Denegado | Mantenimiento S.A.</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-6 col-md-8">
                <div class="card">
                    <div class="card-body text-center p-5">
                        <div class="mb-4">
                            <i class="fas fa-ban text-danger" style="font-size: 5rem;"></i>
                        </div>
                        <h2 class="text-danger mb-3">Acceso Denegado</h2>
                        <p class="lead mb-4">No tienes permisos para acceder a esta funcionalidad.</p>
                        
                        <%
                            String userRole = (String) session.getAttribute("userRole");
                            if (userRole != null) {
                        %>
                            <div class="alert alert-info">
                                <strong>Tu rol actual:</strong> 
                                <span class="badge bg-primary"><%= userRole %></span>
                            </div>
                        <% } %>
                        
                        <div class="d-grid gap-2 d-md-block">
                            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">
                                <i class="fas fa-home me-2"></i>Volver al Inicio
                            </a>
                            <button onclick="history.back()" class="btn btn-secondary">
                                <i class="fas fa-arrow-left me-2"></i>Página Anterior
                            </button>
                        </div>
                        
                        <hr class="my-4">
                        
                        <div class="text-muted small">
                            <h6>Información de permisos:</h6>
                            <ul class="list-unstyled text-start">
                                <li><strong>Admin:</strong> Acceso completo al sistema</li>
                                <li><strong>Técnico:</strong> Puede gestionar servicios</li>
                                <li><strong>Cliente:</strong> Puede crear y ver sus servicios</li>
                            </ul>
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