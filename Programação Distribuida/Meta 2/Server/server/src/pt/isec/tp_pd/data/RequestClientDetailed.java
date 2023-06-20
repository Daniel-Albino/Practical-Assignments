package pt.isec.tp_pd.data;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequestClientDetailed implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String request;

    private String showOption, tfshow;

    private Espetaculo espetaculo;
    private File file;
    private int id;
    private String name, username, password;
    private String newName, newUserName, newPassword;
    private List<Espetaculo> espetaculos;
    private CredentialsLoginMessage CLM;
    private CredentialsSignupMessage CSM;

    private String fila,assento;

    public RequestClientDetailed(String request) {
        this.request = request;
        this.espetaculos = new ArrayList();
    }



    public Espetaculo getEspetaculo() {
        return espetaculo;
    }

    public void setEspetaculo(Espetaculo espetaculo) {
        this.espetaculo = espetaculo;
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

    public String getShowOption() {
        return showOption;
    }

    public void setShowOption(String showOption) {
        this.showOption = showOption;
    }

    public String getTfshow() {
        return tfshow;
    }

    public void setTfshow(String tfshow) {
        this.tfshow = tfshow;
    }


    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getAssento() {
        return assento;
    }

    public void setAssento(String assento) {
        this.assento = assento;
    }


    public CredentialsLoginMessage getCLM() {
        return CLM;
    }

    public void setCLM(CredentialsLoginMessage CLM) {
        this.CLM = CLM;
    }

    public CredentialsSignupMessage getCSM() {
        return CSM;
    }

    public void setCSM(CredentialsSignupMessage CSM) {
        this.CSM = CSM;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public List<Espetaculo> getEspetaculos() {
        return espetaculos;
    }

    public void setEspetaculos(List<Espetaculo> espetaculos) {
        this.espetaculos = espetaculos;
    }

    @Override
    public String toString() {
        return "RequestClientDetailed{" +
                "request='" + request + '\'' +
                ", espetaculos=" + espetaculos +
                '}';
    }
}
