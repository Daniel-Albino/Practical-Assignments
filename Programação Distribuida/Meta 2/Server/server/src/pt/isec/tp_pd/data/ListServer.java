package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//Lista de servidores
public class ListServer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    List<HeardbeatsMessages> listServer;
    public ListServer() {
        this.listServer = new ArrayList();
    }

    public synchronized void addListServer(HeardbeatsMessages serverInfo){
        boolean newDate = false;
        int index = 0;
        for(HeardbeatsMessages h : listServer){
            if(h.getNameServer().equals(serverInfo.getNameServer())) {
                newDate = true;
                break;
            }
            index++;
        }
        if(newDate)
            listServer.get(index).setDateHeardbeat(serverInfo.getDateHeardbeat());
        else
            listServer.add(serverInfo);
    }

    public synchronized List<HeardbeatsMessages> getListServer() {
        return listServer;
    }

    public synchronized void heardbeatVerify(ServerData serverData){
        Calendar calendar = Calendar.getInstance();
        String stringTimeAux = String.format(
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":" +
                        calendar.get(Calendar.SECOND));

        int []auxTimeNow = convertStringToIntTime(stringTimeAux);

        List<Integer> indexToRemove = new ArrayList<>();

        for(int i = 0;i < listServer.size(); i++){
            if(!listServer.get(i).getNameServer().equals(serverData.getServerName())) {
                String getTime = listServer.get(i).getDateHeardbeat();
                int[] auxTimeHeardbeat = convertStringToIntTime(getTime);
                if (removeHeardbeat(auxTimeNow, auxTimeHeardbeat)) {
                    indexToRemove.add(i);
                }
                if (!listServer.get(i).isAvailable())
                    indexToRemove.add(i);
            }
        }

        for(int i : indexToRemove){
            listServer.remove(i);
        }
    }

    private synchronized boolean removeHeardbeat(int[] auxTimeNow, int[] auxTimeHeardbeat) {

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

    @Override
    public String toString() {
        return "ListServer{" + listServer + '}';
    }

    public int getSize() {
        return listServer.size();
    }



}
