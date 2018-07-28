package com.brijir.averias.bd;

import java.util.Date;

public class Fault {

    private String nombre;
    private String descripcion;
    private String type;
    private Location location;
    private Date date;
    private User user;
    private String image;

    public Fault(){}

    public Fault(String nombre, String descripcion, String type, Location location, Date date, User user, String image) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.type = type;
        this.location = location;
        this.date = date;
        this.user = user;
        this.image = image;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
