package com.example.inmobiliaria.ui.inquilino.detalle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentDetalleInquiniloBinding;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.models.Inquilino;

public class DetalleInquiniloFragment extends Fragment {

    private DetalleInquiniloViewModel mViewModel;
    private FragmentDetalleInquiniloBinding binding;
    private Inmueble inmueble;

    public static DetalleInquiniloFragment newInstance() {
        return new DetalleInquiniloFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DetalleInquiniloViewModel.class);
        binding = FragmentDetalleInquiniloBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mViewModel.getInquilino().observe(getViewLifecycleOwner(), inq -> {
            if (inq != null) {
                binding.etIdInquilino.setText(String.valueOf(inq.getIdInquilino()));
                binding.etNombreInquilino.setText(inq.getNombre());
                binding.etApellidoInquilino.setText(inq.getApellido());
                binding.etDniInquilino.setText(inq.getDni());
                binding.etTelefonoInquilino.setText(inq.getTelefono());
                binding.etEmailInquilino.setText(inq.getEmail());
            }
        });

        if (getArguments() != null && getArguments().containsKey("idInmueble")) {
            int idInmueble = getArguments().getInt("idInmueble");
            mViewModel.cargarInquilino(idInmueble);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}