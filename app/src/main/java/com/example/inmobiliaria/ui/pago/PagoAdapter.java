package com.example.inmobiliaria.ui.pago;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.models.Pago;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoViewHolder> {
    private List<Pago> pagos;

    public PagoAdapter(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public void setPagos(List<Pago> nuevosPagos) {
        this.pagos = nuevosPagos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago, parent, false);
        return new PagoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pago pago = pagos.get(position);
        holder.tvDetalle.setText(pago.getDetalle());

        // Formatear fecha (asumiendo formato yyyy-MM-dd)
        String fecha = pago.getFechaPago();
        if (fecha != null && fecha.length() >= 10) {
            holder.tvFecha.setText(fecha.substring(0, 10));
        } else {
            holder.tvFecha.setText(fecha);
        }

        // Formatear monto
        holder.tvMonto.setText(String.format("$ %.2f", pago.getMonto()));

        // Estado y color
        if (pago.isEstado()) {
            holder.tvEstado.setText("Pagado");
            holder.tvEstado.setTextColor(holder.tvEstado.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvEstado.setText("Pendiente");
            holder.tvEstado.setTextColor(holder.tvEstado.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return pagos.size();
    }

    static class PagoViewHolder extends RecyclerView.ViewHolder {
        TextView tvDetalle, tvFecha, tvMonto, tvEstado;
        PagoViewHolder(View v) {
            super(v);
            tvDetalle = v.findViewById(R.id.tvDetallePago);
            tvFecha = v.findViewById(R.id.tvFechaPago);
            tvMonto = v.findViewById(R.id.tvMontoPago);
            tvEstado = v.findViewById(R.id.tvEstadoPago);
        }
    }
}