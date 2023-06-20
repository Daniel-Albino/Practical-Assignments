package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.HeardbeatsMessages;
import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.data.ServerRequest;
import pt.isec.tp_pd.data.Messages;

import java.io.*;
import java.net.Socket;
import java.util.List;

//Thread que pede a base de dados a outro servidor
public class RequestDB extends Thread{
    private ServerData serverData;
    private static final int MAX_DATA = 4000;
    private List<HeardbeatsMessages> list;
    private String FILEMANE = "PD-2022-23-TP.db";

    public RequestDB(ServerData serverData,List<HeardbeatsMessages> list) {
        this.serverData = serverData;
        this.list = list;
    }

    @Override
    public void run() {
        int tentativas = 0;
        Socket cliSocket = null;
        do {
            try {
                cliSocket = new Socket(list.get(tentativas).getIpServer(),list.get(tentativas).getPortTCP());
                break;
            } catch (IOException e) {
                System.out.println("Erro no Socket");
            }
            tentativas++;
        }while (tentativas < list.size());

        if(tentativas >= list.size())
            return;

        System.out.println("Connected to Server -> " +
                cliSocket.getInetAddress().getHostAddress() +
                ":" + cliSocket.getPort());

        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(cliSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStream inputStream = null;
        try {
            inputStream = cliSocket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        ServerRequest serverRequest = new ServerRequest(Messages.SEND_DATABASE);
        try {
            objectOutputStream.writeObject(serverRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(serverData.getDbDirectory() + FILEMANE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int nBytes;
        int i = 1;
        do{
            byte[] msgBuffer = new byte[MAX_DATA];
            try {
                nBytes = inputStream.read(msgBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(nBytes > 0){
                try {
                    fileOutputStream.write(msgBuffer,0,nBytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Bytes read " + i +" :" + nBytes);
            i++;
        }while(nBytes > 0);
        try {
            cliSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
