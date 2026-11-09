package com.example.projetootes;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetootes.model.TipoTransacao;
import com.example.projetootes.model.Transacao;
import com.example.projetootes.model.TransacaoRequest;
import com.example.projetootes.network.FinanceApiService;
import com.example.projetootes.network.RetrofitClient;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {
	private EditText editDescricao;
	private EditText editValor;
	private Spinner spinnerTipo;
	private EditText editCategoria;
	private EditText editDataCadastro;
	private FinanceApiService apiService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_cadastro);

		apiService = RetrofitClient.getService(this);
		bindViews();
		setupSpinner();
		setupButtons();
		fillTodayDate();
	}

	private void bindViews() {
		editDescricao = findViewById(R.id.editDescricao);
		editValor = findViewById(R.id.editValor);
		spinnerTipo = findViewById(R.id.spinnerTipo);
		editCategoria = findViewById(R.id.editCategoria);
		editDataCadastro = findViewById(R.id.editDataCadastro);
	}

	private void setupSpinner() {
		spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"ENTRADA", "SAIDA"}));
	}

	private void setupButtons() {
		Button buttonSalvar = findViewById(R.id.buttonSalvar);
		Button buttonHoje = findViewById(R.id.buttonHoje);
		Button buttonVoltar = findViewById(R.id.buttonVoltar);

		buttonSalvar.setOnClickListener(v -> salvarTransacao());
		buttonHoje.setOnClickListener(v -> fillTodayDate());
		buttonVoltar.setOnClickListener(v -> finish());
	}

	private void salvarTransacao() {
		String descricao = editDescricao.getText().toString().trim();
		String valorTexto = editValor.getText().toString().trim().replace(",", ".");
		String categoria = editCategoria.getText().toString().trim();
		String data = editDataCadastro.getText().toString().trim();

		if (descricao.isEmpty() || valorTexto.isEmpty() || categoria.isEmpty() || data.isEmpty()) {
			toast("Preencha todos os campos");
			return;
		}

		BigDecimal valor;
		try {
			valor = new BigDecimal(valorTexto);
		} catch (NumberFormatException exception) {
			toast("Valor inválido");
			return;
		}

		TipoTransacao tipo = TipoTransacao.valueOf(spinnerTipo.getSelectedItem().toString());
		TransacaoRequest request = new TransacaoRequest(descricao, valor, tipo, categoria, data);

		apiService.criar(request).enqueue(new Callback<>() {
			@Override
			public void onResponse(Call<Transacao> call, Response<Transacao> response) {
				if (response.isSuccessful()) {
					editDescricao.setText("");
					editValor.setText("");
					editCategoria.setText("");
					fillTodayDate();
					spinnerTipo.setSelection(0);
					toast("Transação salva");
				} else {
					toast("Falha ao salvar");
				}
			}

			@Override
			public void onFailure(Call<Transacao> call, Throwable t) {
				toast("Erro de rede: " + t.getMessage());
			}
		});
	}

	private void fillTodayDate() {
		Calendar calendar = Calendar.getInstance();
		editDataCadastro.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d",
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH)));
	}

	private void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}