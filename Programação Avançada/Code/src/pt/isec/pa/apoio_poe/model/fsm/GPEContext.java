package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.*;

import java.io.*;
import java.util.*;

public class GPEContext implements Serializable {
    private DataManager data;
    private IGPEState state;

    public GPEContext(){
        data = new DataManager();
        state = new Inicio(this,data); //1º Estado
    }
    public GPEState getState(){
        return state.getState();
    }
    void changeState(IGPEState state){
        this.state = state;
    }

    //METODOS:
    public boolean iniciar(){
        return state.iniciar();
    }
    public boolean terminar(){
        return state.terminar();
    }
    public boolean voltar(){
        return state.voltar();
    }
    public boolean seguinte() {
        return state.seguinte();
    }
    public Erros fecharFase(){return state.fecharFase();}
    public boolean isFechado(){return state.isFechado();}

    //Gestão Alunos
    public boolean gerirAlunos(){
        return state.gerirAlunos();
    }
    public Erros addAlunos(List<String> values){
        return state.addAlunos(values);
    }
    public Erros removeAlunos(long nAluno){
        return state.removeAlunos(nAluno);
    }
    public Erros setNomeAluno(long n_aluno, String novoNome){return state.setNomeAluno(n_aluno,novoNome);}
    public Erros setClassificacao(long n_aluno, String novaNota){return state.setClassificacao(n_aluno,novaNota);}
    public Erros setAptoEstagio(long nAluno){return state.setAptoEstagio(nAluno);}
    public String getAluno(long nAluno){
        return state.getAluno(nAluno);
    }
    public String getTodosAlunos(){return state.getTodosAlunos();}
    public List<Alunos> getListAlunos(){return state.getListAlunos();}
    public List<Alunos> getAlunos(){return state.getAlunos();}
    public Alunos getAlunoNumber(long nAluno){return state.getAlunoNumber(nAluno);}


    //Gestão Docentes
    public boolean gerirDocentes(){
        return state.gerirDocentes();
    }
    public Erros addDocente(List<String> list){
        return state.addDocente(list);
    }
    public Erros removeDocente(String email){
        return state.removeDocente(email);
    }
    public String getNomeDocente(String email){return data.getNomeDocente(email);}
    public Erros setNomeDocente(String email, String novoNome){
        return state.setNomeDocente(email,novoNome);
    }
    public String getDocente(String emailDoc){
        return state.getDocente(emailDoc);
    }
    public String getTodosDocentes(){
        return state.getTodosDocentes();
    }
    public List<Docentes> getDocentes(){return state.getDocentes();}
    public Docentes getDocenteEmail(String email){return state.getDocenteEmail(email);}

    //Gestão Propostas
    public boolean gerirPropostas(){return state.gerirPropostas();}
    public Erros addProposta(List<String> values){return state.addProposta(values);}
    public Erros removeProposta(String idProposta){return state.removeProposta(idProposta);}
    public Erros setRamo(String idProposta, String novoRamo){return state.setRamo(idProposta, novoRamo);}
    public Erros setTitulo(String idProposta, String novoTitulo){return state.setTitulo(idProposta,novoTitulo);}
    public String getProposta(String idProposta){return state.getProposta(idProposta);}
    public String getTodasPropostas(){return state.getTodasPropostas();}
    public List<Proposta> getPropostas(){return state.getPropostas();}
    public Proposta getProp (String idProposta){return state.getProp(idProposta);}

    //Gestao Candidaturas
    public Erros addCandidatura(List<String> candidatura){return state.addCandidatura(candidatura);}
    public Erros removeCandidatura(long nAluno){return state.removeCandidatura(nAluno);}
    public String getTodasCandidaturas(){return state.getTodasCandidaturas();}
    public Erros setCandidaturas(List<String> candidatura){return state.setCandidaturas(candidatura);}
    public String getInfoCandidaturas(String type, String filtro){return state.getInfoCandidaturas(type,filtro);}
    public List<String> getCandidaturasList(){return state.getCandidaturasList();}
    public String getCandidaturasAlunoList(long nAluno){return state.getCandidaturasAlunoList(nAluno);}
    public List<String> getCandAutopropostasAlunos(){return state.getCandAutopropostasAlunos();}
    public List<String> getCandRegistadaAlunos(){return state.getCandRegistadaAlunos();}
    public List<String> getSemCandRegistadaAlunos(){return state.getSemCandRegistadaAlunos();}
    public List<String> getCandAutopropostasProp(){return state.getCandAutopropostasProp();}
    public List<String> getCandDocenteProp(){return state.getCandDocenteProp();}
    public List<String> getCandProp(){return state.getCandProp();}
    public List<String> getCandSemProp(){return state.getCandSemProp();}

