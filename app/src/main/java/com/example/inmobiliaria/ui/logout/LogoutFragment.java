package com.example.inmobiliaria.ui.logout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentLogoutBinding;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;
    private FragmentLogoutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observamos el LiveData para cerrar la app cuando el ViewModel indique
        logoutViewModel.getCerrarApp().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean cerrar) {
                if (cerrar != null && cerrar) {
                    if (getActivity() != null) {
                        getActivity().finishAffinity();
                    }
                }
            }
        });

        mostrarDialogoDeSalida();

        return root;
    }

    private void mostrarDialogoDeSalida() {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Salida")
                .setMessage("¿Está seguro de que deseas salir?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Avisamos al ViewModel que el usuario confirmó salir
                        logoutViewModel.confirmarLogout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si usás navegación, volvemos atrás
                        if (getParentFragment() != null) {
                            NavHostFragment.findNavController(LogoutFragment.this).popBackStack();
                        } else if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setIcon(R.drawable.imagen)
                .show();
    }
}
