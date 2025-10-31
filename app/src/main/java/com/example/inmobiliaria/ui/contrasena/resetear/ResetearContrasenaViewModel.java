package com.example.inmobiliaria.ui.contrasena.resetear;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.inmobiliaria.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetearContrasenaViewModel extends AndroidViewModel {
    public MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<String> exito = new MutableLiveData<>();

    public ResetearContrasenaViewModel(@NonNull Application application) {
        super(application);
    }

    public void resetearContrasena(String email) {
        if (email == null || email.isEmpty()) {
            error.setValue("El email es obligatorio");
            return;
        }
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<String> call = api.resetearContrasena(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exito.postValue(response.body());
                } else {
                    if (response.code() == 400) {
                        error.postValue("El correo ingresado no existe");
                    } else {
                        error.postValue("No se pudo enviar el correo (" + response.code() + ")");
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.postValue("Error de red: " + t.getMessage());
            }
        });
    }
}