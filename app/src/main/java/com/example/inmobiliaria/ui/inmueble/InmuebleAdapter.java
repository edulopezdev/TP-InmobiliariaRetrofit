package com.example.inmobiliaria.ui.inmueble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.models.Inmueble;
import com.squareup.picasso.Picasso;
import android.util.Log;


import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolderInmueble> {

    private List<Inmueble> listaInmuebles;
    private Context context;
    private  LayoutInflater inflater;

    public InmuebleAdapter(List<Inmueble> inmuebles, Context context, LayoutInflater inflater) {
        this.listaInmuebles = inmuebles;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolderInmueble onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolderInmueble(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInmueble holder, int position) {
        Inmueble inmuebleActual = listaInmuebles.get(position);
        holder.direccion.setText(inmuebleActual.getDireccion());
        holder.precio.setText(inmuebleActual.getValor() + " USD");

        Picasso.get()
                .load("https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net" + inmuebleActual.getImagen())
                .placeholder(R.drawable.loading)
                .error(R.drawable.house)
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    public static class ViewHolderInmueble extends RecyclerView.ViewHolder {
        TextView direccion, superficie, precio;
        ImageView foto;

        public ViewHolderInmueble(@NonNull View itemView) {
            super(itemView);
            direccion = itemView.findViewById(R.id.tvDireccion);
            superficie = itemView.findViewById(R.id.tvAmbientes);
            precio = itemView.findViewById(R.id.tvPrecio);
            foto = itemView.findViewById(R.id.imgInmueble);
        }
    }
}