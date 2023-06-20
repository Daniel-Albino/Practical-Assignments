package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

public class InitialState extends TicketPdStateAdapter {
    public InitialState(TicketPdContext context, ModelData data) {
        super(context,data);
    }


    @Override
    public TicketPdState getState() {
        return TicketPdState.INITIALSTATE;
    }

    @Override
    public void ConnectionPage(){changeState(TicketPdState.CONNECTIONSTATE);}

    @Override
    public void OrdersPage(){changeState(TicketPdState.ORDERSSTATE);}

    @Override
    public void SignupPage(){changeState(TicketPdState.SIGNUPSTATE);}

    @Override
    public void LoginPage(){changeState(TicketPdState.LOGINSTATE);}


    @Override
    public void setSetupNetworkAttributes(String ip, String port){data.setIp(ip);data.setPort(Integer.parseInt(port));}

    @Override
    public void setServers(ServersToClient servers){data.setServers(servers);}
}
