package com.example.projetootes.model;

import java.math.BigDecimal;

public class TransacaoResponse {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private String categoria;
    private String dataCadastro;

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

    public String getDataCadastro() {
        return dataCadastro;
    }
}