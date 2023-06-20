package pt.isec.tp_gps.model;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import pt.isec.tp_gps.model.data.*;
import pt.isec.tp_gps.model.fsm.MEContexto;
import pt.isec.tp_gps.model.fsm.MEEstados;
import pt.isec.tp_gps.model.threads.ThreadAlertaMedicamentos;
import pt.isec.tp_gps.model.threads.ThreadAux;
import pt.isec.tp_gps.model.threads.ThreadCalcTotalMed;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.List;
import java.util.*;

public class Decorator {
    MEContexto context;
    PropertyChangeSupport pcs;
    List<EncomendaTableData> tempEncomendaTableData;
    Utente tempUtente;
    List<MedUtente> tempMedUtente;
    List<String> tempAlergia;
    List<String> tempDoenca;
    ThreadAlertaMedicamentos threadAlertaMedicamentos;
    Map<String,List<TomaMedicamento>> utentesEMedicamentosTemp;

    public Decorator() {
        this.context = new MEContexto();
        this.tempMedUtente = new ArrayList<>();
        this.tempDoenca = new ArrayList<>();
        this.tempAlergia = new ArrayList<>();
        this.tempEncomendaTableData = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
        utentesEMedicamentosTemp = new HashMap<>();
        load();
    }

    public Map<String, List<TomaMedicamento>> getUtentesEMedicamentos() {
        return utentesEMedicamentosTemp;
    }
    public void removeUtentesEMedicamentos(TomaMedicamento tomaMedicamento){
        List<Medicamento> medicamento = getMedicamentoNome(tomaMedicamento.getNomeMedicamento());
        System.out.println(medicamento);
        if(medicamento.get(0).getStock() == 0)
            return;
        medicamento.get(0).decrementStock();
        removeUtenteThread(tomaMedicamento.getCC());
        if(medicamento.get(0).getStock() == 0) {
            ThreadCalcTotalMed t = new ThreadCalcTotalMed(this);
            t.start();
        }
    }

    public synchronized void removeUtenteThread(String cc){
        ThreadAux threadAux = new ThreadAux(utentesEMedicamentosTemp,cc);
        threadAux.start();
    }
    public MEContexto getContext() {
        return context;
    }

    public List<String> getTempAlergia() {
        return tempAlergia;
    }

    public void setTempMedUtente(List<MedUtente> tempMedUtente) {
        this.tempMedUtente = tempMedUtente;
    }

    public void addTempMedList(MedUtente medUtente){
        String []horas = medUtente.getTempHoras().split(";");
        System.out.println(horas[0]);
        List<MedUtente> auxTempMedUtente = new ArrayList<>(tempMedUtente);
        if(auxTempMedUtente.size() == 0){
            auxTempMedUtente.add(new MedUtente(medUtente.getTempMed(),horas[0] + ";"));
        }
        boolean medAdd = false;
        for(MedUtente a : tempMedUtente) {
            for(String s : horas) {
                String h = s + ";";
                if (a.getTempMed().toUpperCase().equals(medUtente.getTempMed().toUpperCase()) && !a.getTempHoras().contains(h)){
                    MedUtente auxMedUtente = a;
                    auxTempMedUtente.remove(auxMedUtente);
                    auxMedUtente.setTempHoras(auxMedUtente.getTempHoras() + h);
                    auxTempMedUtente.add(auxMedUtente);
                    medAdd = true;
                }
            }
        }

        if(!medAdd){
            medAdd = false;
            for(MedUtente a : auxTempMedUtente) {
                if(!a.getTempMed().toUpperCase().equals(medUtente.getTempMed().toUpperCase()))
                    medAdd = true;
                else
                    medAdd = false;
            }
            if(medAdd){
                String h = "";
                for(String s : horas) {
                    h += s + ";";
                }
                medUtente.setTempHoras(h);
                auxTempMedUtente.add(medUtente);
            }

        }


        tempMedUtente = new ArrayList<>(auxTempMedUtente);
    }

