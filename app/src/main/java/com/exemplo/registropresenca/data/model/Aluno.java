package com.exemplo.registropresenca.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Modelo Aluno (atualizado para suportar múltiplas unidades).
 *
 * Tabela no Supabase: CadAluno
 * Colunas:
 * - ra: Registro do aluno (PK)
 * - nome: Nome completo
 * - turma: Turma do aluno
 * - lat_escola: (legado) Latitude da escola - mantido para compatibilidade
 * - lng_escola: (legado) Longitude da escola - mantido para compatibilidade
 * - unidade_id: (novo) Referência à tabela UnidadesEscolares
 */
public class Aluno implements Serializable {

    @SerializedName("ra")
    private String ra;

    @SerializedName("nome")
    private String nome;

    @SerializedName("turma")
    private String turma;

    // Campos legados (mantidos para compatibilidade com versão anterior)
    @SerializedName("lat_escola")
    private double latitudeEscola;  // Pode ser vazio se usar unidade_id

    @SerializedName("lng_escola")
    private double longitudeEscola; // Pode ser vazio se usar unidade_id

    // Novo campo: referência à unidade escolar
    @SerializedName("unidade_id")
    private long unidadeId;  // 0 = não associado a nenhuma unidade

    // Construtores
    public Aluno() {}

    public Aluno(String ra, String nome, String turma, double latitudeEscola, double longitudeEscola) {
        this.ra = ra;
        this.nome = nome;
        this.turma = turma;
        this.latitudeEscola = latitudeEscola;
        this.longitudeEscola = longitudeEscola;
        this.unidadeId = 0; // Sem unidade associada
    }

    public Aluno(String ra, String nome, String turma, long unidadeId) {
        this.ra = ra;
        this.nome = nome;
        this.turma = turma;
        this.unidadeId = unidadeId;
        this.latitudeEscola = 0;
        this.longitudeEscola = 0;
    }

    // Getters
    public String getRa() { return ra; }
    public String getNome() { return nome; }
    public String getTurma() { return turma; }
    public double getLatitudeEscola() { return latitudeEscola; }
    public double getLongitudeEscola() { return longitudeEscola; }
    public long getUnidadeId() { return unidadeId; }

    // Setters
    public void setRa(String ra) { this.ra = ra; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTurma(String turma) { this.turma = turma; }
    public void setLatitudeEscola(double latitudeEscola) { this.latitudeEscola = latitudeEscola; }
    public void setLongitudeEscola(double longitudeEscola) { this.longitudeEscola = longitudeEscola; }
    public void setUnidadeId(long unidadeId) { this.unidadeId = unidadeId; }

    /**
     * Verifica se o aluno usa o novo sistema de unidades.
     * @return true se tem unidade_id associada
     */
    public boolean temUnidadeAssociada() {
        return unidadeId > 0;
    }
}