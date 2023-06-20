package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.data.HeardbeatsMessages;
import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.server.messages.SendHeardbeats;

import java.io.IOException;


//Thread que envia o seu multicast e na fase de arranque espera por Heardbeats -> cria a base de dados ou pede base de dados
public class MulticastThread extends Thread{
    private ServerData serverData;
    private ListServer listServer;


    public MulticastThread(ServerData severData, ListServer listServer) {
        this.serverData = severData;
        this.listServer = listServer;
    }

    @Override
    public void run() {
        try {
            serverData.getMulticastSocket().joinGroup(serverData.getSocketAddress(),serverData.getNetworkInterface());
        } catch (IOException e) {
            System.err.println("Impossível juntar-se ao grupo Multicast!");
        }
        ReceiveMessages receiveMessages = new ReceiveMessages(serverData,listServer);
        receiveMessages.setName("Receive Messages Thread");
        receiveMessages.start();
        VerifyHeardbeats verifyHeardbeats = new VerifyHeardbeats(listServer,serverData);
        verifyHeardbeats.setName("Verify Heardbeats Thread");
        verifyHeardbeats.start();
        boolean keepGoing = true;

        try {
            sleep(300);//30000
        } catch (InterruptedException e) {}

        serverData.setIdServer(listServer.getSize(),listServer.getListServer());


        HeardbeatsMessages myHeardbeatsMessages = new HeardbeatsMessages(serverData.getServerName(), serverData.getIpServer(),serverData.getPortTCP(), serverData.getPortTCP(),serverData.getVersionDB());
        receiveMessages.setMyHeardbeatsMessages(myHeardbeatsMessages);
        while (keepGoing){
            SendHeardbeats sendHeardbeats = new SendHeardbeats();
            sendHeardbeats.sendHeardbeats(listServer,myHeardbeatsMessages,serverData);

            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            myHeardbeatsMessages.newDateHeardbeat();
            receiveMessages.setMyHeardbeatsMessages(myHeardbeatsMessages);
        }
        try {
            serverData.getMulticastSocket().leaveGroup(serverData.getSocketAddress(),serverData.getNetworkInterface());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverData.getMulticastSocket().close();
        try {
            receiveMessages.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
