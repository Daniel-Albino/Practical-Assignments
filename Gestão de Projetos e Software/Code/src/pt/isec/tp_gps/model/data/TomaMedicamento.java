package pt.isec.tp_gps.model.data;

public class TomaMedicamento {

    private String CC;
    private String nomeUtente;
    private String nomeMedicamento;
    private String hora;

    public TomaMedicamento(String nomeUtente, String nomeMedicamento, String hora, String CC) {
        this.CC = CC;
        this.nomeUtente = nomeUtente;
        this.nomeMedicamento = nomeMedicamento;

        String[] aux = hora.split(":");
        System.out.println("aux[1].length()" + aux[1].length());
        if(aux[1].length() == 2){
            String s = "0" + aux[1];
            aux[1] = s;
        }
        this.hora =  aux[0]+":"+aux[1];
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "TomaMedicamento{" +
                "CC='" + CC + '\'' +
                ", nomeUtente='" + nomeUtente + '\'' +
                ", nomeMedicamento='" + nomeMedicamento + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}
