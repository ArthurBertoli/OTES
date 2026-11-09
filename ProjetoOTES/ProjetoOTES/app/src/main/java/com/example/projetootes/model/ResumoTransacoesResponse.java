package com.example.projetootes.model;

import java.math.BigDecimal;
import java.util.List;

public class ResumoTransacoesResponse {
    private BigDecimal saldoTotal;
    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;
    private List<TransacaoResponse> transacoes;

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