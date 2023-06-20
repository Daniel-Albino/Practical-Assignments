package pt.isec.tp_pd.data.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Espetaculo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String descricao;
    private String tipo;
    private String hora;
    private String data;
    private String data_hora;
    private Integer duracao;
    private String local;
    private String localidade;
    private String pais;
    private String classificacao_etaria;
    private Integer visivel;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getClassificacao_etaria() {
        return classificacao_etaria;
    }

    public void setClassificacao_etaria(String classificacao_etaria) {
        this.classificacao_etaria = classificacao_etaria;
    }

    public Integer getVisivel() {
        return visivel;
    }

    public void setVisivel(Integer visivel) {
        this.visivel = visivel;
    }

    @Override
    public String toString() {
        return "Espetaculo{" +
                "ID=" + id +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", data_hora='" + data_hora + '\'' +
                ", duracao=" + duracao +
                ", local='" + local + '\'' +
                ", localidade='" + localidade + '\'' +
                ", pais='" + pais + '\'' +
                ", classificacao_etaria='" + classificacao_etaria + '\'' +
                ", visivel=" + visivel + "\n" +
                '}';
    }

}
