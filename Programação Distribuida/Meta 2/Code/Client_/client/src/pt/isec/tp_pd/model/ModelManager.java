package pt.isec.tp_pd.model;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.model.fsm.TicketPdContext;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.ui.states.PagesEnum;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

public class ModelManager {

    TicketPdContext context;
    PropertyChangeSupport pcs;


    public ModelManager() {
        this.context = new TicketPdContext();
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {//String Property
        pcs.addPropertyChangeListener(listener);
    }

    public TicketPdState getState() {
        return context.getState();
    }
    
    public void setSelectedEspetaculo(Espetaculo espetaculo){ context.getData().setSelectedEspetaculo(espetaculo);}

    public Espetaculo getSeletedEspetaculo(){return context.getData().getSelectedEspetaculo();}

    public PagesEnum getPageState(){ return context.getData().getPage();}

    public void setPageState(PagesEnum pageState){context.getData().setPage(pageState);}

    public String getAttributes(){
        return context.getData().getIp();
        //System.out.println(context.getData().getIp());
        //return null;
    }
    
    public void setIdespe(int id){context.getData().setIndexEspectaculo(id);}

    public int getIdespe(){return context.getData().getIndexEspectaculo();}
    
    
    
    public void setId(int id){context.getData().setId(id);}
    
    public int getId(){return context.getData().getId();}
    public void setFile(File file){context.getData().setFile(file);}

    public File getFile(){return context.getData().getFile();}

    public void setOptionShow(String value){context.getData().setOptionShow(value);}
    public void setfilter(String filter){context.getData().setTFShow(filter);}

    public String getfilter(){return context.getData().getTFShow();}
    public String getOptionShow(){return context.getData().getOptionShow();}

    public void setNewName(String name){context.getData().setNewName(name);}

    public String getNewName(){return context.getData().getNewName();}
    public String getNewUsername(){return context.getData().getNewUsername();}
    public String getNewPassword(){return context.getData().getNewPassword();}

    public void setNewUsername(String name){context.getData().setNewUsername(name);}
    public void setNewPassword(String password){context.getData().setNewPassword(password);}

    public String getlugar(){return context.getData().getLugar();}

    public String getfila(){return context.getData().getFila();}
    public void setlugar(String lugar){
        context.getData().setLugar(lugar);
    }

    public void setfila(String fila){
        context.getData().setFila(fila);
    }

    public String getIp(){
        return context.getData().getIp();
    }

    public int getPort(){
        return context.getData().getPort();
    }


    public void setServers(ServersToClient servers){
        context.setServers(servers);
    }
    public ServersToClient getServers(){
        return context.getData().getServers();
    }

    public void setName(String name){context.getData().setName(name);}
    public void setUsername(String username){context.getData().setUsername(username);}
    public void setPassword(String password){context.getData().setPassword(password);}
    public String getName(){return context.getData().getName();}

    public String getUsername(){return context.getData().getUsername();}

    public String getPassword(){return context.getData().getPassword();}

    public void setAttributes(String ip, String port){
        context.setAttributes(ip, port);
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void setLoginAttributes(String user, String pass){
        context.setLoginAttributes(user, pass);
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void setSignUpAttributes(String name, String user, String pass){
        context.setSignUpAttributes(name, user, pass);
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void setEspetaculos(List<Espetaculo> espetaculos){
        context.setEspetaculos(espetaculos);
        pcs.firePropertyChange(null,null,context.getState());
    }

    public List<Espetaculo> getEspetaculos(){
        return context.getData().getEspetaculos();
    }

    public void ConnectionPage(){
        context.ConnectionPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void InitialPage(){
        context.InitialPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void SignupPage(){
        context.SignupPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void LoginPage(){
        context.LoginPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void OrdersPage(){
        context.OrdersPage();
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void AdminPage(){
        context.AdminPage();
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void EditPage(){
        context.EditPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void SearchPage(){
        context.SearchPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void ReservePage(){
        context.ReservePage();
        pcs.firePropertyChange(null,null,context.getState());
    }

}
