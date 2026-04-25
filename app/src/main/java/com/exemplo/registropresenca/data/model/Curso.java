package com.exemplo.registropresenca.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Curso implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("nome")
    private String nome;

    public Curso() {}

    public Curso(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}