package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.data.command.ICommand;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public interface IGPEState extends ICommand, Serializable{
    boolean iniciar();
    boolean terminar();
    boolean gerirAlunos();
    boolean voltar();
    Erros fecharFase();
    boolean isFechado();

    //Gestão alunos
    Erros addAlunos(List<String> values);
    Erros removeAlunos(long nAluno);
    String getAluno(long nAluno);
    Erros setNomeAluno(long n_aluno, String novoNome);
    Erros setClassificacao(long n_aluno, String novaNota);
    Erros setAptoEstagio(long nAluno);
    String getTodosAlunos();
    List<Alunos> getListAlunos();
    List<Alunos> getAlunos();
    Alunos getAlunoNumber(long nAluno);

    //Gestão Docentes;
    boolean gerirDocentes();
    Erros addDocente(List<String> list);
    Erros removeDocente(String email);
    String getNomeDocente(String email);
    Erros setNomeDocente(String email, String novoNome);
    String getDocente(String emailDoc);
    String getTodosDocentes();
    List<Docentes> getDocentes();
    Docentes getDocenteEmail(String email);

    //Gestão de Propostas
    boolean gerirPropostas();
    Erros addProposta(List<String> values);
    Erros removeProposta(String idProposta);
    Erros setRamo(String idProposta, String novoRamo);
    Erros setTitulo(String idProposta, String novoTitulo);
    String getProposta(String idProposta);
    String getTodasPropostas();
    List<Proposta> getPropostas();
    Proposta getProp(String idProposta);

    //Gestao Candidaturas
    Erros addCandidatura(List<String> candidatura);
    String getTodasCandidaturas();
    Erros removeCandidatura(long nAluno);
    Erros setCandidaturas(List<String> candidatura);
    String getInfoCandidaturas(String type, String filtro);
    List<String> getCandidaturasList();
    String getCandidaturasAlunoList(long nAluno);
    List<String> getCandAutopropostasAlunos();
    List<String> getCandRegistadaAlunos();
    List<String> getSemCandRegistadaAlunos();
    List<String> getCandAutopropostasProp();
    List<String> getCandDocenteProp();
    List<String> getCandProp();
    List<String> getCandSemProp();

    //At Propostas
    boolean atribuirAutomaticamente();
    Erros addAtPropostas();
    Erros addAtPropostasManualmente(String idProposta, long nAluno);
    Erros desempate(String alunoDesempatado);
    Erros removeAtPropostasProp(String nProposta);
    Erros removeAtPropostasAluno(Long nAluno);
    String getAtPropostas();
    List<String> getAtPropostasList();
    String getAlunosEmpatados();
    List<Alunos> getAlunosEmpatadosList();
    Proposta getPropEmpatada();

    //Redo e Undo
    @Override
    Erros execute();
    @Override
    Erros undo();

    //At Orientadores
    Erros addAtOrientadores();
    String mostraPropostasOrientadas();
    Erros addManOrientadores(String docente,String idPropostas);
    Erros removeOrientadorProposta(String idProposta);
    Erros removeOrientadorTodasProposta(String emailDocente);
    Erros editAtOrientadores(String idProposta, String novoDocente);
    List<String> mostraPropostasOrientadasList();
    void undoAtOrientadores();
    void redoAtOrientadores();

    //Consulta
    int distAlunosRamos(String ramo);
    LinkedHashMap<String, Integer> getTop5EmpEstagio();
    LinkedHashMap<String,Integer> getTop5Docentes();
    int nPropAtNAt(String s);
    String alunosAtribuidos();
    String alunosNAtribuidos();
    String propDisponiveis();
    String propAtribuidas();
    String orientacoesPorDocente();
    float mediaOrientacoes();
    int minimoOrientacoes();
    int maximoOrientacoes();
    int numeroOrientacoesDocente(String docente);

    //Ficheiros
    boolean importFile(File f);
    boolean exportFile(File f);
    boolean seguinte(); //Passar para a fase seguinte (sem fechar a fase)
    GPEState getState();
}
