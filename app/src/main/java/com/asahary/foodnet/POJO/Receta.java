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
    private Integer idReceta;
    @SerializedName("idUsuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("fechaAlta")
    @Expose
    private String fechaAlta;
    @SerializedName("bajaUsuario")
    @Expose
    private Integer bajaUsuario;
    @SerializedName("bajaReceta")
    @Expose
    private Integer bajaReceta;
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
    private Integer categoria;

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
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

    public Integer getBajaUsuario() {
        return bajaUsuario;
    }

    public void setBajaUsuario(Integer bajaUsuario) {
        this.bajaUsuario = bajaUsuario;
    }

    public Integer getBajaReceta() {
        return bajaReceta;
    }

    public void setBajaReceta(Integer bajaReceta) {
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

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }



    public Receta(Parcel in) {
        idUsuario=in.readInt();
        idReceta=in.readInt();
        nombre=in.readString();
        descripcion=in.readString();
        ingredientes=in.readString();
        preparacion=in.readString();
        imagen=in.readString();
        fechaAlta=in.readString();
        bajaUsuario=in.readInt();
        bajaReceta=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idUsuario);
        parcel.writeInt(idReceta);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(ingredientes);
        parcel.writeString(preparacion);
        parcel.writeString(imagen);
        parcel.writeString(fechaAlta);
        parcel.writeInt(bajaUsuario);
        parcel.writeInt(bajaReceta);
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
