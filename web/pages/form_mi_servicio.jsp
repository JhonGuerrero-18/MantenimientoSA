<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitar Servicio - MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .form-card {
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            border: none;
            overflow: hidden;
        }
        .form-header {
            background: linear-gradient(135deg, #007bff, #0056b3);
            color: white;
            padding: 1.5rem;
            text-align: center;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card form-card">
                    <div class="form-header">
                        <h4><i class="fas fa-plus-circle me-2"></i>Solicitar Nuevo Servicio</h4>
                        <p class="mb-0">Describe el servicio de mantenimiento que necesitas</p>
                    </div>
                    <div class="card-body p-4">
                        
                        <form action="${pageContext.request.contextPath}/mis-servicios" method="post">
                            
                            <div class="mb-4">
                                <label for="descripcion" class="form-label">
                                    <i class="fas fa-edit me-2"></i>Descripción del Servicio <span class="text-danger">*</span>
                                </label>
                                <textarea class="form-control" id="descripcion" name="descripcion" 
                                          rows="4" required placeholder="Describe detalladamente el servicio que necesitas..."></textarea>
                                <div class="form-text">
                                    Ejemplo: "Mantenimiento preventivo del aire acondicionado", "Reparación de conexión eléctrica", etc.
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <label for="fecha" class="form-label">
                                    <i class="fas fa-calendar me-2"></i>Fecha Preferida
                                </label>
                                <input type="date" class="form-control" id="fecha" name="fecha">
                                <div class="form-text">
                                    Si no seleccionas fecha, se usará la fecha actual.
                                </div>
                            </div>
                            
                            <!-- Información del cliente -->
                            <div class="mb-4">
                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle me-2"></i>
                                    <strong>Solicitante:</strong> <%= session.getAttribute("userName") %>
                                    <br>
                                    <strong>Estado inicial:</strong> <span class="badge bg-primary">Pendiente</span>
                                    <br>
                                    <small>Tu solicitud será revisada y asignada a un técnico disponible.</small>
                                </div>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/mis-servicios" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-paper-plane me-2"></i>Enviar Solicitud
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/footer.jsp"/>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Establecer fecha mínima como hoy
        document.getElementById('fecha').min = new Date().toISOString().split('T')[0];
    </script>
</body>
</html>