package com.example.inmobiliaria.ui.inquilino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.inmobiliaria.databinding.FragmentInquilinoBinding;
import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.ui.inmueble.InmuebleAdapter;

import java.util.List;

public class InquilinoFragment extends Fragment {

    private InquilinoViewModel mViewModel;
    private FragmentInquilinoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InquilinoViewModel.class);

        mViewModel.getListaAlquilados().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getContext(), getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.listaInquilinos.setLayoutManager(glm);
                binding.listaInquilinos.setAdapter(adapter);
            }
        });

        mViewModel.obtenerInmueblesAlquilados();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
