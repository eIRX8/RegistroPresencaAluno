package com.exemplo.registropresenca.data.model;

import com.google.gson.annotations.SerializedName;

public class Aluno {
    @SerializedName("ra")
    private String ra;

    @SerializedName("nome")
    private String nome;

    @SerializedName("turma")
    private String turma;

    @SerializedName("lat_escola")
    private double latitudeEscola;

    @SerializedName("lng_escola")
    private double longitudeEscola;

    // Construtores, getters e setters
    public Aluno() {}

    public Aluno(String ra, String nome, String turma, double latitudeEscola, double longitudeEscola) {
        this.ra = ra;
        this.nome = nome;
        this.turma = turma;
        this.latitudeEscola = latitudeEscola;
        this.longitudeEscola = longitudeEscola;
    }

    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTurma() { return turma; }
    public void setTurma(String turma) { this.turma = turma; }

    public double getLatitudeEscola() { return latitudeEscola; }
    public void setLatitudeEscola(double latitudeEscola) { this.latitudeEscola = latitudeEscola; }

    public double getLongitudeEscola() { return longitudeEscola; }
    public void setLongitudeEscola(double longitudeEscola) { this.longitudeEscola = longitudeEscola; }
}