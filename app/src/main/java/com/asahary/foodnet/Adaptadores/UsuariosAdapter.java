package com.asahary.foodnet.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Busqueda.RecetasFragment;
import com.asahary.foodnet.Principal.Busqueda.UsuariosFragment;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.Principal.Usuario.UsuariosTab;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saha on 25/05/2017.
 */

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.Contenedor> {
    ArrayList<Usuario> usuarios =new ArrayList<>();
    UsuariosAdapter.OnReciclerItemClickListener listener;

    public interface OnReciclerItemClickListener{
        void itemClic(Usuario usuario);
    }




    public UsuariosAdapter(ArrayList<Usuario> usuarios, UsuariosAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.usuarios =usuarios;
    }


    public void swapDatos(ArrayList<Usuario> usuarios){
        this.usuarios =usuarios;
        if(listener instanceof UsuariosTab){
            ((UsuariosTab)listener).checkVacio();
        }else if(listener instanceof UsuariosFragment){
            ((UsuariosFragment)listener).checkVacio();
        }
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
        CircleImageView imgUsuario;

        public Contenedor(View itemView) {
            super(itemView);
            imgUsuario= (CircleImageView) itemView.findViewById(R.id.imgUsuario);
            txtNick = (TextView) itemView.findViewById(R.id.lblNickUsuario);
            txtNombre= (TextView) itemView.findViewById(R.id.lblNombreUsuario);
            txtApellidos= (TextView) itemView.findViewById(R.id.lblApellidosUsuario);

        }

        public void onBin(Usuario usuario){
            txtNick.setText(usuario.getNick());
            txtNombre.setText(usuario.getNombre());
            txtApellidos.setText(usuario.getApellidos());
            Picasso.with(imgUsuario.getContext()).load(usuario.getImagen()).error(R.drawable.user_generic).placeholder(R.drawable.user_generic).fit().into(imgUsuario);

        }


    }
}
