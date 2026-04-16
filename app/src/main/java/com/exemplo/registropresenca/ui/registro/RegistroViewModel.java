package com.exemplo.registropresenca.ui.registro;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.Presenca;
import com.exemplo.registropresenca.data.model.UnidadeEscolar;
import com.exemplo.registropresenca.data.repository.AlunoRepository;
import com.exemplo.registropresenca.data.repository.PresencaRepository;
import com.exemplo.registropresenca.utils.LocationHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroViewModel extends ViewModel {

    private AlunoRepository alunoRepository;
    private PresencaRepository presencaRepository;

    private MutableLiveData<Aluno> alunoLiveData = new MutableLiveData<>();
    private MutableLiveData<UnidadeEscolar> unidadeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensagemLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Presenca> presencaRegistradaLiveData = new MutableLiveData<>();

    private Aluno alunoAtual;
    private UnidadeEscolar unidadeAtual;

    public RegistroViewModel() {
        alunoRepository = new AlunoRepository();
        presencaRepository = new PresencaRepository();
    }

    public LiveData<Aluno> getAluno() { return alunoLiveData; }
    public LiveData<UnidadeEscolar> getUnidade() { return unidadeLiveData; }
    public LiveData<String> getMensagem() { return mensagemLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
    public LiveData<Presenca> getPresencaRegistrada() { return presencaRegistradaLiveData; }

    public void carregarAluno(String ra) {
        loadingLiveData.setValue(true);

        alunoRepository.buscarAlunoComUnidade(ra, new AlunoRepository.AlunoComUnidadeCallback() {
            @Override
            public void onSuccess(Aluno aluno, UnidadeEscolar unidade) {
                alunoAtual = aluno;
                unidadeAtual = unidade;
                alunoLiveData.setValue(aluno);
                unidadeLiveData.setValue(unidade);
                loadingLiveData.setValue(false);
            }

            @Override
            public void onError(String mensagem) {
                mensagemLiveData.setValue(mensagem);
                loadingLiveData.setValue(false);
            }
        });
    }

    /**
     * Registra a presença do aluno.
     * Verifica distância entre aluno e unidade escolar antes de salvar.
     */
    public void registrarPresenca(Location localizacaoAluno) {
        // Validações
        if (alunoAtual == null) {
            mensagemLiveData.setValue("Dados do aluno não carregados");
            return;
        }

        if (unidadeAtual == null) {
            mensagemLiveData.setValue("Dados da unidade escolar não carregados");
            return;
        }

        if (localizacaoAluno == null) {
            mensagemLiveData.setValue("Localização não disponível");
            return;
        }

        // Coordenadas do aluno
        double latAluno = localizacaoAluno.getLatitude();
        double lngAluno = localizacaoAluno.getLongitude();

        // Coordenadas da unidade escolar
        double latEscola = unidadeAtual.getLatitude();
        double lngEscola = unidadeAtual.getLongitude();

        // Calcula distância em metros
        float distancia = LocationHelper.calcularDistancia(latAluno, lngAluno, latEscola, lngEscola);

        // Tolerância de 100 metros
        if (distancia > 100) {
            String msg = "Você não está na escola!\n" +
                    "Distância: " + (int) distancia + " metros\n" +
                    "Unidade: " + unidadeAtual.getNome();
            mensagemLiveData.setValue(msg);
            return;
        }

        // Prepara dados da presença
        SimpleDateFormat sdfData = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date agora = new Date();

        String data = sdfData.format(agora);
        String horario = sdfHora.format(agora);

        Presenca presenca = new Presenca(
                alunoAtual.getRa(),
                alunoAtual.getNome(),
                alunoAtual.getTurma(),
                data,
                horario,
                latAluno,
                lngAluno,
                "Presente"
        );

        // Salva no Supabase
        loadingLiveData.setValue(true);
        presencaRepository.registrarPresenca(presenca, new PresencaRepository.PresencaCallback() {
            @Override
            public void onSuccess(Presenca presenca) {
                presencaRegistradaLiveData.setValue(presenca);
                loadingLiveData.setValue(false);
                mensagemLiveData.setValue("Presença registrada com sucesso!");
            }

            @Override
            public void onError(String mensagem) {
                mensagemLiveData.setValue("Erro ao salvar presença: " + mensagem);
                loadingLiveData.setValue(false);
            }
        });
    }

    public String getNomeUnidade() {
        return unidadeAtual != null ? unidadeAtual.getNome() : "Não informada";
    }

    public String getFotoUrl() {
        return alunoAtual != null ? alunoAtual.getFotoUrl() : null;
    }
}