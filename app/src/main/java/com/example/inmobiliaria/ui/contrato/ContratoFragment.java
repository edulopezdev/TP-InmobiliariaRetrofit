package com.example.inmobiliaria.ui.contrato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.navigation.Navigation;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentContratoBinding;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.ui.inmueble.InmuebleAdapter;

import java.util.List;

public class ContratoFragment extends Fragment {

    private FragmentContratoBinding binding;
    private ContratoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ContratoViewModel.class);
        binding = FragmentContratoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm.getListaContratos().observe(getViewLifecycleOwner(), (List<Inmueble> inmuebles) -> {
            InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getLayoutInflater(), inmueble -> {
                Bundle bundle = new Bundle();
                // Enviar el id que espera DetalleContratoViewModel
                bundle.putInt("idInmueble", inmueble.getIdInmueble());
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.detalleContratoFragment, bundle);
            });
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            binding.listaContratos.setLayoutManager(glm);
            binding.listaContratos.setAdapter(adapter);
        });

        vm.obtenerContratos();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}