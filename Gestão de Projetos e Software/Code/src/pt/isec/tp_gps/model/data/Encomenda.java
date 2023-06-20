package pt.isec.tp_gps.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;

public class Encomenda implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long IDEncomenda;
    private Medicamento EMedicamento;
    private String nomeMedicamento;
    private int Quantidade;
    private final Calendar DataEncomenda;

    public Encomenda(long idEncomenda, Medicamento eMedicamento, int quantidade) {
        this.IDEncomenda = idEncomenda;
        this.EMedicamento = eMedicamento;
        this.Quantidade = quantidade;
        this.DataEncomenda = Calendar.getInstance();
        this.nomeMedicamento = EMedicamento.getNome();
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public long getIDEncomenda() {
        return IDEncomenda;
    }

    public void setIDEncomenda(long IDEncomenda) {
        this.IDEncomenda = IDEncomenda;
    }

    public Medicamento getEMedicamento() {
        return EMedicamento;
    }

    public void setEMedicamento(Medicamento EMedicamento) {
        this.EMedicamento = EMedicamento;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        Quantidade = quantidade;
    }

    public Calendar getDataEncomenda() {
        return DataEncomenda;
    }

    @Override
    public String toString() {
        return "Encomenda {" +
            "\n\tIDEncomendas= " + IDEncomenda +
            ",\n\tEMedicamento= " + EMedicamento +
            ",\n\tQuantidade= " + Quantidade +
            ",\n\tDataEncomenda= " +
                DataEncomenda.get(Calendar.HOUR_OF_DAY) + ":" +  DataEncomenda.get(Calendar.MINUTE) + ":" + DataEncomenda.get(Calendar.SECOND) +
                " " + DataEncomenda.get(Calendar.DATE) + "/" + DataEncomenda.get(Calendar.MONTH) +"/"+ DataEncomenda.get(Calendar.YEAR) +
        "\n}";
    }
}
