package com.example.inmobiliaria.ui.contrato.detalle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentDetalleContratoBinding;

public class DetalleContratoFragment extends Fragment {

    private FragmentDetalleContratoBinding binding;
    private DetalleContratoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetalleContratoViewModel.class);

        // aca lo q hacemos es inicializar el ViewModel con los argumentos
        viewModel.inicializarDesdeArgumentos(getArguments());

        // luego observamos el LiveData del contrato
        viewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            binding.etCodigoContrato.setText(String.valueOf(contrato.getIdContrato()));
            binding.etEstadoContrato.setText(contrato.isEstado() ? "Vigente" : "Finalizado");
            binding.etFechaInicio.setText(contrato.getFechaInicio());
            binding.etFechaFin.setText(contrato.getFechaFinalizacion());
            binding.etMonto.setText("$ " + contrato.getMontoAlquiler());
            binding.etInquilino.setText(
                    contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido()
            );
            binding.etDireccionInmueble.setText(contrato.getInmueble().getDireccion());
        });

        // aca observamos el LiveData de navegación
        viewModel.getNavegarAPagos().observe(getViewLifecycleOwner(), args -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detalleContratoFragment_to_pagoFragment, args);
        });

        // aca lo que estamos haciendo es setear el listener del boton para ir a pagos
        binding.btnPagos.setOnClickListener(v -> viewModel.irAPagos());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
