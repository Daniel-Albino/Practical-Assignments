package pt.isec.tp_pd.server.messages;

import pt.isec.tp_pd.data.HeardbeatsMessages;
import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServerData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class SendHeardbeats {
    public SendHeardbeats() {}

    public synchronized void sendHeardbeats(ListServer listServer, HeardbeatsMessages myHeardbeatsMessages, ServerData serverData){
        listServer.addListServer(myHeardbeatsMessages);
        myHeardbeatsMessages.setVersionDB(serverData.getVersionDB());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            objectOutputStream.writeUnshared(myHeardbeatsMessages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] msg = byteArrayOutputStream.toByteArray();
        System.out.println("Send");
        DatagramPacket datagramPacketSend = new DatagramPacket(msg, msg.length,serverData.getIpMulticast(),serverData.getPortMulticast());
        try {
            serverData.getMulticastSocket().send(datagramPacketSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
