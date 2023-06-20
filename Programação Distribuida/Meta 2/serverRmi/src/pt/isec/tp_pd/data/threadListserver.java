package pt.isec.tp_pd.data;

import java.rmi.RemoteException;

public class threadListserver extends Thread{

    boolean keepGoing;

    RemoteInterface remoteInterface;

    public threadListserver(boolean keepGoing, RemoteInterface remoteInterface) {
        this.keepGoing = keepGoing;
        this.remoteInterface = remoteInterface;
    }

    @Override
    public void run() {


        while(keepGoing){
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String servers = null;
            try {
                servers = remoteInterface.getServers();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Servers:"+servers);
        }
    }
}
