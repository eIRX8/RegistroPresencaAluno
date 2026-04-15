package com.exemplo.registropresenca.ui.registro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.exemplo.registropresenca.R;
import com.exemplo.registropresenca.data.model.Presenca;

/**
 * Tela de sucesso após registrar presença.
 * Exibe foto e dados da presença registrada.
 */
public class SucessoActivity extends AppCompatActivity {

    private ImageView ivFotoSucesso;
    private TextView tvDadosSucesso;

    /**
     * Método estático para abrir esta tela.
     * @param context Contexto do app
     * @param presenca Objeto Presenca a ser exibido
     * @param nomeUnidade Nome da unidade escolar
     * @param fotoUrl URL da foto do aluno
     */
    public static void start(Context context, Presenca presenca, String nomeUnidade, String fotoUrl) {
        Intent intent = new Intent(context, SucessoActivity.class);
        intent.putExtra("presenca", presenca);
        intent.putExtra("nomeUnidade", nomeUnidade);
        intent.putExtra("fotoUrl", fotoUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucesso);

        ivFotoSucesso = findViewById(R.id.ivFotoSucesso);
        tvDadosSucesso = findViewById(R.id.tvDadosSucesso);

        // Recupera os dados passados
        Presenca p = (Presenca) getIntent().getSerializableExtra("presenca");
        String nomeUnidade = getIntent().getStringExtra("nomeUnidade");
        String fotoUrl = getIntent().getStringExtra("fotoUrl");

        // Carrega a foto
        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            Glide.with(this)
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.ic_default_avatar)
                    .circleCrop()
                    .into(ivFotoSucesso);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_default_avatar)
                    .circleCrop()
                    .into(ivFotoSucesso);
        }

        // Exibe os dados da presença
        if (p != null) {
            String texto = "Nome: " + p.getNome() + "\n" +
                    "Turma: " + p.getTurma() + "\n" +
                    "Unidade: " + (nomeUnidade != null ? nomeUnidade : "Não informada") + "\n" +
                    "Data: " + p.getData() + "\n" +
                    "Horário: " + p.getHorario() + "\n" +
                   // "Localização: " + p.getLatitudeAluno() + ", " + p.getLongitudeAluno() + "\n" +
                    "Status: " + p.getStatus();
            tvDadosSucesso.setText(texto);
        }
    }
}