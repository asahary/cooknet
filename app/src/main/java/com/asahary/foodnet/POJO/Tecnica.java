package com.asahary.foodnet.POJO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Saha on 13/06/2017.
 */

public class Tecnica implements Parcelable{
    public String nombre;
    public String descripcion;
    public static final Creator<Tecnica> CREATOR = new Creator<Tecnica>() {
        @Override
        public Tecnica createFromParcel(Parcel in) {
            return new Tecnica(in);
        }

        @Override
        public Tecnica[] newArray(int size) {
            return new Tecnica[size];
        }
    };

    public Tecnica(String nombre,String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }
    protected Tecnica(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
    }
}