    public void removeTempMedUtente(MedUtente selectedItem){
        tempMedUtente.remove(selectedItem);
    }

    public void setTempAlergia(List<String> tempAlergia) {
        this.tempAlergia = tempAlergia;
    }

    public void setTempDoenca(List<String> tempDoenca) {
        this.tempDoenca = tempDoenca;
    }

    public void adicionarUtente(Utente utente){
        context.adicionarUtente(utente);
    }

    public void addTempAlergia(String aux) {
        for(String s : tempAlergia) {
            if (s.toUpperCase().equals(aux.toUpperCase()))
                return;
        }
        this.tempAlergia.add(aux);
    }

    public void removeTempAlergia(String aux) {
        tempAlergia.remove(aux);
    }

    public void removeTempDoencas(String aux) {
        tempDoenca.remove(aux);
    }

    public List<String> getTempDoenca() {
        return tempDoenca;
    }

    public void addTempDoenca(String aux) {
        for(String s : tempDoenca)
            if(s.toUpperCase().equals(aux.toUpperCase()))
                return;

        this.tempDoenca.add(aux);
    }

    public List<MedUtente> getTempMedUtente() {
        return tempMedUtente;
    }

    public void setTempMedUtente(MedUtente aux) {
        this.tempMedUtente.add(aux);
    }

    public List<EncomendaTableData> getTempEncomendaTableData() {
        return tempEncomendaTableData;
    }

    public void setTempEncomendaTableData(List<EncomendaTableData> tempEncomendaTableData) {
        this.tempEncomendaTableData = tempEncomendaTableData;
    }

