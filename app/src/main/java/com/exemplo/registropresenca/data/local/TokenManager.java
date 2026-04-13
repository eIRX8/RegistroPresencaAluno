package com.exemplo.registropresenca.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {
    private static final String PREF_NAME = "secure_prefs";
    private static final String KEY_RA = "aluno_ra";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Fallback para SharedPreferences normal (não seguro, apenas para desenvolvimento)
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public void salvarRA(String ra) {
        sharedPreferences.edit().putString(KEY_RA, ra).apply();
    }

    public String getRA() {
        return sharedPreferences.getString(KEY_RA, null);
    }

    public void limparRA() {
        sharedPreferences.edit().remove(KEY_RA).apply();
    }

    public boolean isFirstRun() {
        return getRA() == null;
    }
}