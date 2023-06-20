package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;

//Classe que envia o pedido ao servidor
public class ServerRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Messages messages;
    private String FILENAME = "PD-2022-23-TP.db";

    private RequestClientDetailed clientDetailed;

    public ServerRequest(){

    }

    public ServerRequest(Messages messages) {
        this.messages = messages;
    }

    public RequestClientDetailed getClientDetailed() {
        return clientDetailed;
    }

    public void setClientDetailed(RequestClientDetailed clientDetailed) {
        this.clientDetailed = clientDetailed;
    }

    public Messages getMessages() {
        return messages;
    }
    public String getFILENAME() {
        return FILENAME;
    }
}
