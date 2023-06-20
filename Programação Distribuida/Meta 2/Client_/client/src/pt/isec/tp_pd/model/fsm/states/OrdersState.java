package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

public class OrdersState extends TicketPdStateAdapter {
    public OrdersState(TicketPdContext context, ModelData data) {
        super(context,data);
    }


    @Override
    public TicketPdState getState() {
        return TicketPdState.ORDERSSTATE;
    }


    @Override
    public void EditPage(){changeState(TicketPdState.EDITUSER);}
    @Override
    public void SearchPage(){changeState(TicketPdState.SEARCHSHOW);}
    @Override
    public void ReservePage(){changeState(TicketPdState.REVIEWRESERVE);}
}

