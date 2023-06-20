package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

public class ConnectionState extends TicketPdStateAdapter {

    public ConnectionState(TicketPdContext context, ModelData data) {
        super(context, data);
    }


    @Override
    public TicketPdState getState() {
        return TicketPdState.CONNECTIONSTATE;
    }

    @Override
    public void InitialPage(){changeState(TicketPdState.INITIALSTATE);}
    @Override
    public void SignupPage(){changeState(TicketPdState.SIGNUPSTATE);}

    @Override
    public void LoginPage(){changeState(TicketPdState.LOGINSTATE);}

    @Override
    public void OrdersPage(){changeState(TicketPdState.ORDERSSTATE);}

    @Override
    public void setServers(ServersToClient servers){data.setServers(servers);}


}
