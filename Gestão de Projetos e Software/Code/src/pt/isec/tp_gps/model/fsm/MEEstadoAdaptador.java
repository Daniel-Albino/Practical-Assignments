package pt.isec.tp_gps.model.fsm;

import pt.isec.tp_gps.model.data.Medicamento;
import pt.isec.tp_gps.model.data.Principal;
import pt.isec.tp_gps.model.data.Utente;

public abstract class MEEstadoAdaptador implements IMEState {
    protected MEContexto context;
    protected Principal data;

    protected MEEstadoAdaptador(MEContexto context, Principal data) {
        this.context = context;
        this.data = data;
    }

    protected void changeState(MEEstados newState) {
        context.changeState(newState.createState(context,data));
    }
    @Override
    public MEEstados getState() {
        return null;
    }



    @Override
    public void changeMessage(String msg) {

    }

    @Override
    public void changeNumber(int nr) {

    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }
    @Override
    public boolean addPerson(Utente aux){
        return false;
    }

    @Override
    public boolean removePerson(String cc){return false;}

    @Override
    public boolean addPill(Medicamento aux){
        return false;
    }

    @Override
    public boolean removePill(int id){return false;}





    @Override
    public void homePage(){}
    @Override
    public void personPage(){}
    @Override
    public void pillPage(){}
}
