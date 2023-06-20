package pt.isec.tp_pd.server;

import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.server.thread.MulticastThread;
import pt.isec.tp_pd.server.thread.TcpThreads;
import pt.isec.tp_pd.server.thread.UdpThreads;

import java.io.IOException;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Sintaxe: java <port_client> <interface_used_for_multicast> <data_base_directory>");
            return;
        }
        //Porto UDP
        int portClientUDP = Integer.parseInt(args[0]);

        //Network Interface para o Multicast
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName(args[1]);
        } catch (SocketException e) {
            System.err.println("Network Interface invalida!");
            return;
        }

        //Atribuição de um porto TCP automático
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            System.err.println("Impossível de iniciar o Server Socket!");
            return;
        }


        MulticastSocket multicastSocket = null;
        InetAddress ipMulticast = null;
        int portMulticast = 4004;

        //Multicast Socket
        try {
            multicastSocket = new MulticastSocket(portMulticast);
        } catch (IOException e) {
            System.err.println("Erro no Multicast Socket!");
            try {
                serverSocket.close();
            } catch (IOException ex) {
                System.err.println("Erro no Server Socket Close!");
                return;
            }
            return;
        }

        //IP Multicast
        try {
            ipMulticast = InetAddress.getByName("239.39.39.39");
        }catch (UnknownHostException e){
            System.err.println("Erro no Inet Address!");
            return;
        }

        SocketAddress socketAddress = new InetSocketAddress(ipMulticast,9001);

        //Server Data -> toda a informação do servidor
        ServerData serverData = null;
        if(serverSocket != null || networkInterface != null || multicastSocket != null || ipMulticast != null)
            serverData = new ServerData(portClientUDP,networkInterface,serverSocket.getLocalPort(),
                    multicastSocket,portMulticast,ipMulticast,socketAddress,args[2]);
        ListServer listServer = new ListServer();


        //....added
        ServersToClient serversToClient = new ServersToClient();

        //Thread do multicast
        MulticastThread multicastThread = new MulticastThread(serverData,listServer);
        multicastThread.setName("Multicast Thread");
        multicastThread.start();

        //Thread que aguarda as ligações TCP
        TcpThreads tcpThreads = new TcpThreads(serverData,serverSocket);
        tcpThreads.setName("TCP Threads");
        tcpThreads.start();

        UdpThreads udpThreads = new UdpThreads(serverData,listServer,serversToClient);
        udpThreads.setName("UPD Threads");
        udpThreads.start();

        try {
            multicastThread.join();
            tcpThreads.join();
            udpThreads.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
