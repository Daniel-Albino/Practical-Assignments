package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.ListServer;
import pt.isec.tp_pd.data.ServerData;

//Thread que verifica de 15 em 15 segundos se um servidor mandou o seu último Heardbeat à 35 segundos ou mais
public class VerifyHeardbeats extends Thread{
    private ListServer listServer;
    private ServerData serverData;

    public VerifyHeardbeats(ListServer listServer,ServerData serverData) {
        this.listServer = listServer;
        this.serverData = serverData;
    }

    @Override
    public void run() {
        while (true){
            try {
                sleep(15000 );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            listServer.heardbeatVerify(serverData);
            System.out.println("heardbeatVerify");
        }
    }
}
