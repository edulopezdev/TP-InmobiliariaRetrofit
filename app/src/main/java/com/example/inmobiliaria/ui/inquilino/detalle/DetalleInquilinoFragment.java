package com.example.inmobiliaria.ui.inquilino.detalle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentDetalleInquilinoBinding;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding binding;
    private DetalleInquilinoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        // aca vamos a observar los datos del ViewModel
        viewModel.getInquilino().observe(getViewLifecycleOwner(), inquilino -> {
            binding.etIdInquilino.setText(String.valueOf(inquilino.getIdInquilino()));
            binding.etNombreInquilino.setText(inquilino.getNombre());
            binding.etApellidoInquilino.setText(inquilino.getApellido());
            binding.etDniInquilino.setText(inquilino.getDni());
            binding.etTelefonoInquilino.setText(inquilino.getTelefono());
            binding.etEmailInquilino.setText(inquilino.getEmail());
        });

        // delegamos al ViewModel la inicialización con los argumentos
        viewModel.inicializarDesdeArgumentos(getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
