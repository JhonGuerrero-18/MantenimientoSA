<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Servicio" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario de Servicio - MantenimientoSA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/navbar.jsp"/>

    <%
        Servicio servicio = (Servicio) request.getAttribute("servicio");
        boolean esEdicion = (servicio != null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4><i class="fas fa-tools me-2"></i>
                            <%= esEdicion ? "Editar Servicio" : "Agregar Nuevo Servicio" %>
                        </h4>
                    </div>
                    <div class="card-body">
                        
                        <form id="servicioForm" action="servicios" method="post">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id" value="<%= servicio.getId() %>">
                            <% } %>
                            
                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción del Servicio <span class="text-danger">*</span></label>
                                <textarea class="form-control" id="descripcion" name="descripcion" 
                                          rows="3" required><%= esEdicion ? (servicio.getDescripcion() != null ? servicio.getDescripcion() : "") : "" %></textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label for="id_usuario" class="form-label">ID Usuario <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="id_usuario" name="id_usuario" 
                                       value="<%= esEdicion ? servicio.getId_usuario() : 1 %>" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="fecha" class="form-label">Fecha del Servicio</label>
                                <input type="date" class="form-control" id="fecha" name="fecha" 
                                       value="<%= esEdicion ? (servicio.getFecha() != null ? sdf.format(servicio.getFecha()) : "") : "" %>">
                            </div>
                            
                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado <span class="text-danger">*</span></label>
                                <select class="form-control" id="estado" name="estado" required>
                                    <option value="">Seleccione un estado</option>
                                    <option value="Pendiente" <%= esEdicion && "Pendiente".equals(servicio.getEstado()) ? "selected" : "" %>>Pendiente</option>
                                    <option value="En Proceso" <%= esEdicion && "En Proceso".equals(servicio.getEstado()) ? "selected" : "" %>>En Proceso</option>
                                    <option value="Completado" <%= esEdicion && "Completado".equals(servicio.getEstado()) ? "selected" : "" %>>Completado</option>
                                </select>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="servicios" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>
                                    <%= esEdicion ? "Actualizar" : "Guardar" %> Servicio
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
</body>
</html>