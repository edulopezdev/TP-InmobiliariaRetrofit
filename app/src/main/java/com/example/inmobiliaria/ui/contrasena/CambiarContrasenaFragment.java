package com.example.inmobiliaria.ui.contrasena;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentCambiarContrasenaBinding;

public class CambiarContrasenaFragment extends Fragment {

    private CambiarContrasenaViewModel vm;
    private FragmentCambiarContrasenaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCambiarContrasenaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CambiarContrasenaViewModel.class);

        // el boton delega la logica al ViewModel
        binding.btnCambiar.setOnClickListener(v -> vm.onCambiarClicked(
                binding.etActual.getText().toString(),
                binding.etNueva.getText().toString()
        ));

        // estos observadores se encargan de actualizar la UI según lo que pase en el ViewModel
        vm.getError().observe(getViewLifecycleOwner(), err -> binding.tvErrorCambiar.setText(err != null ? err : ""));
        vm.getExito().observe(getViewLifecycleOwner(), ok -> vm.manejarExito(requireContext(), this, ok));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
