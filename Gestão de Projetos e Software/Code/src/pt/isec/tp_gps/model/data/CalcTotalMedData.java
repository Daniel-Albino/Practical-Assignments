package pt.isec.tp_gps.model.data;

import java.util.ArrayList;
import java.util.List;

public class CalcTotalMedData {

    private Medicamento med;
    private List<Utente> utentes;


    public CalcTotalMedData(Medicamento med) {
        this.med = med;
        this.utentes = new ArrayList<>();
    }

    public Medicamento getMed() {
        return med;
    }

    public void setMed(Medicamento med) {
        this.med = med;
    }

    public List<Utente> getUtentes() {
        return utentes;
    }

    public void addUtente(Utente a) {
        this.utentes.add(a);
    }

    @Override
    public String toString() {
        return "CalcTotalMedData{" +
                "med=" + med +
                ", utentes=" + utentes +
                '}';
    }
}
