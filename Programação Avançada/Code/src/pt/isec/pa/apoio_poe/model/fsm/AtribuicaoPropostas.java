package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.io.*;
import java.util.List;

public class AtribuicaoPropostas extends GPEStateAdapter implements Serializable {

    protected AtribuicaoPropostas(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public String getAtPropostas() {
        return data.getAtPropostas();
    }

    @Override
    public List<String> getAtPropostasList() {
        return data.getAtPropostasList();
    }

    @Override
    public Erros execute() {
        return data.redoAtPropostas();
    }

    @Override
    public Erros undo() {
        return data.undoAtPropostas();
    }

    @Override
    public boolean voltar() {
        changeState(GPEState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public boolean seguinte() {
        changeState(GPEState.ATRIBUICAO_ORIENTADORES);
        return true;
    }
    @Override
    public boolean atribuirAutomaticamente(){
        changeState(GPEState.ATRIBUICAO_AUTOMATICA_PROPOSTAS);
        return true;
    }

    @Override
    public Erros addAtPropostasManualmente(String idProposta, long nAluno) {
        return data.addAtPropostasManualmente(idProposta, nAluno);
    }

    @Override
    public Erros removeAtPropostasProp(String nProposta) {
        return data.removeAtPropostasProp(nProposta);
    }

    @Override
    public Erros removeAtPropostasAluno(Long nAluno) {
        return data.removeAtPropostasAluno(nAluno);
    }
    @Override
    public Alunos getAlunoNumber(long nAluno) {
        return data.getAlunoNumber(nAluno);
    }
    @Override
    public Proposta getProp(String idProposta) {
        return data.getProp(idProposta);
    }

    @Override
    public boolean exportFile(File f){

        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(data.getAtPropostas());

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Override
    public Erros fecharFase() {
        Erros erros = data.fecharFase(getState());
        if(erros.equals(Erros.NONE))
            changeState(GPEState.ATRIBUICAO_ORIENTADORES);
        return erros;
    }

    @Override
    public boolean isFechado() {
        return data.isFechado(getState());
    }

    @Override
    public GPEState getState() {
        return GPEState.ATRIBUICAO_PROPOSTAS;
    }
}
