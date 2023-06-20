package pt.isec.pa.apoio_poe.model.data.command;

import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager implements Serializable {
    private Deque<ICommand> historyAtPropostas;
    private Deque<ICommand> redoCmdsAtPropostas;

    public CommandManager() {
        historyAtPropostas = new ArrayDeque<>();
        redoCmdsAtPropostas = new ArrayDeque<>();
    }

    public Erros invokeCommand(ICommand cmd) {
        redoCmdsAtPropostas.clear();
        Erros erros = cmd.execute();
        if (erros.equals(Erros.NONE)) {
            historyAtPropostas.push(cmd);
            return Erros.NONE;
        }
        historyAtPropostas.clear();
        return erros;
    }

    public Erros undoAtPropostas() {
        if (historyAtPropostas.isEmpty()) {
            return Erros.UNDO;
        }
        ICommand cmd = historyAtPropostas.pop();


        Erros erros = cmd.undo();
        redoCmdsAtPropostas.push(cmd);
        System.out.println(erros);
        if(!erros.equals(Erros.NONE))
            return Erros.UNDO;
        return Erros.NONE;
    }

    public Erros redoAtPropostas() {
        if (redoCmdsAtPropostas.isEmpty())
            return Erros.REDO;

        ICommand cmd = redoCmdsAtPropostas.pop();
        Erros erros = cmd.execute();
        historyAtPropostas.push(cmd);

        if(!erros.equals(Erros.NONE))
            return Erros.REDO;
        return Erros.NONE;
    }

    public boolean hasUndo() {
        return historyAtPropostas.size()>0;
    }

    public boolean hasRedo() {
        return redoCmdsAtPropostas.size()>0;
    }
}
