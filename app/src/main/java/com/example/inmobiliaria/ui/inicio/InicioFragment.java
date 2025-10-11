package com.example.inmobiliaria.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInicioBinding;
import com.google.android.gms.maps.SupportMapFragment;

public class InicioFragment extends Fragment {

    // Variable para manejar el binding con la UI (fragment_inicio.xml)
    private FragmentInicioBinding binding;

    // Este método crea y devuelve la vista del fragmento
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos la vista usando View Binding (conecta el layout con el código)
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Crear o recuperar el ViewModel asociado a este fragmento
        InicioViewModel inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        // Observar el LiveData que contiene el callback para el mapa
        // getViewLifecycleOwner asegura que la observación respete el ciclo de vida del fragmento
        inicioViewModel.getMapaActualMutableLiveData().observe(getViewLifecycleOwner(), new Observer<InicioViewModel.MapaActual>() {
            @Override
            public void onChanged(InicioViewModel.MapaActual mapaActual) {

                // Buscar el fragmento del mapa dentro de este fragmento (mapa anidado)
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

                if (mapFragment != null) {
                    // Pasar el callback para que el mapa se inicialice cuando esté listo
                    // mapaActual debe implementar OnMapReadyCallback para funcionar
                    mapFragment.getMapAsync(mapaActual);
                }
            }
        });

        // Iniciar la carga o configuración del mapa en el ViewModel
        inicioViewModel.cargarMapa();

        // Devolver la vista inflada para que Android la muestre
        return root;
    }

}
