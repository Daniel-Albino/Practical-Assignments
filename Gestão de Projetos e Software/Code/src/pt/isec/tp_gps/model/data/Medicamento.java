package pt.isec.tp_gps.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;

public class Medicamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String ID;
    private String nome;
    private String ModoAdministracao;
    private Calendar DataValidade;
    private String PrincipioAtivo;
    private String Laboratorio;
    private String Bula;
    private int stock;

    public Medicamento(String ID, String nome, String modoAdministracao, Calendar dataValidade, String principioAtivo, String laboratorio, String bula) {
        this.ID = ID;
        this.nome = nome;
        this.ModoAdministracao = modoAdministracao;
        this.DataValidade = dataValidade;
        this.PrincipioAtivo = principioAtivo;
        this.Laboratorio = laboratorio;
        this.Bula = bula;
        this.stock = 1;
        getDataValidadeString();
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void decrementStock(){
        this.stock--;
    }

    public String getID() { return ID; }
    public String getNome() { return nome; }
    public String getModoAdministracao() { return ModoAdministracao; }
    public Calendar getDataValidade() { return DataValidade; }
    public String getPrincipioAtivo() { return PrincipioAtivo; }
    public String getLaboratorio() { return Laboratorio; }
    public String getBula() { return Bula; }
    public void setID(String ID) { this.ID = ID; }
    public void setNome(String nome) { this.nome = nome; }
    public void setModoAdministracao(String modoAdministracao) { ModoAdministracao = modoAdministracao; }
    public void setDataValidade(Calendar dataValidade) { DataValidade = dataValidade; }
    public void setPrincipioAtivo(String principioAtivo) { PrincipioAtivo = principioAtivo; }
    public void setLaboratorio(String laboratorio) { Laboratorio = laboratorio;}
    public void setBula(String bula) { Bula = bula; }


    @Override
    public boolean equals(Object obj) {
        return ((Medicamento) obj).getID().equals(getID());
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "ID='" + ID + '\'' +
                ", nome='" + nome + '\'' +
                ", ModoAdministracao='" + ModoAdministracao + '\'' +
                ", PrincipioAtivo='" + PrincipioAtivo + '\'' +
                ", Laboratorio='" + Laboratorio + '\'' +
                ", Bula='" + Bula + '\'' +
                ", stock=" + stock +
                '}';
    }

    public void addStock() {
        this.stock++;
    }


    public String getDataValidadeString(){
        Calendar calendar1 = getDataValidade();
        int year = calendar1.get(Calendar.YEAR);

        String auxMonth = calendar1.get(Calendar.MONTH)+"";
        int month = calendar1.get(Calendar.MONTH);
        month++;
        if(auxMonth.length() == 1)
            auxMonth = "0"+month;

        String auxDay = calendar1.get(Calendar.DAY_OF_MONTH)+"";
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        if(auxDay.length() == 1)
            auxDay = "0"+day;

        String date = auxDay + "/" + auxMonth + "/" + year;
        return date;
    }

}
