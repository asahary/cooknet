package com.asahary.foodnet.POJO;

/**
 * Created by Saha on 08/06/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Evento {

    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("idUser")
    @Expose
    private int idUser;
    @SerializedName("nick")
    @Expose
    private String nick;
    @SerializedName("imagenUser")
    @Expose
    private String imagenUser;
    @SerializedName("idReceta")
    @Expose
    private int idReceta;
    @SerializedName("nombreReceta")
    @Expose
    private String nombreReceta;
    @SerializedName("fecha")
    @Expose
    private String fecha;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getImagenUser() {
        return imagenUser;
    }

    public void setImagenUser(String imagenUser) {
        this.imagenUser = imagenUser;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
