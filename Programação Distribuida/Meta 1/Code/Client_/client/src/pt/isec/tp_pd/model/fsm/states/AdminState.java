package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

public class AdminState extends TicketPdStateAdapter {
    public AdminState(TicketPdContext context, ModelData data) {
        super(context, data);
    }

    @Override
    public TicketPdState getState() {
        return TicketPdState.ADMINSTATE;
    }


}
