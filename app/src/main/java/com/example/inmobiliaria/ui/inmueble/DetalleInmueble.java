package com.example.inmobiliaria.ui.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class DetalleInmueble extends Fragment {

    private DetalleInmuebleViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;
    private Inmueble inmueble;

    public static DetalleInmueble newInstance() {
        return new DetalleInmueble();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inmueble = new Inmueble(); // Inicializar la variable inmueble
        mViewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mViewModel.getMInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmuebleRecibido) {
                inmueble = inmuebleRecibido; // Asignar el inmueble recibido a la variable de instancia
                binding.etCodigo.setText(String.valueOf(inmueble.getIdInmueble()));
                binding.etDireccion.setText(inmueble.getDireccion());
                binding.etUso.setText(inmueble.getUso());
                binding.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                // Formatear el valor como moneda argentina
                NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);
                String valorFormateado = nf.format(inmueble.getValor());
                binding.etPrecio.setText("$ " + valorFormateado);
                binding.etTipo.setText(inmueble.getTipo());
                binding.cbDisponible.setChecked(inmueble.isDisponible());
                Picasso.get()
                        .load(ApiClient.BASE_URL + inmueble.getImagen())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.house)
                        .into(binding.ivFoto);
            }
        });

        binding.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inmueble != null) { // Verificar que el inmueble no sea null
                    inmueble.setDisponible(binding.cbDisponible.isChecked());
                    mViewModel.actualizarInmueble(inmueble);
                }
            }
        });

        mViewModel.recuperarInmueble(getArguments());
        return root;
    }

}