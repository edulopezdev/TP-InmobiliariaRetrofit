package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.models.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> propietarioMutable;
    private MutableLiveData<Boolean> mutableEstado = new MutableLiveData<>(false);
    private MutableLiveData<String> mutableTextoBoton = new MutableLiveData<>("Editar");
    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getMutableEstado() {
        return mutableEstado;
    }
    public LiveData<String> getMutableTextoBoton() {
        return mutableTextoBoton;
    }

    public LiveData<Propietario> getMutablePropietario() {
        if(propietarioMutable == null){
            propietarioMutable = new MutableLiveData<>();
        }
        return propietarioMutable;
    }

    public void getPerfil(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio servicio = ApiClient.getInmoServicio();
        Call<Propietario> propietario = servicio.getPropietario("Bearer " + token);

        propietario.enqueue(new retrofit2.Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, retrofit2.Response<Propietario> response) {
                if(response.isSuccessful()){
                    propietarioMutable.postValue(response.body());
                }else{
                    Log.d("PerfilViewModel", "Error al obtener el perfil: " + response.code());
                    Toast.makeText(getApplication(), "Error al obtener el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PerfilViewModel", "Error de red al obtener perfil: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión. Intenta nuevamente.", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void cambiarBoton(String textoBoton, String nombre, String apellido, String dni, String telefono, String email){
        if( textoBoton.equals("Editar")) {
            mutableEstado.setValue(true);
            mutableTextoBoton.setValue("Guardar");
        } else {
            mutableEstado.setValue(false);
            mutableTextoBoton.setValue("Editar");
            Propietario nuevo = new Propietario();
            nuevo.setIdPropietario(propietarioMutable.getValue().getIdPropietario());
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setDni(dni);
            nuevo.setTelefono(telefono);
            nuevo.setEmail(email);

            String token = ApiClient.leerToken(getApplication());
            ApiClient.InmoServicio servicio = ApiClient.getInmoServicio();
            Call<Propietario> propietarioCreado = servicio.editPropietario("Bearer " + token, nuevo);
            propietarioCreado.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, retrofit2.Response<Propietario> response) {
                    if (response.isSuccessful()) {
                        propietarioMutable.postValue(response.body());
                        Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("PerfilViewModel", "Error al actualizar el perfil: " + response.code());
                        Toast.makeText(getApplication(), "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.d("PerfilViewModel", "onFailure: " + t.getMessage());
                    Toast.makeText(getApplication(), "Error de red al actualizar el perfil, posible error de Api", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}