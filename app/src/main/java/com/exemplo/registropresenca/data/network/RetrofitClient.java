package com.exemplo.registropresenca.data.network;

import java.io.IOException;
import com.exemplo.registropresenca.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://ugywmrmrimouwvqwmuqj.supabase.co/rest/v1/";
    //private static final String BASE_URL = BuildConfig.SUPABASE_URL; // ex: https://seuprojeto.supabase.co

    public static SupabaseApiService getApiService() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                   /* .addInterceptor(chain -> {
                        var request = chain.request().newBuilder()
                                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                                .addHeader("Authorization", "Bearer " + BuildConfig.SUPABASE_ANON_KEY)
                                .build();
                        return chain.proceed(request);
                    })
                    */
                    // Código corrigido com classe anônima
                    .addInterceptor(new okhttp3.Interceptor() {
                        @Override
                        public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
                            okhttp3.Request request = chain.request().newBuilder()
                                    .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                                    .addHeader("Authorization", "Bearer " + BuildConfig.SUPABASE_ANON_KEY)
                                    .build();
                            return chain.proceed(request);
                        }
                    })

                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(SupabaseApiService.class);
    }
}