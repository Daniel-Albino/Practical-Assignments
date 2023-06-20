package pt.isec.tp_pd.server.thread.rmi;

import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.RemoteInterface;
import pt.isec.tp_pd.data._NotificationListeners;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class rmiService extends UnicastRemoteObject implements RemoteInterface {
    public static final String SERVICE_NAME = "SHOW_SERVICE_";




    private static List<_NotificationListeners> listeners;
    ListServer listServer;
    private int port;

    public rmiService(int port,ListServer listServer) throws IOException, InterruptedException {
        listeners = new ArrayList<_NotificationListeners>();
        this.listServer = listServer;
        this.port = port;
        init();
    }

    private void init() {
        try {
            try {
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            } catch (RemoteException e) {
                System.out.println("Registry provavelmente ja' em execucao na maquina local!");
            }

            System.out.println("Servico GetRemoteFile criado e em execucao!");

            Naming.bind("rmi://"+ InetAddress.getLocalHost().getHostAddress() + "/" + SERVICE_NAME+port, this);

            System.out.println("Servico " + SERVICE_NAME + " registado no registry...");

            /* Para terminar um servico RMI do tipo UnicastRemoteObject: UnicastRemoteObject.unexportObject(timeService, true); */

        } catch (RemoteException e) {
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }

    @Override
    public void addListener(_NotificationListeners listener) throws RemoteException {
        System.out.println ("[➕] Adding listener: " + listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(_NotificationListeners listener) throws RemoteException {
        System.out.println ("[➖] Removing listener: " + listener);
        listeners.remove(listener);
    }

    @Override
    public synchronized String getServers() throws RemoteException {
        return listServer.getListServer().toString();
    }

    public void notifyListeners(NotifyCode notifyCode, String phrase) {
        System.out.println("[📡] Notifying changes...");
        for (_NotificationListeners listener : listeners)
            try {
                switch (notifyCode) {
                    case CLIENTUDP -> listener.ClientReceivedUDP(phrase);
                    case CLIENTTCP -> listener.ClientConnectionTCP(phrase);
                    case LOSTCONNECTIONTCP -> listener.LostConnectionTCP(phrase);
                    case LOGINCLIENT -> listener.LoginClient(phrase);
                    case LOGOUTCLIENT -> listener.LogoutClient(phrase);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
    }


}