package com.exemplo.registropresenca.ui.cadastro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.local.TokenManager;
import com.exemplo.registropresenca.utils.BiometricHelper;

public class CadastroActivity extends AppCompatActivity {
    private EditText editRa;
    private Button btnCadastrar;
    private CadastroViewModel viewModel;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        tokenManager = new TokenManager(this);
        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        editRa = findViewById(R.id.editRa);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            String ra = editRa.getText().toString().trim();
            if (ra.isEmpty()) {
                Toast.makeText(this, "Digite o RA", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.validarRa(ra);
        });

        viewModel.getAluno().observe(this, aluno -> {
            if (aluno != null) {
                // RA válido, agora associa com biometria
                BiometricHelper helper = new BiometricHelper(this);
                helper.autenticar(new BiometricHelper.BiometricCallback() {
                    @Override
                    public void onSuccess() {
                        tokenManager.salvarRA(aluno.getRa());
                        Toast.makeText(CadastroActivity.this, "Cadastro concluído!", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(String mensagem) {
                        Toast.makeText(CadastroActivity.this, "Biometria falhou: " + mensagem, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        viewModel.getError().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }
}