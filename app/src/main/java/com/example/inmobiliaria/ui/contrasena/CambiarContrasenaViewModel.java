package com.example.inmobiliaria.ui.contrasena;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.inmobiliaria.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContrasenaViewModel extends AndroidViewModel {
    public MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<Boolean> exito = new MutableLiveData<>();

    public CambiarContrasenaViewModel(@NonNull Application application) {
        super(application);
    }

    public void cambiarContrasena(String actual, String nueva) {
        if (actual.isEmpty() || nueva.isEmpty()) {
            error.setValue("Todos los campos son obligatorios");
            return;
        }
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<Void> call = api.cambiarPassword("Bearer " + token, actual, nueva);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    exito.postValue(true);
                } else {
                    error.postValue("Error: " + response.code() + " (verifique la contraseña actual)");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                error.postValue("Error de red: " + t.getMessage());
            }
        });
    }
}