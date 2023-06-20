package pt.isec.pa.apoio_poe.model.data.command;

import pt.isec.pa.apoio_poe.model.data.Erros;

public interface ICommand {
    Erros execute();
    Erros undo();
}