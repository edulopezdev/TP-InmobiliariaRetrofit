package com.example.inmobiliaria;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inmobiliaria.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CALL = 1;
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private MainActivityViewModel viewModel;
    private String numeroPendiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_perfil, R.id.nav_inmueble,
                R.id.nav_inquilino, R.id.nav_contrato, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Mostrar/ocultar FAB solo en la pantalla de inmuebles
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination != null && destination.getId() == R.id.nav_inmueble) {
                binding.appBarMain.fab.show();
            } else {
                binding.appBarMain.fab.hide();
            }
        });

        // Aca vamos a setear los datos del propietario en el header del navigation drawer
        View header = navigationView.getHeaderView(0);
        TextView nombre = header.findViewById(R.id.tvNombreUsuario);
        TextView email = header.findViewById(R.id.tvEmailUsuario);

        viewModel.getPropietarioLiveData().observe(this, p -> {
            nombre.setText(p.getNombre() + " " + p.getApellido());
            email.setText(p.getEmail());
        });

        viewModel.getMensajeLiveData().observe(this,
                msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());

        viewModel.getPedirPermisoLiveData().observe(this, pedir -> {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQ_CALL
            );
        });

        viewModel.getLlamarLiveData().observe(this, numero -> {
            numeroPendiente = numero;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(numeroPendiente)));
        });

        // FAB delega acción al ViewModel
        binding.appBarMain.fab.setOnClickListener(v -> viewModel.onFabClick());

        // Observamos el evento emitido por el ViewModel para navegar al fragment de crear inmueble
        viewModel.getIrACrearInmueble().observe(this, ir -> {
            if (Boolean.TRUE.equals(ir)) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                        .navigate(R.id.crearInmuebleFragment);
                viewModel.resetIrACrearInmueble();
            }
        });

        viewModel.obtenerDatosPropietario();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.permisoConcedido();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startShakeDetection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.stopShakeDetection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}