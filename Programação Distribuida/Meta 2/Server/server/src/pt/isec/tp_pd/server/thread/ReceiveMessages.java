package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.HeardbeatsMessages;
import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.server.messages.SendHeardbeats;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.List;

//Thread que recebe Heardbeats do multicast
public class ReceiveMessages extends Thread{
    private MulticastSocket multicastSocket;
    private String nameServer;
    private ServerData serverData;
    private ListServer listServer;
    private HeardbeatsMessages myHeardbeatsMessages;
    public ReceiveMessages(ServerData serverData, ListServer listServer){
        this.serverData = serverData;
        this.multicastSocket = serverData.getMulticastSocket();
        this.nameServer = serverData.getServerName();
        this.listServer = listServer;
    }

    public synchronized void setMyHeardbeatsMessages(HeardbeatsMessages myHeardbeatsMessages) {
        this.myHeardbeatsMessages = myHeardbeatsMessages;
    }

    @Override
    public void run() {
        try {
            while (true){
                DatagramPacket datagramPacket = new DatagramPacket(new byte[256],256);
                multicastSocket.receive(datagramPacket);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                HeardbeatsMessages msg = (HeardbeatsMessages) objectInputStream.readObject();

                if(!msg.getNameServer().equals(nameServer)){
                    listServer.addListServer(msg);
                }
                System.out.println("Recive -> " +listServer);
                boolean bol = serverData.needDataBase(listServer);
                System.out.println(bol);
                if(bol) {
                    SendHeardbeats sendHeardbeats = new SendHeardbeats();
                    myHeardbeatsMessages.setAvailable(false);
                    System.out.println("Aqui!!!" + myHeardbeatsMessages);
                    sendHeardbeats.sendHeardbeats(listServer,myHeardbeatsMessages,serverData);
                    List<HeardbeatsMessages> listServerAux = serverData.getListServerAux();
                    if (listServerAux.size() > 0) {
                        RequestDB requestDB = new RequestDB(serverData, listServerAux);
                        requestDB.setName("Request DB Thread");
                        requestDB.start();
                        try {
                            requestDB.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        serverData.resetConnectionDB();
                    }
                }
            }
        }catch (IOException | ClassNotFoundException e){}
    }
}