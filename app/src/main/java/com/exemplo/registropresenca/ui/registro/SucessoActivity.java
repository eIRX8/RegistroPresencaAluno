package com.exemplo.registropresenca.ui.registro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.model.Presenca;

public class SucessoActivity extends AppCompatActivity {

    private TextView tvDados;

    // Método para abrir a tela (agora com nome da unidade separado)
    public static void start(Context context, Presenca presenca, String nomeUnidade) {
        Intent intent = new Intent(context, SucessoActivity.class);
        intent.putExtra("presenca", presenca);
        intent.putExtra("nomeUnidade", nomeUnidade);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucesso);
        tvDados = findViewById(R.id.tvDadosSucesso);

        Presenca p = (Presenca) getIntent().getSerializableExtra("presenca");
        String nomeUnidade = getIntent().getStringExtra("nomeUnidade");

        if (p != null) {
            String texto = "Nome: " + p.getNome() + "\n" +
                    "Turma: " + p.getTurma() + "\n" +
                    "Unidade: " + (nomeUnidade != null ? nomeUnidade : "Não informada") + "\n" +
                    "Data: " + p.getData() + "\n" +
                    "Horário: " + p.getHorario() + "\n" +
                    "Localização: " + p.getLatitudeAluno() + ", " + p.getLongitudeAluno() + "\n" +
                    "Status: " + p.getStatus() + "\n\n" +
                    "✅ Presença Registrada. Boa Aula!";
            tvDados.setText(texto);
        }
    }
}