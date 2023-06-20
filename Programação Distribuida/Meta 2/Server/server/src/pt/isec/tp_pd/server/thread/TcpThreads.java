package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.*;
import pt.isec.tp_pd.server.thread.rmi.NotifyCode;
import pt.isec.tp_pd.server.thread.rmi.rmiService;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//Thread que aguarda por ligações TCP
public class TcpThreads extends Thread {

    private rmiService a;
    private ServerData serverData;
    ServerSocket serverSocket;

    public TcpThreads(ServerData serverData, ServerSocket serverSocket, rmiService a) {
        this.serverData = serverData;
        this.serverSocket = serverSocket;
        this.a = a;
    }

    @Override
    public void run() {
        ArrayList<Thread> arrayList = new ArrayList<>(); //Lista de Threads
        //TODO::nonvo arraylist;
        ArrayList<Socket> cliArray = new ArrayList<>(); //Lista de socket de clientes
        boolean keepGoing = true;

        while (keepGoing) {

            Socket cliSocket = null;
            try {
                cliSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(cliSocket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            OutputStream outputStream = null;
            try {
                outputStream = cliSocket.getOutputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            InputStream inputStream = null;
            try{
                inputStream = cliSocket.getInputStream();
            }catch (IOException e){
                throw new RuntimeException(e);
            }

            ObjectOutputStream oout = null;
            try {
                oout = new ObjectOutputStream(cliSocket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            ServerRequest serverRequest;
            try {
                serverRequest = (ServerRequest) objectInputStream.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            switch (serverRequest.getMessages()) {
                case SEND_DATABASE -> {
                    System.out.println("Connected to Client -> " + cliSocket.getInetAddress().getHostAddress() + ":" + cliSocket.getPort());
                    SendDB sendFile = new SendDB(cliSocket, serverData, serverRequest, outputStream);
                    sendFile.setName("Send DB Thread " + arrayList.size());
                    sendFile.start();
                    arrayList.add(sendFile);
                }
                case CLIENT_REQUEST -> {
                    System.out.println("->"+serverRequest.getClientDetailed().getRequest());
                    switch (serverRequest.getClientDetailed().getRequest()) {
                        case "LOGIN", "SIGNUP" -> {
                            ClientTalk clientTalk = new ClientTalk(cliSocket, serverData, serverRequest, outputStream,inputStream,oout,objectInputStream,a);
                            if(serverRequest.getClientDetailed().getRequest().equals("LOGIN"))
                                a.notifyListeners(NotifyCode.LOGINCLIENT,"Login Client: " + serverRequest.getClientDetailed().getCLM().getUsername());
                            cliArray.add(cliSocket);
                            clientTalk.setName("Send CT Thread " + arrayList.size());
                            clientTalk.start();
                            arrayList.add(clientTalk);
                        }
                    }
                }
            }
        }

            for (Thread t : arrayList) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}

