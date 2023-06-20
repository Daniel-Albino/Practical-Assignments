package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

public class ConsultaReservesState extends TicketPdStateAdapter {
    public ConsultaReservesState(TicketPdContext context, ModelData data) {
        super(context, data);
    }


    @Override
    public TicketPdState getState() {
        return TicketPdState.REVIEWRESERVE;
    }

    @Override
    public void OrdersPage(){changeState(TicketPdState.ORDERSSTATE);}
}
