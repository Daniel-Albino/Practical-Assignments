

import pt.isec.tp_pd.data.AuxiliarFunction;
import pt.isec.tp_pd.data.RemoteInterface;
import pt.isec.tp_pd.data._NotificationListeners;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws RemoteException {

        System.out.println("Looking for Something. I GUESS!!");


        if (args.length != 2)
            return;
        AuxiliarFunction Observer = new AuxiliarFunction(args[0],args[1]);

    }

}