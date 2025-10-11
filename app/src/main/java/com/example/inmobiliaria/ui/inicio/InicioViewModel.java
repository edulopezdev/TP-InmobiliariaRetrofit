package com.example.inmobiliaria.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends AndroidViewModel {

    // LiveData que contiene el objeto MapaActual (callback para Google Maps)
    private MutableLiveData<MapaActual> mapaActualMutableLiveData;

    // Constructor obligatorio que recibe la aplicación
    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    // Método para obtener el LiveData que se va a observar desde el fragmento
    public LiveData<MapaActual> getMapaActualMutableLiveData() {
        if(mapaActualMutableLiveData == null) {
            // Inicializar MutableLiveData si aún no existe
            mapaActualMutableLiveData = new MutableLiveData<>();
        }
        return mapaActualMutableLiveData;
    }

    // Método para "cargar" el mapa, es decir, crear el callback y notificar a los observadores
    public void cargarMapa() {
        MapaActual mapaActual = new MapaActual();
        // Publicar el nuevo objeto MapaActual para que el fragmento pueda usarlo
        mapaActualMutableLiveData.postValue(mapaActual);
    }

    // Clase interna que implementa OnMapReadyCallback para manejar el mapa cuando esté listo
    public class MapaActual implements OnMapReadyCallback {

        // Coordenadas de la Universidad de La Punta (ULP)
        LatLng ULP = new LatLng(-33.150720, -66.306864);

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            // Cambiar el tipo de mapa a satélite
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            // Agregar un marcador en las coordenadas ULP con el título "Facu"
            googleMap.addMarker(new MarkerOptions().position(ULP).title("Facu"));

            // Crear una posición personalizada para la cámara del mapa
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(ULP)      // Centrar la cámara en ULP
                    .zoom(10)         // Zoom nivel 10
                    .bearing(0)       // Orientación de la cámara hacia el norte
                    .tilt(30)         // Inclinación de la cámara a 30 grados
                    .build();

            // Crear una actualización de cámara con la posición definida
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

            // Animar el movimiento de la cámara hacia la posición definida
            googleMap.animateCamera(cameraUpdate);
        }
    }
}
