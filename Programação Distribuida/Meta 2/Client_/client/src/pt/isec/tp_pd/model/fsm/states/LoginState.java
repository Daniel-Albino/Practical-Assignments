package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

import java.util.List;

public class LoginState extends TicketPdStateAdapter {
    public LoginState(TicketPdContext context, ModelData data) {
        super(context,data);
    }

    @Override
    public void InitialPage(){changeState(TicketPdState.INITIALSTATE);}
    @Override
    public void OrdersPage(){changeState(TicketPdState.ORDERSSTATE);}
    @Override
    public void AdminPage(){changeState(TicketPdState.ADMINSTATE);}
    @Override
    public TicketPdState getState() {
        return TicketPdState.LOGINSTATE;
    }


    @Override
    public void setServers(ServersToClient servers){data.setServers(servers);}


    @Override
    public void setLoginAttributes(String user, String pass){data.setUsername(user);data.setPassword(pass);}

    @Override
    public void setEspetaculos(List<Espetaculo> espetaculos){data.setEspetaculos(espetaculos);}
}
