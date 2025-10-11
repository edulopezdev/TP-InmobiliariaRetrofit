package com.example.inmobiliaria.ui.inmueble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentInmuebleBinding;

public class InmuebleFragment extends Fragment {

    // Variable para manejar el binding con la UI (fragment_inmueble.xml)
    private FragmentInmuebleBinding binding;

    // Método que crea y devuelve la vista del fragmento
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Crear o recuperar la instancia del ViewModel para este fragmento
        InmuebleViewModel inmuebleViewModel = new ViewModelProvider(this).get(InmuebleViewModel.class);

        // Inflar la vista usando View Binding
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener referencia al TextView del layout (usando binding)
        final TextView textView = binding.textSlideshow;

        // Observar el LiveData<String> del ViewModel y actualizar el TextView cuando cambie
        inmuebleViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Retornar la vista inflada para que Android la muestre
        return root;
    }

    // Método llamado cuando la vista del fragmento se destruye
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar el binding para evitar fugas de memoria
        binding = null;
    }
}
