package com.asahary.foodnet.Utilidades;

import android.content.Context;
import android.widget.Toast;

import com.asahary.foodnet.CookNetService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 10/06/2017.
 */

public class Libreria {
    public static void mostrarMensjeCorto(Context context,String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show();
    }
    public static void mostrarMensjeLargo(Context context,String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }
    public static CookNetService obtenerServicioApi(){
        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        CookNetService service=retrofit.create(CookNetService.class);
        return service;
    }
}
