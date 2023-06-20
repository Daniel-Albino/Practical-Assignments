package pt.isec.tp_gps.model.threads;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.data.Medicamento;
import pt.isec.tp_gps.model.data.TomaMedicamento;
import pt.isec.tp_gps.model.data.Utente;
import pt.isec.tp_gps.model.fsm.MEContexto;

import java.util.*;

public class ThreadAlertaMedicamentos extends Thread {
    private MEContexto contexto;
    private int TIMEOUT = 900000;
    private final int TIMEOUT_BACKUP = 900000;
    private boolean keepgoing = true;

    Map<String,List<TomaMedicamento>> utentesEMedicamentos;
    public ThreadAlertaMedicamentos(MEContexto contexto, Map<String,List<TomaMedicamento>> utentesEMedicamentos) {
        this.contexto = contexto;
        this.utentesEMedicamentos = utentesEMedicamentos;
    }

    public void setKeepgoing() {
        if(keepgoing)
            keepgoing = false;
    }

    public synchronized void apagaUtentesEMedicamentos() {
        utentesEMedicamentos = new HashMap<>();
    }

    public synchronized Map<String, List<TomaMedicamento>> getUtentesEMedicamentos() {
        return utentesEMedicamentos;
    }

    public synchronized void setUtentesEMedicamentos(Map<String, List<TomaMedicamento>> utentesEMedicamentos) {
        this.utentesEMedicamentos = utentesEMedicamentos;
    }

    public synchronized void confirmaToma(TomaMedicamento tomaMedicamento){

    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.MINUTE);

