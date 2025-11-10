package com.example.inmobiliaria.ui.inmueble.crear;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exito = new MutableLiveData<>();
    private final MutableLiveData<Boolean> abrirGaleria = new MutableLiveData<>(false);

    public CrearInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getUriMutable() { return uriMutableLiveData; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getExito() { return exito; }
    public LiveData<Boolean> getAbrirGaleria() { return abrirGaleria; }

    public void resetAbrirGaleria() { abrirGaleria.setValue(false); }

    // Se llama cuando el usuario toca el botón "Cargar Imagen"
    public void onCargarImagenClick() {
        abrirGaleria.setValue(true);
    }

    // Se llama cuando se obtiene la imagen de la galería
    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri uri = result.getData().getData();
            uriMutableLiveData.setValue(uri);
        }
    }

    // Se llama cuando el usuario toca "Guardar"
    public void onGuardarClick(String direccion, String uso, String tipo, String precio,
                               String ambientes, String superficie, String latitud,
                               String longitud, boolean disponible) {
        guardarInmueble(direccion, uso, tipo, precio, ambientes, superficie, latitud, longitud, disponible);
    }

    public void guardarInmueble(String direccion, String uso, String tipo, String precio,
                                String ambientes, String superficie, String latitud,
                                String longitud, boolean disponible) {

        // Validaciones
        if (direccion.isEmpty()) { error.setValue("La dirección es obligatoria"); return; }
        if (uso.isEmpty()) { error.setValue("El uso es obligatorio"); return; }
        if (tipo.isEmpty()) { error.setValue("El tipo es obligatorio"); return; }

        int ambientesInt;
        try {
            ambientesInt = Integer.parseInt(ambientes);
            if (ambientesInt <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error.setValue("Ingrese un número de ambientes válido");
            return;
        }

        int superficieInt;
        try {
            superficieInt = (int) Double.parseDouble(superficie);
            if (superficieInt <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error.setValue("Ingrese una superficie válida");
            return;
        }

        double precioDouble;
        try {
            precioDouble = Double.parseDouble(precio);
            if (precioDouble <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error.setValue("Ingrese un valor válido");
            return;
        }

        double latitudDouble;
        try {
            latitudDouble = Double.parseDouble(latitud);
        } catch (NumberFormatException e) {
            error.setValue("Ingrese una latitud válida");
            return;
        }

        double longitudDouble;
        try {
            longitudDouble = Double.parseDouble(longitud);
        } catch (NumberFormatException e) {
            error.setValue("Ingrese una longitud válida");
            return;
        }

        byte[] imagen = transformarImagen();
        if (imagen.length == 0) {
            error.setValue("Debe seleccionar una imagen para guardar el inmueble.");
            return;
        }

        // Armamos el inmueble
        Inmueble inmueble = new Inmueble();
        inmueble.setDireccion(direccion);
        inmueble.setUso(uso);
        inmueble.setTipo(tipo);
        inmueble.setValor(precioDouble);
        inmueble.setAmbientes(ambientesInt);
        inmueble.setSuperficie(superficieInt);
        inmueble.setLatitud(latitudDouble);
        inmueble.setLongitud(longitudDouble);
        inmueble.setDisponible(disponible);

        // Enviamos al servidor
        String inmuebleJson = new Gson().toJson(inmueble);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
        RequestBody imagenBody = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", imagenBody);

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        String token = ApiClient.leerToken(getApplication());
        Call<Inmueble> call = api.CargarInmueble("Bearer " + token, imagenPart, inmuebleBody);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    exito.postValue(true);
                } else {
                    error.setValue("No se pudo crear el inmueble (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                error.setValue("Error: " + t.getMessage());
            }
        });
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = uriMutableLiveData.getValue();
            if (uri == null) return new byte[]{};
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            error.setValue("Error: Imagen no encontrada.");
            return new byte[]{};
        }
    }
}
