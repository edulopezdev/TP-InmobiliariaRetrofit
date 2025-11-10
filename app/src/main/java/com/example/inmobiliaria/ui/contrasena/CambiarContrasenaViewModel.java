package com.example.inmobiliaria.ui.contrasena;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContrasenaViewModel extends AndroidViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exito = new MutableLiveData<>();

    public CambiarContrasenaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getExito() {
        return exito;
    }

    // este es llamado desde el Fragment
    public void onCambiarClicked(String actual, String nueva) {
        if (actual.trim().isEmpty() || nueva.trim().isEmpty()) {
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
                    error.postValue("Error al cambiar contraseña (verifique la actual)");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                error.postValue("Error de red: " + t.getMessage());
            }
        });
    }

    // El ViewModel también maneja la navegación y el Toast
    public void manejarExito(Context context, Fragment fragment, Boolean ok) {
        if (ok == null || !ok) return;
        Toast.makeText(context, "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(fragment).popBackStack();
        exito.setValue(false); // reseteamos para evitar que vuelva a lanzar
    }
}
