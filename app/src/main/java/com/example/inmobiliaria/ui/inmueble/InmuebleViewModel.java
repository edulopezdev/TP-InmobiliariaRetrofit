package com.example.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> listaInmuebles = new MutableLiveData<>();

    public InmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getListaInmuebles() {
        return listaInmuebles;
    }

    public void obtenerListaInmuebles() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<List<Inmueble>> call = api.getListaInmuebles("Bearer " + token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> inmuebles = response.body();
                    Collections.reverse(inmuebles); // Mostrar los últimos arriba
                    listaInmuebles.postValue(inmuebles);
                    Log.d("InmuebleViewModel", "Lista de inmuebles recibida: " + inmuebles);
                } else {
                    Toast.makeText(getApplication(), "no se obtuvieron Inmuebles", Toast.LENGTH_LONG).show();
                    Log.d("InmuebleViewModel", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                Log.d("errorInmueble", throwable.getMessage());
                Toast.makeText(getApplication(), "Error al obtener Inmuebles", Toast.LENGTH_LONG).show();
            }
        });
    }
}