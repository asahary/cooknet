package com.asahary.foodnet.POJO;

/**
 * Created by Saha on 08/06/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Evento{

    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("idUser")
    @Expose
    private Integer idUser;
    @SerializedName("nick")
    @Expose
    private String nick;
    @SerializedName("imagenUser")
    @Expose
    private String imagenUser;
    @SerializedName("idReceta")
    @Expose
    private Integer idReceta;
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

    public static Comparator<Evento> FechaComparator = new Comparator<Evento>() {

        @Override
        public int compare(Evento evento, Evento t1) {
            if (esProximoDia(evento.fecha,t1.fecha)){
                return -1;
            }else {
                return 1;
            }
        }
    };

    public static boolean esProximoDia(String fecha,String fecha2) {
        String fActual[], fVis[];
        //Separamos y guardamos en un array el dia, el mes y el año
        fActual = fecha2.split("-");
        fVis = fecha.split("-");
        //Comparamos primero los años, luego los meses, luego los dias y luego la hora
        if (Integer.parseInt(fVis[2].substring(0,1)) > Integer.parseInt(fActual[2].substring(0,1))) {
            return true;
        } else if (Integer.parseInt(fVis[2].substring(0,1)) == Integer.parseInt(fActual[2].substring(0,1))) {
            if (Integer.parseInt(fVis[1]) > Integer.parseInt(fActual[1])) {
                return true;
            } else if (Integer.parseInt(fVis[1]) == Integer.parseInt(fActual[1])) {
                if (Integer.parseInt(fVis[0]) > Integer.parseInt(fActual[0])) {
                    return true;
                } else if (Integer.parseInt(fVis[0]) == Integer.parseInt(fActual[0])) {
                    if (esProximaHora(fActual[2],fVis[2])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Retorna true si la hora pasada por parametro es mayor a la hora actual
    public static boolean esProximaHora(String fecha,String fecha2) {
        String fechaArray[], fecha2Array[];
        fechaArray = fecha2.split(":");
        fecha2Array = fecha.split(":");
        if (Integer.parseInt(fecha2Array[0].substring(3)) > Integer.parseInt(fechaArray[0].substring(3))) {
            return true;
        } else if (Integer.parseInt(fecha2Array[0].substring(3)) == Integer.parseInt(fechaArray[0].substring(3))) {
            if (Integer.parseInt(fecha2Array[1]) > Integer.parseInt(fechaArray[1])) {
                return true;
            }
        }
        return false;
    }
}
