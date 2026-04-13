package com.exemplo.registropresenca.ui.cadastro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.exemplo.registropresenca.data.model.Aluno;
import com.exemplo.registropresenca.data.repository.AlunoRepository;

public class CadastroViewModel extends ViewModel {
    private AlunoRepository alunoRepository;
    private MutableLiveData<Aluno> alunoLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public CadastroViewModel() {
        alunoRepository = new AlunoRepository();
    }

    public LiveData<Aluno> getAluno() { return alunoLiveData; }
    public LiveData<String> getError() { return errorLiveData; }

    public void validarRa(String ra) {
        alunoRepository.buscarAlunoPorRa(ra, new AlunoRepository.AlunoCallback() {
            @Override
            public void onSuccess(Aluno aluno) {
                alunoLiveData.setValue(aluno);
            }

            @Override
            public void onError(String mensagem) {
                errorLiveData.setValue(mensagem);
            }
        });
    }
}