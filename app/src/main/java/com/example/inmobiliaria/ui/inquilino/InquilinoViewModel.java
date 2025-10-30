package com.example.inmobiliaria.ui.inquilino;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> listaAlquilados;

    public InquilinoViewModel(@NonNull Application application) {
        super(application);
        listaAlquilados = new MutableLiveData<>();
    }

    public LiveData<List<Inmueble>> getListaAlquilados() {
        return listaAlquilados;
    }

    public void obtenerInmueblesAlquilados() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<List<Inmueble>> call = api.getInmueblesConContratoVigente("Bearer " + token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlquilados.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) { }
        });
    }
}