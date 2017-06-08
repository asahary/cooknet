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
    private String idUser;
    @SerializedName("nick")
    @Expose
    private String nick;
    @SerializedName("idReceta")
    @Expose
    private Object idReceta;
    @SerializedName("nombreReceta")
    @Expose
    private Object nombreReceta;
    @SerializedName("fecha")
    @Expose
    private String fecha;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Object getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Object idReceta) {
        this.idReceta = idReceta;
    }

    public Object getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(Object nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
