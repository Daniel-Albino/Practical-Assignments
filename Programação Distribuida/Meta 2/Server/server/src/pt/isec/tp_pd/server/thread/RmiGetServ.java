
/*
package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.HeardbeatsMessages;
import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.data.RemoteInterface;
import pt.isec.tp_pd.server.thread.rmi.rmiService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class RmiGetServ extends Thread{
    private ServerData serverData;
    private ListServer listServer;
    private int port;

    public RmiGetServ(ServerData severData, ListServer listServer,int port) {
        this.serverData = severData;
        this.listServer = listServer;
        this.port = port;
    }

    @Override
    public void run() {

        try {
            rmiService a = new rmiService(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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


        serverData.setIdServer(listServer.getSize(),listServer.getListServer());


        RemoteInterface ServersService=null;

        try {
            ServersService = (RemoteInterface) Naming
                    .lookup("rmi://"+ InetAddress.getLocalHost().getHostAddress()+"/SHOW_SERVICE_9002");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        List<HeardbeatsMessages> aux = null;
        try {
            aux = ServersService.getServers();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        HeardbeatsMessages myHeardbeatsMessages = new HeardbeatsMessages(String.format("Server%d", aux.size()+1), serverData.getIpServer(),serverData.getPortTCP(), serverData.getPortTCP(),serverData.getVersionDB());
        //receiveMessages.setMyHeardbeatsMessages(myHeardbeatsMessages);
        myHeardbeatsMessages.newDateHeardbeat();

        try {
            ServersService.addServer(myHeardbeatsMessages);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }



        while (keepGoing){
            //SendHeardbeats sendHeardbeats = new SendHeardbeats();
            //sendHeardbeats.sendHeardbeats(listServer,myHeardbeatsMessages,serverData);


            aux = null;
            try {
                aux = ServersService.getServers();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Servers(RMI): ");

            if(aux != null)
            for(HeardbeatsMessages a : aux)
                System.out.println(""+a.toString());

            HeardbeatsMessages myHeardbeatsMessages1 = new HeardbeatsMessages(String.format("Server%d", aux.size()+1), serverData.getIpServer(),serverData.getPortTCP(), serverData.getPortTCP(),serverData.getVersionDB());
            //receiveMessages.setMyHeardbeatsMessages(myHeardbeatsMessages);
            myHeardbeatsMessages.newDateHeardbeat();
            try {
                ServersService.addServer(myHeardbeatsMessages);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }


            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            myHeardbeatsMessages.newDateHeardbeat();
            //receiveMessages.setMyHeardbeatsMessages(myHeardbeatsMessages);
        }
/*        try {
            serverData.getMulticastSocket().leaveGroup(serverData.getSocketAddress(),serverData.getNetworkInterface());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverData.getMulticastSocket().close();*/
/*        try {
            receiveMessages.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
*/