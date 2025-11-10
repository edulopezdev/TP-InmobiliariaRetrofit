package com.example.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.NumberFormat;
import java.util.Locale;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble = new MutableLiveData<>();
    private MutableLiveData<String> mensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> exito = new MutableLiveData<>();

    // Presentation fields
    private MutableLiveData<String> codigo = new MutableLiveData<>();
    private MutableLiveData<String> direccion = new MutableLiveData<>();
    private MutableLiveData<String> uso = new MutableLiveData<>();
    private MutableLiveData<String> ambientes = new MutableLiveData<>();
    private MutableLiveData<String> precioFormateado = new MutableLiveData<>();
    private MutableLiveData<String> tipo = new MutableLiveData<>();
    private MutableLiveData<Boolean> disponible = new MutableLiveData<>();
    private MutableLiveData<String> imagenUrl = new MutableLiveData<>();

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getMInmueble() { return mInmueble; }
    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<Boolean> getExito() { return exito; }

    // Presentation getters
    public LiveData<String> getCodigo() { return codigo; }
    public LiveData<String> getDireccion() { return direccion; }
    public LiveData<String> getUso() { return uso; }
    public LiveData<String> getAmbientes() { return ambientes; }
    public LiveData<String> getPrecioFormateado() { return precioFormateado; }
    public LiveData<String> getTipo() { return tipo; }
    public LiveData<Boolean> getDisponible() { return disponible; }
    public LiveData<String> getImagenUrl() { return imagenUrl; }

    public void recuperarInmueble(Bundle bundle) {
        Inmueble inmueble = (Inmueble) (bundle != null ? bundle.get("inmueble") : null);
        if (inmueble != null) {
            mInmueble.setValue(inmueble);
            actualizarPresentacion(inmueble);
        }
    }

    private void actualizarPresentacion(Inmueble inmueble) {
        codigo.postValue(String.valueOf(inmueble.getIdInmueble()));
        direccion.postValue(inmueble.getDireccion());
        uso.postValue(inmueble.getUso());
        ambientes.postValue(String.valueOf(inmueble.getAmbientes()));
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "AR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        precioFormateado.postValue("$ " + nf.format(inmueble.getValor()));
        tipo.postValue(inmueble.getTipo());
        disponible.postValue(inmueble.isDisponible());
        String imagen = inmueble.getImagen();
        if (imagen != null && !imagen.isEmpty()) {
            imagenUrl.postValue(ApiClient.BASE_URL + imagen);
        } else {
            imagenUrl.postValue(null);
        }
    }

    public void actualizarDisponibilidad(boolean nuevoDisponible) {
        Inmueble current = mInmueble.getValue();
        if (current == null) return;
        current.setDisponible(nuevoDisponible);
        // Llamar al API para actualizar
        actualizarInmueble(current);
    }

    public void actualizarInmueble(Inmueble inmueble) {
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        String token = ApiClient.leerToken(getApplication());
        // asegurar id correcto
        if (mInmueble.getValue() != null) {
            inmueble.setIdInmueble(mInmueble.getValue().getIdInmueble());
        }
        Call<Inmueble> call = api.actualizarInmueble("Bearer " + token, inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mInmueble.postValue(response.body());
                    actualizarPresentacion(response.body());
                    exito.postValue(true);
                    mensaje.postValue("Inmueble actualizado");
                } else {
                    exito.postValue(false);
                    mensaje.postValue("Error al actualizar inmueble: " + response.code());
                    Log.e("API", "Error al actualizar inmueble: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                exito.postValue(false);
                mensaje.postValue("Fallo en la llamada: " + t.getMessage());
                Log.e("API", "Fallo en la llamada: " + t.getMessage());
            }
        });
    }
}
