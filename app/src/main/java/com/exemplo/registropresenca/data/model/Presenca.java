package com.exemplo.registropresenca.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class    Presenca implements Serializable {

    @SerializedName("aluno_ra")
    private String alunoRa;

    @SerializedName("nome")
    private String nome;

    @SerializedName("turma")
    private String turma;

    @SerializedName("data")
    private String data;

    @SerializedName("horario")
    private String horario;

    @SerializedName("lat_aluno")
    private double latitudeAluno;

    @SerializedName("lng_aluno")
    private double longitudeAluno;

    @SerializedName("status")
    private String status;

    public Presenca() {}

    public Presenca(String alunoRa, String nome, String turma, String data, String horario,
                    double latitudeAluno, double longitudeAluno, String status) {
        this.alunoRa = alunoRa;
        this.nome = nome;
        this.turma = turma;
        this.data = data;
        this.horario = horario;
        this.latitudeAluno = latitudeAluno;
        this.longitudeAluno = longitudeAluno;
        this.status = status;
    }

    // Getters e setters (gerar todos ou usar abaixo)
    public String getAlunoRa() { return alunoRa; }
    public void setAlunoRa(String alunoRa) { this.alunoRa = alunoRa; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTurma() { return turma; }
    public void setTurma(String turma) { this.turma = turma; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public double getLatitudeAluno() { return latitudeAluno; }
    public void setLatitudeAluno(double latitudeAluno) { this.latitudeAluno = latitudeAluno; }
    public double getLongitudeAluno() { return longitudeAluno; }
    public void setLongitudeAluno(double longitudeAluno) { this.longitudeAluno = longitudeAluno; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}