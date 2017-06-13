package com.asahary.foodnet.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by omniumlab on 15/05/2017.
 */




public class Usuario implements Parcelable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("nombre")
        @Expose
        private String nombre;
        @SerializedName("apellidos")
        @Expose
        private String apellidos;
        @SerializedName("pass")
        @Expose
        private String pass;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("baja")
        @Expose
        private Integer baja;
        @SerializedName("admin")
        @Expose
        private Integer admin;
        @SerializedName("nick")
        @Expose
        private String nick;
        @SerializedName("imagen")
        @Expose
        private String imagen;

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getBaja() {
            return baja;
        }

        public void setBaja(Integer baja) {
            this.baja = baja;
        }

        public Integer getAdmin() {
            return admin;
        }

        public void setAdmin(Integer admin) {
            this.admin = admin;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }
    public Usuario(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        apellidos = in.readString();
        pass=in.readString();
        email = in.readString();
        baja = in.readInt();
        admin=in.readInt();
        nick = in.readString();
        imagen = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeString(apellidos);
        parcel.writeString(pass);
        parcel.writeString(email);
        parcel.writeInt(baja);
        parcel.writeInt(admin);
        parcel.writeString(nick);
        parcel.writeString(imagen);
    }

}