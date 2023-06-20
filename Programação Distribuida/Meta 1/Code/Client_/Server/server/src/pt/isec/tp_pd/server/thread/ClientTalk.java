package pt.isec.tp_pd.server.thread;

import pt.isec.tp_pd.data.*;

import javax.crypto.spec.PSource;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class ClientTalk extends Thread {

    private static final String DIRECTORY = "./espetaculos";

    private static final int MAX_DATA = 4000;
    private Socket cliSocket;

    private ServerData serverData;
    ServerRequest serverRequest;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    OutputStream outputStream;
    InputStream inputStream;

    public ClientTalk(Socket cliSocket, ServerData serverData, ServerRequest serverRequest,
                      OutputStream outputStream, InputStream inputStream,
                      ObjectOutputStream objOutputStream,ObjectInputStream objInputStream) {
        this.cliSocket = cliSocket;
        this.serverData = serverData;
        this.serverRequest = serverRequest;
        this.objectOutputStream = objOutputStream;
        this.objectInputStream = objInputStream;
        this.outputStream = outputStream;
        this.inputStream = inputStream;

    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    @Override
    public void run() {
        boolean first = true;

        boolean pContinue = true;
        while (pContinue) {

            if(!first) {
                System.out.println("Aqui!First1");
                try {
                    System.out.println("Aqui!First2");
                    serverRequest = (ServerRequest) objectInputStream.readObject();
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                }
                first = false;
            }
            //System.out.println(" "+serverRequest.getClientDetailed().getRequest());
            switch (serverRequest.getClientDetailed().getRequest()) {
                case "LOGIN" -> {
                    ServerRequest request1 = new ServerRequest(Messages.CLIENT_REQUEST);

                    if (!serverData.verifyUser(serverRequest.getClientDetailed().getCLM())) {
                        RequestClientDetailed _newreqdet = new RequestClientDetailed("DENIED");
                        request1.setClientDetailed(_newreqdet);
                        try {
                            objectOutputStream.writeUnshared(request1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        if(serverRequest.getClientDetailed().getCLM().getUsername().equals("admin")){
                            RequestClientDetailed _newreqdet = new RequestClientDetailed("RESPONSE_LOGIN_ADMIN");
                            _newreqdet.setEspetaculos(serverData.selectEspetaculo(SelectEspetaculo.ALL,null));
                            request1.setClientDetailed(_newreqdet);
                            try {
                                objectOutputStream.writeUnshared(request1);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            List<Espetaculo> espetaculos = serverData.selectEspetaculo(SelectEspetaculo.ALL,null);
                            List<Lugar> lugars = new ArrayList<>();
                            for (Espetaculo e : espetaculos){
                                lugars = serverData.selectLugarIdEspetaculo(e.getID());
                                for(Lugar l : lugars){
                                    ReserveSeat reserveSeat = null;
                                    reserveSeat = serverData.listReservationsSeats("id_seat",l.getID());
                                    if(reserveSeat != null && reserveSeat.getId_seat() != -1)
                                        l.setOcupado(true);
                                }
                                e.addLugares(lugars);
                                }
                                RequestClientDetailed _newreqdet = new RequestClientDetailed("RESPONSE_LOGIN");
                                _newreqdet.setEspetaculos(espetaculos);
                                _newreqdet.setUsername(serverRequest.getClientDetailed().getCLM().getUsername());
                                List<Utilizador> users = serverData.selectUtilizador(SelectUtilizador.USERNAME, serverRequest.getClientDetailed().getCLM().getUsername());
                                _newreqdet.setName(users.get(0).getNome());
                                _newreqdet.setUsername(users.get(0).getUsername());
                                _newreqdet.setPassword(users.get(0).getPassword());
                                _newreqdet.setId(users.get(0).getID());
                                request1.setClientDetailed(_newreqdet);
                                try {
                                    objectOutputStream.writeUnshared(request1);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    }

                    first = false;
                }

                case "SIGNUP" -> {
                    ServerRequest request1 = new ServerRequest(Messages.CLIENT_REQUEST);
                    Utilizador newUser = new Utilizador(serverRequest.getClientDetailed().getCSM().getUsername(),
                            serverRequest.getClientDetailed().getCSM().getName(),
                            serverRequest.getClientDetailed().getCSM().getPassword(), 0, 1);
                    System.out.println("name:" + newUser.getNome() + " username:" + newUser.getUsername() + " password:" + newUser.getPassword());
                    if (!serverData.insertUtilizador(newUser)) {
                        RequestClientDetailed _newreqdet = new RequestClientDetailed("USERNOTCREATED");
                        request1.setClientDetailed(_newreqdet);
                        try {
                            objectOutputStream.writeUnshared(request1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        RequestClientDetailed _newreqdet = new RequestClientDetailed("USERCREATED");
                        request1.setClientDetailed(_newreqdet);
                        try {
                            objectOutputStream.writeUnshared(request1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    first = false;

                }
                case "RESERVE" -> {
                    System.out.println("Fila:"+serverRequest.getClientDetailed().getFila()+"Assento:"+serverRequest.getClientDetailed().getAssento());

                    List<Lugar> lugars = serverData.selectLugarIdLugar(serverRequest.getClientDetailed().getId());
                    Calendar calendar = Calendar.getInstance();
                    String s = calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) +"/"+ calendar.get(Calendar.YEAR) + " - " +
                            calendar.get(Calendar.HOUR_OF_DAY) + ":" +  calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

                    List<Utilizador> utilizador = serverData.selectUtilizador(SelectUtilizador.USERNAME,serverRequest.getClientDetailed().getUsername());

                    serverData.insertReservation(new Reserve(-1,s,1,utilizador.get(0).getID(),lugars.get(0).getIdEspetaculo()));

                    int reserveid = serverData.maxReservation();
                    ReserveSeat reserveSeat1 = new ReserveSeat(reserveid,lugars.get(0).getID());
                    serverData.insertReservationsSeats(reserveSeat1);

                    ServerRequest request1 = new ServerRequest(Messages.CLIENT_REQUEST);
                    List<Espetaculo> espetaculos = serverData.selectEspetaculo(SelectEspetaculo.ALL,null);
                    List<Lugar> lugarws = new ArrayList<>();
                    for (Espetaculo e : espetaculos){
                        lugarws = serverData.selectLugarIdEspetaculo(e.getID());
                        for(Lugar l : lugarws){
                            ReserveSeat reserveSeat = null;
                            reserveSeat = serverData.listReservationsSeats("id_seat",l.getID());
                            if(reserveSeat != null && reserveSeat.getId_seat() != -1)
                                l.setOcupado(true);
                        }
                        e.addLugares(lugarws);
                    }
                    RequestClientDetailed _newreqdet = new RequestClientDetailed("ACCEPTRESERVE");
                    _newreqdet.setEspetaculos(espetaculos);
                    request1.setClientDetailed(_newreqdet);
                    try {
                        objectOutputStream.writeUnshared(request1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "USERCHANGE" -> {
                    List<Utilizador> aux = serverData.selectUtilizador(SelectUtilizador.ID,String.valueOf(serverRequest.getClientDetailed().getId()));
                    System.out.println("id"+serverRequest.getClientDetailed().getId()+"nome"+serverRequest.getClientDetailed().getUsername()+"Novo Nome:"+serverRequest.getClientDetailed().getNewName()+" Novo Username:"+serverRequest.getClientDetailed().getNewUserName()+" Nova password:"+serverRequest.getClientDetailed().getNewPassword());
                }
                case "SEARCH" -> {
                    List<Espetaculo> espetaculos = null;
                    System.out.println("Option"+serverRequest.getClientDetailed().getShowOption()+" Filter:"+serverRequest.getClientDetailed().getTfshow());
                    if(serverRequest.getClientDetailed().getShowOption().equals("Nome")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.DESCRIPTION, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("Tipo")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.TYPE, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("Data(DD/MM/YYYY)")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.DATA, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("Duração(min)")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.DURATION, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("Local")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.LOCAL, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("Localidade")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.LOCATION, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("País")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.COUNTRY, serverRequest.getClientDetailed().getTfshow());
                    }else if(serverRequest.getClientDetailed().getShowOption().equals("Classificação etária")){
                        espetaculos = serverData.selectEspetaculo(SelectEspetaculo.ETARY_CLASSIFICATION, serverRequest.getClientDetailed().getTfshow());
                    }

                    ServerRequest request1 = new ServerRequest(Messages.CLIENT_REQUEST);
                    if(espetaculos == null || espetaculos.size() < 1){
                        RequestClientDetailed _newreqdet = new RequestClientDetailed("DENIED");
                        request1.setClientDetailed(_newreqdet);
                    }else{
                        for (Espetaculo a : espetaculos)
                            System.out.println(a.toString());
                        RequestClientDetailed _newreqdet = new RequestClientDetailed("ACCEPTSEARCH");
                        _newreqdet.setEspetaculos(espetaculos);
                        request1.setClientDetailed(_newreqdet);
                    }


                    try {
                        objectOutputStream.writeUnshared(request1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "UPLOADFILE" -> {
                    serverData.insertEspetaculo(serverRequest.getClientDetailed().getEspetaculo());
                }
                case "ELIMINAESPE" -> {
                    /*System.out.println(serverRequest.getClientDetailed().getEspetaculo().toString());*/
                    int id = serverRequest.getClientDetailed().getEspetaculo().getID();
                    serverData.deleteEspetaculo(id);
                    serverData.deleteLugarIdEspetaculo(id);
                }
                    //System.out.println("FILE "+serverRequest.getClientDetailed().getFile().getName());
            }
        }
    }
}



