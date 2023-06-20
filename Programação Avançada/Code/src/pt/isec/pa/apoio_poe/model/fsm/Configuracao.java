package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.Serializable;
import java.util.Map;


public class Configuracao extends GPEStateAdapter implements Serializable {

    protected Configuracao(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public boolean gerirAlunos() {
        changeState(GPEState.GESTAO_ALUNOS);
        return true;
    }

    @Override
    public boolean gerirDocentes() {
        changeState(GPEState.GESTAO_DOCENTES);
        return true;
    }

    @Override
    public boolean gerirPropostas() {
        changeState(GPEState.GESTAO_PROPOSTAS);
        return true;
    }

    public boolean seguinte(){
        changeState(GPEState.OPCOES_CANDIDATURA);
        return true;
    }

    @Override
    public boolean terminar() {
        changeState(GPEState.INICIO);
        return true;
    }

    @Override
    public Erros fecharFase() {
        Erros erros = data.fecharFase(getState());
        if(erros.equals(Erros.NONE))
            changeState(GPEState.OPCOES_CANDIDATURA);
        return erros;
    }

    @Override
    public boolean isFechado() {
        return data.isFechado(getState());
    }

    @Override
    public GPEState getState() {
        return GPEState.CONFIGURACAO;
    }
}
