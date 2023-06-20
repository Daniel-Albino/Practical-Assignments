package pt.isec.tp_pd.model.fsm;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.data.ModelData;

import java.util.List;

public class TicketPdContext {
    ModelData data;
    ITicketPdState state;

    public TicketPdContext() {
        data = new ModelData();
        state = TicketPdState.INITIALSTATE.createState(this,data);
    }


    void changeState(ITicketPdState state) {
        this.state = state;
    }
    public TicketPdState getState() {
        return state.getState();
    }

    public void ConnectionPage(){state.ConnectionPage();}
    public void InitialPage(){state.InitialPage();}
    public void SignupPage(){state.SignupPage();}
    public void LoginPage(){state.LoginPage();}
    public void OrdersPage(){state.OrdersPage();}
    public void EditPage(){state.EditPage();}
    public void SearchPage(){state.SearchPage();}
    public void ReservePage(){state.ReservePage();}
    public void AdminPage(){state.AdminPage();}

    public void setServers(ServersToClient servers){state.setServers(servers);}

    public void setAttributes(String ip, String port) {
        state.setSetupNetworkAttributes(ip,port);
    }
    public void setLoginAttributes(String user, String pass) {
        state.setLoginAttributes(user,pass);
    }

    public void setSignUpAttributes(String name, String user, String pass) {
        state.setSignUpAttributes(name,user,pass);
    }

    public void setEspetaculos(List<Espetaculo> espetaculos){state.setEspetaculos(espetaculos);}



    public ModelData getData(){
        return data;
    }


}
