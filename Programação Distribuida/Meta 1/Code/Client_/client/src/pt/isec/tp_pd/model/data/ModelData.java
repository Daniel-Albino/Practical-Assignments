package pt.isec.tp_pd.model.data;

import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.ServersToClient;
import pt.isec.tp_pd.ui.states.PagesEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelData {


    private String test;
    private String ip;
    private int port;

    private File file;

    private String optionShow, TFShow;

    private PagesEnum page;
    
    private int id;
    private String name, username, password;
    private String newName, newUsername, newPassword;

    private ServersToClient servers;

    private List<Espetaculo> espetaculos;

    private int indexEspectaculo;
    
    private Espetaculo selectedEspetaculo;

    private String fila,lugar;

    public ModelData(){
        this.test = "";
        this.espetaculos = new ArrayList<>();
        this.indexEspectaculo = 0;
    }

    public Espetaculo getSelectedEspetaculo() {
        return selectedEspetaculo;
    }

    public void setSelectedEspetaculo(Espetaculo selectedEspetaculo) {
        this.selectedEspetaculo = selectedEspetaculo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getOptionShow() {
        return optionShow;
    }

    public void setOptionShow(String optionShow) {
        this.optionShow = optionShow;
    }

    public String getTFShow() {
        return TFShow;
    }

    public void setTFShow(String TFShow) {
        this.TFShow = TFShow;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public PagesEnum getPage() {
        return page;
    }

    public void setPage(PagesEnum page) {
        this.page = page;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getIndexEspectaculo() {
        return indexEspectaculo;
    }

    public void setIndexEspectaculo(int indexEspectaculo) {
        this.indexEspectaculo = this.indexEspectaculo+indexEspectaculo;
    }

    public List<Espetaculo> getEspetaculos() {
        return espetaculos;
    }

    public void setEspetaculos(List<Espetaculo> espetaculos) {
        this.espetaculos = espetaculos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ServersToClient getServers() {
        return servers;
    }

    public void setServers(ServersToClient servers) {
        this.servers = servers;
    }



    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
