package pt.isec.tp_pd.model.fsm;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.ServersToClient;

import java.util.List;

public interface ITicketPdState {
    TicketPdState getState();

    void ConnectionPage();
    void InitialPage();
    void SignupPage();
    void LoginPage();
    void OrdersPage();
    void EditPage();
    void SearchPage();
    void ReservePage();
    void AdminPage();



    void setServers(ServersToClient servers);

    void setSetupNetworkAttributes(String ip, String port);

    void setLoginAttributes(String user, String pass);

    void setSignUpAttributes(String name, String user, String pass);

    void setEspetaculos(List<Espetaculo> espetaculos);

    //String getAttr();
}
