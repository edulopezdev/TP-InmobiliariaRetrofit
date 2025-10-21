package com.example.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble;

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getMInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }
    public void setMInmueble(MutableLiveData<Inmueble> mInmueble) {
        this.mInmueble = mInmueble;
    }
    public void recuperarInmueble(Bundle bundle) {
        Inmueble inmueble = (Inmueble) bundle.get("inmueble");
        if (mInmueble != null) {
            mInmueble.setValue(inmueble);
        }
    }
    public void actualizarInmueble(Inmueble inmueble) {
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        String token = ApiClient.leerToken(getApplication());
        inmueble.setIdInmueble(mInmueble.getValue().getIdInmueble());
        Call<Inmueble> call = api.actualizarInmueble("Bearer "+token, inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, retrofit2.Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Inmueble actualizado: " + response.body());
                    Toast.makeText(getApplication(), "Inmueble actualizado", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("API", "Error al actualizar inmueble: " + response.code());
                    Toast.makeText(getApplication(), "Error al actualizar inmueble", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e("API", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getApplication(), "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}