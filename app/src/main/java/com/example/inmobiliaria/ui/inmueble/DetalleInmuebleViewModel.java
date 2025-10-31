package com.example.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble = new MutableLiveData<>();
    private MutableLiveData<String> mensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> exito = new MutableLiveData<>();

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getMInmueble() {
        return mInmueble;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<Boolean> getExito() {
        return exito;
    }

    public void recuperarInmueble(Bundle bundle) {
        Inmueble inmueble = (Inmueble) bundle.get("inmueble");
        if (inmueble != null) {
            mInmueble.setValue(inmueble);
        }
    }

    public void actualizarInmueble(Inmueble inmueble) {
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        String token = ApiClient.leerToken(getApplication());
        inmueble.setIdInmueble(mInmueble.getValue().getIdInmueble());
        Call<Inmueble> call = api.actualizarInmueble("Bearer " + token, inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, retrofit2.Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    exito.postValue(true);
                    mensaje.postValue("Inmueble actualizado");
                } else {
                    exito.postValue(false);
                    mensaje.postValue("Error al actualizar inmueble");
                    Log.e("API", "Error al actualizar inmueble: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                exito.postValue(false);
                mensaje.postValue("Fallo en la llamada: " + t.getMessage());
                Log.e("API", "Fallo en la llamada: " + t.getMessage());
            }
        });
    }
}