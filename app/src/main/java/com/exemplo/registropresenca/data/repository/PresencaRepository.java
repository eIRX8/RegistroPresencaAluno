package com.exemplo.registropresenca.data.repository;

import android.util.Log;

import com.exemplo.registropresenca.data.model.Presenca;
import com.exemplo.registropresenca.data.network.RetrofitClient;
import com.exemplo.registropresenca.data.network.SupabaseApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresencaRepository {
    private SupabaseApiService apiService;

    public PresencaRepository() {
        apiService = RetrofitClient.getApiService();
    }

   /* public void registrarPresenca(Presenca presenca, final PresencaCallback callback) {
        Call<Presenca> call = apiService.registrarPresenca(presenca);
        call.enqueue(new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao registrar: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                Log.e("PresencaRepo", "Erro", t);
                callback.onError("Falha de rede: " + t.getMessage());
            }
        });
    }   ORIGINAL */

   /*  1cORRECAO NAO FUNCIONOU public void registrarPresenca(Presenca presenca, final PresencaCallback callback) {
        // Converte para JSON e verifica o formato
        Gson gson = new Gson();
        String json = gson.toJson(presenca);
        Log.d("PresencaRepo", "JSON enviado: " + json);

        Call<Presenca> call = apiService.registrarPresenca(presenca);
        call.enqueue(new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                Log.d("PresencaRepo", "Código resposta: " + response.code());
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String erro = "Erro ao registrar: ";
                    if (response.errorBody() != null) {
                        try {
                            erro += response.errorBody().string();
                        } catch (Exception e) {
                            erro += e.getMessage();
                        }
                    }
                    callback.onError(erro);
                }
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                Log.e("PresencaRepo", "Falha", t);
                callback.onError("Falha de rede: " + t.getMessage());
            }
        });
    } */

    /*
    public void registrarPresenca(Presenca presenca, final PresencaCallback callback) {
        try {
            // Converte para JSON manualmente para garantir o formato
            Gson gson = new Gson();
            String json = gson.toJson(presenca);
            Log.d("PresencaRepo", "=== JSON ENVIADO ===");
            Log.d("PresencaRepo", json);
            Log.d("PresencaRepo", "==================");

            // Faz a chamada
            Call<Presenca> call = apiService.registrarPresenca(presenca);
            call.enqueue(new Callback<Presenca>() {
                @Override
                public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                    Log.d("PresencaRepo", "Código HTTP: " + response.code());

                    if (response.isSuccessful()) {
                        Log.d("PresencaRepo", "Sucesso! Presença registrada.");
                        callback.onSuccess(response.body());
                    } else {
                        String erroMsg = "Erro HTTP " + response.code();
                        if (response.errorBody() != null) {
                            try {
                                erroMsg = response.errorBody().string();
                                Log.e("PresencaRepo", "Erro body: " + erroMsg);
                            } catch (Exception e) {
                                erroMsg = e.getMessage();
                            }
                        }
                        callback.onError("Erro ao registrar: " + erroMsg);
                    }
                }

                @Override
                public void onFailure(Call<Presenca> call, Throwable t) {
                    Log.e("PresencaRepo", "Falha na requisição", t);
                    callback.onError("Falha de rede: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PresencaRepo", "Erro ao preparar requisição", e);
            callback.onError("Erro interno: " + e.getMessage());
        }
    }
    */

    public void registrarPresenca(Presenca presenca, final PresencaCallback callback) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(presenca);
            Log.d("PresencaRepo", "=== JSON ENVIADO ===");
            Log.d("PresencaRepo", json);
            Log.d("PresencaRepo", "==================");

            // Agora espera um array como resposta
            Call<Presenca[]> call = apiService.registrarPresenca(presenca);
            call.enqueue(new Callback<Presenca[]>() {
                @Override
                public void onResponse(Call<Presenca[]> call, Response<Presenca[]> response) {
                    Log.d("PresencaRepo", "Código HTTP: " + response.code());

                    if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                        Log.d("PresencaRepo", "Sucesso! Presença registrada.");
                        callback.onSuccess(response.body()[0]); // Pega o primeiro elemento do array
                    } else {
                        String erroMsg = "Erro HTTP " + response.code();
                        if (response.errorBody() != null) {
                            try {
                                erroMsg = response.errorBody().string();
                                Log.e("PresencaRepo", "Erro body: " + erroMsg);
                            } catch (Exception e) {
                                erroMsg = e.getMessage();
                            }
                        }
                        callback.onError("Erro ao registrar: " + erroMsg);
                    }
                }

                @Override
                public void onFailure(Call<Presenca[]> call, Throwable t) {
                    Log.e("PresencaRepo", "Falha na requisição", t);
                    callback.onError("Falha de rede: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PresencaRepo", "Erro ao preparar requisição", e);
            callback.onError("Erro interno: " + e.getMessage());
        }
    }

    public interface PresencaCallback {
        void onSuccess(Presenca presenca);
        void onError(String mensagem);
    }
}