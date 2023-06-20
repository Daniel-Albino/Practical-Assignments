package pt.isec.tp_gps.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class ListaMedicamentosUtente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Medicamento medicamento;
    private List<Calendar> HoraMedicamento;
    private int quantidade;
    private String nomeMedicamento;


    public ListaMedicamentosUtente(Medicamento medicamento, List<Calendar> horaMedicamento) {
        this.medicamento = medicamento;
        HoraMedicamento = horaMedicamento;
        this.quantidade = horaMedicamento.size();
        this.nomeMedicamento = this.medicamento.getNome();
    }

    public String getHoras(){
        String s = "";
        for(Calendar c : HoraMedicamento){
            String h = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String m = String.valueOf(c.get(Calendar.MINUTE));
            if(h.length() == 1) {
                h = "0";
                h += c.get(Calendar.HOUR_OF_DAY);
            }
            if(m.length() == 1) {
                m = "0";
                m += c.get(Calendar.MINUTE);
            }

            s += h + ":" + m + ";";
        }
        if(s.isBlank())
            return "Sem dados";
        return s;
    }


    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public List<Calendar> getHoraMedicamento() {
        return HoraMedicamento;
    }

    public void setHoraMedicamento(List<Calendar> horaMedicamento) {
        HoraMedicamento = horaMedicamento;
        quantidade = horaMedicamento.size();
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if(quantidade != HoraMedicamento.size())
            return;
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        String string = "ListaMedicamentosUtente{" + "Medicamento=" + medicamento + ", HoraMedicamento=";

        for(Calendar c : HoraMedicamento)
            string += c.get(Calendar.HOUR_OF_DAY) + ":" +  c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) +
                    " " + c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR)+ " ";

        string += ", quantidade=" + quantidade + '}';
        return string;
    }
}
