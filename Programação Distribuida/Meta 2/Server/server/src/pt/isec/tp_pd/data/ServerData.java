package pt.isec.tp_pd.data;

import pt.isec.tp_pd.server.database.DBConnection;
import pt.isec.tp_pd.server.thread.RequestDB;

import java.io.Serializable;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static pt.isec.tp_pd.data.SelectUtilizador.USERNAME;

//Classe com informação do servidor
public class ServerData implements Serializable {
    private String serverName;
    private int idServer;
    private int portClientUDP;
    private NetworkInterface networkInterface;
    private int portTCP;
    private MulticastSocket multicastSocket;
    private int portMulticast;
    private InetAddress ipMulticast;
    private SocketAddress socketAddress;
    private String dbDirectory;
    private DBConnection dbConnection;

    private int versionDB = -1;

    public ServerData(int portClientUDP,
                      NetworkInterface networkInterface,
                      int portTCP,
                      MulticastSocket multicastSocket,
                      int portMulticast,
                      InetAddress ipMulticast,
                      SocketAddress socketAddress,
                      String directory) {
        this.idServer = -1;
        this.portClientUDP = portClientUDP;
        this.networkInterface = networkInterface;
        this.portTCP = portTCP;
        this.multicastSocket = multicastSocket;
        this.portMulticast = portMulticast;
        this.ipMulticast = ipMulticast;
        this.socketAddress = socketAddress;
        this.dbDirectory = directory;
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public int getVersionDB() {
        return versionDB;
    }

    public void setVersionDB() {
        versionDB = dbConnection.getVersion();
    }

    public String getDbDirectory() {
        return dbDirectory;
    }

    public void setDbDirectory(String dbDirectory) {
        this.dbDirectory = dbDirectory;
    }

    public int getIdServer() {
        return idServer;
    }
    public void setIdServer(int listSize, List<HeardbeatsMessages> list) {
        if(idServer == -1) {
            this.idServer = listSize+1;
            setServerName();
            dbConnection = new DBConnection(dbDirectory,serverName);
            setDbDirectory(dbConnection.getDBDIRECTORY());
        }
        System.out.println("listSize -> " + listSize);
        if (listSize == 0){
            dbConnection.setCreateDB();
            dbConnection.dbConnection();
        }else {
            RequestDB requestDB = new RequestDB(this,list);
            requestDB.setName("Request DB Thread");
            requestDB.start();
            dbConnection.dbConnection();
        }
        setVersionDB();
    }
    private void setServerName() {
        this.serverName = "Server" + idServer;
    }
    public String getServerName() {
        return serverName;
    }
    public String getIpServer(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        //return networkInterface.getInetAddresses().nextElement().getHostAddress();
    }
    public int getPortClientUDP() {
        return portClientUDP;
    }
    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }
    public int getPortTCP() {
        return portTCP;
    }
    public MulticastSocket getMulticastSocket() {
        return multicastSocket;
    }
    public InetAddress getIpMulticast() {
        return ipMulticast;
    }
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
    public int getPortMulticast() {
        return portMulticast;
    }
    private List<HeardbeatsMessages> listServerAux = new ArrayList<>();

    public synchronized boolean needDataBase(ListServer listServer){
        if(dbConnection != null && dbConnection.isConnected()) {
            List<HeardbeatsMessages> list = listServer.getListServer();
            listServerAux = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getNameServer().equals(getServerName()))
                    if (getVersionDB() < list.get(i).getVersionDB()) {
                        listServerAux.add(list.get(i));
                        return true;
                    }
            }
        }
        return false;
    }

    public synchronized boolean verifyUser(CredentialsLoginMessage aux){
        List<Utilizador> listusers = dbConnection.selectUtilizador(USERNAME, aux.getUsername());


        if(listusers != null){
            if(listusers.get(0).getPassword().equals(aux.getPassword())) {
                listusers.get(0).setAutenticado(1);
                dbConnection.updateUsername(listusers.get(0));
                return true;
            }
        }
        return false;
    }
    public synchronized boolean updateEspetaculo(Espetaculo espetaculo) {
        boolean bol = dbConnection.updateEspetaculo(espetaculo);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean insertEspetaculo(Espetaculo espetaculo) {
        boolean bol = false;
        bol = dbConnection.insertEspetaculo(espetaculo);
        if(bol){
            for (Lugar l : espetaculo.getLugaresList())
                bol = dbConnection.insertLugar(l);
        }
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean deleteEspetaculo(int id) {
        boolean bol = dbConnection.deleteEspetaculo(id);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized List<Espetaculo> selectEspetaculo(SelectEspetaculo selectEspetaculo, String filtro) {
        return dbConnection.selectEspetaculo(selectEspetaculo,filtro);
    }
    public synchronized boolean insertLugar(Lugar lugar) {
        boolean bol = dbConnection.insertLugar(lugar);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean updateLugar(Lugar lugar) {
        boolean bol = dbConnection.updateLugar(lugar);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean deleteLugar(int id) {
        boolean bol = dbConnection.deleteLugar(id);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean deleteLugarIdEspetaculo(int id) {
        boolean bol = dbConnection.deleteLugarIdEspetaculo(id);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized List<Lugar> selectLugarIdEspetaculo(int id_espetaculo) {
        return dbConnection.selectLugarIdEspetaculo(id_espetaculo);
    }
    public synchronized List<Lugar> selectLugarIdLugar(int id_lugar) {
        return dbConnection.selectLugarIdLugar(id_lugar);
    }
    public synchronized boolean insertUtilizador(Utilizador utilizador) {
        boolean bol = dbConnection.insertUtilizador(utilizador);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized List<Utilizador> selectUtilizador(SelectUtilizador selectUtilizador, String user)  {
        return dbConnection.selectUtilizador(selectUtilizador,user);
    }
    public synchronized boolean updateUsername(Utilizador utilizador) {
        return dbConnection.updateUsername(utilizador);
    }
    public synchronized boolean deleteUtilizadorUsername(String username) {
        boolean bol = dbConnection.deleteUtilizadorUsername(username);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean deleteUtilizadorNome(String nome) {
        boolean bol =  dbConnection.deleteUtilizadorNome(nome);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized List<Reserve> listReservations(String whereColumn, String whereValue) {
       return dbConnection.listReservations(whereColumn,whereValue);
    }
    public synchronized int maxReservation() {
        return dbConnection.maxReservation();
    }
    public synchronized boolean insertReservation(Reserve reserve) {
        boolean bol = dbConnection.insertReservation(reserve);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean updateReservation(Reserve reserve) {
        boolean bol = dbConnection.updateReservation(reserve);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean deleteReservation(int id) {
        boolean bol = dbConnection.deleteReservation(id);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized ReserveSeat listReservationsSeats(String whereColumn, int whereValue) {
        return dbConnection.listReservationsSeats(whereColumn,whereValue);
    }
    public synchronized boolean insertReservationsSeats(ReserveSeat reserveSeat) {
        boolean bol = dbConnection.insertReservationsSeats(reserveSeat);
        if(bol)
            dbConnection.updateVersion();
        return bol;
    }
    public synchronized boolean deleteReservationSeat(String whereColumn, int whereValue) {
        boolean bol = dbConnection.deleteReservationSeat(whereColumn,whereValue);
        if(bol)
            dbConnection.updateVersion();
        return bol;

    }
    public List<HeardbeatsMessages> getListServerAux() {
        return listServerAux;
    }
    public void resetConnectionDB() {
        dbConnection.dbConnection();
        setVersionDB();
        System.out.println("version -> " + versionDB);
    }
}
