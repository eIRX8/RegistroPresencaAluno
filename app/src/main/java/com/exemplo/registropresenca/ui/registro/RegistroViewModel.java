package com.exemplo.registropresenca.ui.registro;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.model.Presenca;
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
    private MutableLiveData<String> mensagemLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Presenca> presencaRegistradaLiveData = new MutableLiveData<>();

    public RegistroViewModel() {
        alunoRepository = new AlunoRepository();
        presencaRepository = new PresencaRepository();
    }

    public LiveData<Aluno> getAluno() { return alunoLiveData; }
    public LiveData<String> getMensagem() { return mensagemLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
    public LiveData<Presenca> getPresencaRegistrada() { return presencaRegistradaLiveData; }

    public void carregarAluno(String ra) {
        loadingLiveData.setValue(true);
        alunoRepository.buscarAlunoPorRa(ra, new AlunoRepository.AlunoCallback() {
            @Override
            public void onSuccess(Aluno aluno) {
                alunoLiveData.setValue(aluno);
                loadingLiveData.setValue(false);
            }

            @Override
            public void onError(String mensagem) {
                mensagemLiveData.setValue(mensagem);
                loadingLiveData.setValue(false);
            }
        });
    }

    public void registrarPresenca(Aluno aluno, Location localizacaoAluno) {
        if (aluno == null || localizacaoAluno == null) {
            mensagemLiveData.setValue("Dados incompletos para registro");
            return;
        }

        double latAluno = localizacaoAluno.getLatitude();
        double lngAluno = localizacaoAluno.getLongitude();
        double latEscola = aluno.getLatitudeEscola();
        double lngEscola = aluno.getLongitudeEscola();

        float distancia = LocationHelper.calcularDistancia(latAluno, lngAluno, latEscola, lngEscola);
        if (distancia > 100) { // tolerância 100 metros
            mensagemLiveData.setValue("Sua presença não foi registrada pois você não está na escola (distância: " + (int)distancia + "m)");
            return;
        }

        // Registra presença
        SimpleDateFormat sdfData = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String data = sdfData.format(new Date());
        String horario = sdfHora.format(new Date());

        Presenca presenca = new Presenca(
                aluno.getRa(),
                aluno.getNome(),
                aluno.getTurma(),
                data,
                horario,
                latAluno,
                lngAluno,
                "Presente"
        );

        loadingLiveData.setValue(true);
        presencaRepository.registrarPresenca(presenca, new PresencaRepository.PresencaCallback() {
            @Override
            public void onSuccess(Presenca presenca) {
                presencaRegistradaLiveData.setValue(presenca);
                loadingLiveData.setValue(false);
            }

            @Override
            public void onError(String mensagem) {
                mensagemLiveData.setValue("Erro ao salvar presença: " + mensagem);
                loadingLiveData.setValue(false);
            }
        });
    }
}