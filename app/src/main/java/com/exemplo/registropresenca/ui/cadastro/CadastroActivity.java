package com.exemplo.registropresenca.ui.cadastro;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.local.TokenManager;
import com.exemplo.registropresenca.utils.BiometricHelper;

public class CadastroActivity extends AppCompatActivity {

    private EditText editRa;
    private MaterialButton btnCadastrar;
    private TextView tvMensagem;
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
        tvMensagem = findViewById(R.id.tvMensagem);

        btnCadastrar.setOnClickListener(v -> {
            String ra = editRa.getText().toString().trim();
            if (ra.isEmpty()) {
                tvMensagem.setText("Digite o RA do aluno");
                tvMensagem.setVisibility(android.view.View.VISIBLE);
                return;
            }
            tvMensagem.setVisibility(android.view.View.GONE);
            viewModel.validarRa(ra);
        });

        viewModel.getAluno().observe(this, aluno -> {
            if (aluno != null) {
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
                        tvMensagem.setText("Biometria falhou: " + mensagem);
                        tvMensagem.setVisibility(android.view.View.VISIBLE);
                    }
                });
            }
        });

        viewModel.getError().observe(this, error -> {
            tvMensagem.setText(error);
            tvMensagem.setVisibility(android.view.View.VISIBLE);
        });
    }
}