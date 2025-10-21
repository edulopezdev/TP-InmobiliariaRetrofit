package com.example.inmobiliaria.models;

import java.io.Serializable;

public class Inmueble implements Serializable {
    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private int superficie;
    private double latitud;
    private double valor;
    private String imagen;
    private boolean disponible;
    private double longitud;
    private int idPropietario;
    private Propietario propietario;

    // Constructor vacío
    public Inmueble() {
    }

    public Inmueble(int idInmueble, String direccion, String uso, String tipo, int ambientes, int superficie, double latitud, double valor, String imagen, boolean disponible, double longitud, int idPropietario, Propietario propietario) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.uso = uso;
        this.tipo = tipo;
        this.ambientes = ambientes;
        this.superficie = superficie;
        this.latitud = latitud;
        this.valor = valor;
        this.imagen = imagen;
        this.disponible = disponible;
        this.longitud = longitud;
        this.idPropietario = idPropietario;
        this.propietario = propietario;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }
}
