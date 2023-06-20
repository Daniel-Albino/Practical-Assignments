package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.io.Serializable;
import java.util.List;

public class AtribuicaoAutomaticaPropostas extends GPEStateAdapter implements Serializable {
    public AtribuicaoAutomaticaPropostas(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public Erros addAtPropostas(){return data.addAtPropostas();}

    @Override
    public Erros desempate(String alunoDesempatado) {
        return data.desempate(alunoDesempatado);
    }

    @Override
    public String getAlunosEmpatados() {
        return data.getAlunosEmpatados();
    }

    @Override
    public Proposta getPropEmpatada() {
        return data.getPropEmpatada();
    }

    @Override
    public List<Alunos> getAlunosEmpatadosList() {
        return data.getAlunosEmpatadosList();
    }

    public boolean seguinte(){
        changeState(GPEState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public GPEState getState() {
        return GPEState.ATRIBUICAO_AUTOMATICA_PROPOSTAS;
    }
}
