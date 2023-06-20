package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Objects;

public class Alunos implements Serializable {

    private long numero;
    private String nome;
    private String email;
    private String curso;
    private String ramo;
    private double classificacao;
    private boolean apto;

    public Alunos(long numeroAluno, String nomeAluno, String emailAluno, String curso, String ramo, double classificacao, boolean aptoEstagio) {
        this.numero = numeroAluno;
        this.nome = nomeAluno;
        this.email = emailAluno;
        this.curso = curso;
        this.ramo = ramo;
        this.classificacao = classificacao;
        this.apto = aptoEstagio;
    }
    public long getNumero() {
        return numero;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public String getCurso() {
        return curso;
    }
    public String getRamo() {
        return ramo;
    }
    public void setRamo(String ramo) {
        this.ramo = ramo;
    }
    public Double getClassificacao() {
        return classificacao;
    }
    public void setClassificacao(Double classificacao) {
        this.classificacao = classificacao;
    }
    public boolean getApto() {
        return apto;
    }
    public void setAptoEstagio() {
        if(apto)
            apto = false;
        else
            apto = true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alunos alunos = (Alunos) o;
        return numero == alunos.numero && email.equals(alunos.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(numero, email);
    }
    @Override
    public String toString() {
        return String.format("Nº de aluno: %d, Nome: %s, E-mail: %s, Curso: %s, Ramo: %s, Classificação: %f, Apto para estagio: %s",
                numero, nome, email,curso,ramo,classificacao, apto);
    }
}
