package pt.isec.tp_gps.model.data;

import java.util.Objects;

public class MedUtente {
    private String tempMed;
    private String tempHoras;


    public MedUtente(String tempMed, String tempHoras) {
        this.tempMed = tempMed;
        this.tempHoras = tempHoras;
    }

    public String getTempMed() {
        return tempMed;
    }

    public void setTempMed(String tempMed) {
        this.tempMed = tempMed;
    }

    public String getTempHoras() {
        return tempHoras;
    }

    public void setTempHoras(String tempHoras) {
        this.tempHoras = tempHoras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedUtente medUtente = (MedUtente) o;
        return tempMed.equals(medUtente.tempMed) && tempHoras.equals(medUtente.tempHoras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tempMed, tempHoras);
    }

    @Override
    public String toString() {
        return "MedUtente{" +
                "tempMed='" + tempMed + '\'' +
                ", tempHoras='" + tempHoras + '\'' +
                '}';
    }
}
