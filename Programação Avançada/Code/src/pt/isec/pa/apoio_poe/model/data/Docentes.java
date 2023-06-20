package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Objects;

public class Docentes implements Serializable {
    private String nomeDocente;
    private String emailDocente;
    public Docentes(String nomeDocente, String emailDocente) {
        this.nomeDocente = nomeDocente;
        this.emailDocente = emailDocente;
    }
    public String getNomeDocente() {
        return nomeDocente;
    }
    public void setNomeDocente(String nomeDocente) {
        this.nomeDocente = nomeDocente;
    }
    public String getEmailDocente() {
        return emailDocente;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docentes docentes = (Docentes) o;
        return emailDocente.equals(docentes.emailDocente);
    }
    @Override
    public int hashCode() {
        return Objects.hash(emailDocente);
    }
    @Override
    public String toString() {
        return String.format("Nome: %s, E-mail: %s",nomeDocente,emailDocente);
    }
}
