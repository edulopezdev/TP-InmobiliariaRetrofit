package com.example.inmobiliaria.ui.inquilino.detalle;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Inquilino;
import com.example.inmobiliaria.models.Contrato;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquiniloViewModel extends AndroidViewModel {
    private MutableLiveData<Inquilino> inquilinoMutable = new MutableLiveData<>();

    public DetalleInquiniloViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getInquilino() {
        return inquilinoMutable;
    }

    public void cargarInquilino(int idInmueble) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<Contrato> call = api.getContratoPorInmueble("Bearer " + token, idInmueble);
        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inquilinoMutable.postValue(response.body().getInquilino());
                }
            }
            @Override
            public void onFailure(Call<Contrato> call, Throwable t) { }
        });
    }
}