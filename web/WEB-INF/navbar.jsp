<%@page contentType="text/html" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">
            <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Logo" width="30" height="30" class="d-inline-block align-top">
            Mantenimiento S.A.
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp"><i class="fas fa-home me-1"></i> Inicio</a>
                </li>
                <% if (session.getAttribute("user") != null) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/servicios"><i class="fas fa-tools me-1"></i> Servicios</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/tecnicos"><i class="fas fa-user-cog me-1"></i> Técnicos</a>
                    </li>
                <% } %>
            </ul>
            <ul class="navbar-nav ms-auto">
                <% if (session.getAttribute("user") != null) { %>
                    <li class="nav-item">
                        <span class="nav-link">
                            <i class="fas fa-user-circle me-1"></i> <%= session.getAttribute("user") %>
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link btn btn-outline-light btn-sm ms-2" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt me-1"></i> Cerrar Sesión
                        </a>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/pages/login.jsp">
                            <i class="fas fa-sign-in-alt me-1"></i> Login
                        </a>
                    </li>
                    <li class="nav-item ms-2">
                        <a class="nav-link btn btn-light btn-sm text-primary" href="${pageContext.request.contextPath}/pages/registro.jsp">
                            <i class="fas fa-user-plus me-1"></i> Registro
                        </a>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>