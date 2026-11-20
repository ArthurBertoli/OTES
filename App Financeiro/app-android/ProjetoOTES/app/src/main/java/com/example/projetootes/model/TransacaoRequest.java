package com.example.projetootes.model;

import java.math.BigDecimal;

public class TransacaoRequest {
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private String categoria;
    private String dataCadastro;

    public TransacaoRequest(String descricao, BigDecimal valor, TipoTransacao tipo, String categoria, String dataCadastro) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.dataCadastro = dataCadastro;
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