// java
package com.example.inmobiliaria.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

public class InicioViewModel extends AndroidViewModel {

    // LiveData que contiene el objeto MapaActual (callback para Google Maps)
    private MutableLiveData<MapConfig> mapaActualMutableLiveData;

    // Constructor obligatorio que recibe la aplicación
    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    // Método para obtener el LiveData que se va a observar desde el fragmento
    public LiveData<MapConfig> getMapaActualMutableLiveData() {
        if(mapaActualMutableLiveData == null) {
            // Inicializar MutableLiveData si aún no existe
            mapaActualMutableLiveData = new MutableLiveData<>();
        }
        return mapaActualMutableLiveData;
    }

    // Método para "cargar" el mapa, es decir, crear el callback y notificar a los observadores
    public void cargarMapa() {
        // Crear configuración del mapa (datos puros, sin GoogleMap)
        MapConfig config = new MapConfig(
                new LatLng(-33.150720, -66.306864), // ULP
                10f,     // zoom
                30f,     // tilt
                0f,      // bearing
                com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE, // tipo
                "Facu"   // título del marcador
        );
        // Publicar el nuevo objeto de configuración para que el fragmento pueda usarlo
        mapaActualMutableLiveData.postValue(config);
    }

    // Clase interna que representará la configuración del mapa (datos puros)
    public static class MapConfig {
        public final LatLng ULP;
        public final float zoom;
        public final float tilt;
        public final float bearing;
        public final int mapType;
        public final String markerTitle;

        public MapConfig(LatLng ULP, float zoom, float tilt, float bearing, int mapType, String markerTitle) {
            this.ULP = ULP;
            this.zoom = zoom;
            this.tilt = tilt;
            this.bearing = bearing;
            this.mapType = mapType;
            this.markerTitle = markerTitle;
        }
    }
}
