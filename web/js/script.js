document.addEventListener('DOMContentLoaded', function() {
    // Validación formulario técnico
    const formTecnico = document.getElementById('formTecnico');
    if (formTecnico) {
        formTecnico.addEventListener('submit', function(e) {
            const nombre = document.getElementById('nombre').value.trim();
            const especialidad = document.getElementById('especialidad').value.trim();
            
            if (nombre.length < 3) {
                e.preventDefault();
                alert('El nombre debe tener al menos 3 caracteres');
                return;
            }
            
            if (especialidad.length < 3) {
                e.preventDefault();
                alert('La especialidad debe tener al menos 3 caracteres');
                return;
            }
        });
    }

    // Validación formulario servicio
    const formServicio = document.querySelector('form[action*="servicios"]');
    if (formServicio) {
        formServicio.addEventListener('submit', function(e) {
            const descripcion = document.getElementById('descripcion').value.trim();
            const fecha = document.getElementById('fecha').value;
            
            if (descripcion.length < 10) {
                e.preventDefault();
                alert('La descripción debe tener al menos 10 caracteres');
                return;
            }
            
            if (!fecha) {
                e.preventDefault();
                alert('Debe seleccionar una fecha');
                return;
            }
        });
    }
});