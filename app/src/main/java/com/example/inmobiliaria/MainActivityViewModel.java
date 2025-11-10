package com.example.inmobiliaria;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<Propietario> propietarioLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> llamarLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pedirPermisoLiveData = new MutableLiveData<>();

    // ===== Nuevo LiveData para el FAB =====
    private final MutableLiveData<Boolean> irACrearInmueble = new MutableLiveData<>(false);

    private SensorManager sensorManager;
    private SensorEventListener sensorListener;
    private boolean isShaking = false;
    private final float shakeThreshold = 12f;

    private final ApiClient.InmoServicio apiService;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        apiService = ApiClient.getInmoServicio();
    }

    public LiveData<Propietario> getPropietarioLiveData() { return propietarioLiveData; }
    public LiveData<String> getMensajeLiveData() { return mensajeLiveData; }
    public LiveData<String> getLlamarLiveData() { return llamarLiveData; }
    public LiveData<Boolean> getPedirPermisoLiveData() { return pedirPermisoLiveData; }
    public LiveData<Boolean> getIrACrearInmueble() { return irACrearInmueble; }

    public void resetIrACrearInmueble() {
        irACrearInmueble.setValue(false);
    }

    public void obtenerDatosPropietario() {
        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensajeLiveData.postValue("Token no encontrado");
            return;
        }
        apiService.getPropietario("Bearer " + token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    propietarioLiveData.postValue(response.body());
                } else {
                    mensajeLiveData.postValue("Error al obtener propietario");
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensajeLiveData.postValue("Fallo de conexión");
            }
        });
    }

    public void startShakeDetection() {
        if (sensorManager != null && sensorListener != null) return;
        sensorManager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensores = sensorManager != null
                ? sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)
                : Collections.emptyList();
        if (sensores.isEmpty()) return;

        sensorListener = new SensorEventListener() {
            private float last = 0f, current = 0f;

            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                last = current;
                current = (float) Math.sqrt(x * x + y * y + z * z);
                float delta = Math.abs(current - last);

                if (delta > shakeThreshold && !isShaking) {
                    isShaking = true;
                    verificarPermisoOLlamar();
                    new android.os.Handler(android.os.Looper.getMainLooper())
                            .postDelayed(() -> isShaking = false, 3000);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
        sensorManager.registerListener(sensorListener, sensores.get(0), SensorManager.SENSOR_DELAY_UI);
    }

    public void stopShakeDetection() {
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
            sensorListener = null;
        }
        isShaking = false;
    }

    private void verificarPermisoOLlamar() {
        Context context = getApplication().getApplicationContext();
        int permiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);

        if (permiso == PackageManager.PERMISSION_GRANTED) {
            // si el permiso fue ostorgado hacemos la llamada
            llamarLiveData.postValue("tel:2664553747");
        } else {
            // sino pedimos el permiso
            pedirPermisoLiveData.postValue(true);
        }
    }

    public void permisoConcedido() {
        // Método llamado por la Activity cuando el usuario aprueba el permiso
        llamarLiveData.postValue("tel:2664553747");
    }

    // ===== FAB =====
    public void onFabClick() {
        // Emitimos el evento para que la Activity navegue
        irACrearInmueble.setValue(true);
    }
}
