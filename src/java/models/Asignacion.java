// Archivo: src/models/Asignacion.java
package models;

public class Asignacion {
    private int id_asignacion;
    private int id_tecnico;
    private int id_servicio;
    
    // Para mostrar datos relacionados
    private String nombreTecnico;
    private String emailTecnico;
    private String descripcionServicio;
    private String estadoServicio;
    private String clienteNombre;
    
    // Constructores
    public Asignacion() {}
    
    public Asignacion(int id_tecnico, int id_servicio) {
        this.id_tecnico = id_tecnico;
        this.id_servicio = id_servicio;
    }
    
    // Getters y Setters
    public int getId_asignacion() {
        return id_asignacion;
    }
    
    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }
    
    public int getId_tecnico() {
        return id_tecnico;
    }
    
    public void setId_tecnico(int id_tecnico) {
        this.id_tecnico = id_tecnico;
    }
    
    public int getId_servicio() {
        return id_servicio;
    }
    
    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }
    
    public String getNombreTecnico() {
        return nombreTecnico;
    }
    
    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }
    
    public String getEmailTecnico() {
        return emailTecnico;
    }
    
    public void setEmailTecnico(String emailTecnico) {
        this.emailTecnico = emailTecnico;
    }
    
    public String getDescripcionServicio() {
        return descripcionServicio;
    }
    
    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }
    
    public String getEstadoServicio() {
        return estadoServicio;
    }
    
    public void setEstadoServicio(String estadoServicio) {
        this.estadoServicio = estadoServicio;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
    
    @Override
    public String toString() {
        return "Asignacion{" +
                "id_asignacion=" + id_asignacion +
                ", id_tecnico=" + id_tecnico +
                ", id_servicio=" + id_servicio +
                ", nombreTecnico='" + nombreTecnico + '\'' +
                '}';
    }
}