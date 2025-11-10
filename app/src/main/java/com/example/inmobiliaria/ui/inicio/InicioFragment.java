// java
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioFragment extends Fragment {

    // Variable para manejar el binding con la UI (fragment_inicio.xml)
    private FragmentInicioBinding binding;

    // Instancia del GoogleMap y configuración recibida desde el ViewModel
    private GoogleMap mGoogleMap;
    private InicioViewModel.MapConfig mMapConfig;

    // Este método crea y devuelve la vista del fragmento
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos la vista usando View Binding (conecta el layout con el código)
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Crear o recuperar el ViewModel asociado a este fragmento
        InicioViewModel inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        // Obtener el fragmento del mapa dentro de este fragmento (mapa anidado)
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            // Registrar callback que guarda la instancia de GoogleMap y aplica configuración si ya está disponible
            mapFragment.getMapAsync(googleMap -> {
                mGoogleMap = googleMap;
                aplicarConfigSiLista();
            });
        }

        // Observar el LiveData que contiene la configuración del mapa
        // getViewLifecycleOwner asegura que la observación respete el ciclo de vida del fragmento
        inicioViewModel.getMapaActualMutableLiveData().observe(getViewLifecycleOwner(), new Observer<InicioViewModel.MapConfig>() {
            @Override
            public void onChanged(InicioViewModel.MapConfig mapaActual) {
                mMapConfig = mapaActual;
                aplicarConfigSiLista();
            }
        });

        // Iniciar la carga o configuración del mapa en el ViewModel
        inicioViewModel.cargarMapa();

        // Devolver la vista inflada para que Android la muestre
        return root;
    }

    // Aplica la configuración al mapa solo cuando tenemos both: GoogleMap y MapConfig
    private void aplicarConfigSiLista() {
        if (mGoogleMap == null || mMapConfig == null) return;

        mGoogleMap.setMapType(mMapConfig.mapType);
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(mMapConfig.ULP).title(mMapConfig.markerTitle));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mMapConfig.ULP)      // Centrar la cámara en ULP
                .zoom(mMapConfig.zoom)      // Zoom
                .bearing(mMapConfig.bearing)// Orientación
                .tilt(mMapConfig.tilt)      // Inclinación
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mGoogleMap.animateCamera(cameraUpdate);
    }

}
