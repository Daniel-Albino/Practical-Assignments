package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Espetaculo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int ID;
    private String descricao;
    private String tipo;
    private String hora;
    private String data;
    private String data_hora;
    private int duracao;
    private String local;
    private String localidade;
    private String pais;
    private String classificacao_etaria;
    private int visivel;
    private Map<String,List<Lugar>> lugares;

    public Espetaculo() {
        this.descricao = "";
        this.tipo = "";
        this.data_hora = "";
        this.duracao = -1;
        this.local = "";
        this.localidade = "";
        this.pais = "";
        this.classificacao_etaria = "";
        this.visivel = 1;
        this.hora = "";
        this.data = "";
        lugares = new HashMap<>();
    }

    public Espetaculo(String descricao,
                      String tipo,
                      String data_hora,
                      int duracao,
                      String local,
                      String localidade,
                      String pais,
                      String classificacao_etaria,
                      int visivel) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.data_hora = data_hora;
        this.duracao = duracao;
        this.local = local;
        this.localidade = localidade;
        this.pais = pais;
        this.classificacao_etaria = classificacao_etaria;
        this.visivel = visivel;
        this.hora = "";
        this.data = "";
        lugares = new HashMap<>();
    }

    public Map<String, List<Lugar>> getLugares() {
        return lugares;
    }

    public List<Lugar> getLugaresList() {
        List<Lugar> lugaresAux = new ArrayList<>();

        for(List<Lugar> l : lugares.values()){
            for(int i = 0 ; i< l.size(); i++)
                lugaresAux.add(l.get(i));
        }
        return lugaresAux;
    }

    public void setLugares(Map<String, List<Lugar>> lugares) {
        this.lugares = lugares;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;

        for(List<Lugar> l : lugares.values()){
            for(int i = 0 ; i< l.size(); i++)
                l.get(i).setID(getID());
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public boolean setData_hora(String[] data_hora) {

        for(int i = 1;i < data_hora.length;i++){
            try {
                Integer.parseInt(data_hora[i]);
            }catch (Exception e){
                return false;
            }
        }

        if(data_hora[0].equalsIgnoreCase("DATA")) {
            for (int i = 1; i < data_hora.length; i++) {
                this.data_hora += data_hora[i];
                if(i + 1 < data_hora.length)
                    this.data_hora += "/";
            }
            this.data = this.data_hora;
            this.data_hora += " - ";
        }else {
            for (int i = 1; i < data_hora.length; i++) {
                this.data_hora += data_hora[i];
                this.hora += data_hora[i];
                if(i + 1 < data_hora.length) {
                    this.data_hora += ":";
                    this.hora += ":";
                }
            }
        }
        return true;
    }

    public String getHora() {
        if(hora.isBlank() && !data_hora.isBlank()) {
            String[] aux = data_hora.split(" ");
            setHora(aux[aux.length-1]);
        }
        if(data_hora.isBlank())
            return "Sem hora";
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        if(data.isBlank() && !data_hora.isBlank()) {
            String[] aux = data_hora.split(" ");
            setData(aux[0]);
        }
        if(data_hora.isBlank())
            return "Sem Data";
        return data;
    }

    public void addLugares(List<Lugar> lugares) {
        if(lugares == null && lugares.size() == 0)
            return;


        for(Lugar l : lugares){
            if(this.lugares.containsKey(l.getFila())) {
                boolean bol = true;
                for(int i = 0;i<this.lugares.get(l.getFila()).size();i++){
                    if(this.lugares.get(l.getFila()).get(i).getAssento().equals(l.getAssento())) {
                        bol = false;
                        break;
                    }
                }
                if(bol) {
                    this.lugares.get(l.getFila()).add(l);
                }
            }else{
                this.lugares.put(l.getFila(),new ArrayList<>());
                this.lugares.get(l.getFila()).add(l);
            }
        }
    }

    public void setIdEspetaculoLugares(){
        for(List<Lugar> lugares : lugares.values()){
            for(Lugar l : lugares)
                l.setIdEspetaculo(getID());
        }
    }


    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getClassificacao_etaria() {
        return classificacao_etaria;
    }

    public void setClassificacao_etaria(String classificacao_etaria) {
        this.classificacao_etaria = classificacao_etaria;
    }

    public int getVisivel() {
        return visivel;
    }

    public void setVisivel(int visivel) {
        this.visivel = visivel;
    }

    @Override
    public String toString() {
        return "Espetaculo{" +
                "ID=" + ID +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", data_hora='" + data_hora + '\'' +
                ", duracao=" + duracao +
                ", local='" + local + '\'' +
                ", localidade='" + localidade + '\'' +
                ", pais='" + pais + '\'' +
                ", classificacao_etaria='" + classificacao_etaria + '\'' +
                ", visivel=" + visivel + "\n" +
                ", lugares=" + lugares +
                '}';
    }


    private int[] getDuracaoEquals(){
        String[] aux = LerFicheiroEspetaculo.splitLinha(getHora());

        int minutos = Integer.parseInt(aux[aux.length-1]);
        int horas = Integer.parseInt(aux[0]);

        int d = minutos + duracao;

        int h = d / 60 + horas;
        int m = d % 60;

        return new int[]{h, m};
    }

    @Override
    public boolean equals(Object obj) {
        Espetaculo aux = (Espetaculo) obj;

        if(getLocal().equals(aux.getLocal())) {
            if (getData_hora().equals(aux.getData_hora()))
                return true;
            else if(getData().equals(aux.getData())){
                int [] t = getDuracaoEquals();
                String [] a = aux.getHora().split(":");

                if(t[0] > Integer.parseInt(a[0]))
                    return true;
                else if(t[0] == Integer.parseInt(a[0]) && t[t.length-1] > Integer.parseInt(a[a.length-1]))
                    return true;

            }
        }

        return false;
    }
}
