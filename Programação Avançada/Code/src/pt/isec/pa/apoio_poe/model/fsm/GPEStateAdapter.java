package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GPEStateAdapter implements IGPEState{
    protected GPEContext context;
    protected DataManager data;

    protected GPEStateAdapter(GPEContext context, DataManager data){
        this.context = context;
        this.data = data;
    }
    protected void changeState(GPEState state){
        context.changeState(state.createState(context,data));
    }
    //METODOS
    @Override
    public boolean iniciar(){
        return false;
    }
    @Override
    public boolean terminar(){
        return false;
    }
    @Override
    public Erros fecharFase() {
        return null;
    }
    @Override
    public boolean isFechado(){return false;}
    @Override
    public boolean voltar(){
        return false;
    }

    //Gestão Alunos
    @Override
    public boolean gerirAlunos(){
        return false;
    }
    @Override
    public Erros addAlunos(List<String> values){
        return null;
    }
    @Override
    public Erros removeAlunos(long nAluno){
        return null;
    }
    @Override
    public String getAluno(long nAluno){
        return null;
    }
    @Override
    public Erros setNomeAluno(long n_aluno, String novoNome){return null;}
    @Override
    public Erros setClassificacao(long n_aluno, String novaNota){return null;}
    @Override
    public Erros setAptoEstagio(long nAluno){return null;}
    @Override
    public String getTodosAlunos(){return null;}
    @Override
    public List<Alunos> getListAlunos() {
        return null;
    }
    @Override
    public List<Alunos> getAlunos() {
        return null;
    }
    @Override
    public Alunos getAlunoNumber(long nAluno) {
        return null;
    }

    //Gestão Docentes;
    @Override
    public boolean gerirDocentes(){
        return false;
    }
    @Override
    public Erros addDocente(List<String> list){return null;}
    @Override
    public Erros removeDocente(String email){return null;}
    @Override
    public String getNomeDocente(String email) {
        return null;
    }
    @Override
    public Erros setNomeDocente(String email, String novoNome){return null;}
    @Override
    public String getDocente(String emailDoc){return null;}
    @Override
    public String getTodosDocentes(){return null;}
    @Override
    public List<Docentes> getDocentes() {
        return null;
    }
    @Override
    public Docentes getDocenteEmail(String email) {
        return null;
    }

    //Gestão Propostas
    @Override
    public boolean gerirPropostas() {
        return false;
    }
    @Override
    public Erros addProposta(List<String> values) {
        return null;
    }
    @Override
    public Erros removeProposta(String idProposta) {
        return null;
    }
    @Override
    public Erros setRamo(String idProposta, String novoRamo) {
        return null;
    }
    @Override
    public Erros setTitulo(String idProposta, String novoTitulo) {
        return null;
    }
    @Override
    public String getProposta(String idProposta) {
        return null;
    }
    @Override
    public String getTodasPropostas() {
        return null;
    }
    @Override
    public List<Proposta> getPropostas() {
        return null;
    }
    @Override
    public Proposta getProp(String idProposta){return null;}

    //Gestao Candidaturas
    @Override
    public Erros addCandidatura(List<String> candidatura){return null;}
    @Override
    public String getTodasCandidaturas(){return null;}
    @Override
    public Erros removeCandidatura(long nAluno){return null;}
    @Override
    public Erros setCandidaturas(List<String> candidatura){return null;}
    @Override
    public String getInfoCandidaturas(String type, String filtro){return null;}
    @Override
    public List<String> getCandidaturasList(){return null;}
    @Override
    public String getCandidaturasAlunoList(long nAluno) {
        return null;
    }
    @Override
    public List<String> getCandAutopropostasAlunos(){
        return null;
    }
    @Override
    public List<String> getCandRegistadaAlunos(){
        return null;
    }
    @Override
    public List<String> getSemCandRegistadaAlunos(){
        return null;
    }
    @Override
    public List<String> getCandAutopropostasProp(){
        return null;
    }
    @Override
    public List<String> getCandDocenteProp(){
        return null;
    }
    @Override
    public List<String> getCandProp(){
        return null;
    }
    @Override
    public List<String> getCandSemProp(){
        return null;
    }

    //At Propostas
    @Override
    public boolean atribuirAutomaticamente(){return false;}
    @Override
    public Erros addAtPropostas(){return null;}
    @Override
    public Erros addAtPropostasManualmente(String idProposta, long nAluno){return null;}
    @Override
    public Erros desempate(String alunoDesempatado){return null;}
    @Override
    public Erros removeAtPropostasProp(String nProposta){return null;}
    @Override
    public Erros removeAtPropostasAluno(Long nAluno){return null;}
    @Override
    public String getAlunosEmpatados(){return null;}
    @Override
    public String getAtPropostas() {return null;}
    @Override
    public List<String> getAtPropostasList() {
        return null;
    }
    @Override
    public List<Alunos> getAlunosEmpatadosList() {
        return null;
    }
    @Override
    public Proposta getPropEmpatada() {
        return null;
    }

    //Redo Undo
    @Override
    public Erros execute() {
        return null;
    }
    @Override
    public Erros undo() {
        return null;
    }

    //Orientadores
    @Override
    public Erros addAtOrientadores() {return null;}
    @Override
    public String mostraPropostasOrientadas(){return null;}
    @Override
    public Erros addManOrientadores(String docente,String idPropostas){return null;}
    @Override
    public Erros removeOrientadorProposta(String idProposta){return null;}
    @Override
    public Erros removeOrientadorTodasProposta(String emailDocente){return null;}
    @Override
    public Erros editAtOrientadores(String idProposta, String novoDocente) {
        return null;
    }
    @Override
    public List<String> mostraPropostasOrientadasList() {
        return null;
    }
    @Override
    public void undoAtOrientadores() {
        return;
    }
    @Override
    public void redoAtOrientadores() {
        return;
    }

    //Consulta
    @Override
    public int distAlunosRamos(String ramo){return -1;}
    @Override
    public LinkedHashMap<String, Integer> getTop5EmpEstagio(){return null;}
    @Override
    public LinkedHashMap<String, Integer> getTop5Docentes() {
        return null;
    }

    @Override
    public int nPropAtNAt(String s) {
        return -1;
    }
    @Override
    public String alunosAtribuidos(){
        return null;
    }
    @Override
    public String alunosNAtribuidos(){
        return null;
    }
    @Override
    public String propDisponiveis(){
        return null;
    }
    @Override
    public String propAtribuidas() {
        return null;
    }
    @Override
    public String orientacoesPorDocente() {
        return null;
    }
    @Override
    public float mediaOrientacoes() {
        return -1;
    }
    @Override
    public int minimoOrientacoes() {
        return -1;
    }
    @Override
    public int maximoOrientacoes() {
        return -1;
    }
    @Override
    public int numeroOrientacoesDocente(String docente) {
        return -1;
    }

    //Ficheiros
    @Override
    public boolean importFile(File f){return false;}
    @Override
    public boolean exportFile(File f){return false;}

    //Passar de fase
    @Override
    public boolean seguinte(){return false;}
}
