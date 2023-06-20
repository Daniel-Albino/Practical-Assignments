package pt.isec.tp_pd.model.fsm.states;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.model.fsm.TicketPdStateAdapter;

import java.util.List;

public class SignUpState extends TicketPdStateAdapter {
    public SignUpState(TicketPdContext context, ModelData data) {
        super(context,data);
    }

    @Override
    public TicketPdState getState() {
        return TicketPdState.SIGNUPSTATE;
    }

    @Override
    public void InitialPage(){changeState(TicketPdState.INITIALSTATE);}
    @Override
    public void OrdersPage(){changeState(TicketPdState.ORDERSSTATE);}


    @Override
    public void setSignUpAttributes(String name, String user, String pass){data.setName(name);data.setUsername(user);data.setPassword(pass);}

    @Override
    public void setEspetaculos(List<Espetaculo> espetaculos){data.setEspetaculos(espetaculos);}


}
