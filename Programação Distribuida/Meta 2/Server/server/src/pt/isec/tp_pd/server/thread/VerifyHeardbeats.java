
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











/*
package pt.isec.tp_pd.data;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//Thread que verifica de 15 em 15 segundos se um servidor mandou o seu último Heardbeat à 35 segundos ou mais
public class VerifyHeardbeats extends Thread{
    private List<HeardbeatsMessages> listServer;

    public VerifyHeardbeats(List<HeardbeatsMessages> listServer) {
        this.listServer = listServer;
    }

    @Override
    public void run() {
        while (true){
            try {
                sleep(15000 );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // listServer.heardbeatVerify();
            Calendar calendar = Calendar.getInstance();
            String stringTimeAux = String.format(
                    calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                            calendar.get(Calendar.MINUTE) + ":" +
                            calendar.get(Calendar.SECOND));

            int []auxTimeNow = convertStringToIntTime(stringTimeAux);

            List<Integer> indexToRemove = new ArrayList<>();

            for(int i = 0;i < listServer.size(); i++){
                String getTime = listServer.get(i).getDateHeardbeat();
                int[] auxTimeHeardbeat = convertStringToIntTime(getTime);
                if (removeHeardbeat(auxTimeNow, auxTimeHeardbeat)) {
                    indexToRemove.add(i);
                    System.out.println("AQUI:"+indexToRemove);
                }
                if (!listServer.get(i).isAvailable()) {
                    System.out.println("AQUI1:" + i);
                    indexToRemove.add(i);
                }
            }

            for(int i : indexToRemove){
                System.out.println("AQUI2:"+i);
                listServer.remove(i);
            }
            System.out.println("heardbeatVerify");
        }
    }

    private boolean removeHeardbeat(int[] auxTimeNow, int[] auxTimeHeardbeat) {

        int segNow = (auxTimeNow[0] * 60 * 60) + (auxTimeNow[1] * 60) + auxTimeNow[2];
        int segHeardbeat = (auxTimeHeardbeat[0] * 60 * 60) + (auxTimeHeardbeat[1] * 60) + auxTimeHeardbeat[2];

        int difTempo = 0;
        if(segNow > segHeardbeat)
            difTempo = segNow - segHeardbeat;
        if(segNow < segHeardbeat)
            difTempo = segHeardbeat - segNow;

        if(difTempo >= 35)
            return true;

        return false;
    }


    private int[] convertStringToIntTime(String stringTimeAux) {
        String[] s = stringTimeAux.split(":");
        int [] aux = new int[s.length];
        for(int i = 0;i<s.length;i++){
            aux[i] = Integer.parseInt(s[i]);
        }
        return aux;
    }
}
*/
