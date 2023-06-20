package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;

public class CredentialsLoginMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;


    public CredentialsLoginMessage(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "CredentialsLoginMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
