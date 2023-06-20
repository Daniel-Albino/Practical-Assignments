package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.data.command.*;
import pt.isec.pa.apoio_poe.model.data.memento.CareTaker;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public class DataManager implements Serializable {
    Data data;
    CommandManager cm;
    CareTaker careTaker;

    public DataManager() {
        cm = new CommandManager();
        data = new Data();
        careTaker = new CareTaker(data);
    }

    //ALUNOS
    public Erros addAluno(List<String> values) {
        return data.addAluno(values);
    }
    public Erros removeAluno(long nAluno) {
        return data.removeAluno(nAluno);
    }
    public String getAluno(long nAluno) {
        return data.getAluno(nAluno);
    }
    public Erros setNomeAluno(long n_aluno, String novoNome){return data.setNomeAluno(n_aluno,novoNome);}
    public Erros setClassificacao(long n_aluno, String novaNota){return data.setClassificacao(n_aluno,novaNota);}
    public Erros setAptoEstagio(long nAluno){return data.setAptoEstagio(nAluno);}
    public List<Alunos> getAlunos(){return data.getAlunos();}
    public String getTodosAlunos() {
        return data.getTodosAlunos();
    }
    public int numeroAlunos(){return data.numeroAlunos();}
    public List<Alunos> getListAlunos(){return data.getListAlunos();}
    public Alunos getAlunoNumber(long nAluno){return data.getAlunoNumber(nAluno);}

    //DOCENTES
    public Erros addDocente(List<String> list) {
        return data.addDocente(list);
    }
    public Erros removeDocente(String email) {
        return data.removeDocente(email);
    }
    public String getNomeDocente(String email){return data.getNomeDocente(email);}
    public Erros setNomeDocente(String email, String novoNome){
        return data.setNomeDocente(email, novoNome);
    }
    public String getDocente(String emailDoc) {
        return data.getDocente(emailDoc);
    }
    public List<Docentes> getDocentes() {return data.getDocentes();}
    public String getTodosDocentes() {
        return data.getTodosDocentes();
    }
    public Docentes getDocenteEmail(String email){return data.getDocenteEmail(email);}

    //PROPOSTA
    public Erros addProposta(List<String> values) {
        return data.addProposta(values);
    }
    public Erros removeProposta(String idProposta) {
        return data.removeProposta(idProposta);
    }
    public Erros setRamo(String idProposta, String novoRamo){
        return data.setRamo(idProposta,novoRamo);
    }
    public Erros setTitulo(String idProposta, String novoTitulo){
        return data.setTitulo(idProposta, novoTitulo);
    }
    public String getProposta(String idProposta) {
        return data.getProposta(idProposta);
    }
    public Proposta getProp(String idProposta){return data.getProp(idProposta);}
    public List<Proposta> getPropostas(){return data.getPropostas();}
    public String getTodasPropostas() {
        return data.getTodasPropostas();
    }
    public int numeroPropostas(){return data.numeroPropostas();}
    public long getIdAlunoProposta(String idProposta){return data.getIdAlunoProposta(idProposta);}
    public String getIdProposta(long nAluno){return data.getIdProposta(nAluno);}

    //Candidaturas
    public Erros addCandidatura(List<String> candidatura) {
        return data.addCandidatura(candidatura);
    }
    public String getTodasCandidaturas() {
        return data.getTodasCandidaturas();
    }
    public String getCandidaturas(){return data.getCandidaturas();}
    public String exportCandidaturas(){return data.exportCandidaturas();}
    public Erros removeCandidatura(long nAluno) {
        return data.removeCandidatura(nAluno);
    }
    public Erros setCandidaturas(List<String> candidatura){return data.setCandidaturas(candidatura);}
    public String getInfoCandidaturas(String type, String filtro) {
        return data.getInfoCandidaturas(type, filtro);
    }
    public List<String> getCandidaturasList(){return data.getCandidaturasList();}
    public String getCandidaturasAlunoList(long nAluno){return data.getCandidaturasAlunoList(nAluno);}

    public List<String> getCandAutopropostasAlunos(){return data.getCandAutopropostasAlunos();}
    public List<String> getCandRegistadaAlunos(){return data.getCandRegistadaAlunos();}
    public List<String> getSemCandRegistadaAlunos(){return data.getSemCandRegistadaAlunos();}
    public List<String> getCandAutopropostasProp(){return data.getCandAutopropostasProp();}
    public List<String> getCandDocenteProp(){return data.getCandDocenteProp();}
    public List<String> getCandProp(){return data.getCandProp();}
    public List<String> getCandSemProp(){return data.getCandSemProp();}

    //At Propostas
    public Erros addAtPropostas() {
        return data.addAtPropostas();
    }
    public Erros addAtPropostasManualmente(String idProposta, long nAluno){ //Aqui
        return cm.invokeCommand(new AddAtPropostasCommand(data, idProposta, nAluno));
    }
    public Erros desempate(String alunoDesempatado){
        return data.desempate(alunoDesempatado);
    }
    public String getAlunosEmpatados(){return data.getAlunosEmpatados();}
    public String getAtPropostas() {
        return data.getAtPropostas();
    }
    public List<String> getAtPropostasList(){return data.getAtPropostasList();}
    private long getIdAlunoAtProposta(String idProposta){
        return data.getIdAlunoAtProposta(idProposta);
    }
    private String getIdAtProposta(long nAluno){
        return data.getIdAtProposta(nAluno);
    }
    public Erros removeAtPropostasProp(String nProposta) { //Aqui
        return cm.invokeCommand(new RemoveAtPropostasPropCommand(data, nProposta, getIdAlunoAtProposta(nProposta)));
    }
    public Erros removeAtPropostasAluno(Long nAluno) { //Aqui
        return cm.invokeCommand(new RemoveAtPropostasAlunoCommand(data, getIdAtProposta(nAluno), nAluno));
    }
    public boolean isTodosAlunosCandidaturaAtribuida(){return data.isTodosAlunosCandidaturaAtribuida();}
    public List<Alunos> getAlunosEmpatadosList(){
        return data.getAlunosEmpatadosList();
    }

    public Proposta getPropEmpatada(){
        return data.getPropEmpatada();
    }

    //Redo e Undo
    public Erros redoAtPropostas(){
        return cm.redoAtPropostas();
    }
    public Erros undoAtPropostas() {
        return cm.undoAtPropostas();
    }

    //At Orientadores
    public Erros addAtOrientadores() {
        return data.addAtOrientadores();
    }
    public Erros addManOrientadores(String docente,String idPropostas){
        careTaker.saveCaretaker();
        return data.addManOrientadores(docente,idPropostas);
    }
    public Erros removeOrientadorProposta(String idProposta){
        careTaker.saveCaretaker();
        return data.removeOrientadorProposta(idProposta);
    }
    public Erros removeOrientadorTodasProposta(String emailDocente){
        careTaker.saveCaretaker();
        return data.removeOrientadorTodasProposta(emailDocente);
    }
    public Erros editAtOrientadores(String idProposta, String novoDocente){
        careTaker.saveCaretaker();
        return data.editAtOrientadores(idProposta, novoDocente);
    }
    public String mostraPropostasOrientadas(){return data.mostraPropostasOrientadas();}
    public List<String> mostraPropostasOrientadasList(){
        return data.mostraPropostasOrientadasList();
    }
    public void undoAtOrientadores(){
        careTaker.undoCaretaker();
    }
    public void redoAtOrientadores(){
        careTaker.redoCaretaker();
    }
    public void resetCaretaker(){
        careTaker.resetCaretaker();
    }

    //Consulta
    public int distAlunosRamos(String ramo){return data.distAlunosRamos(ramo);}
    public LinkedHashMap<String, Integer> getTop5EmpEstagio(){return data.getTop5EmpEstagio();}
    public LinkedHashMap<String,Integer> getTop5Docentes(){return data.getTop5Docentes();}
    public int nPropAtNAt(String s){return data.nPropAtNAt(s);}
    public String alunosAtribuidos() {
        return data.alunosAtribuidos();
    }
    public String alunosNAtribuidos() {
        return data.alunosNAtribuidos();
    }
    public String propDisponiveis() {
        return data.propDisponiveis();
    }
    public String propAtribuidas(){return data.propAtribuidas();}
    public String orientacoesPorDocente(){return data.orientacoesPorDocente();}
    public float mediaOrientacoes(){return data.mediaOrientacoes();}
    public int minimoOrientacoes(){return data.minimoOrientacoes();}
    public int maximoOrientacoes(){return data.maximoOrientacoes();}
    public int numeroOrientacoesDocente(String docente){return data.numeroOrientacoesDocente(docente);}
    public Erros fecharFase(GPEState state) {
        return data.fecharFase(state);
    }
    public boolean isFechado(GPEState state) {
        return data.isFechado(state);
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
    public int nCandidaturas(){
        return data.nCandidaturas();
    }
    public int nAtPropostas(){
        return data.nAtPropostas();
    }
    public int nAtOrientadores(){
        return data.nAtOrientadores();
    }
}
