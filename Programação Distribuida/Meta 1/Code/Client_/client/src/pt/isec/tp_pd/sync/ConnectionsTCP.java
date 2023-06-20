package pt.isec.tp_pd.sync;

import javafx.application.Platform;
import pt.isec.tp_pd.data.*;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.ui.states.PagesEnum;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.FutureTask;

public class ConnectionsTCP extends Thread {
    public static final int MAX_SIZE = 4096;

    private static final int MAX_DATA = 4000;

    public static final int TIMEOUT = 10; //segundos

    private ModelManager model;

    String request=null;

    int serverPort;
    Socket socketToServer = null;
    ObjectOutputStream oout;
    ObjectInputStream oin;


    PagesEnum page;
    PagesEnum oldPage;

    String requestType;

    ServerRequest objE;

    String stringResponse;

    List<Espetaculo> espetaculos;


    //TODO::INICIO
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Socket getSocketToServer() {
        return socketToServer;
    }

    public void setSocketToServer(Socket socketToServer) {
        this.socketToServer = socketToServer;
    }

    public ObjectOutputStream getOout() {
        return oout;
    }

    public void setOout(ObjectOutputStream oout) {
        this.oout = oout;
    }

    public ObjectInputStream getOin() {
        return oin;
    }

    public void setOin(ObjectInputStream oin) {
        this.oin = oin;
    }
    //TODO::FIM

    public ConnectionsTCP(ModelManager model) {
        this.model = model;
    }

