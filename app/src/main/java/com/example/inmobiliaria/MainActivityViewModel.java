package com.example.inmobiliaria;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> propietarioData;
    private ApiClient.InmoServicio apiService;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        propietarioData = new MutableLiveData<>();
        apiService = ApiClient.getInmoServicio();
    }

    // Método para obtener datos del propietario logueado
    public void obtenerDatosPropietario() {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            Call<Propietario> propietarioCall = apiService.getPropietario("Bearer " + token);
            propietarioCall.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful()) {
                        propietarioData.setValue(response.body());
                    } else {
                        Log.e("MainActivityViewModel", "Error al obtener los datos: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.e("MainActivityViewModel", "Error de conexión: " + t.getMessage());
                }
            });
        }
    }

    // LiveData para observar los cambios en los datos del propietario
    public LiveData<Propietario> getPropietarioData() {
        return propietarioData;
    }
}
