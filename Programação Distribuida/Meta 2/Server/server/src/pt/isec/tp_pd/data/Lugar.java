package pt.isec.tp_pd.data;

import java.io.Serial;
import java.io.Serializable;

public class Lugar implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int ID;
    private String fila;
    private String assento;
    private float preco;
    private int idEspetaculo;
    private boolean ocupado;

    public Lugar(String fila, String assento, float preco, int idEspetaculo) {
        this.fila = fila;
        this.assento = assento;
        this.preco = preco;
        this.idEspetaculo = idEspetaculo;
        this.ocupado = false;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getIdEspetaculo() {
        return idEspetaculo;
    }

    public void setIdEspetaculo(int idEspetaculo) {
        this.idEspetaculo = idEspetaculo;
    }

    @Override
    public String toString() {
        return "Lugar{" +
                "ID=" + ID +
                ", fila='" + fila + '\'' +
                ", assento='" + assento + '\'' +
                ", preco=" + preco +
                ", idEspetaculo=" + idEspetaculo +
                ", ocupado=" + ocupado +
                '}' + "\n";
    }
}
