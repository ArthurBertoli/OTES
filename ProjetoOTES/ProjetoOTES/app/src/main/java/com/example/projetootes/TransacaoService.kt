package com.example.projetootes

import retrofit2.Call
import retrofit2.http.GET

interface TransacaoService {

    // Se no Java o seu colega criar um @GetMapping("/transacoes"),
    // essa linha abaixo já faz o Front-end ler os dados dele perfeitamente!
    @GET("api/transacoes")
    fun listar(): Call<List<Transacao>>
}