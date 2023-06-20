package pt.isec.tp_gps.model.fsm;


import pt.isec.tp_gps.model.data.*;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MEContexto {

    Principal data;
    IMEState state;

    public MEContexto() {
        data = new Principal();
        state = MEEstados.ESTADOINICIAL.createState(this,data);
    }

    public Principal getData() {
        return data;
    }

    public void setData(Principal data) {
        this.data = data;
        System.out.println(this.data.getUtentes());
    }

    void changeState(IMEState state) {
        this.state = state;
    }
    public MEEstados getState() {
        return state.getState();
    }


    public void homePage(){state.homePage();}
    public void personPage(){state.personPage();}
    public void pillPage(){state.pillPage();}



    public boolean addPerson(Utente aux){return state.addPerson(aux);}

    public boolean removePerson(String cc){return state.removePerson(cc);}
    public List<Utente> getUtentes(){
        return data.getUtentes();
    }

    public Map<String,List<Medicamento>> medicamentosATomar(Calendar c, int limSup, int limInf){
        return data.tomarMedicamento(c,limSup,limInf);
    }


    public boolean validaHoras(String horas){
        return data.validaHoras(horas);
    }
    public boolean addPill(Medicamento aux){return state.addPill(aux);}

    public boolean removePill(int id){return state.removePill(id);}

    public void next() {
        state.next();
    }

    public void previous() {
        state.previous();
    }

    public void changeMessage(String msg) {
        state.changeMessage(msg);
    }

    public void changeNumber(int nr) {
        state.changeNumber(nr);
    }

    public void encomendasPage() {state.encomendasPage(); }


    public boolean addEncomenda(Encomenda aux) {
        return state.addEncomenda(aux);
    }

    public boolean adicionarUtente(List<String> dadosUtente, Calendar dataNascimento, List<String> alergias,
                                   List<String> doencas, List<MedUtente> listaMedicamentosUtentes) {
        return data.adicionarUtente(dadosUtente,dataNascimento,alergias,doencas,listaMedicamentosUtentes);
    }

    public boolean removerUtente(String cc) {
        return data.removerUtente(cc);
    }
    public boolean removerUtenteSemHistorico(String cc) {
        return data.removerUtenteSemHistorico(cc);
    }

    public boolean editarUtente(Utente utenteAnterior, Utente utenteEditado) {
        return data.editarUtente(utenteAnterior,utenteEditado);
    }

    public Utente getUtentesCC(String CC) {
        return data.getUtenteCC(CC);
    }

    public void adicionarUtente(Utente utente){
        data.adicionarUtente(utente);
    }

    public List<Utente> getUtenteNome(String nome) {
        return data.getUtenteNome(nome);
    }

    public boolean adicionarEncomendas(String quantidade,String nomeMedicamento) {
        return data.adicionarEncomendas(quantidade,nomeMedicamento);
    }

    public boolean adicionarEncomendas(List<EncomendaTableData> aux) {
        return data.adicionarEncomendas(aux);
    }

    public String idProximaEncomenda() {
        return data.idProximaEncomenda();
    }

    public boolean removerEncomendas(Encomenda encomenda) {
        return data.removerEncomendas(String.valueOf(encomenda.getIDEncomenda()),encomenda.getNomeMedicamento());
    }

    public List<Encomenda> getEncomendas() {
        return data.getEncomendas();
    }

    public boolean editarMedicamento(Medicamento medicamentoAnterior, Medicamento medicamentoEditado) {
        return data.editarMedicamento(medicamentoAnterior,medicamentoEditado);
    }

    public List<Medicamento> getMedicamentos() {
        return data.getMedicamentos();
    }

    public Medicamento getMedicamentoID(String id) {
        return data.getMedicamentoID(id);
    }

    public List<Medicamento> getMedicamentoNome(String nome) {
        return data.getMedicamentoNome(nome);
    }

    public List<UtentesPorMedicamento> getNUtentesPorMedicamento() {
        return data.getNUtentesPorMedicamento();
    }

    public boolean adicionarMedicamento(List<String> dadosMedicamento, Calendar dataValidade) {
        return data.adicionarMedicamento(dadosMedicamento,dataValidade);
    }

    public boolean removerMedicamento(String id) {
        return data.removerMedicamento(id);
    }

    public Map<Integer, Integer> getNumeroUtentesPorMes() {
        return data.getNumeroUtentesPorMes();
    }

    public List<String> getHistoricoUtentes() {
        return data.getHistoricoUtentes();
    }

    public boolean existeMedicamentoNome(String nome) {
        return data.existeMedicamentoNome(nome);
    }


}
