package pt.isec.tp_pd.server.database;

import pt.isec.tp_pd.data.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Classe que realiza a interação com a base de dados
public class DBConnection {
    private final String DATABASE_URL; /*= "jdbc:sqlite:./DBServer1/PD-2022-23-TP.db"*/
    private final String DBDIRECTORY;
    private String FILENAME;
    private Connection dbConn;
    private boolean createDB;
    private boolean connected;

    public DBConnection(String dbDirectory,String serverName) {
        DBDIRECTORY = dbDirectory+serverName+"/";
        FILENAME = DBDIRECTORY+"PD-2022-23-TP.db";
        this.DATABASE_URL = "jdbc:sqlite:"+FILENAME;
        createDB = false;
        connected = false;
    }

    public String getDBDIRECTORY() {
        return DBDIRECTORY;
    }

    public void setCreateDB() {
        if(createDB)
            createDB = false;
        else
            createDB = true;
    }

    public boolean dbConnection(){

        File diretoriaDB = new File(DBDIRECTORY);
        if(!diretoriaDB.exists())
            diretoriaDB.mkdirs();

        File dbFile = new File(FILENAME);
        if(!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Impossível criar a base de dados!");
                createDB = false;
            }
        }else{
            setCreateDB();
        }

        try {
            dbConn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Ligação feita");
            connected = true;
        } catch (SQLException e) {
            System.out.println("Impossível ligar à base de dados! ");
            return false;
        }
        if(createDB) {
            try {
                createTables();
            } catch (SQLException e) {
                System.out.println("Impossivel criar as tabelas da base de dados!");
            }
        }
        return true;
    }

    public boolean isConnected() {
        return connected;
    }

    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
            } catch (SQLException e) {
                System.out.println("Impossível fechar a ligação à base de dados!");
            }
        }
    }
    public void createVersionTable() throws SQLException {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }

        String sqlQuery = "CREATE TABLE version('Version' INTEGER NOT NULL)";

        if (statement != null) {
            statement.execute(sqlQuery);
        }
        assert statement != null;
        statement.close();
    }
    public void insertFirstRegisterTableVersion() throws SQLException {
        Statement statement = dbConn.createStatement();

        String sqlQuery = "INSERT INTO version VALUES(1)";
        statement.executeUpdate(sqlQuery);
        statement.close();
    }
    public void updateVersion() {
        int version = getVersion();
        if (version != -1) {
            version++;
            Statement statement1 = null;
            try {
                statement1 = dbConn.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String sqlQuery1 = "UPDATE version SET Version='" + version + "'";
            try {
                statement1.executeUpdate(sqlQuery1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                statement1.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void createEspetaculoTable() throws SQLException {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }

        String sqlQuery = "CREATE TABLE espetaculo('id' INTEGER Primary Key AUTOINCREMENT NOT NULL, " +
                "'descricao' TEXT NOT NULL, " +
                "'tipo' TEXT NOT NULL," +
                "'data_hora' TEXT NOT NULL," +
                "'duracao' INTEGER NOT NULL," +
                "'local' TEXT NOT NULL," +
                "'localidade' TEXT NOT NULL," +
                "'pais' TEXT NOT NULL," +
                "'classificacao_etaria' TEXT NOT NULL," +
                "'visivel' INTEGER DEFAULT 0 NOT NULL)";

        if (statement != null) {
            statement.execute(sqlQuery);
        }
        assert statement != null;
        statement.close();
    }
    private void createLugarTable() throws SQLException {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }

        String sqlQuery = "CREATE TABLE lugar('id' INTEGER Primary Key AUTOINCREMENT NOT NULL, " +
                "'fila' TEXT NOT NULL," +
                "'assento' TEXT NOT NULL," +
                "'preco' REAL NOT NULL," +
                "'espetaculo_id' INTEGER REFERENCES espetaculo (id) NOT NULL)";


        if (statement != null) {
            statement.execute(sqlQuery);
        }
        assert statement != null;
        statement.close();
    }
    private void createUtilizadorTable() throws SQLException {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }

        String sqlQuery = "CREATE TABLE utilizador('id' INTEGER Primary Key AUTOINCREMENT NOT NULL, " +
                "'username' TEXT UNIQUE NOT NULL," +
                "'nome' TEXT UNIQUE NOT NULL," +
                "'password' TEXT NOT NULL," +
                "'administrador' INTEGER NOT NULL DEFAULT 0," +
                "'autenticado' INTEGER NOT NULL DEFAULT 0)";


        if (statement != null) {
            statement.execute(sqlQuery);
        }
        assert statement != null;
        statement.close();
    }
    private void createReservaTable() throws SQLException {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }

        String sqlQuery = "CREATE TABLE reserva('id' INTEGER Primary Key AUTOINCREMENT NOT NULL, " +
                "'data_hora' TEXT NOT NULL," +
                "'pago' INTEGER DEFAULT 0 NOT NULL," +
                "'id_utilizador' INTEGER REFERENCES utilizador (id) NOT NULL," +
                "'id_espetaculo' INTEGER REFERENCES espetaculo (id) NOT NULL)";


        if (statement != null) {
            statement.execute(sqlQuery);
        }
        assert statement != null;
        statement.close();
    }
    private void createReservaLugarTable() throws SQLException {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }

        String sqlQuery = "CREATE TABLE reserva_lugar('id_reserva' INTEGER REFERENCES reserva (id) NOT NULL, " +
                "'id_lugar' INTEGER REFERENCES lugar (id) NOT NULL," +
                "PRIMARY KEY (id_reserva,id_lugar))";


        if (statement != null) {
            statement.execute(sqlQuery);
        }
        assert statement != null;
        statement.close();
    }

    public void createTables() throws SQLException {
            createVersionTable();
            createEspetaculoTable();
            createLugarTable();
            createUtilizadorTable();
            createReservaTable();
            createReservaLugarTable();
            insertFirstRegisterTableVersion();
            insertAdmin();
    }

    private void insertAdmin() {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println("Impossível criar um statement");
        }
        Utilizador u = new Utilizador("admin","admin","admin",1,0);

        String sqlQuery = "INSERT INTO utilizador VALUES (NULL," +
                "'" + u.getUsername() + "','" +
                u.getNome() + "','" +
                u.getPassword()+ "','" +
                u.getAdministrador() + "','" +
                u.getAutenticado() + "')";


        if (statement != null) {
            try {
                statement.execute(sqlQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        assert statement != null;
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int getVersion() {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return -1;
        }

        String sqlQuery = "SELECT Version FROM version";

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
        }
        int version = -1;
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return -1;
            }
            try {
                version = resultSet.getInt("Version");
            } catch (SQLException e) {
                System.out.println("Get version fail!");
            }
        }

        System.out.println(version);
        try {
            resultSet.close();
        } catch (SQLException e) {
        }
        try {
            statement.close();
        } catch (SQLException e) {
        }

        return version;
    }


    public boolean updateEspetaculo(Espetaculo espetaculo) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "UPDATE espetaculo " +
                "SET descricao='" + espetaculo.getDescricao() +
                "', tipo='" + espetaculo.getTipo() +
                "', data_hora='" + espetaculo.getData_hora() +
                "', duracao='" + espetaculo.getDuracao() +
                "', local='" + espetaculo.getLocal() +
                "', localidade='" + espetaculo.getLocalidade() +
                "', pais='" + espetaculo.getPais() +
                "', classificacao_etaria='" + espetaculo.getClassificacao_etaria() +
                "', visivel='" + espetaculo.getVisivel() +
                "' WHERE id=" + espetaculo.getID();

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }

    public boolean insertEspetaculo(Espetaculo espetaculo) {
        List<Espetaculo> espetaculos = selectEspetaculo(SelectEspetaculo.LOCAL,espetaculo.getLocal());

        for(Espetaculo e : espetaculos){
            if(espetaculo.equals(e))
                return false;
        }

        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }
        String sqlQuery = "INSERT INTO espetaculo VALUES (NULL," +
                "'" + espetaculo.getDescricao() + "','" +
                espetaculo.getTipo() + "','" +
                espetaculo.getData_hora() + "','" +
                espetaculo.getDuracao() + "','" +
                espetaculo.getLocal() + "','" +
                espetaculo.getLocalidade() + "','" +
                espetaculo.getPais() + "','" +
                espetaculo.getClassificacao_etaria() + "','" +
                espetaculo.getVisivel() + "')";
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        ResultSet resultSet = null;
        sqlQuery = "SELECT MAX(ID) FROM espetaculo";
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        int id = -1;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                id = resultSet.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        espetaculo.setID(id);
        espetaculo.setIdEspetaculoLugares();

        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }

        return true;
    }

    public boolean deleteEspetaculo(int id) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "DELETE FROM espetaculo WHERE id=" + id;
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }

    public List<Espetaculo> selectEspetaculo(SelectEspetaculo selectEspetaculo, String filtro) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return null;
        }
        String sqlQuery;
        ResultSet resultSet = null;
        sqlQuery = "SELECT * FROM espetaculo";
        switch (selectEspetaculo){
            case ALL -> {
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case ID -> {
                if (filtro != null)
                    sqlQuery += " WHERE id like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case DESCRIPTION -> {
                if (filtro != null)
                    sqlQuery += " WHERE descricao like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case LOCATION -> {
                if (filtro != null)
                    sqlQuery += " WHERE localidade like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case LOCAL -> {
                if (filtro != null)
                    sqlQuery += " WHERE local like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case DATA -> {
                if (filtro != null)
                    sqlQuery += " WHERE data_hora like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case COUNTRY -> {
                if (filtro != null)
                    sqlQuery += " WHERE pais like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case TYPE -> {
                if (filtro != null)
                    sqlQuery += " WHERE tipo like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case VISIBLE -> {
                if (filtro != null)
                    sqlQuery += " WHERE visivel like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case DURATION -> {
                if (filtro != null)
                    sqlQuery += " WHERE duracao like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
            case ETARY_CLASSIFICATION -> {
                if (filtro != null)
                    sqlQuery += " WHERE classificacao_etaria like '%" + filtro + "%'";
                try {
                    resultSet = statement.executeQuery(sqlQuery);
                } catch (SQLException e) {
                    return null;
                }
            }
        }
        if(resultSet == null)
            return null;

        List<Espetaculo> espetaculos = new ArrayList<>();
        int ID = 0;
        String descricao = null;
        String tipo = null;
        String data_hora = null;
        int duracao = 0;
        String local = null;
        String localidade = null;
        String pais = null;
        String classificacao_etaria = null;
        int visivel = 0;

        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return null;
            }

            try {
                ID = resultSet.getInt("id");
            } catch (SQLException e) {
                return null;
            }
            try {
                descricao = resultSet.getString("descricao");
            } catch (SQLException e) {
                return null;
            }
            try {
                tipo = resultSet.getString("tipo");
            } catch (SQLException e) {
                return null;
            }
            try {
                data_hora = resultSet.getString("data_hora");
            } catch (SQLException e) {
                return null;
            }
            try {
                duracao = resultSet.getInt("duracao");
            } catch (SQLException e) {
                return null;
            }
            try {
                local = resultSet.getString("local");
            } catch (SQLException e) {
                return null;
            }
            try {
                localidade = resultSet.getString("localidade");
            } catch (SQLException e) {
                return null;
            }
            try {
                pais = resultSet.getString("pais");
            } catch (SQLException e) {
                return null;
            }
            try {
                classificacao_etaria = resultSet.getString("classificacao_etaria");
            } catch (SQLException e) {
                return null;
            }
            try {
                visivel = resultSet.getInt("visivel");
            } catch (SQLException e) {
                return null;
            }
            Espetaculo espetaculo = new Espetaculo(descricao,tipo,data_hora,duracao,local,localidade,
                    pais,classificacao_etaria,visivel);
            espetaculo.setID(ID);

            /*espetaculo.setLugares(selectLugar(espetaculo.getID()));*/

            espetaculos.add(espetaculo);
        }

        return espetaculos;
    }

    public boolean insertLugar(Lugar lugar) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }
        String sqlQuery = "INSERT INTO lugar VALUES (NULL," +
                "'" + lugar.getFila() + "','" +
                lugar.getAssento() + "','" +
                lugar.getPreco() + "','" +
                lugar.getIdEspetaculo() + "')";
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }

    public boolean updateLugar(Lugar lugar) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "UPDATE lugar " +
                "SET fila='" + lugar.getFila() +
                "', assento='" + lugar.getAssento() +
                "', preco='" + lugar.getPreco() +
                "', espetaculo_id='" + lugar.getIdEspetaculo() +
                "' WHERE id=" + lugar.getID();

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }


    public boolean deleteLugar(int id) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "DELETE FROM lugar WHERE id=" + id;
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }


        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }
    public boolean deleteLugarIdEspetaculo(int id) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "DELETE FROM lugar WHERE espetaculo_id=" + id;
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }


        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }

    public List<Lugar> selectLugarIdEspetaculo(int id_espetaculo) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return null;
        }
        ResultSet resultSet = null;
        String sqlQuery = "SELECT * FROM lugar WHERE espetaculo_id=" + id_espetaculo;
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return null;
        }

        int ID = 0;
        String fila = null;
        String assento = null;
        float preco = 0;
        int idEspetaculo = 0;
        List<Lugar> lugares = new ArrayList<>();
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return null;
            }
            try {
                ID = resultSet.getInt("id");
            } catch (SQLException e) {
                return null;
            }
            try {
                fila = resultSet.getString("fila");
            } catch (SQLException e) {
                return null;
            }
            try {
                assento = resultSet.getString("assento");
            } catch (SQLException e) {
                return null;
            }
            try {
                preco = resultSet.getFloat("preco");
            } catch (SQLException e) {
                return null;
            }
            try {
                idEspetaculo = resultSet.getInt("espetaculo_id");
            } catch (SQLException e) {
                return null;
            }

            Lugar lugar = new Lugar(fila,assento,preco,idEspetaculo);
            lugar.setID(ID);

            lugares.add(lugar);
        }

        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return lugares;
        }
        return lugares;
    }

    public List<Lugar> selectLugarIdLugar(int id_lugar) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return null;
        }
        ResultSet resultSet = null;
        String sqlQuery = "SELECT * FROM lugar WHERE id=" + id_lugar;
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return null;
        }

        int ID = 0;
        String fila = null;
        String assento = null;
        float preco = 0;
        int idEspetaculo = 0;
        List<Lugar> lugares = new ArrayList<>();
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return null;
            }
            try {
                ID = resultSet.getInt("id");
            } catch (SQLException e) {
                return null;
            }
            try {
                fila = resultSet.getString("fila");
            } catch (SQLException e) {
                return null;
            }
            try {
                assento = resultSet.getString("assento");
            } catch (SQLException e) {
                return null;
            }
            try {
                preco = resultSet.getFloat("preco");
            } catch (SQLException e) {
                return null;
            }
            try {
                idEspetaculo = resultSet.getInt("espetaculo_id");
            } catch (SQLException e) {
                return null;
            }

            Lugar lugar = new Lugar(fila,assento,preco,idEspetaculo);
            lugar.setID(ID);

            lugares.add(lugar);
        }

        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return lugares;
        }
        return lugares;
    }


    public boolean insertUtilizador(Utilizador utilizador) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "INSERT INTO utilizador VALUES (NULL," +
                "'" + utilizador.getUsername() + "','" +
                utilizador.getNome() + "','" +
                utilizador.getPassword() + "','" +
                utilizador.getAdministrador() + "','" +
                utilizador.getAutenticado() + "')";
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }

        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }


    public List<Utilizador> selectUtilizador(SelectUtilizador selectUtilizador, String user)  {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return null;
        }

        String sqlQuery = "SELECT DISTINCT * FROM utilizador";

        if(user != null) {
            switch (selectUtilizador) {
                case NAME -> sqlQuery += " WHERE nome='" + user + "'";
                case USERNAME -> sqlQuery += " WHERE username='" + user + "'";
                case ID -> {
                    int id = Integer.parseInt(user);
                    sqlQuery += " WHERE id='" + id + "'";
                }
            }
        }

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return null;
        }

        List<Utilizador> utilizadores = new ArrayList<>();
        int ID = -1;
        String username = null;
        String nome = null;
        String password = null;
        int administrador = -1;
        int autenticado = -1;

        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return null;
            }

            try {
                ID = resultSet.getInt("id");
            } catch (SQLException e) {
                return null;
            }
            try {
                username = resultSet.getString("username");
            } catch (SQLException e) {
                return null;
            }
            try {
                nome = resultSet.getString("nome");
            } catch (SQLException e) {
                return null;
            }
            try {
                password = resultSet.getString("password");
            } catch (SQLException e) {
                return null;
            }
            try {
                administrador = resultSet.getInt("administrador");
            } catch (SQLException e) {
                return null;
            }
            try {
                autenticado = resultSet.getInt("autenticado");
            } catch (SQLException e) {
                return null;
            }
            Utilizador utilizador = new Utilizador(username,nome,password,administrador,autenticado);
            utilizador.setID(ID);
            utilizadores.add(utilizador);
        }


        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return utilizadores;
        }

        if(utilizadores.size() == 0)
            return null;

        return utilizadores;
    }



    public boolean updateUsername(Utilizador utilizador) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "UPDATE utilizador " +
                "SET username='" + utilizador.getUsername() +
                "', nome='" + utilizador.getNome() +
                "', password='" + utilizador.getPassword() +
                "', administrador='" + utilizador.getAdministrador() +
                "', autenticado='" + utilizador.getAutenticado() +
                "' WHERE id=" + utilizador.getID();

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }

        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }


    public boolean deleteUtilizadorUsername(String username) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "DELETE FROM utilizador WHERE username='" + username + "'";
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }


        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }

    public boolean deleteUtilizadorNome(String nome) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        String sqlQuery = "DELETE FROM utilizador WHERE nome='" + nome + "'";
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }

        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }

    //--------------------------------------------//


    public int maxReservation() {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return -1;
        }
        ResultSet resultSet = null;
        String sqlQuery = "SELECT MAX(ID) FROM reserva";
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return -1;
        }
        int id = -1;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                id = resultSet.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        return id;
    }

    public List<Reserve> listReservations( String whereColumn, String whereValue) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return null;
        }
        List<Reserve> reservationsList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM reserva";

        if (whereColumn != null && whereValue != null)
            switch (whereColumn) {
                case "id" -> sqlQuery += " WHERE id=" + whereValue;
                case "id_user" -> sqlQuery += " WHERE id_utilizador=" + whereValue;
                case "id_show" -> sqlQuery += " WHERE id_espetaculo=" + whereValue;
                case "paid" -> sqlQuery += " WHERE pago=" + whereValue;
            }

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return null;
        }

        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return null;
            }
            try {
                reservationsList.add(new Reserve(
                        resultSet.getInt("id"),
                        resultSet.getString("data_hora"),
                        resultSet.getInt("pago"),
                        resultSet.getInt("id_utilizador"),
                        resultSet.getInt("id_espetaculo")
                ));
            } catch (SQLException e) {
                return null;
            }
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            return reservationsList;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return reservationsList;
        }
        return reservationsList;
    }
    public boolean insertReservation(Reserve reserve) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        List<Espetaculo> shows = selectEspetaculo(SelectEspetaculo.DATA,null);
        boolean showExists = false;
        for (Espetaculo show : shows)
            if (show.getID() == reserve.getId_show()) {
                showExists = true; break;
            }
        if (!showExists)
            return false;

        List<Utilizador> users = selectUtilizador(SelectUtilizador.ALL,null);
        boolean userExists = false;
        if(users != null) {
            for (Utilizador user : users)
                if (user.getID() == reserve.getId_user()) {
                    userExists = true;
                    break;
                }
        }
        if (!userExists)
            return false;


        String sqlQuery = "INSERT INTO reserva (data_hora, pago, id_utilizador, id_espetaculo)" +
                "VALUES ('" + reserve.getDate_hour() + "', " + reserve.getPaid() + ", " +
                reserve.getId_user() + ", " + reserve.getId_show() + ")";

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }
    public boolean updateReservation(Reserve reserve) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }

        List<Espetaculo> shows = selectEspetaculo(SelectEspetaculo.ALL,null);
        boolean showExists = false;
        for (Espetaculo show : shows)
            if (show.getID() == reserve.getId_show()) {
                showExists = true; break;
            }
        if (!showExists)
            return false;

        List<Utilizador> users = selectUtilizador(SelectUtilizador.ALL,null);
        boolean userExists = false;
        for (Utilizador user : users)
            if (user.getID() == reserve.getId_user()) {
                userExists = true; break;
            }
        if (!userExists)
            return false;

        String sqlQuery = "UPDATE reserva " +
                "SET data_hora='" + reserve.getDate_hour() +
                "', pago=" + reserve.getPaid() +
                ", id_utilizador=" + reserve.getId_user() +
                ", id_espetaculo=" + reserve.getId_show() +
                " WHERE id=" + reserve.getId();


        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }
    public boolean deleteReservation(int id) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }
        String sqlQuery = "DELETE FROM reserva WHERE id=" + id;

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return false;
        }
        return true;
    }


    public ReserveSeat listReservationsSeats(String whereColumn, int whereValue) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return null;
        }
        String sqlQuery = "SELECT * FROM reserva_lugar";
        if (whereColumn != null && whereValue != -1)
            switch (whereColumn) {
                case "id_reserve" -> sqlQuery += " WHERE id_reserva='" + whereValue + "'";
                case "id_seat" -> sqlQuery += " WHERE id_lugar='" + whereValue + "'";
            }

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            return null;
        }
        int id_reserve = -1;
        int id_seat = -1;
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                return null;
            }
            try {
                id_reserve = resultSet.getInt("id_reserva");
            } catch (SQLException e) {
                return null;
            }
            try {
                id_seat = resultSet.getInt("id_lugar");
            } catch (SQLException e) {
                return null;
            }
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            return null;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return new ReserveSeat(id_reserve,id_seat);
        }
        return new ReserveSeat(id_reserve,id_seat);
    }
    public boolean insertReservationsSeats(ReserveSeat reserveSeat) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }
        String sqlQuery = "INSERT INTO reserva_lugar " +
                "VALUES (" + reserveSeat.getId_reserve() + ", " + reserveSeat.getId_seat() + ")";

        List<Reserve> reserves = listReservations(null, null);
        boolean reserveExists = false;
        for (Reserve reserve : reserves)
            if (reserve.getId() == reserveSeat.getId_reserve()) {
                reserveExists = true; break;
            }
        if (!reserveExists)
            return false;

        List<Lugar> seats = selectLugarIdLugar(reserveSeat.getId_seat());
        boolean seatExists = false;
        for (Lugar seat : seats)
            if (seat.getID() == reserveSeat.getId_seat()) {
                seatExists = true; break;
            }
        if (!seatExists)
            return false;

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }
    public boolean deleteReservationSeat(String whereColumn, int whereValue) {
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
        } catch (SQLException e) {
            return false;
        }
        String sqlQuery = "DELETE FROM reserva_lugar";

        if (whereColumn != null && whereValue != -1)
            switch (whereColumn) {
                case "id_reserve" -> sqlQuery += " WHERE id_reserva=" + whereValue;
                case "id_seat" -> sqlQuery += " WHERE id_lugar=" + whereValue;
            }

        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return false;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Statement don't close!");
            return true;
        }
        return true;
    }


}
