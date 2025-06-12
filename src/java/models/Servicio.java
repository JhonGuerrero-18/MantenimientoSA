package models;

import java.util.Date;

public class Servicio {
    private int id;
    private String descripcion;
    private Date fecha;
    private int id_usuario;
    private String estado;
    
    // Constructor vacío
    public Servicio() {}
    
    // Constructor completo
    public Servicio(int id, String descripcion, Date fecha, int id_usuario, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.id_usuario = id_usuario;
        this.estado = estado;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    // MÉTODO PARA COMPATIBILIDAD CON JSP (que usa getD_servicio)
    public int getD_servicio() {
        return id;
    }
    
    public void setD_servicio(int id) {
        this.id = id;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    // Método adicional para compatibilidad con sql.Date
    public void setFecha(java.sql.Date fecha) {
        this.fecha = new Date(fecha.getTime());
    }
    
    public int getId_usuario() {
        return id_usuario;
    }
    
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", id_usuario=" + id_usuario +
                ", estado='" + estado + '\'' +
                '}';
    }
}