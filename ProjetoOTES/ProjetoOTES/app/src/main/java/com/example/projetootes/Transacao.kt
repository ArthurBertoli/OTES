package com.example.projetootes

data class Transacao(
    val id: Int,
    val descricao: String,
    val valor: Double,
    val tipo: String // "ENTRADA" ou "SAIDA"
)