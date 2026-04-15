package com.exemplo.registropresenca.data.repository;

import android.util.Log;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.UnidadeEscolar;
import com.exemplo.registropresenca.data.network.SupabaseApiService;
import com.exemplo.registropresenca.data.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repositório de Aluno (atualizado para suportar unidades escolares).
 *
 * Funcionalidades:
 * - Buscar aluno pelo RA
 * - Buscar unidade escolar associada ao aluno
 * - Mantém compatibilidade com versão anterior (campos lat_escola/lng_escola)
 */
public class AlunoRepository {

    private static final String TAG = "AlunoRepo";
    private SupabaseApiService apiService;

    public AlunoRepository() {
        apiService = RetrofitClient.getApiService();
    }

    // ===== CALLBACKS =====

    /**
     * Interface para callback que retorna aluno e unidade.
     */
    public interface AlunoComUnidadeCallback {
        void onSuccess(Aluno aluno, UnidadeEscolar unidade);
        void onError(String mensagem);
    }

    /**
     * Interface para callback de unidade isolada.
     */
    public interface UnidadeCallback {
        void onSuccess(UnidadeEscolar unidade);
        void onError(String mensagem);
    }

    /**
     * Interface para callback de aluno isolado (legado).
     */
    public interface AlunoCallback {
        void onSuccess(Aluno aluno);
        void onError(String mensagem);
    }

    // ===== MÉTODOS =====

    /**
     * Busca um aluno pelo RA (versão legada - mantém compatibilidade).
     * @param ra Registro do aluno
     * @param callback Interface para retornar sucesso ou erro
     */
    public void buscarAlunoPorRa(String ra, final AlunoCallback callback) {
        String raComOperador = "eq." + ra;
        Log.d(TAG, "Buscando RA: " + raComOperador);

        Call<Aluno[]> call = apiService.getAlunoByRa(raComOperador);
        call.enqueue(new Callback<Aluno[]>() {
            @Override
            public void onResponse(Call<Aluno[]> call, Response<Aluno[]> response) {
                Log.d(TAG, "Código da resposta: " + response.code());

                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    callback.onSuccess(response.body()[0]);
                } else {
                    callback.onError("Aluno não encontrado com RA: " + ra);
                }
            }

            @Override
            public void onFailure(Call<Aluno[]> call, Throwable t) {
                Log.e(TAG, "Erro na API", t);
                callback.onError("Erro de rede: " + t.getMessage());
            }
        });
    }

    /**
     * Busca um aluno pelo RA e também sua unidade escolar (se existir).
     * @param ra Registro do aluno
     * @param callback Interface para retornar aluno e unidade
     */
    public void buscarAlunoComUnidade(String ra, final AlunoComUnidadeCallback callback) {
        String raComOperador = "eq." + ra;
        Log.d(TAG, "Buscando RA com unidade: " + raComOperador);

        Call<Aluno[]> call = apiService.getAlunoByRa(raComOperador);
        call.enqueue(new Callback<Aluno[]>() {
            @Override
            public void onResponse(Call<Aluno[]> call, Response<Aluno[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    Aluno aluno = response.body()[0];

                    // Verifica se o aluno tem unidade associada
                    if (aluno.temUnidadeAssociada()) {
                        // Busca a unidade pelo ID
                        buscarUnidadePorId(aluno.getUnidadeId(), new UnidadeCallback() {
                            @Override
                            public void onSuccess(UnidadeEscolar unidade) {
                                callback.onSuccess(aluno, unidade);
                            }

                            @Override
                            public void onError(String mensagem) {
                                // Fallback: tenta usar coordenadas legadas
                                if (aluno.getLatitudeEscola() != 0 || aluno.getLongitudeEscola() != 0) {
                                    Log.w(TAG, "Unidade não encontrada, usando coordenadas legadas");
                                    UnidadeEscolar unidadeFallback = new UnidadeEscolar(
                                            0,
                                            "Unidade (Legado)",
                                            "Endereço não informado",
                                            aluno.getLatitudeEscola(),
                                            aluno.getLongitudeEscola()
                                    );
                                    callback.onSuccess(aluno, unidadeFallback);
                                } else {
                                    callback.onError("Aluno encontrado, mas erro na unidade: " + mensagem);
                                }
                            }
                        });
                    } else if (aluno.getLatitudeEscola() != 0 || aluno.getLongitudeEscola() != 0) {
                        // Usa coordenadas legadas (aluno não migrado)
                        Log.d(TAG, "Usando coordenadas legadas do aluno");
                        UnidadeEscolar unidadeVirtual = new UnidadeEscolar(
                                0,
                                "Unidade (Dados do Aluno)",
                                "Endereço não informado",
                                aluno.getLatitudeEscola(),
                                aluno.getLongitudeEscola()
                        );
                        callback.onSuccess(aluno, unidadeVirtual);
                    } else {
                        callback.onError("Aluno não possui unidade associada nem coordenadas definidas");
                    }
                } else {
                    callback.onError("Aluno não encontrado com RA: " + ra);
                }
            }

            @Override
            public void onFailure(Call<Aluno[]> call, Throwable t) {
                Log.e(TAG, "Erro na API", t);
                callback.onError("Erro de rede: " + t.getMessage());
            }
        });
    }

    /**
     * Busca uma unidade escolar pelo ID.
     * @param id ID da unidade
     * @param callback Interface para retornar a unidade
     */
    private void buscarUnidadePorId(long id, final UnidadeCallback callback) {
        String idComOperador = "eq." + id;
        Log.d(TAG, "Buscando unidade ID: " + idComOperador);

        Call<UnidadeEscolar[]> call = apiService.getUnidadeById(idComOperador);
        call.enqueue(new Callback<UnidadeEscolar[]>() {
            @Override
            public void onResponse(Call<UnidadeEscolar[]> call, Response<UnidadeEscolar[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    callback.onSuccess(response.body()[0]);
                } else {
                    callback.onError("Unidade escolar não encontrada com ID: " + id);
                }
            }

            @Override
            public void onFailure(Call<UnidadeEscolar[]> call, Throwable t) {
                Log.e(TAG, "Erro ao buscar unidade", t);
                callback.onError("Erro ao buscar unidade: " + t.getMessage());
            }
        });
    }
}