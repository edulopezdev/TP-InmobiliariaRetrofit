package com.example.inmobiliaria.ui.contrasena;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.example.inmobiliaria.databinding.FragmentCambiarContrasenaBinding;

public class CambiarContrasenaFragment extends Fragment {

    private CambiarContrasenaViewModel vm;
    private FragmentCambiarContrasenaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCambiarContrasenaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CambiarContrasenaViewModel.class);

        binding.btnCambiar.setOnClickListener(v -> {
            String actual = binding.etActual.getText().toString();
            String nueva = binding.etNueva.getText().toString();
            vm.cambiarContrasena(actual, nueva);
        });

        vm.error.observe(getViewLifecycleOwner(), err -> {
            binding.tvErrorCambiar.setText(err != null ? err : "");
        });

        vm.exito.observe(getViewLifecycleOwner(), ok -> {
            if (ok != null && ok) {
                Toast.makeText(getContext(), "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}