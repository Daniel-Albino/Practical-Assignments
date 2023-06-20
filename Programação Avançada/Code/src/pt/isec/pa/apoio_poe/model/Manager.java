package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.fsm.GPEContext;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public class Manager {
    public static final String PROP_STATE = "state";
    public static final String PROP_DATA  = "data";
    GPEContext fsm;
    PropertyChangeSupport pcs;

    public Manager(){
        fsm = new GPEContext();
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }


    public GPEState getState(){
        return fsm.getState();
    }

    //METODOS:

    public boolean iniciar(){
        boolean bol = fsm.iniciar();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }

    public boolean terminar(){
        boolean bol = fsm.terminar();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }


    public boolean voltar(){
        boolean bol = fsm.voltar();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }

    public boolean seguinte() {
        boolean bol =  fsm.seguinte();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }

    public Erros fecharFase(){
        Erros erros = fsm.fecharFase();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public boolean isFechado(){
        return fsm.isFechado();
    }

    //Gestão Alunos
    public boolean gerirAlunos(){
        boolean bol = fsm.gerirAlunos();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }
    public Erros addAlunos(List<String> values){
        Erros erros = fsm.addAlunos(values);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeAlunos(long nAluno){
        Erros erros = fsm.removeAlunos(nAluno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setNomeAluno(long n_aluno, String novoNome){
        Erros erros = fsm.setNomeAluno(n_aluno,novoNome);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setClassificacao(long n_aluno, String novaNota){
        Erros erros = fsm.setClassificacao(n_aluno,novaNota);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setAptoEstagio(long nAluno){
        Erros erros = fsm.setAptoEstagio(nAluno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public List<Alunos> getListAlunos(){return fsm.getListAlunos();}
    public List<Alunos> getAlunos(){return fsm.getAlunos();}
    public Alunos getAlunoNumber(long nAluno){return fsm.getAlunoNumber(nAluno);}

    //Gestão Docentes
    public boolean gerirDocentes(){
        boolean bol = fsm.gerirDocentes();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }
    public Erros addDocente(List<String> list){
        Erros erros = fsm.addDocente(list);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeDocente(String email){
        Erros erros = fsm.removeDocente(email);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setNomeDocente(String email, String novoNome){
        Erros erros = fsm.setNomeDocente(email,novoNome);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public List<Docentes> getDocentes(){return fsm.getDocentes();}
    public Docentes getDocenteEmail(String email){return fsm.getDocenteEmail(email);}

    //Gestão Propostas
    public boolean gerirPropostas(){
        boolean bol = fsm.gerirPropostas();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }
    public Erros addProposta(List<String> values){
        Erros erros = fsm.addProposta(values);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeProposta(String idProposta){
        Erros erros = fsm.removeProposta(idProposta);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setRamo(String idProposta, String novoRamo){
        Erros erros = fsm.setRamo(idProposta, novoRamo);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setTitulo(String idProposta, String novoTitulo){
        Erros erros = fsm.setTitulo(idProposta,novoTitulo);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public List<Proposta> getPropostas(){return fsm.getPropostas();}
    public Proposta getProp(String idProposta){return fsm.getProp(idProposta);}


    //Gestao Candidaturas
    public Erros addCandidatura(List<String> candidatura){
        Erros erros = fsm.addCandidatura(candidatura);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeCandidatura(long nAluno){
        Erros erros = fsm.removeCandidatura(nAluno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros setCandidaturas(List<String> candidatura){
        Erros erros = fsm.setCandidaturas(candidatura);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public List<String> getCandidaturasList(){return fsm.getCandidaturasList();}
    public String getCandidaturasAlunoList(long nAluno){return fsm.getCandidaturasAlunoList(nAluno);}
    public List<String> getCandAutopropostasAlunos(){return fsm.getCandAutopropostasAlunos();}
    public List<String> getCandRegistadaAlunos(){return fsm.getCandRegistadaAlunos();}
    public List<String> getSemCandRegistadaAlunos(){return fsm.getSemCandRegistadaAlunos();}
    public List<String> getCandAutopropostasProp(){return fsm.getCandAutopropostasProp();}
    public List<String> getCandDocenteProp(){return fsm.getCandDocenteProp();}
    public List<String> getCandProp(){return fsm.getCandProp();}
    public List<String> getCandSemProp(){return fsm.getCandSemProp();}

    //At Propostas
    public boolean atribuirAutomaticamente(){
        boolean bol = fsm.atribuirAutomaticamente();
        pcs.firePropertyChange(PROP_STATE,null,fsm.getState());
        return bol;
    }
    public Erros addAtPropostas(){
        Erros erros = fsm.addAtPropostas();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros addAtPropostasManualmente(String idProposta, long nAluno){
        Erros erros = fsm.addAtPropostasManualmente(idProposta, nAluno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeAtPropostasProp(String nProposta){
        Erros erros = fsm.removeAtPropostasProp(nProposta);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeAtPropostasAluno(Long nAluno){
        Erros erros = fsm.removeAtPropostasAluno(nAluno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros desempate(String alunoDesempatado) {
        Erros erros = fsm.desempate(alunoDesempatado);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public List<String> getAtPropostasList(){return fsm.getAtPropostasList();}
    public List<Alunos> getAlunosEmpatadosList(){
        return fsm.getAlunosEmpatadosList();
    }
    public Proposta getPropEmpatada(){
        return fsm.getPropEmpatada();
    }

    //Redo e Undo
    public Erros execute(){
        Erros erros = fsm.execute();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros undo(){
        Erros erros = fsm.undo();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }

    //AtOrientadores
    public Erros addAtOrientadores(){
        Erros erros = fsm.addAtOrientadores();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros addManOrientadores(String docente,String idPropostas){
        Erros erros = fsm.addManOrientadores(docente,idPropostas);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeOrientadorProposta(String idProposta){
        Erros erros = fsm.removeOrientadorProposta(idProposta);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros removeOrientadorTodasProposta(String emailDocente){
        Erros erros = fsm.removeOrientadorTodasProposta(emailDocente);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public Erros editAtOrientadores(String idProposta, String novoDocente){
        Erros erros = fsm.editAtOrientadores(idProposta, novoDocente);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return erros;
    }
    public List<String> mostraPropostasOrientadasList(){
        return fsm.mostraPropostasOrientadasList();
    }
    public void undoAtOrientadores(){
        fsm.undoAtOrientadores();
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    public void redoAtOrientadores(){
        fsm.redoAtOrientadores();
        pcs.firePropertyChange(PROP_DATA,null,null);
    }

    //Consulta
    public int distAlunosRamos(String ramo){return fsm.distAlunosRamos(ramo);}
    public LinkedHashMap<String, Integer> getTop5EmpEstagio(){return fsm.getTop5EmpEstagio();}
    public LinkedHashMap<String,Integer> getTop5Docentes(){return fsm.getTop5Docentes();}
    public int nPropAtNAt(String s){return fsm.nPropAtNAt(s);}

    //Ficheiros
    public boolean importFile(File f){
        boolean bol = fsm.importFile(f);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return bol;
    }
    public boolean exportFile(File f){
        return fsm.exportFile(f);
    }
    public boolean save(File f){
        return fsm.save(f);
    }
    public boolean load(File f) {
        boolean bol = fsm.load(f);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return bol;
    }
    public int nAlunos(){
        return fsm.nAlunos();
    }
    public int nDocentes(){
        return fsm.nDocentes();
    }
    public int nPropostas(){
        return fsm.nPropostas();
    }
    public int nCandidaturas(){
        return fsm.nCandidaturas();
    }
    public int nAtPropostas(){
        return fsm.nAtPropostas();
    }
    public int nAtOrientadores(){
        return fsm.nAtOrientadores();
    }
}
