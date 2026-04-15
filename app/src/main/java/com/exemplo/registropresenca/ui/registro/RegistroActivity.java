package com.exemplo.registropresenca.ui.registro;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.local.TokenManager;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.UnidadeEscolar;
import com.exemplo.registropresenca.utils.LocationHelper;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Tela de registro de presença.
 * Exibe informações do aluno e unidade em uma única caixa.
 */
public class RegistroActivity extends AppCompatActivity {

    private RegistroViewModel viewModel;
    private TokenManager tokenManager;
    private LocationHelper locationHelper;

    private TextView tvInfoAluno;      // Caixa única com aluno + turma + unidade
    private TextView tvMensagem;
    private Button btnRegistrar;

    private Aluno alunoAtual;
    private UnidadeEscolar unidadeAtual;
    private Location ultimaLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializa componentes
        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);
        tokenManager = new TokenManager(this);
        locationHelper = new LocationHelper(this);

        // Conecta elementos da UI
        tvInfoAluno = findViewById(R.id.tvInfoAluno);
        tvMensagem = findViewById(R.id.tvMensagem);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Recupera RA do aluno
        String ra = tokenManager.getRA();
        if (ra == null) {
            Toast.makeText(this, "Erro: aluno não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Carrega dados do aluno
        viewModel.carregarAluno(ra);

        // Observa o aluno
        viewModel.getAluno().observe(this, aluno -> {
            alunoAtual = aluno;
            atualizarInfoCompleta();
        });

        // Observa a unidade escolar
        viewModel.getUnidade().observe(this, unidade -> {
            unidadeAtual = unidade;
            atualizarInfoCompleta();
        });

        // Observa mensagens
        viewModel.getMensagem().observe(this, msg -> {
            tvMensagem.setText(msg);
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        });

        // Observa loading
        viewModel.getLoading().observe(this, loading -> {
            btnRegistrar.setEnabled(!loading);
        });

        // Observa presença registrada - MODIFICADO para passar o nome da unidade
        viewModel.getPresencaRegistrada().observe(this, presenca -> {
            if (unidadeAtual != null) {
                SucessoActivity.start(this, presenca, unidadeAtual.getNome());
            } else {
                SucessoActivity.start(this, presenca, "Não informada");
            }
        });

        // Configura botão de registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermissoesERegistrar();
            }
        });
    }

    /**
     * Atualiza a exibição das informações do aluno e unidade em uma única caixa.
     */
    private void atualizarInfoCompleta() {
        StringBuilder texto = new StringBuilder();

        if (alunoAtual != null) {
            texto.append("Aluno: ").append(alunoAtual.getNome()).append("\n");
            texto.append("Turma: ").append(alunoAtual.getTurma());

            if (unidadeAtual != null) {
                texto.append("\nUnidade: ").append(unidadeAtual.getNome());
            }
        }

        tvInfoAluno.setText(texto.toString());
    }

    /**
     * Verifica permissões e GPS antes de registrar.
     */
    private void verificarPermissoesERegistrar() {
        // Verifica permissão de localização
        if (!locationHelper.hasLocationPermission()) {
            locationHelper.requestLocationPermission(this);
            Toast.makeText(this, "Por favor, conceda permissão de localização", Toast.LENGTH_LONG).show();
            return;
        }

        // Verifica se o GPS está ligado
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Por favor, ative a localização nas configurações do celular", Toast.LENGTH_LONG).show();
            return;
        }

        // Obtém localização atual
        com.google.android.gms.tasks.Task<Location> task = locationHelper.getCurrentLocation();
        if (task == null) {
            Toast.makeText(this, "Erro ao obter localização. Tente novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    ultimaLocalizacao = location;
                    viewModel.registrarPresenca(alunoAtual, unidadeAtual, location);
                } else {
                    Toast.makeText(RegistroActivity.this, "Não foi possível obter localização. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationHelper.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão de localização concedida. Clique novamente em Registrar.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sua presença não será registrada. Favor autorizar a localização.", Toast.LENGTH_LONG).show();
            }
        }
    }
}