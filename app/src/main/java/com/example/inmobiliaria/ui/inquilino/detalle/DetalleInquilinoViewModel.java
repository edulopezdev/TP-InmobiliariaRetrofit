package com.example.inmobiliaria.ui.inquilino.detalle;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Contrato;
import com.example.inmobiliaria.models.Inquilino;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private final MutableLiveData<Inquilino> inquilino = new MutableLiveData<>();

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getInquilino() {
        return inquilino;
    }

    // entoncs aca lo que hacemos es inicializar el ViewModel con los argumentos recibidos
    public void inicializarDesdeArgumentos(Bundle args) {
        if (args == null || !args.containsKey("idInmueble")) {
            Log.w("DetalleInquilinoVM", "Faltan argumentos: idInmueble no encontrado");
            inquilino.postValue(new Inquilino()); // devolvemos un objeto vacío
            return;
        }

        int idInmueble = args.getInt("idInmueble");
        cargarInquilino(idInmueble);
    }

    private void cargarInquilino(int idInmueble) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Contrato> call = api.getContratoPorInmueble("Bearer " + token, idInmueble);
        Log.d("DetalleInquilinoVM", "Solicitando contrato para inmueble id: " + idInmueble);

        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Inquilino i = response.body().getInquilino();
                    inquilino.postValue(normalizar(i));
                } else {
                    Log.e("DetalleInquilinoVM", "Respuesta no exitosa o vacía");
                    inquilino.postValue(new Inquilino()); // objeto vacío
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Log.e("DetalleInquilinoVM", "Error en solicitud: " + t.getMessage());
                inquilino.postValue(new Inquilino());
            }
        });
    }

    // este metodo lo q hace es devolver un inquilino con campos no nulos
    private Inquilino normalizar(Inquilino i) {
        if (i == null) return new Inquilino();

        Inquilino limpio = new Inquilino();
        limpio.setIdInquilino(i.getIdInquilino());
        limpio.setNombre(i.getNombre() != null ? i.getNombre() : "");
        limpio.setApellido(i.getApellido() != null ? i.getApellido() : "");
        limpio.setDni(i.getDni() != null ? i.getDni() : "");
        limpio.setTelefono(i.getTelefono() != null ? i.getTelefono() : "");
        limpio.setEmail(i.getEmail() != null ? i.getEmail() : "");
        return limpio;
    }
}
