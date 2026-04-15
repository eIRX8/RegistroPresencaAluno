package com.exemplo.registropresenca.data.network;

import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.Presenca;
import com.exemplo.registropresenca.data.model.UnidadeEscolar;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Interface com os endpoints da API do Supabase.
 * (Atualizada para suportar UnidadesEscolares)
 */
public interface SupabaseApiService {

    // ===== ALUNOS =====

    /**
     * Busca um aluno pelo RA.
     * @param ra RA com operador eq. (ex: "eq.12345")
     * @return Call que retorna um array de Aluno
     */
    @GET("CadAluno")
    Call<Aluno[]> getAlunoByRa(@Query("ra") String ra);

    // ===== UNIDADES ESCOLARES =====

    /**
     * Busca uma unidade escolar pelo ID.
     * @param id ID com operador eq. (ex: "eq.1")
     * @return Call que retorna um array de UnidadeEscolar
     */
    @GET("UnidadesEscolares")
    Call<UnidadeEscolar[]> getUnidadeById(@Query("id") String id);

    /**
     * Busca todas as unidades escolares (para seleção).
     * @return Call que retorna um array de UnidadeEscolar
     */
    @GET("UnidadesEscolares")
    Call<UnidadeEscolar[]> getAllUnidades();

    // ===== PRESENÇAS =====

    /**
     * Registra uma presença.
     * @param presenca Objeto Presenca a ser inserido
     * @return Call que retorna um array com a presença inserida
     */
    @Headers({"Content-Type: application/json", "Prefer: return=representation"})
    @POST("Presencas")
    Call<Presenca[]> registrarPresenca(@Body Presenca presenca);
}