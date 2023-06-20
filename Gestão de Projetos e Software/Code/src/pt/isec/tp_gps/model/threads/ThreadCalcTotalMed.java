package pt.isec.tp_gps.model.threads;

import pt.isec.tp_gps.model.*;
import pt.isec.tp_gps.model.data.*;

import java.util.ArrayList;
import java.util.List;

public class ThreadCalcTotalMed extends Thread {

    private Decorator model;

    private List<EncomendaTableData> encomendaTableData;

    public ThreadCalcTotalMed(Decorator model) {
        this.model = model;
        this.encomendaTableData = new ArrayList<>();
    }


    @Override
    public void run() {

        List<CalcTotalMedData> listMedUtentes = new ArrayList<>();
        List<Medicamento> meds = model.getMedicamentos();
        List<Utente> utentes = model.getUtentes();
        List<Encomenda> encomendas = model.getEncomendas();


        int i=0;
        for (Medicamento a : meds) {
            listMedUtentes.add(new CalcTotalMedData(a));
            for (Utente o : utentes) {
                for(ListaMedicamentosUtente e : o.getListaMedicamentos()){
                    if(e.getMedicamento().equals(a))
                        listMedUtentes.get(i).addUtente(o);
                }
            }
            ++i;
        }

        for(CalcTotalMedData a : listMedUtentes){
            System.out.println(a.toString());
            System.out.println("Stock->"+a.getMed().getStock()+" ohte: ");
            if(a.getMed().getStock() < (a.getUtentes().size()*14)){
                boolean encomendar = true;
                if(encomendas!=null) {
                    for (Encomenda encomenda : encomendas) {
                        if (encomenda.getNomeMedicamento().equals(a.getMed().getNome())) {
                            encomendar = false;
                            break;
                        }else{
                            encomendar = true;
                        }
                    }
                }
                if(encomendar) {
                    encomendaTableData.add(new EncomendaTableData(String.valueOf(a.getUtentes().size() * 50), a.getMed().getNome()));
                    System.out.println("Encomendas: " + encomendaTableData);
                }
            }
        }
        if(encomendaTableData.size() > 0) {
            model.adicionarEncomendas(encomendaTableData);
        }

    }
}
