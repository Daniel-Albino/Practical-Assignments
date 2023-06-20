package pt.isec.pa.apoio_poe.model.data.command;

import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.Serializable;
import java.util.Map;

public class AddAtPropostasCommand extends CommandAdapter implements Serializable {
    private final String idProposta;
    private final Long nAluno;


    public AddAtPropostasCommand(Data data, String idProposta, Long nAluno) {
        super(data);
        this.idProposta = idProposta;
        this.nAluno = nAluno;
    }


    @Override
    public Erros execute() {
        return data.addAtPropostasManualmente(idProposta, nAluno);
    }

    @Override
    public Erros undo() {
        return data.removeAtPropostasProp(idProposta);
    }
}
