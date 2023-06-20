package tests;

import pt.isec.tp_gps.model.data.ListaMedicamentosUtente;
import pt.isec.tp_gps.model.data.Medicamento;
import pt.isec.tp_gps.model.data.Utente;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class testDataClass {
    public static void main(String[] args) {
        System.out.println("Hello testDataClass!");
        Calendar dataValidade = Calendar.getInstance();
        Medicamento m1 = new Medicamento("AB12", "Medicamento_A", "ORAL", dataValidade, "nosei", "lab", "bula");
        Medicamento m2 = new Medicamento("AB123", "Medicamento_B", "ORAL", dataValidade, "nosei", "lab", "bula");
        //Encomenda e = new Encomenda(1, m1, 7);
        /*System.out.println(m1.equals(m2));
        System.out.println(m1);
        System.out.println(e);*/

        List<Calendar> horasMedic = new ArrayList<>(); horasMedic.add(dataValidade);
        ListaMedicamentosUtente listMedicUtente = new ListaMedicamentosUtente(m1, horasMedic);
        List<String> alegias = new ArrayList<>(); alegias.add("alegiasA"); alegias.add("alegiasB");
        List<String> doencas = new ArrayList<>(); doencas.add("doencasA"); doencas.add("doencasB");
        List<ListaMedicamentosUtente> uMedic = new ArrayList<>(); uMedic.add(listMedicUtente);

        Calendar dataNacismento = Calendar.getInstance(); dataNacismento.set(1985, 07, 15);
        Utente u1 = new Utente("ACC123123", "Manel", dataNacismento, "myP@BOOGLE.yxz", 178236897, "M_I_Guess", "Pescador", alegias, doencas, uMedic);

        System.out.println(u1);
        System.out.println(u1.adicionarMedicamento(m2, horasMedic));
        System.out.println(u1);
        System.out.println(u1.removerMedicamento(m1));
        System.out.println(u1);
        System.out.println(u1.removerMedicamento(m1));
        ListaMedicamentosUtente listaMedicamentosUtenteandterior = listMedicUtente;
        horasMedic.add(dataNacismento);
        listMedicUtente.setHoraMedicamento(horasMedic);
        System.out.println(listMedicUtente);
        System.out.println(u1.editarMedicamento(listaMedicamentosUtenteandterior,listMedicUtente));
        System.out.println(u1);
    }
}
