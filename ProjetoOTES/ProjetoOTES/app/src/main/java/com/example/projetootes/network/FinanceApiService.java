package com.example.projetootes.network;

import com.example.projetootes.model.ResumoTransacoesResponse;
import com.example.projetootes.model.Transacao;
import com.example.projetootes.model.TransacaoRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FinanceApiService {
    @POST("api/transacoes")
    Call<Transacao> criar(@Body TransacaoRequest request);

    @GET("api/transacoes")
    Call<ResumoTransacoesResponse> listar(
            @Query("tipo") String tipo,
            @Query("categoria") String categoria,
            @Query("mes") Integer mes,
            @Query("ano") Integer ano);

    @DELETE("api/transacoes/{id}")
    Call<Void> excluir(@Path("id") Long id);
}