package com.exemplo.registropresenca.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Aluno implements Serializable {

    // Campos do Supabase (Matriculas)
    @SerializedName("ra")
    private String ra;

    @SerializedName("nome")
    private String primeiroNome;

    @SerializedName("sobrenome")
    private String sobrenome;

    @SerializedName("turma_id")
    private long turmaId;

    @SerializedName("curso_id")
    private long cursoId;

    @SerializedName("unidade_id")
    private long unidadeId;

    @SerializedName("foto")
    private String fotoUrl;

    // Campos adicionais
    @SerializedName("email")
    private String email;

    @SerializedName("cpf")
    private String cpf;

    // Campos legados (para compatibilidade com CadAluno)
    @SerializedName("turma")
    private String turmaLegada;

    @SerializedName("lat_escola")
    private double latitudeEscola;

    @SerializedName("lng_escola")
    private double longitudeEscola;

    // Nome completo (cache)
    private transient String nomeCompleto;

    public Aluno() {}

    // ===== GETTERS E SETTERS PRINCIPAIS =====

    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }

    /**
     * Retorna o nome completo (nome + sobrenome)
     */
    public String getNome() {
        if (nomeCompleto != null) return nomeCompleto;

        if (primeiroNome != null && sobrenome != null && !primeiroNome.isEmpty()) {
            nomeCompleto = primeiroNome + " " + sobrenome;
            return nomeCompleto;
        }
        if (primeiroNome != null && !primeiroNome.isEmpty()) {
            return primeiroNome;
        }
        return "";
    }

    public void setNome(String nome) {
        this.nomeCompleto = nome;
        // Se precisar separar, fazemos depois
    }

    public String getPrimeiroNome() { return primeiroNome; }
    public void setPrimeiroNome(String primeiroNome) { this.primeiroNome = primeiroNome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    // ===== MÉTODO GET TURMA (compatível com versão antiga) =====

    /**
     * Retorna o nome da turma
     * Prioriza turmaLegada (CadAluno) se disponível, senão usa turmaId
     */
    public String getTurma() {
        if (turmaLegada != null && !turmaLegada.isEmpty()) {
            return turmaLegada;
        }
        // Se tiver turmaId, retorna o ID como string (será substituído depois pela busca)
        if (turmaId > 0) {
            return "Turma ID: " + turmaId;
        }
        return "Não informada";
    }

    public void setTurma(String turma) {
        this.turmaLegada = turma;
    }

    public long getTurmaId() { return turmaId; }
    public void setTurmaId(long turmaId) { this.turmaId = turmaId; }

    // ===== CURSO =====

    public long getCursoId() { return cursoId; }
    public void setCursoId(long cursoId) { this.cursoId = cursoId; }

    // ===== UNIDADE =====

    public long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(long unidadeId) { this.unidadeId = unidadeId; }

    public boolean temUnidadeAssociada() {
        return unidadeId > 0;
    }

    // ===== FOTO =====

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    // ===== CAMPOS LEGADOS (latitude/longitude) =====

    public double getLatitudeEscola() { return latitudeEscola; }
    public void setLatitudeEscola(double latitudeEscola) { this.latitudeEscola = latitudeEscola; }

    public double getLongitudeEscola() { return longitudeEscola; }
    public void setLongitudeEscola(double longitudeEscola) { this.longitudeEscola = longitudeEscola; }

    // ===== CAMPOS ADICIONAIS =====

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}