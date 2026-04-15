package com.exemplo.registropresenca.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Modelo UnidadeEscolar.
 * Representa uma unidade da escola (ex: Unidade Centro, Unidade Norte).
 *
 * Tabela no Supabase: UnidadesEscolares
 * Colunas:
 * - id: identificador único
 * - nome: nome da unidade (ex: "Unidade Centro")
 * - endereco: endereço completo
 * - lat_unidade: latitude da unidade
 * - lng_unidade: longitude da unidade
 */
public class UnidadeEscolar implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("endereco")
    private String endereco;

    @SerializedName("lat_unidade")
    private double latitude;

    @SerializedName("lng_unidade")
    private double longitude;

    // Construtor vazio (necessário para o Gson)
    public UnidadeEscolar() {}

    // Construtor completo
    public UnidadeEscolar(long id, String nome, String endereco, double latitude, double longitude) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}