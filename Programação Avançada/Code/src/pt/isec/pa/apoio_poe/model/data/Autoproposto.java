package pt.isec.pa.apoio_poe.model.data;

public class Autoproposto extends Proposta {
    private long idAluno;
    public Autoproposto(String codId, String titulo,Alunos aluno) {
        super(codId, aluno.getRamo(), titulo,"T3");
        this.idAluno = aluno.getNumero();
    }
    @Override
    public long getIdAluno() {
        return idAluno;
    }
    @Override
    public String toString() {
        if(idAluno == -1)
            return String.format("Estágio/Projeto autoproposto: %s",super.toString());
        return String.format("Estagio/Projeto autoproposto: %s, Nº Aluno: %d",super.toString(),idAluno);
    }
}
