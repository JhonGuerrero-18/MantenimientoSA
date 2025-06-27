package models;

import java.sql.Timestamp;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String email;
    private String contraseña;
    private Timestamp fecha_creacion;
    private Timestamp ultimo_login;
    private String rol; // NUEVO CAMPO
    
    public Usuario() {}
    
    // Getters y Setters existentes
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    
    public int getId() { return id_usuario; }
    public void setId(int id) { this.id_usuario = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    
    public String getPassword() { return contraseña; }
    public void setPassword(String password) { this.contraseña = password; }
    
    public Timestamp getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(Timestamp fecha_creacion) { this.fecha_creacion = fecha_creacion; }
    
    public Timestamp getUltimo_login() { return ultimo_login; }
    public void setUltimo_login(Timestamp ultimo_login) { this.ultimo_login = ultimo_login; }
    
    // NUEVOS MÉTODOS PARA ROL
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    // Métodos de conveniencia
    public boolean isAdmin() { return "admin".equals(rol); }
    public boolean isTecnico() { return "tecnico".equals(rol); }
    public boolean isCliente() { return "cliente".equals(rol); }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", fecha_creacion=" + fecha_creacion +
                '}';
    }
}