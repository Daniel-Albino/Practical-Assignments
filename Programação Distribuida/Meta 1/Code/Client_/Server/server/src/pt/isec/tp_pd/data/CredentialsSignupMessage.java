package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;

public class CredentialsSignupMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String username;
    private String password;

    public CredentialsSignupMessage(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "CredentialsSignupMessage{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
