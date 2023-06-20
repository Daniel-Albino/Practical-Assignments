package pt.isec.tp_pd.data;

public class Utilizador {
    private int ID;
    private String username;
    private String nome;
    private String password;
    private int administrador;
    private int autenticado;

    public Utilizador(String username, String nome, String password, int administrador, int autenticado) {
        this.username = username;
        this.nome = nome;
        this.password = password;
        this.administrador = administrador;
        this.autenticado = autenticado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getAdministrador() {
        return administrador;
    }

    public void setAdministrador(int administrador) {
        this.administrador = administrador;
    }

    public int getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(int autenticado) {
        this.autenticado = autenticado;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", password='" + password + '\'' +
                ", administrador=" + administrador +
                ", autenticado=" + autenticado +
                '}';
    }
}