    @Override
    public void run() {


        boolean pcontinue = true;

                serverPort = model.getServers().getListServers().get(0).getPortTCP();


                try {
                    socketToServer = new Socket(model.getServers().getListServers().get(0).getIpServer(), serverPort);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                try {
                    socketToServer.setSoTimeout(TIMEOUT * 1000);
                } catch (SocketException e) {
                    throw new RuntimeException(e);
                }

                try {
                    oout = new ObjectOutputStream(socketToServer.getOutputStream());
                    oin = new ObjectInputStream(socketToServer.getInputStream());

                } catch (Exception e) {
                }


                //TODO::APAGAR
                /*
                OutputStream outputStream = null;
                try {
                    outputStream = socketToServer.getOutputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                InputStream inputStream = null;
                try {
                    inputStream = socketToServer.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
                //TODO::APAGAR




        while(pcontinue) {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                    if(model.getPageState() == PagesEnum.ELIMINAESPE){
                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        RequestClientDetailed _newReq = new RequestClientDetailed("ELIMINAESPE");

                        System.out.println(model.getSeletedEspetaculo());
                        _newReq.setEspetaculo(model.getSeletedEspetaculo());
                        _new.setClientDetailed(_newReq);
                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if(model.getPageState() == PagesEnum.UPLOADFILE){
                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        RequestClientDetailed _newReq = new RequestClientDetailed("UPLOADFILE");
                        LerFicheiroEspetaculo a = new LerFicheiroEspetaculo(model.getFile().getAbsolutePath());
                        Espetaculo aux = a.lerEspetaculo();
                        _newReq.setEspetaculo(aux);
                        _new.setClientDetailed(_newReq);
                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    if(model.getPageState() == PagesEnum.SEARCHSH){
                        model.setPageState(PagesEnum.NONE);
                        System.out.println("entrei");
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        RequestClientDetailed _newReq = new RequestClientDetailed("SEARCH");
                        _newReq.setShowOption(model.getOptionShow());
                        _newReq.setTfshow(model.getfilter());
                        _new.setClientDetailed(_newReq);

                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        ServerRequest _new1;
                        try {
                            _new1 = (ServerRequest) oin.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        switch (_new1.getMessages()) {
                            case CLIENT_REQUEST -> {
                                switch (_new1.getClientDetailed().getRequest()) {
                                    case "DENIED" -> {
                                        FutureTask futureTask = new FutureTask(new Alerta("Aviso Pesquisa!", "sem resultados."));
                                        Platform.runLater(futureTask);
                                        try {
                                            futureTask.get();
                                        } catch (Exception ex) {
                                            throw new RuntimeException(ex);
                                        }

                                    }
                                    case "ACCEPTSEARCH" -> {
                                        model.setEspetaculos(_new1.getClientDetailed().getEspetaculos());
                                        model.OrdersPage();
                                    }

                                }

                            }
                        }

                    }


                    if(model.getPageState() == PagesEnum.USERCHANGE){
                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        RequestClientDetailed _newReq = new RequestClientDetailed("USERCHANGE");
                        _newReq.setNewName(model.getNewName());
                        _newReq.setId(model.getId());
                        _newReq.setNewUserName(model.getNewUsername());
                        _newReq.setNewPassword(model.getNewPassword());
                        _newReq.setUsername(model.getUsername());
                        _new.setClientDetailed(_newReq);

                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }


                    if (model.getPageState() == PagesEnum.RESERVE){

                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        RequestClientDetailed _newReq = new RequestClientDetailed("RESERVE");
                        _newReq.setAssento(model.getlugar());
                        _newReq.setFila(model.getfila());
                        _newReq.setUsername(model.getUsername());
                        List<Espetaculo> aux = model.getEspetaculos();
                        for(Lugar l : aux.get(model.getIdespe()).getLugaresList()){
                            if(l.getAssento().equals(model.getlugar()) && l.getFila().equals(model.getfila())){
                                _newReq.setId(l.getID());
                            }
                        }
                        _new.setClientDetailed(_newReq);


                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                        ServerRequest _new1;
                        try {
                            _new1 = (ServerRequest) oin.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        switch (_new1.getMessages()) {
                            case CLIENT_REQUEST -> {
                                switch (_new1.getClientDetailed().getRequest()) {
                                    case "DENIED" -> {
                                        FutureTask futureTask = new FutureTask(new Alerta("Aviso Reserva!", "Lugar indisponível."));
                                        Platform.runLater(futureTask);
                                        try {
                                            futureTask.get();
                                        } catch (Exception ex) {
                                            throw new RuntimeException(ex);
                                        }

                                    }
                                    case "ACCEPTRESERVE" -> {
                                        model.setEspetaculos(_new1.getClientDetailed().getEspetaculos());
                                        for (Espetaculo a : model.getEspetaculos())
                                            System.out.println(a);
                                        model.OrdersPage();
                                    }

                                }

                            }
                        }

                    }

                    if(model.getPageState() == PagesEnum.REQUESTADMIN){
                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        CredentialsLoginMessage aux = new CredentialsLoginMessage(model.getUsername(), model.getPassword());
                        RequestClientDetailed _newReq = new RequestClientDetailed("LOGIN");
                        _newReq.setCLM(aux);
                        _new.setClientDetailed(_newReq);

                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        ServerRequest _new1;
                        try {
                            _new1 = (ServerRequest) oin.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        switch (_new1.getMessages()) {
                            case CLIENT_REQUEST -> {
                                switch (_new1.getClientDetailed().getRequest()) {

                                    case "RESPONSE_LOGIN_ADMIN" -> {
                                        model.setEspetaculos(_new1.getClientDetailed().getEspetaculos());
                                        model.AdminPage();
                                    }
                                }
                            }
                        }
                    }

                    if (model.getPageState() == PagesEnum.LOGIN) {
                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);

                        CredentialsLoginMessage aux = new CredentialsLoginMessage(model.getUsername(), model.getPassword());
                        RequestClientDetailed _newReq = new RequestClientDetailed("LOGIN");
                        _newReq.setCLM(aux);
                        _new.setClientDetailed(_newReq);

                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        ServerRequest _new1;
                        try {
                            _new1 = (ServerRequest) oin.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        switch (_new1.getMessages()) {
                            case CLIENT_REQUEST -> {
                                switch (_new1.getClientDetailed().getRequest()) {
                                    case "DENIED" -> {
                                        FutureTask futureTask = new FutureTask(new Alerta("LOGIN INVÁLIDO!", "Utilizador não registado."));
                                        Platform.runLater(futureTask);
                                        try {
                                            futureTask.get();
                                        } catch (Exception ex) {
                                            throw new RuntimeException(ex);
                                        }

                                    }
                                    case "RESPONSE_LOGIN" -> {
                                        System.out.println("received an espetaculo");
                                        model.setName(_new1.getClientDetailed().getName());
                                        model.setUsername(_new1.getClientDetailed().getUsername());
                                        model.setPassword(_new1.getClientDetailed().getPassword());
                                        model.setId(_new1.getClientDetailed().getId());

                                        System.out.println(_new1.getClientDetailed().getName());
                                        System.out.println(_new1.getClientDetailed().getUsername());
                                        System.out.println(_new1.getClientDetailed().getPassword());
                                        model.setEspetaculos(_new1.getClientDetailed().getEspetaculos());
                                        for (Espetaculo a : model.getEspetaculos())
                                            System.out.println(a);
                                        model.OrdersPage();
                                    }


                                }

                            }
                        }
                    }


                    if (model.getPageState() == PagesEnum.SIGNUP) {
                        model.setPageState(PagesEnum.NONE);
                        ServerRequest _new = new ServerRequest(Messages.CLIENT_REQUEST);
                        CredentialsSignupMessage aux = new CredentialsSignupMessage(model.getName(), model.getUsername(), model.getPassword());
                        RequestClientDetailed _newReq = new RequestClientDetailed("SIGNUP");
                        _newReq.setCSM(aux);
                        _new.setClientDetailed(_newReq);
                        try {
                            oout.writeUnshared(_new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        ServerRequest _new1;
                        try {
                            _new1 = (ServerRequest) oin.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        switch (_new1.getMessages()) {
                            case CLIENT_REQUEST -> {
                                switch (_new1.getClientDetailed().getRequest()) {
                                    case "USERNOTCREATED" -> {
                                        FutureTask futureTask = new FutureTask(new Alerta("REGISTO INVÁLIDO!", "Utilizador não foi registado."));
                                        Platform.runLater(futureTask);
                                        try {
                                            futureTask.get();
                                        } catch (Exception ex) {
                                            throw new RuntimeException(ex);
                                        }

                                    }
                                    case "USERCREATED" -> {
                                        System.out.println("received an espetaculo");
                                        model.setEspetaculos(_new1.getClientDetailed().getEspetaculos());
                                        for (Espetaculo a : model.getEspetaculos())
                                            System.out.println(a);
                                        model.OrdersPage();
                                    }

                                }

                            }
                        }
                    }
        }
    }
}


