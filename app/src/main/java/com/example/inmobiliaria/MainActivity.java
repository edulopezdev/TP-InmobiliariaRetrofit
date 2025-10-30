package com.example.inmobiliaria;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiliaria.databinding.ActivityMainBinding;
import com.example.inmobiliaria.models.Propietario;

public class MainActivity extends AppCompatActivity {

    // Variable para configurar la barra de navegación y el drawer
    private AppBarConfiguration mAppBarConfiguration;

    // Binding para acceder a las vistas del layout activity_main.xml sin findViewById
    private ActivityMainBinding binding;

    // ViewModel para manejar los datos del propietario
    private MainActivityViewModel mViewModel;

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
