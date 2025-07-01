<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Verificar si el usuario está logueado
    String userName = (String) session.getAttribute("userName");
    String userRole = (String) session.getAttribute("userRole");
    boolean isLoggedIn = (userName != null && userRole != null);
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mantenimiento S.A.</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .feature-card {
            border: none;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
            height: 100%;
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
        .stats-section {
            background-color: #f8f9fa;
            padding: 3rem 0;
        }
        .cliente-action {
            background: linear-gradient(135deg, #28a745, #20c997);
            border: none;
            color: white;
            padding: 15px 30px;
            font-size: 1.2rem;
            border-radius: 50px;
            box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
            transition: all 0.3s ease;
        }
        .cliente-action:hover {
            background: linear-gradient(135deg, #20c997, #28a745);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
        }
        .cliente-section {
            background: linear-gradient(135deg, rgba(40, 167, 69, 0.1), rgba(32, 201, 151, 0.1));
            border: 2px solid #28a745;
            border-radius: 15px;
            padding: 2rem;
            margin: 2rem 0;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <!-- SECCIÓN HERO - IGUAL PARA TODOS -->
    <section class="hero-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8 col-md-10">
                    <div class="hero-content">
                        <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Logo" width="180" class="mb-4 img-fluid">
                        <h1 class="display-4 mb-4 fw-bold">Sistema de Gestión de Mantenimiento</h1>
                        <p class="lead mb-5 text-muted">La solución profesional para la gestión de servicios técnicos y mantenimiento preventivo</p>
                        
                        <!-- BOTONES CONDICIONALES -->
                        <% if (!isLoggedIn) { %>
                            <!-- Solo mostrar botones si NO está logueado -->
                            <div class="d-flex justify-content-center flex-wrap">
                                <a href="${pageContext.request.contextPath}/pages/login.jsp" class="btn btn-primary btn-hero mx-2 mb-2">
                                    <i class="fas fa-sign-in-alt me-2"></i> Iniciar Sesión
                                </a>
                                <a href="${pageContext.request.contextPath}/pages/registro.jsp" class="btn btn-success btn-hero mx-2 mb-2">
                                    <i class="fas fa-user-plus me-2"></i> Registrarse
                                </a>
                            </div>
                        <% } else { %>
                            <!-- Mostrar mensaje de bienvenida si está logueado -->
                            <div class="alert alert-success d-inline-block">
                                <i class="fas fa-user-check me-2"></i>
                                ¡Bienvenido, <strong><%= userName %></strong>! 
                                <span class="badge bg-primary ms-2"><%= userRole %></span>
                            </div>
                            
                            <!-- NUEVO: Sección especial para clientes -->
                            <% if ("cliente".equals(userRole)) { %>
                                <div class="cliente-section text-center">
                                    <h3 class="text-success mb-3">
                                        <i class="fas fa-tools me-2"></i>¿Necesitas un servicio de mantenimiento?
                                    </h3>
                                    <p class="lead mb-4 text-muted">
                                        Solicita tu servicio de manera rápida y sencilla. Nuestros técnicos especializados están listos para ayudarte.
                                    </p>
                                    <a href="${pageContext.request.contextPath}/mis-servicios" class="btn cliente-action">
                                        <i class="fas fa-plus-circle me-2"></i>Pide tu servicio aquí
                                    </a>
                                    <div class="mt-3">
                                        <small class="text-muted">
                                            <i class="fas fa-clock me-1"></i> Respuesta en 24 horas | 
                                            <i class="fas fa-shield-alt me-1"></i> Servicio garantizado
                                        </small>
                                    </div>
                                </div>
                            <% } %>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- CARACTERÍSTICAS DEL SISTEMA - SIEMPRE VISIBLE -->
    <div class="container my-5">
        <h2 class="text-center mb-5">¿Por qué elegir nuestro sistema?</h2>
        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card feature-card">
                    <div class="card-body text-center">
                        <i class="fas fa-users text-primary mb-3" style="font-size: 3rem;"></i>
                        <h5>Gestión de Usuarios</h5>
                        <p class="text-muted">Sistema de roles: administradores, técnicos y clientes con permisos específicos.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card feature-card">
                    <div class="card-body text-center">
                        <i class="fas fa-clipboard-list text-success mb-3" style="font-size: 3rem;"></i>
                        <h5>Control de Servicios</h5>
                        <p class="text-muted">Seguimiento completo de servicios desde solicitud hasta finalización.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card feature-card">
                    <div class="card-body text-center">
                        <i class="fas fa-mobile-alt text-info mb-3" style="font-size: 3rem;"></i>
                        <h5>Responsive Design</h5>
                        <p class="text-muted">Acceso desde cualquier dispositivo: computadora, tablet o móvil.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- ESTADÍSTICAS - SIEMPRE VISIBLE -->
    <div class="stats-section">
        <div class="container">
            <div class="row text-center">
                <div class="col-md-3 mb-3">
                    <h3 class="text-primary">3</h3>
                    <p class="text-muted">Roles de Usuario</p>
                </div>
                <div class="col-md-3 mb-3">
                    <h3 class="text-success">100%</h3>
                    <p class="text-muted">Responsive</p>
                </div>
                <div class="col-md-3 mb-3">
                    <h3 class="text-info">24/7</h3>
                    <p class="text-muted">Disponibilidad</p>
                </div>
                <div class="col-md-3 mb-3">
                    <h3 class="text-warning">Seguro</h3>
                    <p class="text-muted">Control de Acceso</p>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>