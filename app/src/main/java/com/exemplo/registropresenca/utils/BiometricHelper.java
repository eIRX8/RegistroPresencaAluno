package com.exemplo.registropresenca.utils;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class BiometricHelper {
    private final Context context;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public BiometricHelper(FragmentActivity activity) {
        this.context = activity;
        Executor executor = ContextCompat.getMainExecutor(activity);
        biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        if (callback != null) callback.onSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        if (callback != null) callback.onFailure("Falha na autenticação biométrica");
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (callback != null) callback.onFailure(errString.toString());
                    }
                });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Biométrica")
                .setSubtitle("Use sua digital para confirmar sua identidade")
                .setNegativeButtonText("Cancelar")
                .build();
    }

    private BiometricCallback callback;
    public void autenticar(BiometricCallback callback) {
        this.callback = callback;
        BiometricManager manager = BiometricManager.from(context);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                callback.onFailure("Dispositivo não suporta biometria");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                callback.onFailure("Hardware biométrico indisponível");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                callback.onFailure("Nenhuma digital cadastrada no sistema");
                break;
            default:
                callback.onFailure("Erro desconhecido na biometria");
        }
    }

    public interface BiometricCallback {
        void onSuccess();
        void onFailure(String mensagem);
    }
}