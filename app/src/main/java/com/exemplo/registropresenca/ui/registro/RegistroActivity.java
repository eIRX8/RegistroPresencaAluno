package com.exemplo.registropresenca.ui.registro;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.local.TokenManager;
import com.exemplo.registropresenca.utils.LocationHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

public class RegistroActivity extends AppCompatActivity {

    private RegistroViewModel viewModel;
    private TokenManager tokenManager;
    private LocationHelper locationHelper;

    private ImageView ivFotoAluno;
    private TextView txtNome;
    private TextView txtTurma;
    private TextView txtUnidade;
    //private TextView tvMensagem;
    private MaterialButton botaoPresenca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);
        tokenManager = new TokenManager(this);
        locationHelper = new LocationHelper(this);

        ivFotoAluno = findViewById(R.id.ivFotoAluno);
        txtNome = findViewById(R.id.txtNome);
        txtTurma = findViewById(R.id.txtTurma);
        txtUnidade = findViewById(R.id.txtUnidade);
       // tvMensagem = findViewById(R.id.tvMensagem);
        botaoPresenca = findViewById(R.id.botaoPresenca);

        String ra = tokenManager.getRA();
        if (ra == null) {
            Toast.makeText(this, "Erro: aluno não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.carregarAluno(ra);

        // Observa aluno
        viewModel.getAluno().observe(this, aluno -> {
            if (aluno != null) {
                txtNome.setText(aluno.getNome());
                txtTurma.setText(aluno.getTurma());

                if (aluno.getFotoUrl() != null && !aluno.getFotoUrl().isEmpty()) {
                    Glide.with(this)
                            .load(aluno.getFotoUrl())
                            .circleCrop()
                            .into(ivFotoAluno);
                }
            }
        });

        // Observa unidade
        viewModel.getUnidade().observe(this, unidade -> {
            if (unidade != null) {
                txtUnidade.setText(unidade.getNome());
            }
        });

        // Observa mensagens
 /*     viewModel.getMensagem().observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                tvMensagem.setText(msg);
                tvMensagem.setVisibility(View.VISIBLE);
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

                // Esconde mensagem após 5 segundos
                tvMensagem.postDelayed(() -> tvMensagem.setVisibility(View.GONE), 5000);
            }
       });
  */

        // Observa mensagens (apenas Toast)
        viewModel.getMensagem().observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(RegistroActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

        // Observa loading
        viewModel.getLoading().observe(this, loading -> {
            botaoPresenca.setEnabled(!loading);
            botaoPresenca.setText(loading ? "REGISTRANDO..." : "REGISTRAR PRESENÇA");
        });

        // Observa presença registrada
        viewModel.getPresencaRegistrada().observe(this, presenca -> {
            SucessoActivity.start(this, presenca, viewModel.getNomeUnidade(), viewModel.getFotoUrl());
        });

        // Botão Registrar
        botaoPresenca.setOnClickListener(v -> registrarPresenca());
    }

    private void registrarPresenca() {
        // Verifica permissão de localização
        if (!locationHelper.hasLocationPermission()) {
            locationHelper.requestLocationPermission(this);
            Toast.makeText(this, "Permissão de localização necessária", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se GPS está ligado
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Por favor, ative o GPS nas configurações", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtém localização
        com.google.android.gms.tasks.Task<Location> task = locationHelper.getCurrentLocation();
        if (task == null) {
            Toast.makeText(this, "Erro ao obter localização", Toast.LENGTH_SHORT).show();
            return;
        }

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    viewModel.registrarPresenca(location);
                } else {
                    Toast.makeText(RegistroActivity.this,
                            "Localização não disponível. Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationHelper.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão concedida! Clique em Registrar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "A localização é necessária para registrar presença", Toast.LENGTH_LONG).show();
            }
        }
    }
}