package com.financeiro.api.transacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financeiro.api.transacao.TipoTransacao;

public class TransacaoResponse {
	private Long id;
	private String descricao;
	private BigDecimal valor;
	private TipoTransacao tipo;
	private String categoria;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataCadastro;

	public TransacaoResponse() {
	}

	public TransacaoResponse(Long id, String descricao, BigDecimal valor, TipoTransacao tipo, String categoria, LocalDate dataCadastro) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.tipo = tipo;
		this.categoria = categoria;
		this.dataCadastro = dataCadastro;
	}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public TipoTransacao getTipo() {
		return tipo;
	}

	public String getCategoria() {
		return categoria;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}
}