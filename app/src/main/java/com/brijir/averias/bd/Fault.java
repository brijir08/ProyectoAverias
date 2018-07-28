package com.brijir.averias.bd;

public class Fault {

    private String nombre;
    private String descripcion;
    private String tipo;
    private Ubicacion ubicacion;
    private String fecha;
    private Usuario usuario;
    private String imagen;

    public Fault(){}

    public Fault(String nombre, String descripcion, String tipo, Ubicacion ubicacion, String fecha, Usuario usuario, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.usuario = usuario;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
