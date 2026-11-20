package com.financeiro.api.transacao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/** Garante que os filtros de mês e ano funcionam com as datas gravadas pelo SQLite. */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TransacaoFiltroTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TransacaoRepository repository;

	@BeforeEach
	void prepararDados() throws Exception {
		repository.deleteAll();
		criar("Salario junho 2026", "2000.00", "ENTRADA", "Salario", "2026-06-01");
		criar("Mercado junho 2026", "150.00", "SAIDA", "Alimentacao", "2026-06-30");
		criar("Mercado julho 2026", "80.00", "SAIDA", "Alimentacao", "2026-07-01");
		criar("Mercado junho 2025", "60.00", "SAIDA", "Alimentacao", "2025-06-15");
	}

	@Test
	void filtraPorMesEAno() throws Exception {
		mockMvc.perform(get("/api/transacoes").param("mes", "6").param("ano", "2026"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transacoes.length()").value(2))
				.andExpect(jsonPath("$.totalEntradas").value(2000.00))
				.andExpect(jsonPath("$.totalSaidas").value(150.00))
				.andExpect(jsonPath("$.saldoTotal").value(1850.00));
	}

	@Test
	void filtraSoPorAno() throws Exception {
		mockMvc.perform(get("/api/transacoes").param("ano", "2026"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transacoes.length()").value(3));
	}

	@Test
	void filtraSoPorMesEmQualquerAno() throws Exception {
		mockMvc.perform(get("/api/transacoes").param("mes", "6"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transacoes.length()").value(3));
	}

	@Test
	void combinaTipoCategoriaEData() throws Exception {
		mockMvc.perform(get("/api/transacoes").param("tipo", "SAIDA").param("categoria", "aliment")
				.param("mes", "6").param("ano", "2026"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transacoes.length()").value(1))
				.andExpect(jsonPath("$.transacoes[0].dataCadastro").value("2026-06-30"));
	}

	@Test
	void recusaMesInvalido() throws Exception {
		mockMvc.perform(get("/api/transacoes").param("mes", "13"))
				.andExpect(status().isBadRequest());
	}

	private void criar(String descricao, String valor, String tipo, String categoria, String data) throws Exception {
		String corpo = """
				{"descricao": "%s", "valor": %s, "tipo": "%s", "categoria": "%s", "dataCadastro": "%s"}
				""".formatted(descricao, valor, tipo, categoria, data);
		mockMvc.perform(post("/api/transacoes").contentType(MediaType.APPLICATION_JSON).content(corpo))
				.andExpect(status().isCreated());
	}
}
