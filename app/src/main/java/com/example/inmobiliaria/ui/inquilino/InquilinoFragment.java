package com.example.inmobiliaria.ui.inquilino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInquilinoBinding;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.ui.inmueble.InmuebleAdapter;

import java.util.ArrayList;
import java.util.List;

public class InquilinoFragment extends Fragment {

    private FragmentInquilinoBinding binding;
    private InquilinoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InquilinoViewModel.class);
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);

        RecyclerView rv = binding.listaInquilinos;
        InmuebleAdapter initialAdapter = new InmuebleAdapter(new ArrayList<>(), getLayoutInflater(), inmueble -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idInmueble", inmueble.getIdInmueble());
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_nav_inquilino_to_detalleInquiniloFragment, bundle);
        });
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rv.setAdapter(initialAdapter);

        vm.getListaAlquilados().observe(getViewLifecycleOwner(), (List<Inmueble> inmuebles) -> {
            List<Inmueble> listaSegura = inmuebles != null ? inmuebles : new ArrayList<>();
            InmuebleAdapter adapter = new InmuebleAdapter(listaSegura, getLayoutInflater(), inmueble -> {
                Bundle bundle = new Bundle();
                bundle.putInt("idInmueble", inmueble.getIdInmueble());
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_nav_inquilino_to_detalleInquiniloFragment, bundle);
            });
            rv.setAdapter(adapter);
        });

        vm.obtenerInmueblesAlquilados();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}