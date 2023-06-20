package pt.isec.pa.apoio_poe.model.data.memento;

public interface IOriginator {
    IMemento save();
    void restore(IMemento memento);
}