    //At Propostas
    public boolean atribuirAutomaticamente(){return state.atribuirAutomaticamente();}
    public Erros addAtPropostas(){return state.addAtPropostas();}

    public Erros addAtPropostasManualmente(String idProposta, long nAluno){
        return state.addAtPropostasManualmente(idProposta, nAluno);
    }
    public Erros removeAtPropostasProp(String nProposta){
        return state.removeAtPropostasProp(nProposta);
    }
    public Erros removeAtPropostasAluno(Long nAluno){
        return state.removeAtPropostasAluno(nAluno);
    }
    public Erros desempate(String alunoDesempatado){return state.desempate(alunoDesempatado);}
    public String getAtPropostas() {return state.getAtPropostas();}
    public List<String> getAtPropostasList(){return state.getAtPropostasList();}
    public String getAlunosEmpatados(){return state.getAlunosEmpatados();}
    public List<Alunos> getAlunosEmpatadosList(){
        return state.getAlunosEmpatadosList();
    }
    public Proposta getPropEmpatada(){
        return state.getPropEmpatada();
    }

    //Redo e Undo
    public Erros execute(){return state.execute();}

    public Erros undo(){return state.undo();}

    //AtOrientadores
    public Erros addAtOrientadores(){return state.addAtOrientadores();}
    public Erros addManOrientadores(String docente,String idPropostas){return state.addManOrientadores(docente,idPropostas);}
    public Erros removeOrientadorProposta(String idProposta){return state.removeOrientadorProposta(idProposta);}
    public Erros removeOrientadorTodasProposta(String emailDocente){return state.removeOrientadorTodasProposta(emailDocente);}
    public String mostraPropostasOrientadas(){return state.mostraPropostasOrientadas();}
    public List<String> mostraPropostasOrientadasList(){return state.mostraPropostasOrientadasList();}
    public Erros editAtOrientadores(String idProposta, String novoDocente){return state.editAtOrientadores(idProposta, novoDocente);}
    public void undoAtOrientadores(){state.undoAtOrientadores();}
    public void redoAtOrientadores(){state.redoAtOrientadores();}


    //Consulta
    public int distAlunosRamos(String ramo){return state.distAlunosRamos(ramo);}
    public LinkedHashMap<String, Integer> getTop5EmpEstagio(){return state.getTop5EmpEstagio();}
    public LinkedHashMap<String,Integer> getTop5Docentes(){return state.getTop5Docentes();}
    public int nPropAtNAt(String s){return state.nPropAtNAt(s);}
    public String alunosAtribuidos(){
        return state.alunosAtribuidos();
    }
    public String alunosNAtribuidos(){
        return state.alunosNAtribuidos();
    }
    public String propDisponiveis(){return state.propDisponiveis();}
    public String propAtribuidas(){return state.propAtribuidas();}
    public String orientacoesPorDocente(){return state.orientacoesPorDocente();}
    public float mediaOrientacoes(){return state.mediaOrientacoes();}
    public int minimoOrientacoes(){return state.minimoOrientacoes();}
    public int maximoOrientacoes(){return state.maximoOrientacoes();}
    public int numeroOrientacoesDocente(String docente){return state.numeroOrientacoesDocente(docente);}

    //Ficheiros
    public boolean importFile(File f){return state.importFile(f);}
    public boolean exportFile(File f){return state.exportFile(f);}
    public boolean save(File f){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))){
            oos.writeObject(data);
            oos.writeObject(state.getState());
        }catch (Exception e){
            System.err.println("Error!");
            return false;
        }
        return true;
    }
    public boolean load(File f) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
            data = (DataManager) ois.readObject();
            GPEState aux = (GPEState) ois.readObject();
            changeState(aux.createState(this,data));
            data.resetCaretaker();
        }catch (Exception e){
            System.err.println("Error! " + e);
            return false;
        }
        return true;
    }
    public int nAlunos(){
        return data.nAlunos();
    }
    public int nDocentes(){
        return data.nDocentes();
    }
    public int nPropostas(){
        return data.nPropostas();
    }
    public int nAtPropostas(){
        return data.nAtPropostas();
    }
    public int nAtOrientadores(){
        return data.nAtOrientadores();
    }
    public int nCandidaturas() {
        return data.nCandidaturas();
    }
}
