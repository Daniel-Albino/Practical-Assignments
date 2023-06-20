package pt.isec.tp_gps.model.data;

import pt.isec.tp_gps.ui.seccoes.Pages;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Principal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Map<Long,List<Encomenda>> encomendas;
    private Map<String,Utente> utentes;
    private Map<String,Medicamento> medicamentos;
    private Map<Integer,Integer> numeroUtentesPorMes;
    private List<String> historicoUtentes;
    private List<String> historicoEncomendas;
    private Pages pages;

    public Principal() {
        this.encomendas = new HashMap<>();
        this.utentes = new HashMap<>();
        this.medicamentos = new HashMap<>();
        this.historicoUtentes = new ArrayList<>();
        this.historicoEncomendas = new ArrayList<>();
        this.numeroUtentesPorMes = new HashMap<>();
        inicializarNumeroUtentesPorMes();
    }

    private void inicializarNumeroUtentesPorMes() {
        numeroUtentesPorMes.put(1,0);
        numeroUtentesPorMes.put(2,0);
        numeroUtentesPorMes.put(3,0);
        numeroUtentesPorMes.put(4,0);
        numeroUtentesPorMes.put(5,0);
        numeroUtentesPorMes.put(6,0);
        numeroUtentesPorMes.put(7,0);
        numeroUtentesPorMes.put(8,0);
        numeroUtentesPorMes.put(9,0);
        numeroUtentesPorMes.put(10,0);
        numeroUtentesPorMes.put(11,0);
        numeroUtentesPorMes.put(12,0);
        ordenaNumeroUtentesPorMes();
    }


    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public List<String> getHistoricoUtentes() {
        return historicoUtentes;
    }

    public List<String> getHistoricoEncomendas() {
        return historicoEncomendas;
    }

    /*public Map<String, Integer> getNumeroUtentesPorMes() {
        return numeroUtentesPorMes;
    }*/
    public Map<Integer, Integer> getNumeroUtentesPorMes() {
        return numeroUtentesPorMes;
    }

    private void ordenaNumeroUtentesPorMes(){
        final Map<Integer, Integer> mapOrdenada = numeroUtentesPorMes.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        //mapOrdenada.forEach((key, value) -> System.out.println(value + " - " + key));
        numeroUtentesPorMes = new HashMap<>(mapOrdenada);
    }

    public boolean adicionarUtente(List<String> dadosUtente, Calendar dataNascimento, List<String> alergias, List<String> doencas,
                                   List<MedUtente> listaMedicamentosUtentes){
        //List<ListaMedicamentosUtente> listaMedicamentosUtentes -> é uma List<String> listaMedicamentosUtentes
        if(dadosUtente == null || dataNascimento == null ||alergias == null || doencas == null || listaMedicamentosUtentes == null)
            return false;
        if(dadosUtente.size() != 6)
            return false;

        if(dadosUtente.get(0).isBlank() || dadosUtente.get(1).isBlank()
                || dadosUtente.get(2).isBlank() || dadosUtente.get(3).isBlank()
                || dadosUtente.get(4).isBlank())
            return false;

        if(utentes.containsKey(dadosUtente.get(1)))
            return false;
        if(!dadosUtente.get(2).contains("@"))
            return false;

        int tel = 0;
        if(dadosUtente.get(3).length() != 9)
            return false;
        try {
            tel = Integer.parseInt(dadosUtente.get(3));
        }catch (NumberFormatException e){
            return false;
        }
        String ocupacao = dadosUtente.get(5);
        if(ocupacao.isBlank())
            ocupacao = "Sem ocupação";

        Calendar dataNascimentoAux = Calendar.getInstance();
        dataNascimentoAux.set(dataNascimento.get(Calendar.YEAR),dataNascimento.get(Calendar.MONTH),dataNascimento.get(Calendar.DAY_OF_MONTH));

        List<ListaMedicamentosUtente> listAux = new ArrayList<>();
        if(listaMedicamentosUtentes.size() != 0) {
            List<String> medicamento = new ArrayList<>();
            for (Medicamento med : medicamentos.values()) {
                medicamento.add(med.getNome());
            }

            if (medicamento.size() > 0) {
                for (MedUtente medUtente : listaMedicamentosUtentes) {
                    if (!medicamento.contains(medUtente.getTempMed()))
                        return false;
                }

                for (MedUtente medUtente : listaMedicamentosUtentes) {
                    if (getUmMedicamentoNome(medUtente.getTempMed()) != null) {
                        Medicamento medicamentoAux = getUmMedicamentoNome(medUtente.getTempMed());
                        List<String> horas = divideHoras(medUtente.getTempHoras());
                        List<Calendar> calendars = new ArrayList<>();
                        if (horas == null)
                            return false;
                        for (String s : horas) {
                            String[] h = s.split(":");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0, 0, 0, Integer.parseInt(h[0]), Integer.parseInt(h[1]));
                            calendars.add(calendar);
                        }
                        if (calendars.size() == 0)
                            return false;
                        listAux.add(new ListaMedicamentosUtente(medicamentoAux, calendars));
                    }
                }


                if (listAux.size() == 0)
                    return false;
            }
        }

        Utente utente = new Utente(dadosUtente.get(1),dadosUtente.get(0),dataNascimentoAux,dadosUtente.get(2),
                tel,dadosUtente.get(4),ocupacao,alergias,doencas,listAux);

        utentes.put(utente.getCC(),utente);
        addNumeroUtentesPorMes();
        return true;
    }
    public boolean validaHoras(String horas){
        String[] h = horas.split(";");

        for(int i = 0;i<h.length;i++){
            for(int j = i+1;j<h.length;j++)
                if(h[i].equals(h[j]))
                    return false;
        }

        Pattern pattern = Pattern.compile("^[0-9][0-9]:[0-9][0-9]$", Pattern.CASE_INSENSITIVE);
        for(String a : h){
            Matcher matcher = pattern.matcher(a);
            if(!matcher.find())
                return false;
            else{
                  String aux[] = a.split(":");
                  if((Integer.parseInt(aux[0]) < 0 || Integer.parseInt(aux[0]) > 23) || (Integer.parseInt(aux[1]) < 0 || Integer.parseInt(aux[1]) > 59))
                      return false;
            }

        }
        return true;
    }

    private List<String> divideHoras(String tempHoras) {
        List<String> horas = new ArrayList<>();
        if(tempHoras.length() > 4) {
            if (tempHoras.contains(";")) {
                String[] horasAux;
                horasAux = tempHoras.split(";");

                if (horasAux.length >= 1) {
                    for (int i = 0; i < horasAux.length; i++)
                        horas.add(horasAux[i]);
                }
                return horas;
            }
            horas.add(tempHoras);
        }
        return horas;
    }



    private Medicamento getUmMedicamentoNome(String nome){
        Medicamento medicamento = null;
        for(Medicamento med : medicamentos.values()){
            if(med.getNome().toUpperCase().equals(nome.toUpperCase())){
                medicamento = med;
                break;
            }
        }

        return medicamento;
    }

    private void addNumeroUtentesPorMes(){
        int mes = obterMes();
        int aux = numeroUtentesPorMes.get(mes);
        aux++;
        numeroUtentesPorMes.put(mes,aux);
        ordenaNumeroUtentesPorMes();
    }
    private void removeNumeroUtentesPorMes(){
        int mes = obterMes();
        int aux = numeroUtentesPorMes.get(mes);
        aux--;
        numeroUtentesPorMes.put(mes,aux);
        ordenaNumeroUtentesPorMes();
    }

    private Integer obterMes() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        //int month = 10;
        return month;
    }

    /*private String obterMes() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        return switch (month){
            case 1 -> "Jan";
            case 2 -> "Feb";
            case 3 -> "Mar";
            case 4 -> "Apr";
            case 5 -> "May";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Aug";
            case 9 -> "Sep";
            case 10 -> "Oct";
            case 11 -> "Nov";
            case 12 -> "Dec";
            default -> "";
        };
    }*/

    public boolean removerUtente(String CC){
        if(utentes.containsKey(CC)){
            Calendar calendar = Calendar.getInstance();

            String historico = "O utente com CC " + CC + " e nome " + utentes.get(CC).getNome() + " saiu no dia " +
                    calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)
                    + " às " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ".";

            historicoUtentes.add(historico);
            if(utentes.remove(CC,utentes.get(CC))) {
                removeNumeroUtentesPorMes();
                return true;
            }
        }
        return false;
    }

    public boolean removerUtenteSemHistorico(String CC){
        if(utentes.containsKey(CC)){
            Calendar calendar = Calendar.getInstance();

            String historico = "O utente com CC " + CC + " e nome " + utentes.get(CC).getNome() + " saiu no dia " +
                    calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)
                    + " às " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ".";

            if(utentes.remove(CC,utentes.get(CC))) {
                removeNumeroUtentesPorMes();
                return true;
            }
        }
        return false;
    }



    public boolean editarUtente(Utente utenteAnterior,Utente utenteEditado){
        if(utentes.containsKey(utenteEditado.getCC()))
            return false;
        if(utentes.remove(utenteAnterior.getCC(),utenteAnterior)) {
            utentes.put(utenteEditado.getCC(),utenteEditado);
            return true;
        }
        return false;
    }

    public List<Utente> getUtentes(){
        return List.copyOf(utentes.values());
    }

    public Utente getUtenteCC(String CC){
        if(utentes.containsKey(CC))
            return utentes.get(CC);
        return null;
    }

    public List<Utente> getUtenteNome(String nome){
        List<Utente> utenteAux = new ArrayList<>();
        for(Utente utente : utentes.values()){
            if(utente.getNome().toUpperCase().contains(nome.toUpperCase()))
                utenteAux.add(utente);
        }
        return utenteAux;
    }

    public Map<String,List<Medicamento>> tomarMedicamento(Calendar calendar, int limiteSup, int limiteInf){
        Map<String,List<Medicamento>> medicamentoUtente = new HashMap<>();
        int hora_atual = calendar.get(Calendar.HOUR_OF_DAY);
        int min_atual = calendar.get(Calendar.MINUTE);
        for(Utente utente : getUtentes()){
            List<ListaMedicamentosUtente> list = new ArrayList<>(utente.getListaMedicamentos());
            for(ListaMedicamentosUtente l : list){
                for(Calendar c : l.getHoraMedicamento()){
                    int hora_utente = c.get(Calendar.HOUR_OF_DAY);
                    int min_utente = c.get(Calendar.MINUTE);
                    if((hora_atual == hora_utente || hora_utente == (hora_atual+1)) && (min_utente <= limiteSup && min_utente >= limiteInf)) {
                        if(!medicamentoUtente.containsKey(utente.getCC())) {
                            List<Medicamento> medAux = new ArrayList<>();
                            medAux.add(l.getMedicamento());
                            medicamentoUtente.put(utente.getCC(),medAux);
                        }else
                            medicamentoUtente.get(utente.getCC()).add(l.getMedicamento());
                    }
                }
            }
        }
        return medicamentoUtente;
    }

    public boolean adicionarEncomendas(String quantidade,String nomeMedicamento){
        if(quantidade == null || nomeMedicamento == null)
            return false;
        if(quantidade.isBlank() || nomeMedicamento.isBlank())
            return false;

        int quant = 0;
        try {
            quant = Integer.parseInt(quantidade);
        }catch (NumberFormatException e){
            return false;
        }

        Medicamento medicamento = null;

        for(Medicamento med : medicamentos.values())
            if(med.equals(nomeMedicamento))
                    medicamento = med;

        if (medicamento == null)
            return false;

        if(!medicamentos.containsKey(medicamento.getID()))
            return false;
        long id = Long.parseLong(idProximaEncomenda());
        Encomenda encomenda = new Encomenda(id,medicamento,quant);
        List<Encomenda> listEncomendas = new ArrayList<>();
        if(encomendas.size() == 0)
            listEncomendas.add(encomenda);
        else
            listEncomendas = encomendas.get(id);
        encomendas.put(id,listEncomendas);
        return true;
    }


    public void adicionarUtente(Utente utente){
        utentes.put(utente.getCC(),utente);
    }


    public boolean adicionarEncomendas(List<EncomendaTableData> aux){

        if(aux == null || aux.size() == 0)
            return false;

        long id = -1;
        try {
            id = Long.parseLong(idProximaEncomenda());
        }catch (NumberFormatException e){
            return false;
        }

        List<Encomenda> enc = new ArrayList<>();

        for(EncomendaTableData a : aux) {
            if (a.getQuantidade() == null || a.getQuantidade().isBlank() || a.getMedicamento() == null || a.getMedicamento().isBlank())
                return false;

            int quantidade = 0;
            try {
                quantidade = Integer.parseInt(a.getQuantidade());
            }catch (NumberFormatException e){
                return false;
            }

            List<Medicamento> med = getMedicamentoNome(a.getMedicamento());
            System.out.println("Aqui!!!!!- > " + getMedicamentoNome(a.getMedicamento()));

            for(Medicamento medicamento : med){
                Encomenda encomenda = new Encomenda(id,medicamento,quantidade);
                enc.add(encomenda);
            }
        }

        if(enc.size() == 0)
            return false;

        encomendas.put(id,enc);
        return true;
    }



    public String idProximaEncomenda(){
        return String.valueOf(encomendas.size());
    }

    public boolean removerEncomendas(String idEnc,String nomeMed){
        long id = -1;
        try {
            id = Long.parseLong(idEnc);
        }catch (NumberFormatException e){
            return false;
        }
        if(id == -1)
            return false;
        if(encomendas.containsKey(id)){
            Encomenda encomenda = getEncomendasIdMed(id,nomeMed);
            if(encomenda == null)
                return false;
            Calendar calendar = Calendar.getInstance();
            String historico = "A encomenda com id " + encomenda.getIDEncomenda() + " e medicamento " + encomenda.getEMedicamento() +
                    " entrou no dia " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)
                    + " às " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ".";
            historicoEncomendas.add(historico);
            Medicamento med = getUmMedicamentoNome(nomeMed);
            int q = med.getStock();
            q += encomenda.getQuantidade();
            med.setStock(q);
            medicamentos.put(med.getID(),med);
            return encomendas.remove(encomenda.getIDEncomenda(),encomendas.get(encomenda.getIDEncomenda()));
        }
        return false;
    }

    private Encomenda getEncomendasIdMed(long id,String nomeMed) {
        if(encomendas.containsKey(id)) {
            for(Encomenda encomenda : encomendas.get(id))
                if(encomenda.getEMedicamento().getNome().toUpperCase().equals(nomeMed.toUpperCase()))
                    return encomenda;
        }
        return null;
    }

    public List<Encomenda> getEncomendas(){
        List<Encomenda> listEncomendas = new ArrayList<>();
        if(encomendas.size() == 0)
            return null;
        else {
            for (List<Encomenda> lE : encomendas.values()) {
                if(lE != null) {
                    for (Encomenda e : lE)
                        listEncomendas.add(e);
                }
            }
        }
        return listEncomendas;
    }



    public boolean adicionarMedicamento(List<String> dadosMedicamento, Calendar DataValidade) {
        if (dadosMedicamento == null || dadosMedicamento.size() != 6)
            return false;
        for(String s : dadosMedicamento)
            if(s.isBlank())
                return false;

        for(Medicamento med : medicamentos.values()){
            if(med.getNome().toUpperCase().equals(dadosMedicamento.get(0).toUpperCase()))
                return false;
        }

        if(medicamentos.containsKey(dadosMedicamento.get(1))) {
            medicamentos.get(dadosMedicamento.get(1)).addStock();
            return false;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(DataValidade.get(Calendar.YEAR),DataValidade.get(Calendar.MONTH),DataValidade.get(Calendar.DAY_OF_MONTH));
        Medicamento medicamento = new Medicamento(dadosMedicamento.get(1),dadosMedicamento.get(0),dadosMedicamento.get(2),calendar,
                dadosMedicamento.get(3),dadosMedicamento.get(4),dadosMedicamento.get(5));

        medicamentos.put(dadosMedicamento.get(1),medicamento);
        System.out.println(medicamentos);
        return true;
    }


    public boolean removerMedicamento(String id){
        if(medicamentos.containsKey(id)) {
            Medicamento med = medicamentos.get(id);
            for (Utente u : utentes.values()) {
                for (ListaMedicamentosUtente l : u.getListaMedicamentos())
                    if (l.getNomeMedicamento().toUpperCase().equals(med.getNome().toUpperCase()))
                        return false;
            }
        }

        return medicamentos.remove(id,medicamentos.get(id));
    }


    public boolean editarMedicamento(Medicamento medicamentoAnterior,Medicamento medicamentoEditado){ //idanterior
        if(!medicamentoAnterior.getID().toUpperCase().equals(medicamentoEditado.getID().toUpperCase()) && medicamentos.containsKey(medicamentoEditado.getID()))
            return false;
        if(medicamentos.remove(medicamentoAnterior.getID(),medicamentoAnterior)) {
            medicamentos.put(medicamentoEditado.getID(), medicamentoEditado);
            return true;
        }
        return false;
    }


    public List<Medicamento> getMedicamentos(){
        return List.copyOf(medicamentos.values());
    }

    public Medicamento getMedicamentoID(String ID){
        if(medicamentos.containsKey(ID))
            return medicamentos.get(ID);
        return null;
    }

    public boolean existeMedicamentoNome(String nome){
        for(Medicamento med : medicamentos.values()){
            if(med.getNome().equals(nome))
                return true;
        }
        return false;
    }

    public List<Medicamento> getMedicamentoNome(String nome){
        List<Medicamento> medAux = new ArrayList<>();

        for(Medicamento med : medicamentos.values()){
            if(med.getNome().equals(nome))
                medAux.add(med);
        }
        return medAux;
    }



    public List<UtentesPorMedicamento> getNUtentesPorMedicamento(){
        Map<String,Integer> aux = new HashMap<>();

        for(Utente u : utentes.values()){
            for(ListaMedicamentosUtente l : u.getListaMedicamentos()){
                if(aux.containsKey(l.getMedicamento().getNome())){
                    int i = aux.get(l.getMedicamento().getNome()) + 1;
                    aux.put(l.getMedicamento().getNome(),i);
                }else{
                    aux.put(l.getMedicamento().getNome(),1);
                }
            }
        }

        List<UtentesPorMedicamento> list = new ArrayList<>();
        for(String s : aux.keySet()){
            list.add(new UtentesPorMedicamento(s,aux.get(s)));
        }
        return list;
    }
}
