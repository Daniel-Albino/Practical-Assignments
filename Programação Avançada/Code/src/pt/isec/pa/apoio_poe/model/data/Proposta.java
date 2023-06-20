package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Objects;

public class Proposta implements Serializable {
    private String codId;
    private String tipo;
    private String ramo;
    private String titulo;
    public Proposta(String codId, String ramo, String titulo, String tipo) {
        this.codId = codId;
        this.ramo = ramo;
        this.titulo = titulo;
        this.tipo=tipo;
    }
    public String getCodId() {
        return codId;
    }
    public boolean containsCodId(){
        if(codId == null)
            return false;
        return true;
    }
    public String getRamo() {
        return ramo;
    }
    public void setRamo(String ramo) {
        this.ramo = ramo;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getTipo() {
        return tipo;
    }
    public String getIdEmpresa(){
        if(tipo.equals("T1"))
            return this.getIdEmpresa();
        return "Sem empresa";
    }
    public boolean setIdAluno(long idAluno){
        if(this.getTipo().equalsIgnoreCase("T3"))
            return false;
        return this.setIdAluno(idAluno);
    }
    public long getIdAluno() {
        return this.getIdAluno();
    }
    public String getEmailDocente() {
        if(this.getTipo().equalsIgnoreCase("T2"))
            return this.getEmailDocente();
        return "Sem docente";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposta proposta = (Proposta) o;
        return codId.equals(proposta.codId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(codId);
    }
    @Override
    public String toString() {
        if(getTipo().equals("T3")){
            return String.format("Codigo de Identificação: %s, Tipo: %s, Titulo: %s",codId,tipo,titulo);
        }
        return String.format("Codigo de Identificação: %s, Tipo: %s, Ramo: %s, Titulo: %s",codId,tipo,ramo,titulo);
    }
}
