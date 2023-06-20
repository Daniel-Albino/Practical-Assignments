package pt.isec.tp_pd.sync;

import javafx.application.Platform;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.ui.states.PagesEnum;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.FutureTask;


public class SendSignalUDP extends Thread{

    public static final int MAX_SIZE = 4096; //256
    public static final String REQUEST = "CONNECT";
    public static final int TIMEOUT = 7; //segundos
    private ModelManager model;
    InetAddress serverAddr = null;
    int serverPort = 0;


    ByteArrayOutputStream bout;
    ObjectOutputStream oout;

    ByteArrayInputStream bin;
    ObjectInputStream oin;

    DatagramSocket socket = null;
    DatagramPacket packet = null;
    ServersToClient response;

    PagesEnum page;

    public SendSignalUDP(ModelManager model, PagesEnum page) {
        this.model = model;
        this.page = page;
    }

    @Override
    public void run()
    {
        try {
            serverAddr = InetAddress.getByName(model.getIp());
            serverPort = model.getPort();

            System.out.println("ip:"+model.getIp()+"port:"+model.getPort());


            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT * 1000);

            System.out.println(model.getIp() + ":" + model.getPort());

            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);

            String t = REQUEST;

            oout.writeObject(t);
            byte[] msg = bout.toByteArray();
            //oout.flush();


            DatagramPacket datagramPacketSend = new DatagramPacket(msg, msg.length, serverAddr, serverPort);
            socket.send(datagramPacketSend);

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);



            bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            oin = new ObjectInputStream(bin);

            response = (ServersToClient) oin.readObject();


            System.out.println("Servidor: " + response.toString());


            model.setServers(response);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(page == PagesEnum.SIGNUP)
                model.SignupPage();
            else if (page == PagesEnum.LOGIN) {
                model.LoginPage();
            }

        }catch(SocketTimeoutException e){
            FutureTask futureTask = new FutureTask(new Alerta("TIMEOUT!","Verifique se o servidor que indicou está operacional"));
            Platform.runLater(futureTask);
            try {
                futureTask.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            model.InitialPage();
        }catch(Exception e){
            FutureTask futureTask = new FutureTask(new Alerta("Alguma coisa correu mal!","Erro: " + e));
            Platform.runLater(futureTask);
            try {
                futureTask.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            model.InitialPage();
        }finally{
            if(socket != null){
                socket.close();
            }
        }
    }
}
