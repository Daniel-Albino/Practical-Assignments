package pt.isec.pa.apoio_poe.model.data;

public class Projetos extends Proposta {
    private String emailDocente;
    private long idAluno;
    public Projetos(String codId, String ramo, String titulo, String emailDocente,long idAluno) {
        super(codId, ramo, titulo,"T2");
        this.emailDocente=emailDocente;
        this.idAluno = idAluno;
    }
    public String getEmailDocente() {
        return emailDocente;
    }
    @Override
    public long getIdAluno() {
        return idAluno;
    }
    @Override
    public boolean setIdAluno(long idAluno) {
        if(idAluno != -1)
            return false;
        this.idAluno = idAluno;
        return true;
    }
    @Override
    public String toString() {
        if(idAluno == -1)
            return String.format("Projetos: %s, E-mail do Docente: %s",super.toString(),emailDocente);
        return String.format("Projetos: %s, E-mail do Docente: %s, Nº Aluno: %d",super.toString(),emailDocente,idAluno);
    }
}
