package com.example.inmobiliaria.ui.inmueble.crear;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentCrearInmuebleBinding;

public class CrearInmuebleFragment extends Fragment {

    private CrearInmuebleViewModel mv;
    private FragmentCrearInmuebleBinding binding;
    private ActivityResultLauncher<Intent> arl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearInmuebleBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);

        // registrar ActivityResultLauncher
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), mv::recibirFoto);

        // Observadores
        mv.getUriMutable().observe(getViewLifecycleOwner(), uri -> binding.ivPrevia.setImageURI(uri));

        mv.getError().observe(getViewLifecycleOwner(), err ->
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show()
        );

        mv.getExito().observe(getViewLifecycleOwner(), exito -> {
            if (Boolean.TRUE.equals(exito)) {
                Toast.makeText(getContext(), "Inmueble creado correctamente", Toast.LENGTH_SHORT).show();
                // desde aca nos vamos al fragment de lista de inmuebles
                NavHostFragment.findNavController(this).navigate(R.id.nav_inmueble);
            }
        });

        mv.getAbrirGaleria().observe(getViewLifecycleOwner(), abrir -> {
            if (Boolean.TRUE.equals(abrir)) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                arl.launch(intent);
                mv.resetAbrirGaleria();
            }
        });

        // Listeners que delegan al ViewModel
        binding.btnCargarImg.setOnClickListener(v -> mv.onCargarImagenClick());
        binding.btnGuardar.setOnClickListener(v -> mv.onGuardarClick(
                binding.etDireccion.getText().toString().trim(),
                binding.etUso.getText().toString().trim(),
                binding.etTipo.getText().toString().trim(),
                binding.etValor.getText().toString().trim(),
                binding.etAmbientes.getText().toString().trim(),
                binding.etSuperficie.getText().toString().trim(),
                binding.etLatitud.getText().toString().trim(),
                binding.etLongitud.getText().toString().trim(),
                binding.cbDisponible.isChecked()
        ));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}