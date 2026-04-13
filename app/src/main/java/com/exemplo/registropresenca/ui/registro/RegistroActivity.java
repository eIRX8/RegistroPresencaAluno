package com.exemplo.registropresenca.ui.registro;

import android.content.pm.PackageManager;
import android.location.Location;
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
import com.exemplo.registropresenca.utils.LocationHelper;

public class RegistroActivity extends AppCompatActivity {
    private RegistroViewModel viewModel;
    private TokenManager tokenManager;
    private LocationHelper locationHelper;
    private TextView tvInfoAluno, tvMensagem;
    private Button btnRegistrar;
    private Aluno alunoAtual;
    private Location ultimaLocalizacao;


    /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);
        tokenManager = new TokenManager(this);
        locationHelper = new LocationHelper(this);

        tvInfoAluno = findViewById(R.id.tvInfoAluno);
        tvMensagem = findViewById(R.id.tvMensagem);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        String ra = tokenManager.getRA();
        if (ra == null) {
            Toast.makeText(this, "Erro: aluno não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.carregarAluno(ra);
        viewModel.getAluno().observe(this, aluno -> {
            alunoAtual = aluno;
            tvInfoAluno.setText("Aluno: " + aluno.getNome() + "\nTurma: " + aluno.getTurma());
        });

        viewModel.getMensagem().observe(this, msg -> {
            tvMensagem.setText(msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });

        viewModel.getLoading().observe(this, loading -> {
            btnRegistrar.setEnabled(!loading);
        });

        viewModel.getPresencaRegistrada().observe(this, presenca -> {
            // Abre tela de sucesso
            SucessoActivity.start(this, presenca);
        });

        btnRegistrar.setOnClickListener(v -> {
            if (!locationHelper.hasLocationPermission()) {
                locationHelper.requestLocationPermission(this);
                return;
            }
            // Obtém localização atual
           // var task = locationHelper.getCurrentLocation();
            com.google.android.gms.tasks.Task<android.location.Location> task = locationHelper.getCurrentLocation();
            if (task == null) {
                Toast.makeText(this, "Erro ao obter localização", Toast.LENGTH_SHORT).show();
                return;
            }
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    ultimaLocalizacao = location;
                    viewModel.registrarPresenca(alunoAtual, location);
                } else {
                    Toast.makeText(this, "Não foi possível obter localização. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);
        tokenManager = new TokenManager(this);
        locationHelper = new LocationHelper(this);

        tvInfoAluno = findViewById(R.id.tvInfoAluno);
        tvMensagem = findViewById(R.id.tvMensagem);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        String ra = tokenManager.getRA();
        if (ra == null) {
            Toast.makeText(this, "Erro: aluno não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.carregarAluno(ra);
        viewModel.getAluno().observe(this, aluno -> {
            alunoAtual = aluno;
            tvInfoAluno.setText("Aluno: " + aluno.getNome() + "\nTurma: " + aluno.getTurma());
        });

        viewModel.getMensagem().observe(this, msg -> {
            tvMensagem.setText(msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });

        viewModel.getLoading().observe(this, loading -> {
            btnRegistrar.setEnabled(!loading);
        });

        viewModel.getPresencaRegistrada().observe(this, presenca -> {
            SucessoActivity.start(this, presenca);
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica permissão de localização ANTES de qualquer coisa
                if (!locationHelper.hasLocationPermission()) {
                    locationHelper.requestLocationPermission(RegistroActivity.this);
                    Toast.makeText(RegistroActivity.this, "Por favor, conceda permissão de localização", Toast.LENGTH_LONG).show();
                    return;
                }

                // Verifica se a localização está ativa no sistema
                android.location.LocationManager lm = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
                if (!lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(RegistroActivity.this, "Por favor, ative a localização nas configurações do celular", Toast.LENGTH_LONG).show();
                    return;
                }

                // Obtém localização atual
                com.google.android.gms.tasks.Task<android.location.Location> task = locationHelper.getCurrentLocation();
                if (task == null) {
                    Toast.makeText(RegistroActivity.this, "Erro ao obter localização. Tente novamente.", Toast.LENGTH_SHORT).show();
                    return;
                }

                task.addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if (location != null) {
                            ultimaLocalizacao = location;
                            viewModel.registrarPresenca(alunoAtual, location);
                        } else {
                            Toast.makeText(RegistroActivity.this, "Não foi possível obter localização. Tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
