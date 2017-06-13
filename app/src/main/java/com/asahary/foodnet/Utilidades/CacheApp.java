package com.asahary.foodnet.Utilidades;

import com.asahary.foodnet.POJO.Evento;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Tecnica;
import com.asahary.foodnet.POJO.Usuario;

import java.util.ArrayList;

/**
 * Created by Saha on 10/06/2017.
 */

public class CacheApp {
    public static ArrayList<Receta> misRecetas=new ArrayList<>();
    public static ArrayList<Receta> misFavoritos=new ArrayList<>();

    public static ArrayList<Usuario> misSeguidores=new ArrayList<>();
    public static ArrayList<Usuario> misSiguiendo=new ArrayList<>();

    public static ArrayList<Evento> misEventos=new ArrayList<>();

    public static ArrayList<Tecnica> glosario=new ArrayList<>();

    public static Usuario user=null;
}
