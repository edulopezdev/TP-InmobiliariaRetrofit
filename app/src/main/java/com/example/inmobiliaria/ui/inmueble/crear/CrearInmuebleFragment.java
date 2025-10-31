package com.example.inmobiliaria.ui.inmueble.crear;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inmobiliaria.databinding.FragmentCrearInmuebleBinding;

public class CrearInmuebleFragment extends Fragment {

    private CrearInmuebleViewModel mv;
    private FragmentCrearInmuebleBinding binding;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;

    public static CrearInmuebleFragment newInstance() {
        return new CrearInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearInmuebleBinding.inflate(inflater, container, false);

        mv = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);

        abrirGaleria();

        binding.btnCargarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarInmueble();
            }
        });

        mv.getUriMutable().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivPrevia.setImageURI(uri);
            }
        });

        // Observa errores del ViewModel y los muestra como Toast
        mv.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null && !err.isEmpty()) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        // Observa el éxito y navega a la lista de inmuebles
        mv.getExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Inmueble creado correctamente", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mv.recibirFoto(result);
            }
        });
    }

    private void agregarInmueble() {
        // Obtiene los valores de los campos
        String direccion = binding.etDireccion.getText().toString().trim();
        String uso = binding.etUso.getText().toString().trim();
        String tipo = binding.etTipo.getText().toString().trim();
        String precio = binding.etValor.getText().toString().trim();
        String ambientes = binding.etAmbientes.getText().toString().trim();
        String superficie = binding.etSuperficie.getText().toString().trim();
        String latitud = binding.etLatitud.getText().toString().trim();
        String longitud = binding.etLongitud.getText().toString().trim();
        boolean disponible = binding.cbDisponible.isChecked();

        // Llama al ViewModel para validar y guardar
        mv.guardarInmueble(direccion, uso, tipo, precio, ambientes, superficie, latitud, longitud, disponible);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}