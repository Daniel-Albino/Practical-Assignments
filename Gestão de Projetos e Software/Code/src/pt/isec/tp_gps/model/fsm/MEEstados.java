package pt.isec.tp_gps.model.fsm;

import pt.isec.tp_gps.model.data.Principal;
import pt.isec.tp_gps.model.fsm.states.*;

public enum MEEstados {
    ESTADOINICIAL,ESTADOPRINCIPAL, ESTADOUTENTE, ESTADOMEDICAMENTOS, ESTADOENCOMENDAS;

    public IMEState createState(MEContexto context, Principal data) {
        return switch (this) {
            case ESTADOINICIAL -> new EstadoInicial(context,data);
            case ESTADOPRINCIPAL -> new EstadoPrincipal(context,data);
            case ESTADOUTENTE -> new EstadoUtentes(context,data);
            case ESTADOMEDICAMENTOS -> new EstadoMedicamentos(context,data);
            case ESTADOENCOMENDAS -> new EstadoEncomendas(context,data);

        };
    }
}