    public void addTempListEncomenda(EncomendaTableData encomendaTableData){
        if(getMedicamentoNome(encomendaTableData.getMedicamento()).size() != 1)
            return;
        try {
            Integer.parseInt(encomendaTableData.getQuantidade());
        }catch (NumberFormatException e){
            return;
        }
        List<EncomendaTableData> aux = new ArrayList<>(tempEncomendaTableData);
        if(aux.size() == 0){
            if (getMedicamentoNome(encomendaTableData.getMedicamento()).size() == 1)
                aux.add(encomendaTableData);
        }else {
            for (EncomendaTableData e : aux) {
                if (e.getMedicamento().toUpperCase().equals(encomendaTableData.getMedicamento().toUpperCase())) {
                    int q = Integer.parseInt(e.getQuantidade());
                    q += Integer.parseInt(encomendaTableData.getQuantidade());
                    e.setQuantidade(String.valueOf(q));
                }
                if (!e.getMedicamento().toUpperCase().equals(encomendaTableData.getMedicamento().toUpperCase())
                        && getMedicamentoNome(encomendaTableData.getMedicamento()).size() == 1) {
                    aux.add(encomendaTableData);
                }
            }
        }
        tempEncomendaTableData = new ArrayList<>(aux);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {//String Property
        pcs.addPropertyChangeListener(listener);
    }

    public MEEstados getState() {
        return context.getState();
    }
    public Utente getTempUtente() {
        return tempUtente;
    }

    public void setTempUtente(Utente tempUtente) {
        this.tempUtente = tempUtente;
    }

    public void updateUI(){
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void next() {
        context.next();
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void previous() {
        context.previous();
        pcs.firePropertyChange(null,null,context.getState());
    }




    //----------------------------Principal-------------------------------------//

    public boolean adicionarUtente(List<String> dadosUtente, Calendar dataNascimento, List<String> alergias, List<String> doencas,
                                   List<MedUtente> listaMedicamentosUtentes){
        boolean bol = context.adicionarUtente(dadosUtente,dataNascimento,alergias,doencas,listaMedicamentosUtentes);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public boolean removerUtente(String CC){
        boolean bol = context.removerUtente(CC);
        if(bol)
            removeUtenteThread(CC);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }
    public boolean removerUtenteSemHistorico(String cc) {
        boolean bol = context.removerUtenteSemHistorico(cc);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public boolean editarUtente(Utente utenteAnterior,Utente utenteEditado){
        boolean bol = context.editarUtente(utenteAnterior,utenteEditado);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }
    public boolean existeMedicamentoNome(String nome){
        return context.existeMedicamentoNome(nome);
    }


    public List<Utente> getUtentes(){
        return context.getUtentes();
    }

    public Utente getUtenteCC(String CC){
        return context.getUtentesCC(CC);
    }

    public List<Utente> getUtenteNome(String nome){
        return context.getUtenteNome(nome);
    }

    public Map<String,List<Medicamento>> tomarMedicamento(Calendar calendar, int limiteSup, int limiteInf){
        return context.medicamentosATomar(calendar,limiteSup,limiteInf);
    }

    public boolean adicionarEncomendas(String quantidade,String nomeMedicamento){
        boolean bol = context.adicionarEncomendas(quantidade,nomeMedicamento);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public boolean adicionarEncomendas(List<EncomendaTableData> aux){
        boolean bol = context.adicionarEncomendas(aux);
        if(bol)
            gerarPDF(aux);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public String idProximaEncomenda(){
        return context.idProximaEncomenda();
    }


    public boolean removerEncomendas(Encomenda encomenda){
        boolean bol = context.removerEncomendas(encomenda);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }


    public List<Encomenda> getEncomendas(){
        return context.getEncomendas();
    }

    public boolean adicionarMedicamento(List<String> dadosMedicamento, Calendar DataValidade) {
        boolean bol = context.adicionarMedicamento(dadosMedicamento,DataValidade);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public boolean removerMedicamento(String id){
        boolean bol = context.removerMedicamento(id);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public boolean editarMedicamento(Medicamento medicamentoAnterior,Medicamento medicamentoEditado){
        boolean bol = context.editarMedicamento(medicamentoAnterior,medicamentoEditado);
        pcs.firePropertyChange(null,null,context.getData());
        return bol;
    }

    public List<Medicamento> getMedicamentos(){
        return context.getMedicamentos();
    }

    public Medicamento getMedicamentoID(String ID){
        return context.getMedicamentoID(ID);
    }

    public List<Medicamento> getMedicamentoNome(String nome){
        return context.getMedicamentoNome(nome);
    }

    public List<UtentesPorMedicamento> getNUtentesPorMedicamento(){
        return context.getNUtentesPorMedicamento();
    }

    public Map<Integer, Integer> getNumeroUtentesPorMes() {
        return context.getNumeroUtentesPorMes();
    }

    public List<String> getHistoricoUtentes() {
        return context.getHistoricoUtentes();
    }

    //-----------------------------------------------------------------//

    //TODO::
    //=======================Utente=========================
    //======================================================
    public boolean validaHoras(String horas){
        return context.validaHoras(horas);
    }


    public List<Utente> HistoricoUtentes(){
        List<Utente> aux = new ArrayList<>();

        ArrayList<String> a = new ArrayList<>();
        a.add("array-a1");
        a.add("array-a2");
        ArrayList<String> b = new ArrayList<>();
        a.add("array-b1");
        a.add("array-b2");
        ArrayList<String> c = new ArrayList<>();
        a.add("array-a1");
        a.add("array-a2");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,1998);
        aux.add(new Utente("125125","Manel", calendar,"ade@gmail.com",91656466,"masculino","something",a,b,null));
        aux.add(new Utente("12725","Joao", calendar,"joa@gmail.com",9165766,"masculino","something",a,b,null));
        return aux;
    }

    public List<Utente> DosageUtente(){
        List<Utente> aux = new ArrayList<>();

        ArrayList<String> a = new ArrayList<>();
        a.add("array-a1");
        a.add("array-a2");
        ArrayList<String> b = new ArrayList<>();
        a.add("array-b1");
        a.add("array-b2");
        ArrayList<String> c = new ArrayList<>();
        a.add("array-a1");
        a.add("array-a2");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,1998);
        aux.add(new Utente("125125","Manel", calendar,"ade@gmail.com",91656466,"masculino","something",a,b,null));
        aux.add(new Utente("12725","Joao", calendar,"joa@gmail.com",9165766,"masculino","something",a,b,null));
        return aux;
    }
    public boolean removeUtente(Utente _oldUtente){
        return true;
    }

    public boolean adicionaUtente(Calendar dataNasci,List<String> alergias,List<String> doencas,List<String> Med,List<String> fields){
        //TODO::call function from model data
        return false;
    }

    public List<Utente> getAllUtentes(){
        List<Utente> aux = new ArrayList<>();
        return aux;
    }

    //=======================Utente=========================
    //======================================================
    //TODO::

    public void homePage(){
        context.homePage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void personPage(){
        context.personPage();
        pcs.firePropertyChange(null,null,context.getState());
    }
    public void pillPage(){
        context.pillPage();
        pcs.firePropertyChange(null,null,context.getState());
    }

    public void encomendasPage(){
        context.encomendasPage();
        pcs.firePropertyChange(null,null,context.getState());
    }



    public boolean addPerson(Utente aux){
        return context.addPerson(aux);
    }

    public boolean removePerson(String cc){
        return context.removePerson(cc);
    }

    public boolean addPill(Medicamento aux){
        return context.addPill(aux);
    }


    public boolean removePill(int id){
        return context.removePill(id);
    }



    public void changeMessage(String msg) {
        context.changeMessage(msg);
        pcs.firePropertyChange(null,null,null);
    }

    public void changeNumber(int nr) {
        context.changeNumber(nr);
        pcs.firePropertyChange(null,null,null);
    }

    public void serializaObjeto() {
        Principal a = context.getData();

        try {
            FileOutputStream fileOut =
                    new FileOutputStream("./serializeFiles/Dados.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(a);
            out.close();
            fileOut.close();
            System.out.printf("Objeto serializado com sucesso na diretoria ./serializeFiles/Dados.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
        threadAlertaMedicamentos.stop();
    }

    public void load() {
        Principal a = null;
        try {
            FileInputStream fileIn = new FileInputStream("./serializeFiles/Dados.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            a = (Principal) in.readObject();
            in.close();
            fileIn.close();
            context.setData(a);
            System.out.println("Leitura de objeto serializado");
        } catch (FileNotFoundException e){
            System.out.println("Sem ficheiro!");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Sem objetos serializados");
            c.printStackTrace();
        }

        threadAlertaMedicamentos = new ThreadAlertaMedicamentos(context,utentesEMedicamentosTemp);
        threadAlertaMedicamentos.start();
    }

    public boolean addEncomenda(Encomenda aux){
        return context.addEncomenda(aux);
    }


    public void gerarPDF(List<EncomendaTableData> list){

        Document doc = new Document();
        try
        {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int s = calendar.get(Calendar.SECOND);
            String nome = "pdfEncomenda_" + d + "_" + m + "_" + y + "_" + h + "_" + min + "_" + s +".pdf";
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("./PDFs/" + nome));
            System.out.println("PDF created.");
            doc.open();
            doc.addTitle("Nota de Encomenda " + d + "/" + m + "/" + y + " - " + h + ":" + min + ":" + s);
            doc.setMargins(20,20,20,20);
            doc.add(new Phrase("Nota de Encomenda " + d + "/" + m + "/" + y + " - " + h + ":" + min + ":" + s));
            doc.add(new LineSeparator());
            doc.add(new Paragraph("\n\n\nProdutos:"));
            ListItem a = new ListItem();
            for(EncomendaTableData b : list) {
                a.add(new Chunk("Medicamento: " + b.getMedicamento() + "        Quantidade: " + b.getQuantidade()+"\n"));
            }
            doc.add(a);
            doc.close();
            writer.close();


        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


}
