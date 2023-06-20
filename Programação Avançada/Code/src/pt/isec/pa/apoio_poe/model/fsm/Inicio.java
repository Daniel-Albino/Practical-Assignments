package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;


import java.io.Serializable;
import java.util.Map;

public class Inicio extends GPEStateAdapter implements Serializable {
    public Inicio(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public boolean iniciar() {
        changeState(GPEState.CONFIGURACAO);
        return true;
    }


    @Override
    public GPEState getState() {
        return GPEState.INICIO;
    }
}
