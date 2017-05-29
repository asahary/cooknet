package com.asahary.foodnet.POJO;

/**
 * Created by Saha on 29/05/2017.
 */

public class Comentario {
    String nickUsusario;
    String texto;
    String fecha;

    public Comentario(String nickUsusario,String text,String fecha){
        this.nickUsusario=nickUsusario;
        this.texto=text;
        this.fecha=fecha;
    }
}
