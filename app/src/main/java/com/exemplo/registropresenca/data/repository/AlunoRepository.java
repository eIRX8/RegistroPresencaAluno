package com.exemplo.registropresenca.data.repository;

import android.util.Log;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.UnidadeEscolar;
import com.exemplo.registropresenca.data.network.SupabaseApiService;
import com.exemplo.registropresenca.data.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoRepository {

    private static final String TAG = "AlunoRepo";
    private SupabaseApiService apiService;

    public AlunoRepository() {
        apiService = RetrofitClient.getApiService();
    }

    // ===== CALLBACKS =====

    public interface AlunoCallback {
        void onSuccess(Aluno aluno);
        void onError(String mensagem);
    }

    public interface AlunoComUnidadeCallback {
        void onSuccess(Aluno aluno, UnidadeEscolar unidade);
        void onError(String mensagem);
    }

    public interface UnidadeCallback {
        void onSuccess(UnidadeEscolar unidade);
        void onError(String mensagem);
    }

    // ===== MÉTODO PARA BUSCAR APENAS ALUNO =====

    public void buscarAlunoPorRa(String ra, final AlunoCallback callback) {
        String raComOperador = "eq." + ra;
        Log.d(TAG, "Buscando RA em Matriculas: " + raComOperador);

        Call<Aluno[]> call = apiService.getAlunoByRa(raComOperador);
        call.enqueue(new Callback<Aluno[]>() {
            @Override
            public void onResponse(Call<Aluno[]> call, Response<Aluno[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    Aluno aluno = response.body()[0];
                    Log.d(TAG, "Aluno encontrado em Matriculas: " + aluno.getNome());
                    callback.onSuccess(aluno);
                } else {
                    callback.onError("Aluno não encontrado na base de dados com RA: " + ra);
                }
            }

            @Override
            public void onFailure(Call<Aluno[]> call, Throwable t) {
                Log.e(TAG, "Erro ao buscar em Matriculas", t);
                callback.onError("Erro de rede: " + t.getMessage());
            }
        });
    }

    // ===== MÉTODO PARA BUSCAR ALUNO COM UNIDADE =====

    public void buscarAlunoComUnidade(String ra, final AlunoComUnidadeCallback callback) {
        String raComOperador = "eq." + ra;
        Log.d(TAG, "Buscando RA em Matriculas: " + raComOperador);

        Call<Aluno[]> call = apiService.getAlunoByRa(raComOperador);
        call.enqueue(new Callback<Aluno[]>() {
            @Override
            public void onResponse(Call<Aluno[]> call, Response<Aluno[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    Aluno aluno = response.body()[0];
                    Log.d(TAG, "Aluno encontrado em Matriculas: " + aluno.getNome());

                    buscarUnidadePorId(aluno.getUnidadeId(), new UnidadeCallback() {
                        @Override
                        public void onSuccess(UnidadeEscolar unidade) {
                            callback.onSuccess(aluno, unidade);
                        }

                        @Override
                        public void onError(String mensagem) {
                            callback.onError("Unidade não encontrada: " + mensagem);
                        }
                    });
                } else {
                    callback.onError("Aluno não encontrado na base de dados com RA: " + ra);
                }
            }

            @Override
            public void onFailure(Call<Aluno[]> call, Throwable t) {
                Log.e(TAG, "Erro ao buscar em Matriculas", t);
                callback.onError("Erro de rede: " + t.getMessage());
            }
        });
    }

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
                    callback.onError("Unidade não encontrada: " + id);
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