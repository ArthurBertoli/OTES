package com.financeiro.api.transacao.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumoTransacoesResponse {

	private final BigDecimal saldoTotal;
	private final BigDecimal totalEntradas;
	private final BigDecimal totalSaidas;
	private final List<TransacaoResponse> transacoes;

	public ResumoTransacoesResponse(BigDecimal saldoTotal, BigDecimal totalEntradas, BigDecimal totalSaidas, List<TransacaoResponse> transacoes) {
		this.saldoTotal = saldoTotal;
		this.totalEntradas = totalEntradas;
		this.totalSaidas = totalSaidas;
		this.transacoes = transacoes;
	}

	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}

	public BigDecimal getTotalEntradas() {
		return totalEntradas;
	}

	public BigDecimal getTotalSaidas() {
		return totalSaidas;
	}

	public List<TransacaoResponse> getTransacoes() {
		return transacoes;
	}
}