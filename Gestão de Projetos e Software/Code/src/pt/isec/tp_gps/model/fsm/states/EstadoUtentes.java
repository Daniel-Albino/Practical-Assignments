package pt.isec.tp_gps.model.fsm.states;

import pt.isec.tp_gps.model.data.Encomenda;
import pt.isec.tp_gps.model.data.Principal;
import pt.isec.tp_gps.model.data.Utente;
import pt.isec.tp_gps.model.fsm.MEContexto;
import pt.isec.tp_gps.model.fsm.MEEstadoAdaptador;
import pt.isec.tp_gps.model.fsm.MEEstados;

public class EstadoUtentes extends MEEstadoAdaptador {
    public EstadoUtentes(MEContexto context, Principal data) {
        super(context,data);
    }

    @Override
    public void homePage(){changeState(MEEstados.ESTADOPRINCIPAL);}
    @Override
    public void personPage(){changeState(MEEstados.ESTADOUTENTE);}
    @Override
    public void pillPage(){changeState(MEEstados.ESTADOMEDICAMENTOS);}

    @Override
    public void encomendasPage(){changeState(MEEstados.ESTADOENCOMENDAS);}

    @Override
    public boolean addEncomenda(Encomenda aux) {
        return false;
    }


    @Override
    public boolean addPerson(Utente aux){
        //return data.addPerson(aux);
        return false;
    }

    @Override
    public boolean removePerson(String cc){
        //return data.removePerson(cc);
        return false;
    }



    @Override
    public MEEstados getState() {
        return MEEstados.ESTADOUTENTE;
    }
}
