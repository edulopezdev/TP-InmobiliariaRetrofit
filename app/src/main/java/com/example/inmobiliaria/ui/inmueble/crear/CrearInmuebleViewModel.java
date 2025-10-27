package com.example.inmobiliaria.ui.inmueble.crear;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> uriMutableLiveData;
    private MutableLiveData<Inmueble> mInmueble;
    private static Inmueble inmueblelleno;
    public CrearInmuebleViewModel(@NonNull Application application) {
        super(application);
        inmueblelleno = new Inmueble();
    }
    public LiveData<Uri> getUriMutable() {
        if (uriMutableLiveData == null) {
            uriMutableLiveData = new MutableLiveData<>();
        }
        return uriMutableLiveData;
    }
    public LiveData<Inmueble> getmInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }
    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri uri = data.getData();
            Log.d("salada", uri.toString());
            uriMutableLiveData.setValue(uri);
        }
    }

    public void guardarInmueble(String direccion, String uso, String tipo, String precio, String ambientes, String superficie, String latitud, String longitud, boolean disponible) {
        try {
            int ambientesInt = Integer.parseInt(ambientes);
            double superficieDouble = Double.parseDouble(superficie);
            double precioDouble = Double.parseDouble(precio);
            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setValor(precioDouble);
            inmueble.setAmbientes(ambientesInt);
            inmueble.setSuperficie((int) superficieDouble);
            inmueble.setDisponible(disponible);

            //convertir la imagen en bits
            byte[] imagen = transformarImagen();

            //como me doy cuenta si el usuario habia elegido una imagen o no?
            if (imagen.length == 0) {
                Toast.makeText(getApplication(), "Error: Debe seleccionar una imagen para guardar el inmueble.", Toast.LENGTH_SHORT).show();
                return;
            }

            // convertir la imagen a JSON
            String inmuebleJson = new Gson().toJson(inmueble);
            // crear el RequestBody para la imagen para enviarla al servidor
            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
            //ahora vamos a armar la imagen en otro RequestBody
            RequestBody imagenBody = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
            // ahora armar el MultipartBody.Part para la imagen
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", imagenBody);
            // ahora avamos a invocar del ApiClient el metodo para guardar el inmueble
            ApiClient.InmoServicio api = ApiClient.getInmoServicio();
            // leer el token
            String token = ApiClient.leerToken(getApplication());
            // vamos a llamar al metodo guardarInmueble del ApiClient pasandole el token, el inmuebleBody y la imagenPart
            Call<Inmueble> call = api.CargarInmueble("Bearer " + token, imagenPart, inmuebleBody);

            //que nos falta? Faltarìa ejecutar la llamada asincronica con enqueue y manejar la respuesta
            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplication(), "Inmueble creado correctamente (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("InmuebleVM", "error en la respuesta: " + response.code());
                        Toast.makeText(getApplication(), "No se pudo actulizar el inmueble" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    Log.d("errorActualizar", "Error: " + t.getMessage());
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(), "Error: Formato numérico inválido.", Toast.LENGTH_SHORT).show();
        }
    }
    private byte[] transformarImagen() {
        try {
            Uri uri = uriMutableLiveData.getValue(); // Obtener la URI de la imagen seleccionada
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uriMutableLiveData.getValue());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch ( FileNotFoundException e) {
            Toast.makeText(getApplication(), "Error: Imagen no encontrada.", Toast.LENGTH_SHORT).show();
            return new byte[]{};
        }


    }
}