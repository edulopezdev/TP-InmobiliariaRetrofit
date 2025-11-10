package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        vm.getPropietario().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario == null) return;
            binding.etNombre.setText(propietario.getNombre());
            binding.etApellido.setText(propietario.getApellido());
            binding.etDni.setText(propietario.getDni());
            binding.etTelefono.setText(propietario.getTelefono());
            binding.etEmail.setText(propietario.getEmail());
        });

        vm.getTextoBoton().observe(getViewLifecycleOwner(), binding.btPerfil::setText);
        vm.getEditable().observe(getViewLifecycleOwner(), editable -> vm.aplicarEstilos(binding, editable));
        vm.getMensaje().observe(getViewLifecycleOwner(), mensaje -> vm.mostrarToast(requireContext(), mensaje));

        binding.btPerfil.setOnClickListener(v -> vm.onBotonPerfilClick(
                binding.etNombre.getText().toString(),
                binding.etApellido.getText().toString(),
                binding.etDni.getText().toString(),
                binding.etTelefono.getText().toString(),
                binding.etEmail.getText().toString()
        ));

        binding.btnCambiarPassword.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(com.example.inmobiliaria.R.id.action_nav_perfil_to_cambiarContrasenaFragment)
        );

        vm.cargarPerfil();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
