package pt.isec.tp_pd.model.fsm;

import pt.isec.tp_pd.model.data.ModelData;
import pt.isec.tp_pd.model.fsm.states.*;

public enum TicketPdState {
    CONNECTIONSTATE,EDITUSER,REVIEWRESERVE,SEARCHSHOW,ADMINSTATE,INITIALSTATE,SIGNUPSTATE, LOGINSTATE, ORDERSSTATE;

    public ITicketPdState createState(TicketPdContext context, ModelData data) {
        return switch (this) {
            case CONNECTIONSTATE -> new ConnectionState(context,data);
            case EDITUSER -> new EditDataState(context,data);
            case REVIEWRESERVE -> new ConsultaReservesState(context,data);
            case SEARCHSHOW -> new SearchShow(context, data);
            case ADMINSTATE -> new AdminState(context,data);
            case INITIALSTATE -> new InitialState(context,data);
            case SIGNUPSTATE -> new SignUpState(context,data);
            case LOGINSTATE -> new LoginState(context,data);
            case ORDERSSTATE -> new OrdersState(context,data);
            //default -> null;
        };
    }
}
