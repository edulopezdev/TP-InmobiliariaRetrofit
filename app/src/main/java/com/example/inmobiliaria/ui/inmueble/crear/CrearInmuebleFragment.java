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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        //  return inflater.inflate(R.layout.fragment_crear_inmueble, container, false);
        binding = FragmentCrearInmuebleBinding.inflate(inflater, container, false);

        mv = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);
        // TODO: Use the ViewModel
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
        mv.getUriMutable().observe(getViewLifecycleOwner(), new Observer<Uri>() {;
            @Override
            public void onChanged(Uri uri) {
                binding.ivPrevia.setImageURI(uri);
            }
        });

        return binding.getRoot();
    }



    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mv.recibirFoto(result);
            }
        });
        binding.btnCargarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarInmueble();
            }
        });
    }
    private void agregarInmueble() {
        // Implementar la lógica para agregar el inmueble
        String direccion = binding.etDireccion.getText().toString();
        String uso = binding.etUso.getText().toString();
        String tipo = binding.etTipo.getText().toString();
        String precio = binding.etValor.getText().toString();
        String ambientes = binding.etAmbientes.getText().toString();
        String superficie = binding.etSuperficie.getText().toString();
        double latitud = 0;
        double longitud = 0;
        boolean disponible = binding.cbDisponible.isChecked();

        mv.guardarInmueble(direccion, uso, tipo, precio, ambientes, superficie, String.valueOf(latitud), String.valueOf(longitud), disponible);

    }

}
