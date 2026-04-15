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

/**
 * ViewModel da tela de registro de presença.
 */
public class RegistroViewModel extends ViewModel {

    private AlunoRepository alunoRepository;
    private PresencaRepository presencaRepository;

    // LiveData para a UI
    private MutableLiveData<Aluno> alunoLiveData = new MutableLiveData<>();
    private MutableLiveData<UnidadeEscolar> unidadeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensagemLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Presenca> presencaRegistradaLiveData = new MutableLiveData<>();

    // Dados temporários
    private Aluno alunoAtual;
    private UnidadeEscolar unidadeAtual;

    public RegistroViewModel() {
        alunoRepository = new AlunoRepository();
        presencaRepository = new PresencaRepository();
    }

    // Getters para a UI
    public LiveData<Aluno> getAluno() { return alunoLiveData; }
    public LiveData<UnidadeEscolar> getUnidade() { return unidadeLiveData; }
    public LiveData<String> getMensagem() { return mensagemLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
    public LiveData<Presenca> getPresencaRegistrada() { return presencaRegistradaLiveData; }

    /**
     * Carrega os dados do aluno e sua unidade escolar.
     * @param ra Registro do aluno
     */
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

                // Log para debug
                android.util.Log.d("RegistroVM", "Aluno: " + aluno.getNome());
                android.util.Log.d("RegistroVM", "Unidade: " + unidade.getNome());
                android.util.Log.d("RegistroVM", "Lat: " + unidade.getLatitude() + ", Lng: " + unidade.getLongitude());
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
     * @param aluno Aluno que está registrando
     * @param unidade Unidade escolar do aluno
     * @param localizacaoAluno Localização GPS atual do aluno
     */
    public void registrarPresenca(Aluno aluno, UnidadeEscolar unidade, Location localizacaoAluno) {
        // Validação
        if (aluno == null) {
            mensagemLiveData.setValue("Dados do aluno não carregados");
            return;
        }

        if (unidade == null) {
            mensagemLiveData.setValue("Dados da unidade escolar não carregados");
            return;
        }

        if (localizacaoAluno == null) {
            mensagemLiveData.setValue("Localização não disponível");
            return;
        }

        // Obtém coordenadas
        double latAluno = localizacaoAluno.getLatitude();
        double lngAluno = localizacaoAluno.getLongitude();
        double latEscola = unidade.getLatitude();
        double lngEscola = unidade.getLongitude();

        // Calcula distância
        float distancia = LocationHelper.calcularDistancia(latAluno, lngAluno, latEscola, lngEscola);

        android.util.Log.d("RegistroVM", "Distância calculada: " + distancia + "m");

        // Tolerância de 100 metros
        if (distancia > 100) {
            mensagemLiveData.setValue("Sua presença não foi registrada pois você não está na escola.\n" +
                    "Distância: " + (int) distancia + " metros\n" +
                    "Unidade: " + unidade.getNome());
            return;
        }

        // Prepara os dados da presença
        SimpleDateFormat sdfData = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String data = sdfData.format(new Date());
        String horario = sdfHora.format(new Date());

        // Cria a presença (sem o campo nomeUnidade - apenas 8 parâmetros)
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

        // Salva no Supabase
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

    // Método para obter a unidade atual (usado pela Activity)
    public UnidadeEscolar getUnidadeAtual() {
        return unidadeAtual;
    }
}