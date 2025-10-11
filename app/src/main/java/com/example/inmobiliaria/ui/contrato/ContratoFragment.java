package com.example.inmobiliaria.ui.contrato;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;

public class ContratoFragment extends Fragment {

    private ContratoViewModel mViewModel; // ViewModel para manejar datos y lógica de negocio

    // Método para crear una nueva instancia del fragmento
    public static ContratoFragment newInstance() {
        return new ContratoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout XML asociado a este fragmento
        return inflater.inflate(R.layout.fragment_contrato, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtener instancia del ViewModel para usarlo en este fragmento
        mViewModel = new ViewModelProvider(this).get(ContratoViewModel.class);
        // TODO: Usar el ViewModel para cargar datos o manejar eventos
    }

}
