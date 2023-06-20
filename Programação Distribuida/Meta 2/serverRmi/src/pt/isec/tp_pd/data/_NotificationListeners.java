package pt.isec.tp_pd.data;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface _NotificationListeners extends Remote {

    public void ClientReceivedUDP(String phrase) throws RemoteException;

    public void ClientConnectionTCP(String phrase) throws RemoteException;

    public void LostConnectionTCP(String phrase) throws RemoteException;

    public void LoginClient(String phrase) throws RemoteException;

    public void LogoutClient(String phrase) throws RemoteException;
}
