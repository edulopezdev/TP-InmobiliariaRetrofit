package com.example.inmobiliaria.ui.inmueble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.navigation.Navigation;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.example.inmobiliaria.models.Inmueble;
import java.util.List;

public class InmuebleFragment extends Fragment {

    private FragmentInmuebleBinding binding;
    private InmuebleViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmuebleViewModel.class);
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm.getListaInmuebles().observe(getViewLifecycleOwner(), (List<Inmueble> inmuebles) -> {
            InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getLayoutInflater(), inmueble -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble", inmueble);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.detalleInmueble, bundle);
            });
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            binding.listaInmuebles.setLayoutManager(glm);
            binding.listaInmuebles.setAdapter(adapter);
        });

        vm.obtenerListaInmuebles();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
