package com.exemplo.registropresenca.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.exemplo.registropresenca.MainActivity; // será a RegistroActivity
import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.local.TokenManager;
import com.exemplo.registropresenca.ui.cadastro.CadastroActivity;
import com.exemplo.registropresenca.ui.registro.RegistroActivity;
import com.exemplo.registropresenca.utils.BiometricHelper;

public class BiometricActivity extends AppCompatActivity {
    private BiometricHelper biometricHelper;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        tokenManager = new TokenManager(this);

        // Verifica se é primeira execução
        if (tokenManager.isFirstRun()) {
            startActivity(new Intent(this, CadastroActivity.class));
            finish();
            return;
        }

        biometricHelper = new BiometricHelper(this);
        biometricHelper.autenticar(new BiometricHelper.BiometricCallback() {
            @Override
            public void onSuccess() {
                // RA já está salvo
                startActivity(new Intent(BiometricActivity.this, RegistroActivity.class));
                finish();
            }

            @Override
            public void onFailure(String mensagem) {
                Toast.makeText(BiometricActivity.this, "Erro: " + mensagem, Toast.LENGTH_LONG).show();
                // Permite tentar novamente? Fecha o app ou reinicia
                finish();
            }
        });
    }
}
