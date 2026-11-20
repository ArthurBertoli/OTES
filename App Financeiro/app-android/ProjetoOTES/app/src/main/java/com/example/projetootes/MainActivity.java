package com.example.projetootes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetootes.model.ResumoTransacoesResponse;
import com.example.projetootes.network.FinanceApiService;
import com.example.projetootes.network.RetrofitClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
	private final DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00", DecimalFormatSymbols.getInstance(new Locale("pt", "BR")));

	private TextView textSaldoTotal;
	private TextView textTotais;
	private FinanceApiService apiService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_main);

		apiService = RetrofitClient.getService(this);
		textSaldoTotal = findViewById(R.id.textSaldoTotal);
		textTotais = findViewById(R.id.textTotais);

		findViewById(R.id.buttonCadastro).setOnClickListener(v -> startActivity(new Intent(this, CadastroActivity.class)));
		findViewById(R.id.buttonHistorico).setOnClickListener(v -> startActivity(new Intent(this, HistoricoActivity.class)));
		findViewById(R.id.buttonNotificacoes).setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
		findViewById(R.id.buttonAtualizar).setOnClickListener(v -> carregarResumo());

		carregarResumo();
	}

	@Override
	protected void onResume() {
		super.onResume();
		carregarResumo();
	}

	private void carregarResumo() {
		apiService.listar(null, null, null, null).enqueue(new Callback<>() {
			@Override
			public void onResponse(Call<ResumoTransacoesResponse> call, Response<ResumoTransacoesResponse> response) {
				if (response.isSuccessful() && response.body() != null) {
					ResumoTransacoesResponse resumo = response.body();
					textSaldoTotal.setText(currencyFormat.format(resumo.getSaldoTotal()));
					textTotais.setText("Entradas: " + currencyFormat.format(resumo.getTotalEntradas()) + " | Saídas: " + currencyFormat.format(resumo.getTotalSaidas()));
				} else {
					toast("Falha ao carregar o resumo");
				}
			}

			@Override
			public void onFailure(Call<ResumoTransacoesResponse> call, Throwable t) {
				toast("Erro de rede: " + t.getMessage());
			}
		});
	}

	private void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}