package com.example.inmobiliaria.ui.contrato.detalle;

import android.util.Log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentDetalleContratoBinding;
import com.example.inmobiliaria.models.Contrato;

public class DetalleContratoFragment extends Fragment {

    private DetalleContratoViewModel mViewModel;
    private FragmentDetalleContratoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetalleContratoViewModel.class);

        mViewModel.getContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                Log.d("DetalleContratoFragment", "onChanged: contrato=" + contrato);
                if (contrato != null) {
                    binding.etCodigoContrato.setText(String.valueOf(contrato.getIdContrato()));
                    binding.etEstadoContrato.setText(contrato.isEstado() ? "Vigente" : "Finalizado");
                    binding.etFechaInicio.setText(contrato.getFechaInicio());
                    binding.etFechaFin.setText(contrato.getFechaFinalizacion());
                    binding.etMonto.setText("$ " + contrato.getMontoAlquiler());
                    if (contrato.getInquilino() != null) {
                        binding.etInquilino.setText(contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
                    }
                    if (contrato.getInmueble() != null) {
                        binding.etDireccionInmueble.setText(contrato.getInmueble().getDireccion());
                    }
                }
            }
        });

        if (getArguments() != null && getArguments().containsKey("idInmueble")) {
            int idInmueble = getArguments().getInt("idInmueble");
            Log.d("DetalleContratoFragment", "Recibido idInmueble: " + idInmueble);
            mViewModel.cargarContrato(idInmueble);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}