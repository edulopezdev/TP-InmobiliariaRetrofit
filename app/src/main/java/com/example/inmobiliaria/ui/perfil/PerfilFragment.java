package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        perfilViewModel =new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        perfilViewModel.getMutableEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enEdicion) {
                binding.etNombre.setEnabled(enEdicion);
                binding.etApellido.setEnabled(enEdicion);
                binding.etDni.setEnabled(enEdicion);
                binding.etTelefono.setEnabled(enEdicion);
                binding.etEmail.setEnabled(enEdicion);

                // Aplicar estilos diferentes según el modo
                aplicarEstilos(enEdicion);
            }
        });

        perfilViewModel.getMutableTextoBoton().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btPerfil.setText(s);
            }
        });

        binding.btPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilViewModel.cambiarBoton(binding.btPerfil.getText().toString(),
                        binding.etNombre.getText().toString(),
                        binding.etApellido.getText().toString(),
                        binding.etDni.getText().toString(),
                        binding.etTelefono.getText().toString(),
                        binding.etEmail.getText().toString());
            }
        });

        //mutable del propietario
        perfilViewModel.getMutablePropietario().observe(getViewLifecycleOwner(), new Observer<com.example.inmobiliaria.models.Propietario>() {

            @Override
            public void onChanged(com.example.inmobiliaria.models.Propietario propietario) {
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(propietario.getDni());
                binding.etTelefono.setText(propietario.getTelefono());
                binding.etEmail.setText(propietario.getEmail());
            }
        });

        perfilViewModel.getPerfil();

        // Aplicar el estilo inicial (modo lectura)
        aplicarEstilos(false);

        return root;
    }

    /**
     * Método para aplicar los estilos visuales dependiendo del modo (edición o solo lectura)
     *
     * @param enEdicion true si los campos están en modo edición, false si están en modo solo lectura
     */
    private void aplicarEstilos(boolean enEdicion) {
        int estilo = enEdicion ? R.style.EditableTextStyle : R.style.ReadOnlyEditTextStyle;

        // Aplicar estilo a cada campo
        binding.etNombre.setTextAppearance(estilo);
        binding.etApellido.setTextAppearance(estilo);
        binding.etDni.setTextAppearance(estilo);
        binding.etTelefono.setTextAppearance(estilo);
        binding.etEmail.setTextAppearance(estilo);

        // Ajustar el color del texto manualmente para asegurar el contraste apropiado
        if (enEdicion) {
            // En modo edición: texto negro oscuro
            binding.etNombre.setTextColor(getResources().getColor(android.R.color.black));
            binding.etApellido.setTextColor(getResources().getColor(android.R.color.black));
            binding.etDni.setTextColor(getResources().getColor(android.R.color.black));
            binding.etTelefono.setTextColor(getResources().getColor(android.R.color.black));
            binding.etEmail.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            // En modo solo lectura: texto gris claro
            int colorGrisClaro = getResources().getColor(android.R.color.darker_gray);
            binding.etNombre.setTextColor(colorGrisClaro);
            binding.etApellido.setTextColor(colorGrisClaro);
            binding.etDni.setTextColor(colorGrisClaro);
            binding.etTelefono.setTextColor(colorGrisClaro);
            binding.etEmail.setTextColor(colorGrisClaro);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}