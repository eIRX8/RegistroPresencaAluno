package com.exemplo.registropresenca.data.network;

import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.Presenca;
import retrofit2.Call;
import retrofit2.http.*;

public interface SupabaseApiService {
    // Buscar aluno pelo RA (assumindo que RA é chave primária na tabela CadAluno)
    @GET("CadAluno")
    Call<Aluno[]> getAlunoByRa(@Query("ra") String ra);

    // Inserir presença (usando o endpoint REST do Supabase)
    @Headers({"Content-Type: application/json", "Prefer: return=representation"})
    @POST("Presencas")
    Call<Presenca[]> registrarPresenca(@Body Presenca presenca);
}