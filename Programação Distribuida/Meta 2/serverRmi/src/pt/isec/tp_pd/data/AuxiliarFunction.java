package pt.isec.tp_pd.data;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class AuxiliarFunction extends UnicastRemoteObject implements _NotificationListeners {

    public static final String SERVICE_NAME = "SHOW_SERVICE_";
    String porto;
    String register;

    RemoteInterface remote;

    threadListserver th;

    boolean keepGoing;


    public AuxiliarFunction(String porto, String register) throws RemoteException {
        this.porto = porto;
        this.register = register;
        init();
    }


    public String getPorto() {
        return porto;
    }

    public void setPorto(String porto) {
        this.porto = porto;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }


    public void init() throws RemoteException {
        try {

            String registration = "rmi://" + this.register +"/"+ SERVICE_NAME+porto;

            System.out.println("Registo:"+registration);

            Remote remoteService = Naming.lookup(registration);
            remote = (RemoteInterface) remoteService;



            keepGoing = true;
            th = new threadListserver(keepGoing,remote);
            th.start();

            String servers = remote.getServers();
            System.out.println("Servers:"+servers);



            remote.addListener(this);

            System.out.println("should crash");

        } catch (NotBoundException e) {
            System.out.println("No sensors available");
        } catch (RemoteException e) {
            System.out.println("RMI Error - " + e);
        } catch (Exception e) {
            System.out.println("Error - " + e);
        }finally{
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
            remote.removeListener(this);
            try {
                keepGoing = false;
                th.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);


        }
    }

    @Override
    public void ClientReceivedUDP(String phrase) throws RemoteException {
        System.out.println("Received: "+phrase);
    }

    @Override
    public void ClientConnectionTCP(String phrase) throws RemoteException {
        System.out.println("Received: "+phrase);
    }

    @Override
    public void LostConnectionTCP(String phrase) throws RemoteException {
        System.out.println("Received: "+phrase);
    }

    @Override
    public void LoginClient(String phrase) throws RemoteException {
        System.out.println("Received: "+phrase);
    }

    @Override
    public void LogoutClient(String phrase) throws RemoteException {
        System.out.println("Received: "+phrase);
    }
}
