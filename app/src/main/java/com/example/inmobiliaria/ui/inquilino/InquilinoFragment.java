package com.example.inmobiliaria.ui.inquilino;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;

public class InquilinoFragment extends Fragment {

    // Variable para guardar la instancia del ViewModel
    private InquilinoViewModel mViewModel;

    // Método estático para crear una nueva instancia de este fragmento
    public static InquilinoFragment newInstance() {
        return new InquilinoFragment();
    }

    // Se llama para crear la vista (layout) del fragmento
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout fragment_inquilino.xml para mostrar la UI
        return inflater.inflate(R.layout.fragment_inquilino, container, false);
    }

    // Se llama cuando la actividad que contiene el fragmento ha sido creada
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inicializar el ViewModel para manejar la lógica y datos de este fragmento
        mViewModel = new ViewModelProvider(this).get(InquilinoViewModel.class);

        // TODO: Usar el ViewModel para manejar la UI y datos
    }

}
