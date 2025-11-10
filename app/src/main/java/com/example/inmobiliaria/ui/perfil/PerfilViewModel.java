package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;
import com.example.inmobiliaria.models.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<Boolean> editable = new MutableLiveData<>(false);
    private final MutableLiveData<String> textoBoton = new MutableLiveData<>("Editar");
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    // getters LiveData
    public LiveData<Propietario> getPropietario() { return propietario; }
    public LiveData<Boolean> getEditable() { return editable; }
    public LiveData<String> getTextoBoton() { return textoBoton; }
    public LiveData<String> getMensaje() { return mensaje; }

    // estos son llamados desde el Fragment
    public void onBotonPerfilClick(String nombre, String apellido, String dni, String telefono, String email) {
        if (Boolean.FALSE.equals(editable.getValue())) {
            editable.setValue(true);
            textoBoton.setValue("Guardar");
        } else {
            guardarCambios(nombre, apellido, dni, telefono, email);
        }
    }

    public void aplicarEstilos(FragmentPerfilBinding binding, boolean editable) {
        int estilo = editable ? R.style.EditableTextStyle : R.style.ReadOnlyEditTextStyle;
        int color = getApplication().getResources().getColor(
                editable ? android.R.color.black : android.R.color.darker_gray
        );

        //aca lo q hacemos es setear los estilos a los edittext dependiendo si estan en modo editable o no
        binding.etNombre.setEnabled(editable);
        binding.etApellido.setEnabled(editable);
        binding.etDni.setEnabled(editable);
        binding.etTelefono.setEnabled(editable);
        binding.etEmail.setEnabled(editable);

        binding.etNombre.setTextAppearance(estilo);
        binding.etApellido.setTextAppearance(estilo);
        binding.etDni.setTextAppearance(estilo);
        binding.etTelefono.setTextAppearance(estilo);
        binding.etEmail.setTextAppearance(estilo);

        binding.etNombre.setTextColor(color);
        binding.etApellido.setTextColor(color);
        binding.etDni.setTextColor(color);
        binding.etTelefono.setTextColor(color);
        binding.etEmail.setTextColor(color);
    }

    public void mostrarToast(Context context, String msg) {
        if (msg == null || msg.isEmpty()) return;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        mensaje.setValue(null);
    }

    // llamados a la API
    public void cargarPerfil() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio servicio = ApiClient.getInmoServicio();
        servicio.getPropietario("Bearer " + token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietario.postValue(response.body());
                } else {
                    Log.e("PerfilVM", "Error al obtener perfil: " + response.code());
                    mensaje.postValue("Error al obtener el perfil");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PerfilVM", "Fallo red: " + t.getMessage());
                mensaje.postValue("Error de conexión");
            }
        });
    }

    private void guardarCambios(String nombre, String apellido, String dni, String telefono, String email) {
        Propietario actual = propietario.getValue();
        if (actual == null) {
            mensaje.postValue("No hay perfil cargado para actualizar");
            return;
        }

        Propietario nuevo = new Propietario(
                actual.getIdPropietario(), nombre, apellido, dni, telefono, email,
                actual.getClave() // si no se cambia la clave, se mantiene la misma
        );

        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio servicio = ApiClient.getInmoServicio();
        servicio.editPropietario("Bearer " + token, nuevo).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietario.postValue(response.body());
                    mensaje.postValue("Perfil actualizado");
                } else {
                    mensaje.postValue("Error al actualizar el perfil");
                }
                editable.postValue(false);
                textoBoton.postValue("Editar");
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PerfilVM", "Error: " + t.getMessage());
                mensaje.postValue("Error de red al actualizar");
                editable.postValue(false);
                textoBoton.postValue("Editar");
            }
        });
    }
}
