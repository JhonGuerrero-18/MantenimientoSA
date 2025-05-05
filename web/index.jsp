<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mantenimiento S.A.</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <section class="hero-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8 col-md-10">
                    <div class="hero-content">
                        <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Logo" width="180" class="mb-4 img-fluid">
                        <h1 class="display-4 mb-4 fw-bold">Sistema de Gestión de Mantenimiento</h1>
                        <p class="lead mb-5 text-muted">La solución profesional para la gestión de servicios técnicos y mantenimiento preventivo</p>
                        <div class="d-flex justify-content-center flex-wrap">
                            <a href="${pageContext.request.contextPath}/pages/login.jsp" class="btn btn-primary btn-hero mx-2 mb-2">
                                <i class="fas fa-sign-in-alt me-2"></i> Iniciar Sesión
                            </a>
                            <a href="${pageContext.request.contextPath}/pages/registro.jsp" class="btn btn-success btn-hero mx-2 mb-2">
                                <i class="fas fa-user-plus me-2"></i> Registrarse
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="/WEB-INF/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>