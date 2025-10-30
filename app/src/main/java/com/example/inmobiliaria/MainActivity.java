package com.example.inmobiliaria;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inmobiliaria.databinding.ActivityMainBinding;
import com.example.inmobiliaria.models.Propietario;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Variable para configurar la barra de navegación y el drawer
    private AppBarConfiguration mAppBarConfiguration;

    // Binding para acceder a las vistas del layout activity_main.xml sin findViewById
    private ActivityMainBinding binding;

    // ViewModel para manejar los datos del propietario
    private MainActivityViewModel mViewModel;

    // --- Variables para el sensor de shake ---
    private SensorManager manager;
    private SensorEventListener maneja;
    private float accelerationCurrentValue = 0;
    private float accelerationLastValue = 0;
    private boolean isShaking = false;
    private final float shakeThreshold = 12.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflamos el layout usando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializamos el ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Establecemos la toolbar como la ActionBar de la actividad
        setSupportActionBar(binding.appBarMain.toolbar);

        // Configuramos el botón flotante (FAB) para mostrar un Snackbar cuando se clickea
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Muestra un Snackbar con un mensaje y una acción (null en este caso)
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab) // Ancla el Snackbar al FAB para que no tape otros elementos
                        .show();
            }
        });

        // Referencia al DrawerLayout (menú lateral deslizable)
        DrawerLayout drawer = binding.drawerLayout;

        // Referencia al NavigationView, que contiene el menú lateral con las opciones
        NavigationView navigationView = binding.navView;

        // Configuramos los destinos principales que se consideran top level destinations
        // Esto afecta cómo se muestra el botón de navegación en la toolbar (hamburguesa o back)
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_perfil, R.id.nav_inmueble,
                R.id.nav_inquilino, R.id.nav_contrato, R.id.nav_logout)
                .setOpenableLayout(drawer) // Asignamos el DrawerLayout para que se abra/cierre con el botón hamburguesa
                .build();

        // Obtenemos el NavController, que maneja la navegación entre fragmentos dentro del nav_host_fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // Mostramos u ocultamos el FAB según el destino actual
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_inmueble) {
                binding.appBarMain.fab.show();
            } else {
                binding.appBarMain.fab.hide();
            }
        });

        binding.appBarMain.fab.setOnClickListener(v -> {navController.navigate(R.id.crearInmuebleFragment);});

        // Vinculamos la ActionBar con el NavController para que actualice el título y el botón de navegación
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Configuramos el NavigationView para que navegue con el NavController al seleccionar un menú
        NavigationUI.setupWithNavController(navigationView, navController);

        // Obtenemos referencias a los TextViews del header
        View headerView = navigationView.getHeaderView(0);
        final TextView tvNombreUsuario = headerView.findViewById(R.id.tvNombreUsuario);
        final TextView tvEmailUsuario = headerView.findViewById(R.id.tvEmailUsuario);

        // Observamos los cambios en los datos del propietario
        mViewModel.getPropietarioData().observe(this, new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    // Actualizamos los TextViews con los datos del propietario
                    String nombreCompleto = propietario.getNombre() + " " + propietario.getApellido();
                    tvNombreUsuario.setText(nombreCompleto);
                    tvEmailUsuario.setText(propietario.getEmail());
                }
            }
        });

        // Solicitamos los datos del propietario
        mViewModel.obtenerDatosPropietario();
    }

    // --- Lógica de shake y llamada ---
    @Override
    protected void onResume() {
        super.onResume();
        activaEscucha();
    }

    @Override
    protected void onPause() {
        super.onPause();
        desactivaEscucha();
    }

    public void activaEscucha() {
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensores = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensores.size() > 0) {
            maneja = new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}

                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];

                        accelerationLastValue = accelerationCurrentValue;
                        accelerationCurrentValue = (float) Math.sqrt((x * x + y * y + z * z));
                        float delta = accelerationCurrentValue - accelerationLastValue;

                        if (delta > shakeThreshold && !isShaking) {
                            isShaking = true;
                            llamarInmobiliaria();
                        }
                    }
                }
            };
            manager.registerListener(maneja, sensores.get(0), SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void desactivaEscucha() {
        if (manager != null && maneja != null)
            manager.unregisterListener(maneja);
        isShaking = false;
    }

    private void llamarInmobiliaria() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            isShaking = false;
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(android.net.Uri.parse("tel:2664553747"));
        startActivity(intent);
        Toast.makeText(this, "Llamando a la inmobiliaria...", Toast.LENGTH_SHORT).show();
        new android.os.Handler().postDelayed(() -> isShaking = false, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            llamarInmobiliaria();
        }
    }

    // Inflamos el menú de opciones de la ActionBar (si está presente)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Controla el comportamiento del botón de navegación "Up" (flecha atrás) en la toolbar
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Delegamos la navegación al NavController o fallback al comportamiento por defecto
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}