<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String userRole = (String) session.getAttribute("userRole");
%>
<style>
    /* Corregir el hover del botón de cerrar sesión */
    .btn-outline-light:hover {
        background-color: rgba(255, 255, 255, 0.2) !important;
        color: white !important;
        border-color: white !important;
    }
    .btn-outline-light {
        color: white !important;
        border-color: rgba(255, 255, 255, 0.5) !important;
    }
</style>

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
                    <!-- Solo admin y tecnico pueden ver Servicios -->
                    <% if ("admin".equals(userRole) || "tecnico".equals(userRole)) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/servicios"><i class="fas fa-tools me-1"></i> Servicios</a>
                        </li>
                    <% } %>
                    <!-- Solo admin puede ver Técnicos -->
                    <% if ("admin".equals(userRole)) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/tecnicos"><i class="fas fa-user-cog me-1"></i> Técnicos</a>
                        </li>
                    <% } %>
                    <!-- Solo admin puede ver Gestión de Usuarios -->
                    <% if ("admin".equals(userRole)) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/usuarios"><i class="fas fa-users me-1"></i> Usuarios</a>
                        </li>
                    <% } %>
                    <!-- NUEVO: Solo admin puede ver Asignaciones -->
                    <% if ("admin".equals(userRole)) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/asignaciones"><i class="fas fa-tasks me-1"></i> Asignaciones</a>
                        </li>
                    <% } %>
                    <!-- Solo clientes pueden ver Mis Servicios -->
                    <% if ("cliente".equals(userRole)) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/mis-servicios"><i class="fas fa-clipboard-list me-1"></i> Mis Servicios</a>
                        </li>
                    <% } %>
                <% } %>
            </ul>
            <ul class="navbar-nav ms-auto">
                <% if (session.getAttribute("user") != null) { %>
                    <li class="nav-item">
                        <span class="nav-link">
                            <i class="fas fa-user-circle me-1"></i> <%= session.getAttribute("user") %>
                            <% if (userRole != null) { %>
                                <small class="badge bg-light text-dark ms-1"><%= userRole %></small>
                            <% } %>
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link btn btn-outline-light btn-sm ms-2" href="${pageContext.request.contextPath}/logout" style="color: white !important;">
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