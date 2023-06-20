package pt.isec.tp_pd.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String username;
    private String nome;
    private String password;
    private Integer administrador;
    private Integer autenticado;


    public Integer getId() {
        return id;
    }
    public void setId(Integer ID) {
        this.id = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Integer administrador) {
        this.administrador = administrador;
    }

    public Integer getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(Integer autenticado) {
        this.autenticado = autenticado;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "ID=" + id +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", password='" + password + '\'' +
                ", administrador=" + administrador +
                ", autenticado=" + autenticado +
                '}';
    }
}
