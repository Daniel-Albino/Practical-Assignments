package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;

//Classe com a informação enviada pelos Heardbeats
public class HeardbeatsMessages implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nameServer;
    private String ipServer;
    private int portTCPClient;
    private int portTCP;
    private boolean available;
    private int activeConnections;
    private String dateHeardbeat;
    private int versionDB;

    public HeardbeatsMessages(String nameServer,String ipServer,int portTCPClient,int portTCP,int versionDB) {
        this.nameServer = nameServer;
        this.ipServer = ipServer;
        this.portTCPClient = portTCPClient;
        this.portTCP = portTCP;
        this.available = true;
        this.activeConnections = 0;
        this.versionDB = versionDB;
        newDateHeardbeat();
    }

    public void newDateHeardbeat(){
        Calendar calendar = Calendar.getInstance();
        dateHeardbeat = String.format(
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + ":" +
                calendar.get(Calendar.SECOND));
    }

    public int getPortTCPClient() {
        return portTCPClient;
    }

    public void setPortTCPClient(int portTCPClient) {
        this.portTCPClient = portTCPClient;
    }

    public String getDateHeardbeat() {
        return dateHeardbeat;
    }

    public void setDateHeardbeat(String dateHeardbeat) {
        this.dateHeardbeat = dateHeardbeat;
    }

    public String getNameServer() {
        return nameServer;
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

    public int getVersionDB() {
        return versionDB;
    }

    public void setVersionDB(int versionDB) {
        this.versionDB = versionDB;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    @Override
    public String toString() {
        return "HeardbeatsMessages{" +
                "nameServer='" + nameServer + '\'' +
                ", ipServer=" + ipServer +
                ", portTCP=" + portTCP +
                ", available=" + available +
                ", activeConnections=" + activeConnections +
                ", dateHeardbeat='" + dateHeardbeat + '\'' +
                ", versionDB=" + versionDB +
                '}';
    }
}
