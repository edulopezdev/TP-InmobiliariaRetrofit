package com.example.inmobiliaria.ui.inmueble;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolderInmueble> {

    private List<Inmueble> listaInmuebles;
    private Context context;
    private LayoutInflater inflater;
    private int destinoNav;

    // Constructor que recibe la lista, el contexto, el inflater y el destino de navegación
    public InmuebleAdapter(List<Inmueble> inmuebles, Context context, LayoutInflater inflater, int destinoNav) {
        this.listaInmuebles = inmuebles;
        this.context = context;
        this.inflater = inflater;
        this.destinoNav = destinoNav;
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

        // Formatea el valor en formato argentino (punto miles, coma decimales)
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
        String valorFormateado = nf.format(inmuebleActual.getValor());
        holder.precio.setText("$ " + valorFormateado);

        Picasso.get()
                .load(ApiClient.BASE_URL + inmuebleActual.getImagen())
                .placeholder(R.drawable.loading)
                .error(R.drawable.house)
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (destinoNav == R.id.detalleInmueble) {
                    // Para detalle de inmueble, pasa el objeto completo
                    bundle.putSerializable("inmueble", inmuebleActual);
                } else if (destinoNav == R.id.detalleInquiniloFragment || destinoNav == R.id.detalleContratoFragment) {
                    // Para detalle de inquilino o contrato, pasa el id del inmueble
                    bundle.putInt("idInmueble", inmuebleActual.getIdInmueble());
                }
                Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main)
                        .navigate(destinoNav, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    // ViewHolder para los elementos de la lista
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