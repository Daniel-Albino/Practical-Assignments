package pt.isec.tp_gps.model.fsm.states;


import pt.isec.tp_gps.model.data.Encomenda;
import pt.isec.tp_gps.model.data.Principal;
import pt.isec.tp_gps.model.fsm.MEContexto;
import pt.isec.tp_gps.model.fsm.MEEstadoAdaptador;
import pt.isec.tp_gps.model.fsm.MEEstados;

public class EstadoInicial extends MEEstadoAdaptador {
    public EstadoInicial(MEContexto context, Principal data) {
        super(context,data);
    }

    @Override
    public void next() {
        changeState(MEEstados.ESTADOPRINCIPAL);
    }

    @Override
    public void encomendasPage() {
        changeState(MEEstados.ESTADOENCOMENDAS);
    }

    @Override
    public boolean addEncomenda(Encomenda aux) {
        return false;
    }

    @Override
    public MEEstados getState() {
        return MEEstados.ESTADOINICIAL;
    }
}
