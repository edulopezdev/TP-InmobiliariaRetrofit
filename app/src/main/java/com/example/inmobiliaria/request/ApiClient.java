package com.example.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.inmobiliaria.models.Pago;

import com.example.inmobiliaria.models.Inmueble;
import com.example.inmobiliaria.models.Propietario;
import com.example.inmobiliaria.models.Contrato;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {
    // URL base de la API
    public final static String BASE_URL = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";

    // Método para crear el servicio de Retrofit con Gson
    public static InmoServicio getInmoServicio() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmoServicio.class);
    }

    // Interfaz con los endpoints de la API
    public interface InmoServicio {

        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> loginForm(@Field("Usuario") String usuario, @Field("Clave") String clave);  // Login con usuario y clave

        @GET("api/Propietarios")
        Call<Propietario> getPropietario(@Header("Authorization") String token);  // Traer datos del propietario con token

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> editPropietario(@Header("Authorization") String token, @Body Propietario propietario);  // Actualizar propietario

        @GET("api/Inmuebles")
        Call<List<Inmueble>> getListaInmuebles(@Header("Authorization") String token);  // Traer lista de inmuebles con token
        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);  // Actualizar inmueble

        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> CargarInmueble(@Header("Authorization") String token,
                                      @Part MultipartBody.Part imagen,
                                      @Part("inmueble") RequestBody inmuebleBody);

        @GET("api/Inmuebles/GetContratoVigente")
        Call<List<Inmueble>> getInmueblesConContratoVigente(@Header("Authorization") String token);

        @GET("api/contratos/inmueble/{id}")
        Call<Contrato> getContratoPorInmueble(@Header("Authorization") String token, @retrofit2.http.Path("id") int idInmueble);

        @GET("api/pagos/contrato/{id}")
        Call<List<Pago>> getPagosPorContrato(@Header("Authorization") String token, @retrofit2.http.Path("id") int idContrato);
    }

    // Guardar token en SharedPreferences
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    // Leer token de SharedPreferences
    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }
}
