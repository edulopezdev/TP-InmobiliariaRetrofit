package com.example.inmobiliaria.ui.contrasena.resetear;

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
import com.example.inmobiliaria.databinding.FragmentResetearContrasenaBinding;

public class ResetearContrasenaFragment extends Fragment {

    private ResetearContrasenaViewModel vm;
    private FragmentResetearContrasenaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentResetearContrasenaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(ResetearContrasenaViewModel.class);

        binding.btnEnviar.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            vm.resetearContrasena(email);
        });

        vm.error.observe(getViewLifecycleOwner(), err -> {
            binding.tvErrorResetear.setText(err != null ? err : "");
        });

        vm.exito.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
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