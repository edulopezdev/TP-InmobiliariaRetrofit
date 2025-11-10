package com.example.inmobiliaria.ui.contrato.detalle;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Contrato;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {

    private static final String TAG = "DetalleContratoVM";

    // este es el LiveData q va a contener el contrato
    private final MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();

    // este es el LiveData para la navegación
    private final MutableLiveData<Bundle> navegarAPagos = new MutableLiveData<>();

    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContrato() {
        return contratoMutable;
    }

    public LiveData<Bundle> getNavegarAPagos() {
        return navegarAPagos;
    }

    // Este método lo llama el Fragment para pasarle los argumentos
    public void inicializarDesdeArgumentos(Bundle args) {
        if (args != null && args.containsKey("idInmueble")) {
            int idInmueble = args.getInt("idInmueble");
            cargarContrato(idInmueble);
        }
    }

    // aca vamos a obtener el contrato desde la API
    private void cargarContrato(int idInmueble) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Contrato> call = api.getContratoPorInmueble("Bearer " + token, idInmueble);
        Log.d(TAG, "Solicitando contrato para inmueble id: " + idInmueble);

        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contratoMutable.setValue(response.body());
                    Log.d(TAG, "Contrato recibido correctamente");
                } else {
                    Log.e(TAG, "Error en respuesta o cuerpo vacío");
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Log.e(TAG, "Error en solicitud: " + t.getMessage());
            }
        });
    }

    // esta es la acción para navegar a pagos
    public void irAPagos() {
        Contrato contrato = contratoMutable.getValue();
        if (contrato != null) {
            Bundle args = new Bundle();
            args.putInt("idContrato", contrato.getIdContrato());
            navegarAPagos.setValue(args);
        }
    }
}
