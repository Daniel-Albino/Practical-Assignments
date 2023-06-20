package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServersToClient implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    List<ServerAttributes> servers;

    public ServersToClient() {
        this.servers = new ArrayList<>();
    }

    public void add(ServerAttributes _new){
        servers.add(_new);
    }

    public List<ServerAttributes> getListServers(){return servers;}

    @Override
    public String toString() {
        return "ServersToClient{" +
                "servers=" + servers +
                '}';
    }
}
