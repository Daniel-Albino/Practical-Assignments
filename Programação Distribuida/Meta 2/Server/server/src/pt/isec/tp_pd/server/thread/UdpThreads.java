package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServerAttributes;
import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.server.thread.rmi.NotifyCode;
import pt.isec.tp_pd.server.thread.rmi.rmiService;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UdpThreads extends Thread{
    public static final int MAX_SIZE = 4096; //256
    public static final String REQUEST = "CONNECT";
    public static final int TIMEOUT = 10; //segundos
    private ServerData serverData;
    private ListServer listServer;
    private ServersToClient serversToClient;
    private rmiService rmiService;


    public UdpThreads(ServerData serverData, ListServer listServer, ServersToClient serversToClient, rmiService rmiService) {
        this.serverData = serverData;
        this.listServer = listServer;
        this.serversToClient = serversToClient;
        this.rmiService = rmiService;
    }

    @Override
    public void run() {
        int listeningPort = serverData.getPortClientUDP();
        DatagramSocket socket = null;
        DatagramPacket packet;

        ByteArrayInputStream bin;
        ObjectInputStream oin;

        ByteArrayOutputStream bout;
        ObjectOutputStream oout;

        String receivedMsg;
        Calendar calendar;

        try {

            socket = new DatagramSocket(listeningPort);

            System.out.println("Waiting for client...");

            while (true) {

                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);


                //Deserializar os bytes recebidos (objecto do tipo String)
                bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                oin = new ObjectInputStream(bin);
                receivedMsg = (String) oin.readObject();

                rmiService.notifyListeners(NotifyCode.CLIENTUDP,"Ligação UDP do cliente com " +
                        "@IP:Port -> " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

                System.out.println("Recebido " + receivedMsg + " de " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if (!receivedMsg.equalsIgnoreCase(REQUEST)) {
                    continue;
                }


                for(int i=0;i<listServer.getSize();++i){
                    ServerAttributes newserver = new ServerAttributes();
                    newserver.setNameServer(listServer.getListServer().get(i).getNameServer());
                    newserver.setIpServer(listServer.getListServer().get(i).getIpServer());
                    newserver.setPortTCP(listServer.getListServer().get(i).getPortTCP());
                    newserver.setActiveConnections(listServer.getListServer().get(i).getActiveConnections());
                    newserver.setAvailable(listServer.getListServer().get(i).isAvailable());
                    serversToClient.add(newserver);
                }




                //Serializar o objecto calendar para bout
                bout = new ByteArrayOutputStream();
                oout = new ObjectOutputStream(bout);
                //oout.writeObject(serversToClient);
                oout.writeUnshared(serversToClient);

                packet.setData(bout.toByteArray());
                packet.setLength(bout.size());

                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);
            }

        } catch (Exception e) {
            System.out.println("Problema:\n\t" + e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
