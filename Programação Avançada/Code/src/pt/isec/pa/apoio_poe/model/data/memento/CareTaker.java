package pt.isec.pa.apoio_poe.model.data.memento;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class CareTaker implements Serializable {
    IOriginator originator;
    Deque<IMemento> history;
    Deque<IMemento> redoHist;
    public CareTaker(IOriginator originator) {
        this.originator = originator;
        history = new ArrayDeque<>();
        redoHist= new ArrayDeque<>();
    }

    public void saveCaretaker() {
        redoHist.clear();
        history.push(originator.save());
    }

    public void undoCaretaker() {
        if (history.isEmpty()) {
            return;
        }
        redoHist.push(originator.save());
        originator.restore(history.pop());
    }

    public void redoCaretaker() {
        if (redoHist.isEmpty())
            return;
        history.push(originator.save());
        originator.restore(redoHist.pop());
    }

    public void resetCaretaker() {
        history.clear();
        redoHist.clear();
    }

    public boolean hasUndoCaretaker() {
        return !history.isEmpty();
    }

    public boolean hasRedoCaretaker() {
        return !redoHist.isEmpty();
    }
}
