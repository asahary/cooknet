package com.asahary.foodnet.POJO;

/**
 * Created by Saha on 23/05/2017.
 */

public class Ingrediente {

    public int medida;
    public double cantidad;
    public String nombre;

    public Ingrediente(double cantidad,int medida,String nombre){
        this.cantidad=cantidad;
        this.medida=medida;
        this.nombre=nombre;
    }

    public Ingrediente(){
        this.cantidad=0;
        this.medida=0;
        this.nombre="";
    }
}
