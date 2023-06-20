package pt.isec.pa.apoio_poe.model.data;


public class Estagios extends Proposta {
    private String idEmpresa;
    private long idAluno;
    public Estagios(String codId, String ramo, String titulo, String idEmpresa,long idAluno) {
        super(codId, ramo, titulo,"T1");
        this.idEmpresa=idEmpresa;
        this.idAluno = idAluno;
    }
    @Override
    public String getIdEmpresa() {
        return idEmpresa;
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
            return String.format("Estagio: %s, Empresa: %s",super.toString(),idEmpresa);
        return String.format("Estagio: %s, Empresa: %s, Nº Aluno: %d",super.toString(),idEmpresa,idAluno);
    }
}


