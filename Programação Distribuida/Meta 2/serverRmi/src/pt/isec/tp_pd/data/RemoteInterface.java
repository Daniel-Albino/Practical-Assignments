package pt.isec.tp_pd.data;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    public void addListener (_NotificationListeners listener) throws RemoteException;

    public void removeListener (_NotificationListeners listener) throws RemoteException;


    public String getServers() throws RemoteException;

}
