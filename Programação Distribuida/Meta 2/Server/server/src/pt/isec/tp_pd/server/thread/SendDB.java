package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.ServerData;
import pt.isec.tp_pd.data.ServerRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

//Thread que envia a sua base de dados
public class SendDB extends Thread{
    private static String DIRECTORY;
    private static final int MAX_DATA = 4000;
    private Socket cliSocket;
    ServerRequest serverRequest; OutputStream outputStream;

    public SendDB(Socket cliSocket, ServerData serverData, ServerRequest serverRequest, OutputStream outputStream) {
        this.cliSocket = cliSocket;
        DIRECTORY = serverData.getDbDirectory() + "PD-2022-23-TP.db";
        this.serverRequest = serverRequest;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {

        byte[] chunk = new byte[MAX_DATA];
        int bytes = 0;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(DIRECTORY);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        do {
            try {
                bytes = fileInputStream.read(chunk);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (bytes > -1) {
                try {
                    outputStream.write(chunk, 0, bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    outputStream.write(new byte[0], 0, 0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } while (bytes > -1);
        try {
            cliSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}