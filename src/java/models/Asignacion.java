package models;

public class Asignacion {
    private int id_asignacion;
    private int id_tecnico;
    private int id_servicio;

    public Asignacion() {}

    public Asignacion(int id_asignacion, int id_tecnico, int id_servicio) {
        this.id_asignacion = id_asignacion;
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
}