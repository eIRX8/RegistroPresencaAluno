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

    public static void start(Context context, Presenca presenca) {
        Intent intent = new Intent(context, SucessoActivity.class);
        intent.putExtra("presenca", presenca);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucesso);
        tvDados = findViewById(R.id.tvDadosSucesso);

        Presenca p = (Presenca) getIntent().getSerializableExtra("presenca");
        if (p != null) {
            String texto = "Nome: " + p.getNome() + "\n" +
                    "Turma: " + p.getTurma() + "\n" +
                    "Data: " + p.getData() + "\n" +
                    "Horário: " + p.getHorario() + "\n" +
                    "Localização: " + p.getLatitudeAluno() + ", " + p.getLongitudeAluno() + "\n" +
                    "Status: " + p.getStatus() + "\n\n" +
                    "✅ Presença Registrada. Boa Aula!";
            tvDados.setText(texto);
        }
    }
}