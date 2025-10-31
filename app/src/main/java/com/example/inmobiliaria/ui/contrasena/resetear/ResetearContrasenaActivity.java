package com.example.inmobiliaria.ui.contrasena.resetear;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.inmobiliaria.R;

public class ResetearContrasenaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetear_contrasena);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_resetear, new ResetearContrasenaFragment())
                    .commit();
        }
    }
}