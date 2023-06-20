package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;

public class ServerAttributes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String nameServer;
    private String ipServer;
    private int portTCP;
    private boolean available;
    private int activeConnections;

    public ServerAttributes() {
        this.nameServer = "";
        this.portTCP = 0;
        this.available = true;
        this.activeConnections = 0;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public int getPortTCP() {
        return portTCP;
    }

    public void setPortTCP(int portTCP) {
        this.portTCP = portTCP;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }

    @Override
    public String toString() {
        return "ServerAttributes{" +
                "nameServer='" + nameServer + '\'' +
                "ipServer=" + ipServer +
                ", portTCP=" + portTCP +
                ", available=" + available +
                ", activeConnections=" + activeConnections +
                '}';
    }
}
