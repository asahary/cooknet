package com.asahary.foodnet.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saha on 29/05/2017.
 */

public class Comentario implements Parcelable{

    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("imagen")
    @Expose
    private String imagen;
    @SerializedName("idUsuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("idCreador")
    @Expose
    private Integer idCreador;
    @SerializedName("idReceta")
    @Expose
    private Integer idReceta;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("comentario")
    @Expose
    private String comentario;



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(Integer idCreador) {
        this.idCreador = idCreador;
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(imagen);
        parcel.writeInt(idUsuario);
        parcel.writeInt(idCreador);
        parcel.writeInt(idReceta);
        parcel.writeString(fecha);
        parcel.writeString(comentario);
    }

    protected Comentario(Parcel in) {
        nombre = in.readString();
        imagen = in.readString();
        idUsuario = in.readInt();
        idCreador = in.readInt();
        idReceta = in.readInt();
        fecha = in.readString();
        comentario = in.readString();
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };
}