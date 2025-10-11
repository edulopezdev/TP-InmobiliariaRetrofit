package com.example.inmobiliaria.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    // Variable para manejar el ViewModel con la lógica de login
    private LoginActivityViewModel viewModel;

    // Variable para el binding con la UI (activity_login.xml)
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar la vista usando View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Crear o recuperar la instancia del ViewModel asociado a esta actividad
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(LoginActivityViewModel.class);

        // Observar los mensajes de error que envía el ViewModel
        viewModel.getErrorMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // Mostrar el error en el TextView de la UI
                binding.tvError.setText(s);
            }
        });

        // Manejar el click en el botón Login
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el email y la contraseña ingresados por el usuario
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                // Llamar al método login del ViewModel para iniciar sesión
                viewModel.login(email, password);
            }
        });
    }
}
