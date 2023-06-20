package pt.isec.tp_gps.model.fsm;

import pt.isec.tp_gps.model.data.Encomenda;
import pt.isec.tp_gps.model.data.Medicamento;
import pt.isec.tp_gps.model.data.Utente;

public interface IMEState {
    MEEstados getState();

    void changeMessage(String msg);
    void changeNumber(int nr);

    void next();
    void previous();
    boolean addPerson(Utente aux);
    boolean removePerson(String cc);

    boolean addPill(Medicamento aux);
    boolean removePill(int id);


    void homePage();
    void personPage();
    void pillPage();


    void encomendasPage();

    boolean addEncomenda(Encomenda aux);
}
