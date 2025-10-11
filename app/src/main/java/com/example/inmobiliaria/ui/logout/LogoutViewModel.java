package com.example.inmobiliaria.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    // LiveData para avisar al fragmento cuándo cerrar la app
    private final MutableLiveData<Boolean> cerrarApp = new MutableLiveData<>();

    public LiveData<Boolean> getCerrarApp() {
        return cerrarApp;
    }

    // Método para llamar cuando el usuario confirma logout
    public void confirmarLogout() {
        // Aquí podrías limpiar sesión, tokens, etc si lo necesitás
        cerrarApp.setValue(true); // Avisamos que la app debe cerrarse
    }
}
