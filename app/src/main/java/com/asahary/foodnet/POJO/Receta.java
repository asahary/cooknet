package com.asahary.foodnet.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.asahary.foodnet.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

/**
 * Created by Saha on 22/05/2017.
 */

public class Receta implements Parcelable{

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

    public Receta(Parcel in) {
        idUsuario=in.readString();
        idReceta=in.readString();
        nombre=in.readString();
        descripcion=in.readString();
        ingredientes=in.readString();
        preparacion=in.readString();
        imagen=in.readString();
        fechaAlta=in.readString();
        bajaUsuario=in.readString();
        bajaReceta=in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idUsuario);
        parcel.writeString(idReceta);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(ingredientes);
        parcel.writeString(preparacion);
        parcel.writeString(imagen);
        parcel.writeString(fechaAlta);
        parcel.writeString(bajaUsuario);
        parcel.writeString(bajaReceta);
    }

    public static final Creator<Receta> CREATOR = new Creator<Receta>() {
        @Override
        public Receta createFromParcel(Parcel in) {
            return new Receta(in);
        }

        @Override
        public Receta[] newArray(int size) {
            return new Receta[size];
        }
    };
}
