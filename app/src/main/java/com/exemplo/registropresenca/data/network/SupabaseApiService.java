package com.exemplo.registropresenca.data.network;

import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.Curso;
import com.exemplo.registropresenca.data.model.Presenca;
import com.exemplo.registropresenca.data.model.Turma;
import com.exemplo.registropresenca.data.model.UnidadeEscolar;
import retrofit2.Call;
import retrofit2.http.*;

public interface SupabaseApiService {

    // ===== CADASTRO LEGADO (CadAluno) =====
    @GET("CadAluno")
    Call<Aluno[]> getAlunoByRaLegado(@Query("ra") String ra);

    // ===== MATRICULAS (Nova tabela) =====
    @GET("Matriculas")
    Call<Aluno[]> getAlunoByRa(@Query("ra") String ra);

    // ===== UNIDADES ESCOLARES =====
    @GET("UnidadesEscolares")
    Call<UnidadeEscolar[]> getUnidadeById(@Query("id") String id);

    // ===== TURMAS =====
    @GET("Turmas")
    Call<Turma[]> getTurmaById(@Query("id") String id);

    // ===== CURSOS =====
    @GET("Cursos")
    Call<Curso[]> getCursoById(@Query("id") String id);

    // ===== PRESENÇAS =====
    @Headers({"Content-Type: application/json", "Prefer: return=representation"})
    @POST("Presencas")
    Call<Presenca[]> registrarPresenca(@Body Presenca presenca);
}