        if (time < 15 || (15 < time && time < 30) || (30 < time && time < 45) || (45 < time && time < 59)) {
            //Verificar se alguem precisa de tomar os medicamentos!
            System.out.println("verificaMedicamentosPrimeiraVez");
            verificaMedicamentosPrimeiraVez(time,calendar);
        }
        verificaMedicamentos(time,calendar);
        System.out.println("verificaMedicamentos");
        System.out.println("verificaMedicamentos " + utentesEMedicamentos);
        setTIMEOUT(time);
        while (keepgoing) {
            completeTask();
            calendar = Calendar.getInstance();
            time = calendar.get(Calendar.MINUTE);
            verificaMedicamentos(time,calendar);
            setTIMEOUT(time);
        }
    }

    private synchronized void verificaMedicamentos(int time, Calendar calendar) {
        int limSup = -1,limInf = -1;
        Map<String,List<Medicamento>> aux = new HashMap<>();
        if(time < 15){
            aux = contexto.medicamentosATomar(calendar,30,15);
            limSup = 30;
            limInf = 15;
        }else if(15 < time && time < 30){
            aux = contexto.medicamentosATomar(calendar,45,30);
            limSup = 45;
            limInf = 30;
        }else if(30 < time && time < 45){
            aux = contexto.medicamentosATomar(calendar,59,45);
            limSup = 59;
            limInf = 45;
        }else if(45 < time && time < 59){
            aux = contexto.medicamentosATomar(calendar,15,0);
            limSup = 15;
            limInf = 0;
        }
        adiciona(aux,calendar,limSup,limInf);
        System.out.println(utentesEMedicamentos);
    }

    private synchronized void verificaMedicamentosPrimeiraVez(int time, Calendar calendar){
        Map<String,List<Medicamento>> aux = new HashMap<>();
        int limSup = -1,limInf = -1;
        if(time < 15){
            aux = contexto.medicamentosATomar(calendar,15,0);
            limSup = 15;
            limInf = 0;
        }else if(15 < time && time < 30){
            aux = contexto.medicamentosATomar(calendar,30,15);
            limSup = 30;
            limInf = 15;
        }else if(30 < time && time < 45){
            aux = contexto.medicamentosATomar(calendar,45,30);
            limSup = 45;
            limInf = 30;
        }else if(45 < time && time < 59){
            aux = contexto.medicamentosATomar(calendar,59,45);
            limSup = 59;
            limInf = 45;
        }
        adiciona(aux,calendar,limSup,limInf);
        System.out.println("verificaMedicamentosPrimeiraVez " + utentesEMedicamentos);
    }


    private synchronized void adiciona(Map<String,List<Medicamento>> aux, Calendar calendar, int limSup, int limInf){
        boolean popup = false;
        if(aux.size() != 0) {
            for (String CC : aux.keySet()) {
                for (List<Medicamento> m : aux.values()) {
                    for (Medicamento med : m) {
                        Utente utenteAux = contexto.getUtentesCC(CC);
                        String hora = utenteAux.getHorasMedicamento(med.getNome(), calendar.get(Calendar.HOUR_OF_DAY), limSup, limInf);
                        System.out.println(hora);
                        if (hora != null) {
                            if (!utentesEMedicamentos.containsKey(CC)) {
                                List<TomaMedicamento> medAux = new ArrayList<>();
                                TomaMedicamento tomaMedicamento = new TomaMedicamento(utenteAux.getCC(), med.getNome(), hora, CC);
                                medAux.add(tomaMedicamento);
                                utentesEMedicamentos.put(CC, medAux);
                                if(!popup) {
                                    Platform.runLater(() -> {
                                        Alert a = new Alert(Alert.AlertType.WARNING);
                                        a.setTitle("Toma de medicamentos!!");
                                        a.setHeaderText(null);
                                        a.setContentText("Tem utentes que precisam de medicamentos");
                                        final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                                        try {
                                            stage.getIcons().add(new Image(this.getClass().getResource("./resources/images/pills.png").toString()));
                                        } catch (NullPointerException e) {
                                        }
                                        a.showAndWait();
                                        if (a.getResult() == ButtonType.OK) {
                                            stage.close();
                                        }
                                    });
                                }
                                popup = true;
                            } else {
                                boolean add = true;
                                for(TomaMedicamento tm : utentesEMedicamentos.get(CC))
                                    if(tm.getHora().equals(hora)) {
                                        add = false;
                                        break;
                                    }
                                if(add) {
                                    TomaMedicamento tomaMedicamento = new TomaMedicamento(utenteAux.getCC(), med.getNome(), hora, CC);
                                    utentesEMedicamentos.get(CC).add(tomaMedicamento);
                                    if(!popup) {
                                        Platform.runLater(() -> {
                                            Alert a = new Alert(Alert.AlertType.WARNING);
                                            a.setTitle("Toma de medicamentos!!");
                                            a.setHeaderText(null);
                                            a.setContentText("Tem utentes que precisam de medicamentos");
                                            final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                                            try {
                                                stage.getIcons().add(new Image(this.getClass().getResource("./resources/images/pills.png").toString()));
                                            } catch (NullPointerException e) {
                                            }
                                            a.showAndWait();
                                            if (a.getResult() == ButtonType.OK) {
                                                stage.close();
                                            }
                                        });
                                    }
                                    popup = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized void setTIMEOUT(int time){
        if(time < 15){
            int aux = 15 - time;
            TIMEOUT = aux * 60000;
        }else if(15 < time && time < 30){
            int aux = 30 - time;
            TIMEOUT = aux * 60000;
        }else if(30 < time && time < 45){
            int aux = 45 - time;
            TIMEOUT = aux * 60000;
        }else if(45 < time && time < 59){
            int aux = 59 - time;
            TIMEOUT = aux * 60000;
        }
    }

    private synchronized void completeTask() {
        try {
            /*Thread.sleep(TIMEOUT);*/
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TIMEOUT = TIMEOUT_BACKUP;
    }



    @Override
    public String toString() {
        return "ThreadAlertaMedicamentos{" +
                "utentesEMedicamentos=" + utentesEMedicamentos +
                '}';
    }
}