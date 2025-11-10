package com.example.inmobiliaria.ui.inmueble;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolderInmueble> {

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    private List<Inmueble> listaInmuebles;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public InmuebleAdapter(List<Inmueble> inmuebles, LayoutInflater inflater, OnItemClickListener listener) {
        this.listaInmuebles = inmuebles;
        this.inflater = inflater;
        this.listener = listener;
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

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
        String valorFormateado = nf.format(inmuebleActual.getValor());
        holder.precio.setText("$ " + valorFormateado);

        Picasso.get()
                .load(ApiClient.BASE_URL + inmuebleActual.getImagen())
                .placeholder(R.drawable.loading)
                .error(R.drawable.house)
                .into(holder.foto);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(inmuebleActual);
        });
    }

    @Override
    public int getItemCount() {
        return listaInmuebles != null ? listaInmuebles.size() : 0;
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
