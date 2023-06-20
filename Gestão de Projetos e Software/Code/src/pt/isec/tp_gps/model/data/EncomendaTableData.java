package pt.isec.tp_gps.model.data;

public class EncomendaTableData {

    private String quantidade;
    private String medicamento;
    public EncomendaTableData(String quantidade, String medicamento) {
        this.quantidade = quantidade;
        this.medicamento = medicamento;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public String toString() {
        return "EncomendaTableData{" +
                "quantidade='" + quantidade + '\'' +
                ", medicamento='" + medicamento + '\'' +
                '}';
    }

}
