package com.example.inmobiliaria.ui.inmueble;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InmuebleViewModel extends ViewModel {

    // MutableLiveData que contiene el texto que se mostrará en el fragmento
    private final MutableLiveData<String> mText;

    // Constructor: inicializa el MutableLiveData con un texto por defecto
    public InmuebleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    // Método público para obtener el LiveData (solo lectura) que será observado por la UI
    public LiveData<String> getText() {
        return mText;
    }
}
