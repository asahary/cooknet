package com.asahary.foodnet.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saha on 22/05/2017.
 */

public class Receta {

    @SerializedName("idReceta")
    @Expose
    private String idReceta;
    @SerializedName("idUsuario")
    @Expose
    private String idUsuario;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("fechaAlta")
    @Expose
    private String fechaAlta;
    @SerializedName("bajaUsuario")
    @Expose
    private String bajaUsuario;
    @SerializedName("bajaReceta")
    @Expose
    private String bajaReceta;
    @SerializedName("preparacion")
    @Expose
    private String preparacion;
    @SerializedName("ingredientes")
    @Expose
    private String ingredientes;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("imagen")
    @Expose
    private String imagen;
    @SerializedName("categoria")
    @Expose
    private String categoria;

    public String getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getBajaUsuario() {
        return bajaUsuario;
    }

    public void setBajaUsuario(String bajaUsuario) {
        this.bajaUsuario = bajaUsuario;
    }

    public String getBajaReceta() {
        return bajaReceta;
    }

    public void setBajaReceta(String bajaReceta) {
        this.bajaReceta = bajaReceta;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
