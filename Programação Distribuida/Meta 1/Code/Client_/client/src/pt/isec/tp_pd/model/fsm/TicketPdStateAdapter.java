package pt.isec.tp_pd.model.fsm;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.data.ModelData;

import java.util.List;

public abstract class TicketPdStateAdapter implements ITicketPdState{
    protected TicketPdContext context;
    protected ModelData data;

    protected TicketPdStateAdapter(TicketPdContext context, ModelData data) {
        this.context = context;
        this.data = data;
    }

    protected void changeState(TicketPdState newState) {
        context.changeState(newState.createState(context,data));
    }

    @Override
    public TicketPdState getState() {
        return null;
    }


    @Override
    public void ConnectionPage(){}
    @Override
    public void InitialPage(){}
    @Override
    public void SignupPage(){}
    @Override
    public void LoginPage(){}
    @Override
    public void OrdersPage(){}
    @Override
    public void EditPage(){}
    @Override
    public void SearchPage(){}
    @Override
    public void ReservePage(){}
    @Override
    public void AdminPage(){}


    @Override
    public void setServers(ServersToClient servers){}
    @Override
    public void setSetupNetworkAttributes(String ip, String port){}

    @Override
    public void setLoginAttributes(String user, String pass){}

    @Override
    public void setSignUpAttributes(String name, String user, String pass){}

    @Override
    public void setEspetaculos(List<Espetaculo> espetaculos){}

}
