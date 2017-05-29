package com.asahary.foodnet.Principal.Busqueda;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Agregar.AgregarAdapter;
import com.asahary.foodnet.Principal.Favoritos.FavoritosAdapter;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Saha on 25/05/2017.
 */

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.Contenedor> {
    ArrayList<Usuario> usuarios =new ArrayList<>();


    public interface OnReciclerItemClickListener{
        void itemClic(Usuario usuario);
    }


    UsuariosAdapter.OnReciclerItemClickListener listener;

    public UsuariosAdapter(ArrayList<Usuario> usuarios, UsuariosAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.usuarios =usuarios;

    }


    public void swapDatos(ArrayList<Usuario> usuarios){
        this.usuarios =usuarios;
        this.notifyDataSetChanged();
    }

    @Override
    public UsuariosAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario,parent,false);

        final UsuariosAdapter.Contenedor tvh = new UsuariosAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClic(usuarios.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(UsuariosAdapter.Contenedor holder, final int position) {
        holder.onBin(usuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }


    public class Contenedor extends RecyclerView.ViewHolder{

        TextView txtNick,txtNombre,txtApellidos;
        ImageView imgUsuario;

        public Contenedor(View itemView) {
            super(itemView);
            imgUsuario= (ImageView) itemView.findViewById(R.id.imgUsuario);
            txtNick = (TextView) itemView.findViewById(R.id.lblNickUsuario);
            txtNombre= (TextView) itemView.findViewById(R.id.lblNombreUsuario);
            txtApellidos= (TextView) itemView.findViewById(R.id.lblApellidosUsuario);

        }

        public void onBin(Usuario usuario){


            txtNick.setText(usuario.getNick());
            txtNombre.setText(usuario.getNombre());
            txtApellidos.setText(usuario.getApellidos());
            Picasso.with(imgUsuario.getContext()).load("https://image.flaticon.com/icons/png/512/78/78373.png").fit().into(imgUsuario);

        }


    }
}
