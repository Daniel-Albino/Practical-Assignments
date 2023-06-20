package pt.isec.pa.apoio_poe.model.data.memento;

public interface IMemento {
    default Object getSnapshot() { return null; }
}
