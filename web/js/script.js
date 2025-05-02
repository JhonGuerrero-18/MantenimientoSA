document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formTecnico');
    
    if (form) {
        form.addEventListener('submit', function(e) {
            const nombre = document.getElementById('nombre').value.trim();
            const especialidad = document.getElementById('especialidad').value.trim();
            
            if (!nombre || !especialidad) {
                e.preventDefault();
                alert('Por favor complete todos los campos requeridos');
            }
        });
    }
});