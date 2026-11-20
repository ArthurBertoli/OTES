package com.example.projetootes.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetootes.R;
import com.example.projetootes.model.TransacaoResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransacaoAdapter extends RecyclerView.Adapter<TransacaoAdapter.ViewHolder> {
    public interface OnExcluirClickListener {
        void onExcluirClick(TransacaoResponse transacao);
    }

    private final List<TransacaoResponse> items = new ArrayList<>();
    private final OnExcluirClickListener excluirClickListener;
    private final DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00", DecimalFormatSymbols.getInstance(new Locale("pt", "BR")));

    public TransacaoAdapter(OnExcluirClickListener excluirClickListener) {
        this.excluirClickListener = excluirClickListener;
    }

    public void submitList(List<TransacaoResponse> transacoes) {
        items.clear();
        if (transacoes != null) {
            items.addAll(transacoes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransacaoResponse item = items.get(position);
        String descricao = item.getDescricao() != null ? item.getDescricao() : "Sem descrição";
        String tipo = item.getTipo() != null ? item.getTipo().name() : "-";
        holder.textDescricao.setText(descricao);
        holder.textMeta.setText(safeText(item.getCategoria()) + " • " + tipo + " • " + safeText(item.getDataCadastro()));
        holder.textValor.setText(formatValue(item.getValor(), tipo));
        holder.textValor.setTextColor("ENTRADA".equals(tipo) ? Color.parseColor("#15803D") : Color.parseColor("#B91C1C"));
        holder.buttonExcluir.setOnClickListener(v -> excluirClickListener.onExcluirClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatValue(BigDecimal valor, String tipo) {
        BigDecimal safeValue = valor == null ? BigDecimal.ZERO : valor;
        String formatted = currencyFormat.format(safeValue);
        return "ENTRADA".equals(tipo) ? "+ " + formatted : "- " + formatted;
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textDescricao;
        final TextView textMeta;
        final TextView textValor;
        final Button buttonExcluir;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textMeta = itemView.findViewById(R.id.textMeta);
            textValor = itemView.findViewById(R.id.textValor);
            buttonExcluir = itemView.findViewById(R.id.buttonExcluir);
        }
    }
}