package com.exemplo.registropresenca.data.repository;


import android.util.Log;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.network.SupabaseApiService;
import com.exemplo.registropresenca.data.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoRepository {
    private SupabaseApiService apiService;

    public AlunoRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public void buscarAlunoPorRa(String ra, final AlunoCallback callback) {
        //Call<Aluno[]> call = apiService.getAlunoByRa(ra);
        String raComOperador = "eq." + ra;
        Call<Aluno[]> call = apiService.getAlunoByRa(raComOperador);
        call.enqueue(new Callback<Aluno[]>() {
            @Override
            public void onResponse(Call<Aluno[]> call, Response<Aluno[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    callback.onSuccess(response.body()[0]);
                } else {
                    callback.onError("Aluno não encontrado com RA: " + ra);
                }
            }

            @Override
            public void onFailure(Call<Aluno[]> call, Throwable t) {
                Log.e("AlunoRepo", "Erro na API", t);
                callback.onError("Erro de rede: " + t.getMessage());
            }
        });
    }

    public interface AlunoCallback {
        void onSuccess(Aluno aluno);
        void onError(String mensagem);
    }
}