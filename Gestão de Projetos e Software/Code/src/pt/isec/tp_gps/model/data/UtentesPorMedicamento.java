package pt.isec.tp_gps.model.data;

public class UtentesPorMedicamento {
    private String nomeMedicamento;
    private int quantidade;

    public UtentesPorMedicamento(String nomeMedicamento, int quantidade) {
        this.nomeMedicamento = nomeMedicamento;
        this.quantidade = quantidade;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "UtentesPorMedicamento{" +
                "nomeMedicamento='" + nomeMedicamento + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }
}
