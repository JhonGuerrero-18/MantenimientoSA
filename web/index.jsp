<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mantenimiento S.A.</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8 text-center">
                <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Logo" width="200">
                <h1 class="mt-4">Sistema de Gestión de Mantenimiento</h1>
                <div class="mt-4">
                    <a href="${pageContext.request.contextPath}/pages/login.jsp" class="btn btn-primary btn-lg mx-2">
                        Iniciar Sesión
                    </a>
                    <a href="${pageContext.request.contextPath}/pages/registro.jsp" class="btn btn-success btn-lg mx-2">
                        Registrarse
                    </a>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>