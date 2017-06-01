package com.asahary.foodnet.Principal.Agregar;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.R;

import java.util.ArrayList;

/**
 * Created by Saha on 23/05/2017.
 */

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.Contenedor> implements View.OnClickListener{
    ArrayList<Ingrediente> ingredientes =new ArrayList<>();

    @Override
    public void onClick(View view) {

    }

    public interface OnReciclerItemClickListener{
        void itemClic(Ingrediente receta);
    }


    IngredienteAdapter.OnReciclerItemClickListener listener;

    public IngredienteAdapter(ArrayList<Ingrediente> ingredientes, IngredienteAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.ingredientes =ingredientes;

    }
    public IngredienteAdapter(ArrayList<Ingrediente> ingredientes){
        super();
        this.ingredientes =ingredientes;

    }

    public void swapDatos(ArrayList<Ingrediente> recetas){
        this.ingredientes =recetas;
        this.notifyDataSetChanged();
    }

    @Override
    public IngredienteAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingrediente,parent,false);

        final IngredienteAdapter.Contenedor tvh = new IngredienteAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.itemClic(ingredientes.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(IngredienteAdapter.Contenedor holder, final int position) {
        holder.onBin(ingredientes.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }


    public class Contenedor extends RecyclerView.ViewHolder{

        TextView txtNombre,txtCant;
        Spinner sp;
        ImageButton btnEliminar;

        public Contenedor(View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtCant= (TextView) itemView.findViewById(R.id.txtCant);
            sp= (Spinner) itemView.findViewById(R.id.medidas);
            btnEliminar= (ImageButton) itemView.findViewById(R.id.btnQuitarr);

        }

        public void onBin(final Ingrediente ingrediente){
            //Obtenemos la posicion del ingrediente en el array
            final int position=IngredienteAdapter.this.ingredientes.indexOf(ingrediente);


            txtNombre.setText(ingrediente.nombre);
            txtCant.setText(String.valueOf(ingrediente.cantidad));
            sp.setSelection(ingrediente.medida);


            //Cada vez que uno de los campos pierde el foco hacemos que guarde lo que tenia en el campo
            //Porque si se actualiza la pantalla se pierde la informacion
            txtCant.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(txtCant.getText().toString().isEmpty()){
                        ingredientes.get(position).cantidad=0.0;
                    }else{
                        ingredientes.get(position).cantidad=Double.parseDouble(txtCant.getText().toString());
                    }
                }
            });

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ingredientes.get(position).medida=sp.getSelectedItemPosition();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            txtNombre.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    ingredientes.get(position).nombre=txtNombre.getText().toString();
                }
            });

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=IngredienteAdapter.this.ingredientes.indexOf(ingrediente);
                    ingredientes.remove(position);
                    IngredienteAdapter.this.notifyDataSetChanged();
                }
            });
        }


    }
}
