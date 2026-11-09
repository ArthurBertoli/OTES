package com.example.projetootes;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetootes.model.ResumoTransacoesResponse;
import com.example.projetootes.model.TransacaoResponse;
import com.example.projetootes.network.FinanceApiService;
import com.example.projetootes.network.RetrofitClient;
import com.example.projetootes.ui.TransacaoAdapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoActivity extends AppCompatActivity {
	private final DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00", DecimalFormatSymbols.getInstance(new Locale("pt", "BR")));
	private TextView textSaldoTotal;
	private TextView textTotais;
	private Spinner spinnerFiltroTipo;
	private EditText editFiltroCategoria;
	private EditText editFiltroMes;
	private EditText editFiltroAno;
	private TransacaoAdapter adapter;
	private FinanceApiService apiService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_historico);

		apiService = RetrofitClient.getService(this);
		bindViews();
		setupSpinner();
		setupRecyclerView();
		setupButtons();
		carregarTransacoes();
	}

	private void bindViews() {
		textSaldoTotal = findViewById(R.id.textSaldoTotal);
		textTotais = findViewById(R.id.textTotais);
		spinnerFiltroTipo = findViewById(R.id.spinnerFiltroTipo);
		editFiltroCategoria = findViewById(R.id.editFiltroCategoria);
		editFiltroMes = findViewById(R.id.editFiltroMes);
		editFiltroAno = findViewById(R.id.editFiltroAno);
	}

	private void setupSpinner() {
		spinnerFiltroTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Todos", "ENTRADA", "SAIDA"}));
	}

	private void setupRecyclerView() {
		RecyclerView recyclerView = findViewById(R.id.recyclerViewTransacoes);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new TransacaoAdapter(this::excluirTransacao);
		recyclerView.setAdapter(adapter);
	}

	private void setupButtons() {
		Button buttonAplicar = findViewById(R.id.buttonAplicarFiltros);
		Button buttonLimpar = findViewById(R.id.buttonLimparFiltros);
		Button buttonVoltar = findViewById(R.id.buttonVoltar);

		buttonAplicar.setOnClickListener(v -> carregarTransacoes());
		buttonLimpar.setOnClickListener(v -> {
			spinnerFiltroTipo.setSelection(0);
			editFiltroCategoria.setText("");
			editFiltroMes.setText("");
			editFiltroAno.setText("");
			carregarTransacoes();
		});
		buttonVoltar.setOnClickListener(v -> finish());
	}

	private void carregarTransacoes() {
		String tipo = spinnerFiltroTipo.getSelectedItemPosition() == 0 ? null : spinnerFiltroTipo.getSelectedItem().toString();
		String categoria = emptyToNull(editFiltroCategoria.getText().toString());
		Integer mes = parseIntegerOrNull(editFiltroMes.getText().toString());
		Integer ano = parseIntegerOrNull(editFiltroAno.getText().toString());

		apiService.listar(tipo, categoria, mes, ano).enqueue(new Callback<>() {
			@Override
			public void onResponse(Call<ResumoTransacoesResponse> call, Response<ResumoTransacoesResponse> response) {
				if (response.isSuccessful() && response.body() != null) {
					ResumoTransacoesResponse resumo = response.body();
					textSaldoTotal.setText(currencyFormat.format(resumo.getSaldoTotal()));
					textTotais.setText("Entradas: " + currencyFormat.format(resumo.getTotalEntradas()) + " | Saídas: " + currencyFormat.format(resumo.getTotalSaidas()));
					adapter.submitList(resumo.getTransacoes());
				} else {
					toast("Falha ao carregar histórico");
				}
			}

			@Override
			public void onFailure(Call<ResumoTransacoesResponse> call, Throwable t) {
				toast("Erro de rede: " + t.getMessage());
			}
		});
	}

	private void excluirTransacao(TransacaoResponse transacao) {
		if (transacao.getId() == null) {
			return;
		}

		apiService.excluir(transacao.getId()).enqueue(new Callback<>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful() || response.code() == 204) {
					carregarTransacoes();
					toast("Transação excluída");
				}
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				toast("Erro de rede: " + t.getMessage());
			}
		});
	}

	private static Integer parseIntegerOrNull(String value) {
		String sanitized = value == null ? "" : value.trim();
		if (sanitized.isEmpty()) {
			return null;
		}
		try {
			return Integer.parseInt(sanitized);
		} catch (NumberFormatException exception) {
			return null;
		}
	}

	private static String emptyToNull(String value) {
		if (value == null) {
			return null;
		}
		String sanitized = value.trim();
		return sanitized.isEmpty() ? null : sanitized;
	}

	private void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}