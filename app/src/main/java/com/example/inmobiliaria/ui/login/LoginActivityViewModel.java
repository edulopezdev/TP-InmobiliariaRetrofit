package com.example.inmobiliaria.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.MainActivity;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    // Contexto de la aplicación para lanzar actividades y acceder a recursos
    private Context context;

    // LiveData para comunicar mensajes de error a la UI
    private MutableLiveData<String> errorMutable;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = getApplication(); // Guardamos contexto de la app
    }

    // Método para obtener el LiveData de errores (crea si es null)
    public LiveData<String> getErrorMutableLiveData() {
        if (errorMutable == null) {
            errorMutable = new MutableLiveData<>();
        }
        return errorMutable;
    }

    // Método para manejar el login con email y password
    public void login(String email, String password) {
        // Validación simple de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            errorMutable.setValue("Todos los campos son obligatorios");
            // Mostrar Toast para mejorar la experiencia del usuario
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la instancia del servicio API para hacer la llamada
        ApiClient.InmoServicio inmoServicio = ApiClient.getInmoServicio();

        // Realizar la llamada a la API para login (asíncrona)
        Call<String> call = inmoServicio.loginForm(email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Si el login fue exitoso, obtener el token
                    String token = response.body();

                    // Guardar token en SharedPreferences para futuras llamadas
                    ApiClient.guardarToken(context, token);

                    // Crear intent para abrir MainActivity (pantalla principal)
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Nueva tarea para la actividad
                    getApplication().startActivity(intent);

                } else {
                    // En caso de error en la respuesta, mostrar mensaje amigable
                    Log.d("Error", response.message());
                    Log.d("Error", String.valueOf(response.code()));

                    String mensajeError;
                    switch (response.code()) {
                        case 400: // Bad Request
                            mensajeError = "Usuario o contraseña incorrectos";
                            break;
                        case 401: // Unauthorized
                            mensajeError = "No autorizado. Verifique sus credenciales";
                            break;
                        case 403: // Forbidden
                            mensajeError = "Acceso denegado";
                            break;
                        case 404: // Not Found
                            mensajeError = "Servicio no disponible";
                            break;
                        case 500: // Server Error
                            mensajeError = "Error en el servidor. Intente más tarde";
                            break;
                        default:
                            mensajeError = "Error de conexión. Inténtelo de nuevo";
                    }

                    errorMutable.setValue(mensajeError);
                    // Mostrar Toast con mensaje amigable
                    Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Si falla la llamada (problemas red, etc)
                String mensajeError = "Error de conexión. Compruebe su red e intente nuevamente";
                errorMutable.setValue(mensajeError);
                // Mostrar Toast con mensaje amigable
                Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
