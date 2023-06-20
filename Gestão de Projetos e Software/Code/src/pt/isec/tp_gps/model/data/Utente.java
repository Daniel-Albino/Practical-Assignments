package pt.isec.tp_gps.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String CC;
    private String Nome;
    private Calendar DataNascimento;
    private int idade;
    private String Email;
    private int TelefoneEme;
    private String Sexo;
    private String Ocupacao;
    private List<String> Alergias;
    private List<String> Doencas;
    private List<ListaMedicamentosUtente> UMedicamento;

    public Utente(String CC, String nome, Calendar dataNascimento, String email, int telefoneEme,
                  String sexo, String ocupacao, List<String> alergias, List<String> doencas,
                  List<ListaMedicamentosUtente> UMedicamento) {
        this.CC = CC;
        Nome = nome;
        DataNascimento = dataNascimento;
        Email = email;
        TelefoneEme = telefoneEme;
        Sexo = sexo;
        Ocupacao = ocupacao;
        Alergias = alergias;
        Doencas = doencas;
        this.UMedicamento = UMedicamento;
        setIdade(dataNascimento);
    }

    public int getIdade() {
        return idade;
    }

    private void setIdade(Calendar dataNascimento) {
        Calendar dataAtual = Calendar.getInstance();
        int idade = dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);
        if ((dataNascimento.get(Calendar.MONTH) > dataAtual.get(Calendar.MONTH)) ||
                (dataNascimento.get(Calendar.MONTH) == dataAtual.get(Calendar.MONTH) &&
                        dataNascimento.get(Calendar.DAY_OF_MONTH) > dataAtual.get(Calendar.DAY_OF_MONTH))) {
            idade--;
        }
        this.idade = idade;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Calendar getDateNascimento() {
        return DataNascimento;
    }

    public void setDateNascimento(Calendar dateNascimento) {
        DataNascimento = dateNascimento;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getTelefoneEme() {
        return TelefoneEme;
    }

    public void setTelefoneEme(int telefoneEme) {
        TelefoneEme = telefoneEme;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getOcupacao() {
        return Ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        Ocupacao = ocupacao;
    }

    public List<String> getAlergias() {
        return Alergias;
    }

    public void setAlergias(List<String> alergias) {
        Alergias = alergias;
    }

    public List<String> getDoencas() {
        return Doencas;
    }

    public void setDoencas(List<String> doencas) {
        Doencas = doencas;
    }

    public boolean adicionarMedicamento(Medicamento medicamento,List<Calendar> hmedicamento){

        for(int i = 0;i<UMedicamento.size();i++){
            if(UMedicamento.get(i).getMedicamento().equals(medicamento))
                return false;
        }
        ListaMedicamentosUtente a = new ListaMedicamentosUtente(medicamento,hmedicamento);
        UMedicamento.add(a);
        return true;
    }

    public boolean removerMedicamento(Medicamento medicamento){
        for(int i = 0;i<UMedicamento.size();i++){
            if(UMedicamento.get(i).getMedicamento().equals(medicamento)) {
                UMedicamento.remove(i);
                return true;
            }
        }
        return false;
    }


    public boolean editarMedicamento(ListaMedicamentosUtente listaMedicamentosUtenteAnterior, ListaMedicamentosUtente listaMedicamentosUtenteEditado){

        for(int i = 0;i<UMedicamento.size();i++){
            if(listaMedicamentosUtenteAnterior.getMedicamento().equals(listaMedicamentosUtenteEditado.getMedicamento())){
                UMedicamento.remove(i);
                UMedicamento.add(listaMedicamentosUtenteEditado);
                return true;
            }
        }
        return false;
    }
    public List<ListaMedicamentosUtente> getListaMedicamentos(){
        return UMedicamento;
    }
    public List<MedUtente> getListaMedicamentosMedUtente(){
        List<MedUtente> aux = new ArrayList<>();

        for(ListaMedicamentosUtente l : UMedicamento)
            aux.add(new MedUtente(l.getNomeMedicamento(),l.getHoras()));
        if(aux.size() == 0)
            return null;
        return aux;
    }


    public String getHorasMedicamento(String nomeMed,int limH, int limsupM, int liminfM){
        if(liminfM == 0)
            limH += 1;
        List<Calendar> calendarList = new ArrayList<>();
        for(ListaMedicamentosUtente l : UMedicamento){
            if(l.getMedicamento().getNome().toUpperCase().equals(nomeMed.toUpperCase())) {
                for (Calendar calendar : l.getHoraMedicamento())
                    calendarList.add(calendar);
            }
        }
        String hora = null;
        for(Calendar calendar : calendarList){
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);

            if(h == limH){
                if(m <= limsupM && m >= liminfM){
                    hora = h + ":" + m + ";";
                }
            }
        }
        return hora;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "CC='" + CC + '\'' +
                ", Nome='" + Nome + '\'' +
                ", Date= " +
                DataNascimento.get(Calendar.HOUR_OF_DAY) + ":" +  DataNascimento.get(Calendar.MINUTE) + ":" + DataNascimento.get(Calendar.SECOND) +
                " " + DataNascimento.get(Calendar.DATE) + "/" + DataNascimento.get(Calendar.MONTH) +"/"+ DataNascimento.get(Calendar.YEAR) +
                ", Email='" + Email + '\'' +
                ", TelefoneEme=" + TelefoneEme +
                ", Sexo='" + Sexo + '\'' +
                ", Ocupacao='" + Ocupacao + '\'' +
                ", Alergias=" + Alergias +
                ", Doencas=" + Doencas +
                ", UMedicamento=" + UMedicamento + "}\n";
    }

    public String getDataNascimentoString(){
        Calendar calendar1 = getDateNascimento();
        int year = calendar1.get(Calendar.YEAR);

        String auxMonth = calendar1.get(Calendar.MONTH)+"";
        int month = calendar1.get(Calendar.MONTH);
        month++;
        if(auxMonth.length() == 1)
            auxMonth = "0"+month;

        String auxDay = calendar1.get(Calendar.DAY_OF_MONTH)+"";
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        if(auxDay.length() == 1)
            auxDay = "0"+day;

        String date = auxDay + "/" + auxMonth + "/" + year;
        return date;
    }
}
