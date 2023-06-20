package pt.isec.tp_gps.model.threads;

import pt.isec.tp_gps.model.data.TomaMedicamento;

import java.util.List;
import java.util.Map;

public class ThreadAux extends Thread{

    Map<String, List<TomaMedicamento>> utentesEMedicamentos;
    String cc;

    public ThreadAux(Map<String, List<TomaMedicamento>> utentesEMedicamentos,String cc) {
        this.utentesEMedicamentos = utentesEMedicamentos;
        this.cc = cc;
    }

    @Override
    public void run() {
        utentesEMedicamentos.remove(cc);
    }
}
