package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.data.memento.IMemento;
import pt.isec.pa.apoio_poe.model.data.memento.IOriginator;
import pt.isec.pa.apoio_poe.model.data.memento.Memento;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Data implements Serializable, IOriginator {
    private Map<Long, Alunos> alunos;
    private Map<String, Docentes> docentes;
    private Map<String, Proposta> propostas;
    private Map<Long, List<String>> candidaturas;
    private Map<String, Long> atPropostas;
    private List<Long> idAlunosEmpatados;
    private Map<String,Map<String,Long>> atOrientadores;
    private Map<GPEState,Boolean> faseFechada;

    public Data() {
        alunos = new HashMap<>();
        docentes = new HashMap<>();
        propostas = new HashMap<>();
        candidaturas = new HashMap<>();
        atPropostas = new HashMap<>();
        idAlunosEmpatados = new ArrayList<>();
        atOrientadores = new HashMap<>();
        faseFechada = new HashMap<>();
        initFaseFechada();
    }
    private void initFaseFechada() {
        faseFechada.put(GPEState.CONFIGURACAO,false);
        faseFechada.put(GPEState.OPCOES_CANDIDATURA,false);
        faseFechada.put(GPEState.ATRIBUICAO_PROPOSTAS,false);
        faseFechada.put(GPEState.ATRIBUICAO_ORIENTADORES,false);
    }

    //ALUNOS
    private Erros erroAluno;
    public Erros addAluno(List<String> values) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        Alunos aluno = aceitaAluno(values);
        if (aluno == null)
            return erroAluno;
        if (alunos.containsKey(aluno.getNumero())) {
            return Erros.ALUNO_EXISTE;
        }
        alunos.put(aluno.getNumero(), aluno);
        return Erros.NONE;
    }
    private Alunos aceitaAluno(List<String> values) {
        if (values.size() != 7) {
            erroAluno = Erros.ALUNO_ADD;
            return null;
        }
        long numeroAluno = 0;
        String nomeAluno;
        String emailAluno = "";
        String curso = "";
        String ramo = "";
        double classificacao = -1;
        boolean aptoEstagio = false;

        try {
            numeroAluno = Long.parseLong(values.get(0));
        } catch (Exception e) {
            erroAluno = Erros.ALUNO_ID;
            return null;
        }

        nomeAluno = values.get(1);

        String emailaux = "a" + values.get(0) + "@isec.pt";
        if (values.get(2).equals(emailaux))
            emailAluno = values.get(2);
        else {
            erroAluno = Erros.ALUNO_EMAIL;
            return null;
        }

        if (values.get(3).equalsIgnoreCase("LEI") || values.get(3).equalsIgnoreCase("LEI-PL")) {
            curso = values.get(3).toUpperCase();
        } else {
            erroAluno = Erros.ALUNO_CURSO;
            return null;
        }

        if (values.get(4).equalsIgnoreCase("DA") ||
                values.get(4).equalsIgnoreCase("SI") ||
                values.get(4).equalsIgnoreCase("RAS")) {
            ramo = values.get(4).toUpperCase();
        } else {
            erroAluno = Erros.ALUNO_RAMO;
            return null;
        }

        try {
            classificacao = Double.parseDouble(values.get(5));
        } catch (Exception e) {
            erroAluno = Erros.ALUNO_CLASSIFICACAO;
            return null;
        }

        if(classificacao > 1) {
            erroAluno = Erros.ALUNO_CLASSIFICACAO;
            return null;
        }

        if ("true".equalsIgnoreCase(values.get(6)))
            aptoEstagio = true;
        else if ("false".equalsIgnoreCase(values.get(6)))
            aptoEstagio = false;
        else {
            erroAluno = Erros.ALUNO_ESTAGIO;
            return null;
        }
        return new Alunos(numeroAluno, nomeAluno, emailAluno, curso, ramo, classificacao, aptoEstagio);
    }
    public List<Alunos> getListAlunos(){
        if (alunos.isEmpty())
            return null;
        return new ArrayList<>(alunos.values());
    }
    private void removeAlunoAtPropostasOrientadores(long nAluno){

        String auxP = "";
        String auxO = "";

        for(Map.Entry<String, Long> at : atPropostas.entrySet()) {
            if (at.getValue() == nAluno) {
                auxP = at.getKey();
                for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()) {
                    if (atO.getValue().equals(at)){
                        auxO = atO.getKey();
                    }
                }
            }
        }

        if(!auxP.isBlank())
            atPropostas.remove(auxP);

        if(!auxO.isBlank())
            atOrientadores.remove(auxO);

    }
    public Erros removeAluno(long nAluno) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if(!alunos.containsKey(nAluno))
            return Erros.VALOR_INCORRETO;

        if(!alunos.remove(nAluno, alunos.get(nAluno)))
            return Erros.ALUNO_REMOVE;

        candidaturas.remove(nAluno);

        removeAlunoAtPropostasOrientadores(nAluno);

        return Erros.NONE;
    }
    public Erros setNomeAluno(long n_aluno, String novoNome) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (novoNome.isBlank())
            return Erros.ALUNO_EDIT_NOME;

        if (!alunos.containsKey(n_aluno)) {
            return Erros.ALUNO_ID;
        }
        Alunos aluno_aux = alunos.get(n_aluno);
        aluno_aux.setNome(novoNome);
        alunos.put(n_aluno, aluno_aux);
        return Erros.NONE;

    }
    public Erros setClassificacao(long n_aluno, String novaNota) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (novaNota.isBlank())
            return Erros.ALUNO_EDIT_CLASSIFICAO;

        double nota = -1;

        try {
            nota = Double.parseDouble(novaNota);
        } catch (Exception e) {
            return Erros.ALUNO_CLASSIFICACAO;
        }

        if (nota < 0)
            return Erros.ALUNO_EDIT_CLASSIFICAO;

        if (!alunos.containsKey(n_aluno)) {
            return Erros.ALUNO_ID;
        }

        Alunos aluno_aux = alunos.get(n_aluno);
        aluno_aux.setClassificacao(nota);
        alunos.put(n_aluno, aluno_aux);
        return Erros.NONE;

    }
    public Erros setAptoEstagio(long nAluno) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (!alunos.containsKey(nAluno)) {
            return Erros.ALUNO_ID;
        }

        Alunos aluno_aux = alunos.get(nAluno);
        aluno_aux.setAptoEstagio();
        alunos.put(nAluno, aluno_aux);

        if(!aluno_aux.getApto()){
            removeAlunoAtPropostasOrientadores(nAluno);
        }

        return Erros.NONE;
    }
    public List<Alunos> getAlunos() {
        return List.copyOf(alunos.values());
    }
    public String getAluno(long nAluno) {
        if (alunos.get(nAluno) != null)
            return alunos.get(nAluno).toString();
        return "Não existe nenhum aluno com esse número de aluno!";
    }
    public Alunos getAlunoNumber(long nAluno) {
        if (alunos.containsKey(nAluno)) {
            return alunos.get(nAluno);
        }
        return null;
    }
    public String getTodosAlunos() {
        StringBuilder sb = new StringBuilder("Alunos:\n");
        if (alunos == null || alunos.size() == 0)
            sb.append("Não existem alunos no sistema!");
        else
            for (Alunos aluno : alunos.values())
                sb.append(String.format("\t- %s\n", aluno.toString()));
        return sb.toString();
    }
    public int numeroAlunos(){
        return alunos.size();
    }

    //DOCENTES
    public Erros addDocente(List<String> list) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (list.size() != 2) {
            return Erros.DOCENTE_ADD;
        }
        if (!list.get(1).contains("@isec.pt"))
            return Erros.DOCENTE_EMAIL;

        if (docentes.containsKey(list.get(1))) {
            return Erros.DOCENTES_EXISTE;
        }

        docentes.put(list.get(1), new Docentes(list.get(0), list.get(1)));
        return Erros.NONE;
    }
    public Erros removeDocente(String email) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (email.isBlank())
            return Erros.DOCENTE_REMOVE;

        if(!docentes.containsKey(email))
            return Erros.DOCENTE_REMOVE;

        atOrientadores.remove(email);

        String auxP = "";
        for(Proposta p : propostas.values()){
            if(p.getTipo().equals("T2") && p.getEmailDocente().equals(email))
                auxP = p.getCodId();
        }

        removeProposta(auxP);
        docentes.remove(email, docentes.get(email));
        return Erros.NONE;
    }
    public String getNomeDocente(String email){
        if(!docentes.containsKey(email))
            return "Não existe nenhum docente com o email " + email + "!";
        return docentes.get(email).getNomeDocente();
    }
    public Erros setNomeDocente(String email, String novoNome) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (email.isBlank() || novoNome.isBlank())
            return Erros.DOCENTE_EDIT;

        if (!docentes.containsKey(email)) {
            return Erros.DOCENTE_EMAIL;
        }

        Docentes docente_aux = docentes.get(email);
        docente_aux.setNomeDocente(novoNome);
        docentes.put(email, docente_aux);
        return Erros.NONE;
    }
    public List<Docentes> getDocentes() {
        return List.copyOf(docentes.values());
    }
    public String getDocente(String emailDoc) {
        if (docentes.containsKey(emailDoc)) {
            return docentes.get(emailDoc).toString();
        }
        return "Não existe nenhum docente com esse e-mail!";
    }
    public Docentes getDocenteEmail(String email){
        if(docentes.containsKey(email))
            return docentes.get(email);
        return null;
    }
    public String getTodosDocentes() {
        StringBuilder sb = new StringBuilder("Docentes:\n");
        if (docentes == null || docentes.size() == 0)
            sb.append("Não existem docentes no sistema!");
        else
            for (Docentes docente : docentes.values())
                sb.append(String.format("\t- %s\n", docente.toString()));
        return sb.toString();
    }

    //PROPOSTA
    private Erros erroPorposta;
    public Erros addProposta(List<String> values) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        Proposta novaProposta = aceitaProposta(values);

        if (novaProposta == null) {
            return erroPorposta;
        }

        if (propostas.containsKey(novaProposta.getCodId())) {
            return Erros.PROPOSTA_ADD;
        }
        propostas.put(novaProposta.getCodId(), novaProposta);

        return Erros.NONE;
    }
    private final List<String> ramos = new ArrayList<>(Arrays.asList("DA", "SI", "RAS", "DA|SI", "DA|RAS", "SI|DA", "SI|RAS", "RAS|DA", "RAS|SI",
            "DA|RAS|SI", "DA|SI|RAS", "SI|DA|RAS", "SI|RAS|DA", "RAS|DA|SI", "RAS|SI|DA"));
    private Proposta aceitaProposta(List<String> values) {
        if (values.size() < 4) {
            erroPorposta = Erros.PROPOSTA_ADD;
            return null;
        }
        for (String s : values)
            if (s.isBlank()) {
                erroPorposta = Erros.PROPOSTA_ADD;
                return null;
            }
        if (propostas.containsKey(values.get(1))) { //Verifica código
            erroPorposta = Erros.PROPOSTA_ID;
            return null;
        }
        if (!values.get(0).equals("T3")) {
            if (!ramos.contains(values.get(2))) { //Verificar ramos
                erroPorposta = Erros.PROPOSTA_RAMO;
                return null;
            }
        }

        long idAluno;
        try {
            idAluno = Long.parseLong(values.get(values.size() - 1));
        } catch (Exception e) {
            idAluno = 0;
        }

        switch (values.get(0)) {
            case "T1" -> {
                if (idAluno != 0) {
                    if (!alunos.containsKey(idAluno) || !alunos.get(idAluno).getApto() || !values.get(2).contains(alunos.get(idAluno).getRamo())) {
                        erroPorposta = Erros.PROPOSTA_ADD;
                        return null;
                    }
                    addAlunosAtribuidos(new Estagios(values.get(1), values.get(2), values.get(3), values.get(4), idAluno));
                }
                return new Estagios(values.get(1), values.get(2), values.get(3), values.get(4), idAluno);
            }
            case "T2" -> {

                if (!docentes.containsKey(values.get(4))) {
                    erroPorposta = Erros.PROPOSTA_DOCENTES;
                    return null;
                }

                if (idAluno != 0) {
                    if (!alunos.containsKey(idAluno) || !alunos.get(idAluno).getApto() || !values.get(2).contains(alunos.get(idAluno).getRamo())) {
                        erroPorposta = Erros.PROPOSTA_ADD;
                        return null;
                    }
                    addAlunosAtribuidos(new Projetos(values.get(1), values.get(2), values.get(3), values.get(4), idAluno));
                }
                return new Projetos(values.get(1), values.get(2), values.get(3), values.get(4), idAluno);
            }
            case "T3" -> {
                if (idAluno != 0) {
                    if (!alunos.containsKey(idAluno) || !alunos.get(idAluno).getApto()) {
                        erroPorposta = Erros.PROPOSTA_ADD;
                        return null;
                    }
                }
                Autoproposto aux = new Autoproposto(values.get(1), values.get(2), alunos.get(idAluno));
                addAlunosAtribuidos(aux);
                return aux;
            }
        }
        return null;
    }
    public Erros removeProposta(String idProposta) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (idProposta.isBlank())
            return Erros.PROPOSTA_REMOVE_FALTA_ID;

        if(!propostas.remove(idProposta, propostas.get(idProposta)))
            return Erros.PROPOSTA_REMOVE;

        for(Map.Entry<Long, List<String>> s : candidaturas.entrySet()){
            for(int i = 0;i<s.getValue().size();i++){
                if(s.getValue().get(i).equals(idProposta))
                    candidaturas.get(s.getKey()).remove(i);
            }
        }

        List<Long> auxC = new ArrayList<>();

        for(Map.Entry<Long, List<String>> s : candidaturas.entrySet()){
            if(s.getValue().isEmpty())
                auxC.add(s.getKey());
        }

        for(Long n : auxC)
            candidaturas.remove(n);

        atPropostas.remove(idProposta);

        for(Map<String, Long> atO : atOrientadores.values()){
            atO.remove(idProposta);
        }

        return Erros.NONE;
    }
    public Erros setRamo(String idProposta, String novoRamo) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (idProposta.isBlank() || novoRamo.isBlank())
            return Erros.PROPOSTA_EDIT;

        if (!propostas.containsKey(idProposta)) {
            return Erros.PROPOSTA_EDIT_NEXISTE;
        }
        if(!ramos.contains(novoRamo))
            return Erros.PROPOSTA_RAMO;

        Proposta proposta_aux = propostas.get(idProposta);
        proposta_aux.setRamo(novoRamo);
        propostas.put(idProposta, proposta_aux);

        if(atPropostas.containsKey(idProposta) && !alunos.get(atPropostas.get(idProposta)).getRamo().equals(novoRamo)) {
            for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
                if(atO.getValue().containsKey(idProposta))
                    atOrientadores.get(atO.getKey()).remove(idProposta);
            }
            atPropostas.remove(idProposta);
        }

        return Erros.NONE;
    }
    public Erros setTitulo(String idProposta, String novoTitulo) {
        if(isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_FECHADA;
        if (idProposta.isBlank() || novoTitulo.isBlank())
            return Erros.PROPOSTA_EDIT;

        if (!propostas.containsKey(idProposta)) {
            return Erros.PROPOSTA_EDIT_NEXISTE;
        }

        Proposta proposta_aux = propostas.get(idProposta);
        proposta_aux.setTitulo(novoTitulo);
        propostas.put(idProposta, proposta_aux);
        return Erros.NONE;
    }
    public List<Proposta> getPropostas() {
        return List.copyOf(propostas.values());
    }
    public String getProposta(String idProposta) {

        if (propostas.containsKey(idProposta)) {
            return propostas.get(idProposta).toString();
        }
        return "Não existe nenhum proposta com esse código";
    }
    public Proposta getProp(String idProposta){
        if (propostas.containsKey(idProposta)) {
            return propostas.get(idProposta);
        }
        return null;
    }
    public String getTodasPropostas() {
        StringBuilder sb = new StringBuilder("Propostas:\n");
        if (propostas == null || propostas.size() == 0)
            sb.append("Não existem propostas no sistema!");
        else
            for (Proposta proposta : propostas.values())
                sb.append(String.format("\t- %s\n", proposta.toString()));
        return sb.toString();
    }
    public int numeroPropostas(){
        return propostas.size();
    }
    public long getIdAlunoProposta(String idProposta){return propostas.get(idProposta).getIdAluno();}
    public String getIdProposta(long nAluno) {
        String auxNAluno = "";
        for(Map.Entry<String, Proposta> p : propostas.entrySet()){
            if(p.getValue().getIdAluno() == nAluno)
                auxNAluno = p.getKey();
        }
        return auxNAluno;
    }

    // CANDIDATURAS
    public Erros addCandidatura(List<String> candidatura) {
        if(isFechado(GPEState.OPCOES_CANDIDATURA))
            return Erros.CANDIDATURAS_FECHADA;
        if (candidatura.size() == 1) {
            return Erros.CANDIDATURAS_ADD;
        }

        if (alunos.isEmpty() || propostas.isEmpty())
            return Erros.CANDIDATURAS_ADD;

        long idAluno;
        try {
            idAluno = Long.parseLong(candidatura.get(0));
        } catch (Exception e) {
            return Erros.VALOR_INCORRETO;
        }

        if (!alunos.containsKey(idAluno) || !alunos.get(idAluno).getApto()) //Verifica se já existe o aluno e verifica se o aluno está apto
            return Erros.CANDIDATURAS_ADD_ALUNO;

        if (candidaturas.containsKey(idAluno))
            return Erros.CANDIDATURAS_ADD_ALUNOEXISTE;

        List<String> auxP = new ArrayList<>(); //Guardar todas as propostas existentes e válidas
        for (int i = 1; i < candidatura.size(); i++) {
            for (Proposta p : propostas.values()) {
                if (candidatura.get(i).equals(p.getCodId()) && p.getIdAluno() == 0 && p.getRamo().contains(alunos.get(idAluno).getRamo())) {
                    auxP.add(candidatura.get(i));
                    break;
                }
            }
        }

        if (auxP.isEmpty())
            return Erros.CANDIDATURAS_ADD;

        candidaturas.put(idAluno, auxP);
        return Erros.NONE;
    }
    public String getTodasCandidaturas() {
        StringBuilder sb = new StringBuilder("Candidaturas\n");

        if (candidaturas == null || candidaturas.size() == 0) {
            return "Sem candidaturas disponiveis.";
        }

        for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
            sb.append(String.format("\t- Número aluno: %s, Id propostas: %s\n", pair.getKey(), pair.getValue()));
        }

        return sb.toString();
    }
    public Erros removeCandidatura(long nAluno) {
        if(isFechado(GPEState.OPCOES_CANDIDATURA))
            return Erros.CANDIDATURAS_FECHADA;
        if (candidaturas.get(nAluno) == null) {
            return Erros.CANDIDATURAS_REMOVE;
        }

        if(!candidaturas.containsKey(nAluno))
            return Erros.CANDIDATURAS_REMOVE_NEXITE;


        removeAlunoAtPropostasOrientadores(nAluno);

        candidaturas.remove(nAluno);

        return Erros.NONE;
    }
    public Erros setCandidaturas(List<String> candidatura){
        if(isFechado(GPEState.OPCOES_CANDIDATURA))
            return Erros.CANDIDATURAS_FECHADA;
        if (candidatura.size() == 1) {
            return Erros.CANDIDATURAS_EDIT_ARGUMENTOS;
        }

        if (alunos.isEmpty() || propostas.isEmpty())
            return Erros.CANDIDATURAS_EDIT;

        long idAluno;
        try {
            idAluno = Long.parseLong(candidatura.get(0));
        } catch (Exception e) {
            return Erros.VALOR_INCORRETO;
        }

        if(!candidaturas.containsKey(idAluno))
            return Erros.CANDIDATURAS_EDIT_IDALUNO;

        List<String> aux = new ArrayList<>();

        for (int i = 1; i < candidatura.size(); i++) {
            for (Proposta p : propostas.values()) {
                if (candidatura.get(i).equals(p.getCodId()) && p.getIdAluno() == 0 && p.getRamo().contains(alunos.get(idAluno).getRamo())) {
                    aux.add(candidatura.get(i));
                    break;
                }
            }
        }

        if (aux.isEmpty())
            return Erros.CANDIDATURAS_EDIT;
        candidaturas.put(idAluno, aux);
        return Erros.NONE;
    }
    public String getCandidaturas() {
        return candidaturas.toString();
    }
    public List<String> getCandidaturasList(){
        List<String> aux = new ArrayList<>();

        if (candidaturas == null || candidaturas.size() == 0) {
            aux.add("Sem candidaturas disponiveis.");
        }

        for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
            aux.add(String.format("Número de aluno: %d Nome: %s -> Candidaturas: %s\n", pair.getKey(),alunos.get(pair.getKey()).getNome(), pair.getValue()));
        }

        return aux;
    }
    public String getCandidaturasAlunoList(long nAluno){
        if(!candidaturas.containsKey(nAluno))
            return null;
         return String.format("Candidaturas do aluno %s: %s",alunos.get(nAluno).getNome(),candidaturas.get(nAluno));
    }
    public String exportCandidaturas() {
        StringBuilder sb = new StringBuilder();

        if (candidaturas == null || candidaturas.size() == 0) {
            return "Sem candidaturas disponiveis.";
        }

        for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
            sb.append(String.format("%d, %s\n", pair.getKey(), pair.getValue()));
        }

        return sb.toString();
    }
    public String getInfoCandidaturas(String type, String filtro) {
        String info = "";
        switch (type) {
            case "aluno" -> info = getCandidaturasAlunos(filtro);
            case "propostas" -> info = getCandidaturasPropostas(filtro);
        }
        if (info.isBlank())
            return "Sem informação!";
        return info;
    }

    private String getCandidaturasAlunos(String filtro) {
        StringBuilder sb = new StringBuilder();
        switch (filtro) {
            case "Com autoproposta" -> {
                for (Proposta p : propostas.values()) {
                    if (p.getTipo().equals("T3")) {
                        sb.append("\t- " + getAluno(p.getIdAluno()) + "\n");
                    }
                }
            }
            case "Com candidatura já registada" -> {
                for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
                    sb.append("\t- " + getAluno(pair.getKey()) + "\n");
                }
            }
            case "Sem candidatura já registada" -> {
                boolean aux;
                for (Alunos a : alunos.values()) {
                    aux = true;
                    for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
                        if (a.getNumero() == pair.getKey()) {
                            aux = false;
                            break;
                        }
                    }
                    if (aux) {
                        sb.append(a + "\n");
                    }
                }
                for (Proposta p : propostas.values()) {
                    if (p.getIdAluno() != 0) {
                        sb.append("\t- " + getAluno(p.getIdAluno()) + "\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getCandidaturasPropostas(String filtro) {
        if (filtro.isBlank())
            return getTodasPropostas();
        StringBuilder sb = new StringBuilder();
        switch (filtro) {
            case "Autopropostas de alunos" -> {
                for (Proposta p : propostas.values()) {
                    if (p.getTipo().equals("T3"))
                        sb.append("\t- " + p + "\n");
                }
            }
            case "Propostas de docentes" -> {
                for (Proposta p : propostas.values()) {
                    if (!p.getEmailDocente().equals("NA"))
                        sb.append("\t- " + p + "\n");
                }
            }
            case "Propostas com candidaturas" -> {
                List<String> aux = getPropostasComCandidatura();
                for (Proposta p : propostas.values()) {
                    if (aux.contains(p.getCodId()) || p.getIdAluno() != 0)
                        sb.append("\t- " + p + "\n");
                }

            }
            case "Propostas sem candidatura" -> {
                List<String> aux = getPropostasComCandidatura();
                for (Proposta p : propostas.values()) {
                    if (!aux.contains(p.getCodId()) && p.getIdAluno() == 0)
                        sb.append("\t- " + p + "\n");
                }
            }
        }
        return sb.toString();
    }
    private List<String> getPropostasComCandidatura() {
        List<String> aux = new ArrayList<>();
        for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
            for (Proposta p : propostas.values()) {
                for (int i = 0; i < pair.getValue().size(); i++)
                    if (pair.getValue().get(i).equals(p.getCodId())) {
                        if (aux.isEmpty())
                            aux.add(p.getCodId());
                        if (!aux.contains(p.getCodId()))
                            aux.add(p.getCodId());
                    }
            }
        }
        return aux;
    }

    //List Mais Info Candidaturas
    public List<String> getCandAutopropostasAlunos(){
        List<String> aux = new ArrayList<>();
        for (Proposta p : propostas.values()) {
            if (p.getTipo().equals("T3")) {
                aux.add(getAluno(p.getIdAluno()));
            }
        }
        return aux;
    }

    public List<String> getCandRegistadaAlunos(){
        List<String> aux = new ArrayList<>();
        for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
            aux.add(getAluno(pair.getKey()));
        }
        return aux;
    }

    public List<String> getSemCandRegistadaAlunos(){
        List<String> aux = new ArrayList<>();
        for (Alunos a : alunos.values()) {
            boolean bol = true;
            for (Map.Entry<Long, List<String>> pair : candidaturas.entrySet()) {
                if (a.getNumero() == pair.getKey()) {
                    bol = false;
                    break;
                }
            }
            if (bol) {
                aux.add(a + "\n");
            }
        }
        for (Proposta p : propostas.values()) {
            if (p.getIdAluno() != 0) {
                aux.add(getAluno(p.getIdAluno()));
            }
        }
        return aux;
    }

    public List<String> getCandAutopropostasProp() {
        List<String> aux = new ArrayList<>();
        for (Proposta p : propostas.values()) {
            if (p.getTipo().equals("T3"))
                aux.add(String.valueOf(p));
        }
        return aux;
    }
    public List<String> getCandDocenteProp() {
        List<String> aux = new ArrayList<>();
        for (Proposta p : propostas.values()) {
            if (!p.getEmailDocente().equals("NA"))
                aux.add(String.valueOf(p));
        }
        return aux;
    }
    public List<String> getCandProp() {
        List<String> aux = new ArrayList<>();
        List<String> prop = getPropostasComCandidatura();
        for (Proposta p : propostas.values()) {
            if (prop.contains(p.getCodId()) || p.getIdAluno() != 0)
                aux.add(String.valueOf(p));
        }
        return aux;
    }
    public List<String> getCandSemProp() {
        List<String> aux = new ArrayList<>();
        List<String> prop = getPropostasComCandidatura();
        for (Proposta p : propostas.values()) {
            if (!prop.contains(p.getCodId()) && p.getIdAluno() == 0)
                aux.add(String.valueOf(p));
        }
        return aux;
    }



    // Atribuição de Propostas
    private void addAlunosAtribuidos(Proposta aux) {
        atPropostas.put(aux.getCodId(), aux.getIdAluno());
    }
    private String idPropostaEmpatada;
    public Erros addAtPropostas() {
        if(!faseFechada.get(GPEState.OPCOES_CANDIDATURA))
            return Erros.FASE_NFECHADA;
        if(isFechado(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.ATRIBUICAO_FECHADA;
        Map<String, Integer> aux;
        for(int i = 0;i < candidaturas.values().size()-1; i++){
            aux = verficaQuantasPropostasRepetidas(i);
            for(Map.Entry<String, Integer> a : aux.entrySet()){
                if(a.getValue() == 1){
                    if(!atPropostas.containsKey(a.getKey())){
                        for (Map.Entry<Long, List<String>> c : candidaturas.entrySet()) {
                            if (c.getValue().contains(a.getKey()) && !atPropostas.containsValue(c.getKey())) {
                                atPropostas.put(a.getKey(), c.getKey());
                            }
                        }
                    }
                }
                else{
                    List<Long> nAlunos = new ArrayList<>();
                    for(Map.Entry<Long, List<String>> c : candidaturas.entrySet()){
                        if(c.getValue().get(i).contains(a.getKey()) && !atPropostas.containsValue(c.getKey())
                                && !atPropostas.containsKey(a.getKey()) && !nAlunos.contains(c.getKey())) {
                            nAlunos.add(c.getKey());
                        }
                    }
                    if(!nAlunos.isEmpty()) {
                        long idAluno = medMaisAlta(nAlunos);
                        if(idAluno == 0) {
                            idPropostaEmpatada = a.getKey();
                            idAlunosEmpatados = new ArrayList<>(nAlunos);
                            return Erros.EMPATE;
                        }
                        atPropostas.put(a.getKey(),idAluno);
                    }
                }
            }
            aux.clear();
        }
        return Erros.NONE;
    }
    public Erros addAtPropostasManualmente(String idProposta, long nAluno){
        if(isFechado(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.ATRIBUICAO_FECHADA;
        if(!faseFechada.get(GPEState.OPCOES_CANDIDATURA))
            return Erros.FASE_NFECHADA;

        if(idProposta.isBlank())
            return Erros.ATRIBUICAO_ADD_IDPROPOSTA;

        if(!propostas.containsKey(idProposta))
            return Erros.ATRIBUICAO_ADD_IDPROPOSTA;

        if(!alunos.containsKey(nAluno))
            return Erros.ATRIBUICAO_ADD_NALUNO;

        if(!candidaturas.containsKey(nAluno))
            return Erros.ATRIBUICAO_ADD_NALUNO;

        if(atPropostas.containsKey(idProposta))
            return Erros.ATRIBUICAO_ADD_ATPROPOSTA;

        if(atPropostas.containsValue(nAluno))
            return Erros.ATRIBUICAO_ADD_ATALUNO;

        if(!propostas.get(idProposta).getRamo().contains(alunos.get(nAluno).getRamo()))
            return Erros.ATRIBUICAO_ADD_ALUNO_RAMO;

        atPropostas.put(idProposta,nAluno);
        return Erros.NONE;
    }
    public Erros desempate(String alunoDesempatado) {
        if(alunoDesempatado.isBlank())
            return Erros.VALOR_INCORRETO;
        long numeroAluno;
        try {
            numeroAluno = Long.parseLong(alunoDesempatado);
        } catch (Exception e) {
            return Erros.VALOR_INCORRETO;
        }
        if(!idAlunosEmpatados.contains(numeroAluno))
            return Erros.VALOR_INCORRETO;
        atPropostas.put(idPropostaEmpatada, numeroAluno);
        idAlunosEmpatados = new ArrayList<>();
        return Erros.NONE;
    }
    public String getAlunosEmpatados(){
        StringBuilder sb = new StringBuilder();

        if(idAlunosEmpatados.isEmpty())
            return "Não existem alunos empatados!";

        sb.append(String.format("Proposta empatada %s\nAlunos empatados:\n",idPropostaEmpatada));

        for (long nAluno : idAlunosEmpatados) {
            sb.append("\t - " + getAluno(nAluno) + "\n");
        }

        return sb.toString();
    }

    public List<Alunos> getAlunosEmpatadosList(){
        List<Alunos> aux = new ArrayList<>();
        for(long n : idAlunosEmpatados){
            aux.add(alunos.get(n));
        }
        return aux;
    }

    public Proposta getPropEmpatada(){
        return propostas.get(idPropostaEmpatada);
    }

    private Map<String, Integer> verficaQuantasPropostasRepetidas(int i){
        Map<String, Integer> aux = new HashMap<>();

        int count;
        for(Map.Entry<Long, List<String>> c : candidaturas.entrySet()){
            count=1;
            if(i <= c.getValue().size()-1) {
                if (aux.containsKey(c.getValue().get(i))) {
                    count += aux.get(c.getValue().get(i));
                    aux.replace(c.getValue().get(i), count);
                } else {
                    aux.put(c.getValue().get(i), count);
                }
            }
        }
        return aux;
    }
    private long medMaisAlta(List<Long> nAlunos){
        double classMax = 0,classAnterior = 0;
        long idAluno = 0,idAlunoAnterior = 0;
        boolean empate = false;

        for(Alunos a : alunos.values()){
            if(nAlunos.contains(a.getNumero())){
                if(classMax == 0) {
                    classMax = a.getClassificacao();
                    idAluno = a.getNumero();
                }
                else if(classMax < a.getClassificacao()) {
                    classAnterior = classMax;
                    classMax = a.getClassificacao();
                    idAlunoAnterior = idAluno;
                    idAluno = a.getNumero();
                    empate = false;
                }
                else if(classMax == classAnterior || classMax == a.getClassificacao()){
                    empate = true;
                    if(!idAlunosEmpatados.contains(idAlunoAnterior))
                        idAlunosEmpatados.add(idAlunoAnterior);
                    idAluno = a.getNumero();
                    idAlunosEmpatados.add(idAluno);
                }
            }
        }

        if(empate)
            idAluno = 0;
        return idAluno;
    }
    public Erros removeAtPropostasProp(String nProposta) {
        if(!faseFechada.get(GPEState.OPCOES_CANDIDATURA))
            return Erros.FASE_NFECHADA;
        if(isFechado(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.ATRIBUICAO_FECHADA;

        if(!atPropostas.containsKey(nProposta))
            return Erros.ATRIBUICAO_REMOVE_IDPROPOSTA;

        if(atOrientadores.containsValue(atPropostas.get(nProposta)))
            atOrientadores.values().remove(atPropostas.get(nProposta));

        if(!atPropostas.remove(nProposta,atPropostas.get(nProposta)))
            return Erros.ATRIBUICAO_REMOVE;

        return Erros.NONE;
    }
    public Erros removeAtPropostasAluno(Long nAluno) {
        if(!faseFechada.get(GPEState.OPCOES_CANDIDATURA))
            return Erros.FASE_NFECHADA;
        if(isFechado(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.ATRIBUICAO_FECHADA;

        if(!alunos.containsKey(nAluno) || !atPropostas.containsValue(nAluno))
            return Erros.ATRIBUICAO_REMOVE_NALUNO;

        String auxIdP = "";

        for(Map.Entry<String, Long> atP : atPropostas.entrySet()){
            if(atP.getValue().longValue() == nAluno)
                auxIdP = atP.getKey();
        }

        if(auxIdP.isBlank())
            return Erros.ATRIBUICAO_REMOVE;

        return removeAtPropostasProp(auxIdP);
    }
    public String getAtPropostas(){
        StringBuilder sb = new StringBuilder("Atribuição de Propostas: \n");

        if (atPropostas == null || atPropostas.size() == 0) {
            return "Sem propostas atribuidas.";
        }

        for (Map.Entry<String, Long> pair : atPropostas.entrySet()) {
            sb.append(String.format("\t- Id proposta: %s, Numero aluno: %s\n", pair.getKey(), pair.getValue()));
        }
        return sb.toString();
    }

    public List<String> getAtPropostasList(){
        List<String> aux = new ArrayList<>();

        if (atPropostas == null || atPropostas.size() == 0) {
            aux.add("Sem propostas atribuidas");
            return aux;
        }

        for (Map.Entry<String, Long> pair : atPropostas.entrySet()) {
            aux.add(String.format("Id proposta: %s, Numero aluno: %s, Nome: %s", pair.getKey(), pair.getValue()
                    ,alunos.get(pair.getValue()).getNome()));
        }
        return aux;
    }
    public boolean isTodosAlunosCandidaturaAtribuida(){
        for(Map.Entry<Long, List<String>> c : candidaturas.entrySet()){
            if(!atPropostas.containsValue(c.getKey()))
                return false;
        }
        return true;
    }
    public long getIdAlunoAtProposta(String idProposta){
        if(atPropostas.containsKey(idProposta))
            return atPropostas.get(idProposta);
        return 0;
    }
    public String getIdAtProposta(long nAluno){
        String aux = "";
        for(Map.Entry<String, Long> at : atPropostas.entrySet()){
            if(at.getValue() == nAluno)
                aux = at.getKey();
        }
        return aux;
    }

    // Atribui Orientadores Automáticamente
    public Erros addAtOrientadores(){
        if(isFechado(GPEState.ATRIBUICAO_ORIENTADORES))
            return Erros.ORIENTADORES_FECHADA;
        if(atPropostas.isEmpty())
            return Erros.ATRIBUICAO_VAZIO;

        for(Map.Entry<String, Long> atProp : atPropostas.entrySet()){
            Map<String, Long> auxP = new HashMap<>();
            String idP = atProp.getKey();
            if(propostas.get(idP).getTipo().equals("T2")){
               if(atOrientadores.containsKey(propostas.get(idP).getEmailDocente())){
                   auxP.putAll(atOrientadores.get(propostas.get(idP).getEmailDocente()));
                   auxP.put(atProp.getKey(),atProp.getValue());
                   atOrientadores.get(propostas.get(idP).getEmailDocente()).putAll(auxP);
               }else{
                   auxP.put(atProp.getKey(),atProp.getValue());
                   atOrientadores.put(propostas.get(idP).getEmailDocente(),auxP);
               }
            }
        }

        if(atOrientadores.isEmpty())
            return Erros.ORIENTADORES_VAZIO;

        return Erros.NONE;
    }

    //Atribui Orientadores Manualmente
    public Erros addManOrientadores(String emailDocente,String idPropostas){
        if(isFechado(GPEState.ATRIBUICAO_ORIENTADORES))
            return Erros.ORIENTADORES_FECHADA;
        if(!faseFechada.get(GPEState.ATRIBUICAO_PROPOSTAS))
                    return Erros.FASE_NFECHADA;

        if(emailDocente.isBlank() || idPropostas.isBlank())
            return Erros.VALOR_INCORRETO;

        if(!docentes.containsKey(emailDocente))
            return Erros.DOCENTE_EMAIL;

        if(!propostas.containsKey(idPropostas))
            return Erros.PROPOSTA_EDIT_NEXISTE;

        if(propostas.get(idPropostas).getTipo().equals("T2")){
           return Erros.ORIENTADORES_ADD_T2; //O id da proposta é do tipo T2 e não pode ser agregada a nenhum orientador diferente!
        }

        for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
            if(atO.getValue().containsKey(idPropostas))
                return Erros.ORIENTADORES_EXISTE; //Já existe uma proposta a ser orientada por um docente com idProposta
        }

        if(!atPropostas.containsKey(idPropostas))
            return Erros.ORIENTADORES_ATPROPOSTA_NEXISTE; //Não existe nunhuma proposta atribuida a um aluno com esse idProposta

        if(atOrientadores.containsKey(emailDocente)){
            atOrientadores.get(emailDocente).put(idPropostas,atPropostas.get(idPropostas));
        }else{
            Map<String,Long> auxP = new HashMap<>();
            auxP.put(idPropostas,atPropostas.get(idPropostas));
            atOrientadores.put(emailDocente,auxP);
        }

        return Erros.NONE;
    }
    //Remove Orientador
    //Remover uma proposta atribuida a um docente
    public Erros removeOrientadorProposta(String idProposta){
        if(!faseFechada.get(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.FASE_NFECHADA;
        if(isFechado(GPEState.ATRIBUICAO_ORIENTADORES))
            return Erros.ORIENTADORES_FECHADA;
        if(idProposta.isBlank())
            return Erros.VALOR_INCORRETO;

        if(!propostas.containsKey(idProposta))
            return Erros.PROPOSTA_ID;

        if(propostas.get(idProposta).getTipo().equals("T2"))
            return Erros.ORIENTADORES_REMOVE_T2; //Impossivel remover propostas que os docentes propuseram

        String auxAtO = "";

        for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
            if(atO.getValue().containsKey(idProposta)) {
                auxAtO = atO.getKey();
            }
        }

        if(auxAtO.isBlank())
            return Erros.ORIENTADORES_REMOVE;

        atOrientadores.get(auxAtO).remove(idProposta);

        return Erros.NONE;
    }

    //Remover todas as propostas atribuidas a um docente
    public Erros removeOrientadorTodasProposta(String emailDocente){
        if(!faseFechada.get(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.FASE_NFECHADA;
        if(isFechado(GPEState.ATRIBUICAO_ORIENTADORES))
            return Erros.ORIENTADORES_FECHADA;
        if(emailDocente.isBlank())
            return Erros.VALOR_INCORRETO;

        if(!docentes.containsKey(emailDocente))
            return Erros.DOCENTE_EMAIL;

        if(!atOrientadores.containsKey(emailDocente))
            return Erros.ORIENTADORES_REMOVE_NEXISTE_DOCENTE;

        boolean T2 = false;
        Map<String, Long> auxP = new HashMap<>();
        auxP.putAll(atOrientadores.get(emailDocente));

        for(Map.Entry<String, Long> aux : auxP.entrySet()){
            if(propostas.get(aux.getKey()).getTipo().equals("T2"))
                T2 = true;
        }

        atOrientadores.remove(emailDocente,auxP);

        if(T2)
            addAtOrientadores();

        return Erros.NONE;
    }
    public Erros editAtOrientadores(String idProposta, String novoDocente){
        //Função onde é alterada o docente que orienta essa proposta (menos as propostas T2)
        if(isFechado(GPEState.ATRIBUICAO_ORIENTADORES))
            return Erros.ORIENTADORES_FECHADA;
        if(!faseFechada.get(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.FASE_NFECHADA;

        if(idProposta.isBlank() || novoDocente.isBlank())
            return Erros.VALOR_INCORRETO;

        if(!propostas.containsKey(idProposta))
            return Erros.PROPOSTA_ID;

        if(propostas.get(idProposta).getTipo().equals("T2"))
            return Erros.ORIENTADORES_EDIT_T2;

        if(!docentes.containsKey(novoDocente))
            return Erros.DOCENTE_EMAIL;

        String auxO = "";

        for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
            if(atO.getValue().containsKey(idProposta))
                auxO = atO.getKey();
        }

        if(auxO.isBlank())
            return Erros.ORIENTADORES_EDIT_PROPOSTA;

        atOrientadores.get(auxO).remove(idProposta,atPropostas.get(idProposta));
        if(atOrientadores.get(auxO).values().isEmpty())
            atOrientadores.remove(auxO);

        if(!atOrientadores.containsKey(novoDocente)){
            Map<String,Long> aux = new HashMap<>();
            aux.put(idProposta,atPropostas.get(idProposta));
            atOrientadores.put(novoDocente,aux);
        }else
            atOrientadores.get(novoDocente).put(idProposta,atPropostas.get(idProposta));


        return Erros.NONE;
    }

    //Mostra Todas as propostas com orientadores
    public String mostraPropostasOrientadas(){

        StringBuilder sb = new StringBuilder("Orientadores Atribuidos: \n");
        if (atOrientadores == null || atOrientadores.size() == 0)
            sb.append("Não existem Orientadores Atribuidos no sistema!");
        else
            for (Map.Entry<String, Map<String, Long>> atOri : atOrientadores.entrySet()){
                sb.append(String.format("\t- %s, %s\n", atOri.getKey(),atOri.getValue()));
            }
        return sb.toString();
    }
    public List<String> mostraPropostasOrientadasList(){
        List<String> aux = new ArrayList<>();
        if (atOrientadores == null || atOrientadores.size() == 0)
            aux.add("Não existem Orientadores Atribuidos no sistema!");
        else
            for (Map.Entry<String, Map<String, Long>> atOri : atOrientadores.entrySet()){
                for(Map.Entry<String,Long> prop : atOri.getValue().entrySet()) {
                    aux.add(String.format("Docente: %s -> Proposta: %s, Nome do aluno: %s\n",
                            docentes.get(atOri.getKey()).getNomeDocente(), prop.getKey(),alunos.get(prop.getValue()).getNome()));
                }
            }
        return aux;
    }

    //Memento
    @Override
    public IMemento save() {
        return new Memento(atOrientadores);
    }
    @Override
    public void restore(IMemento memento) {
        Object object = (Object) memento.getSnapshot();
        this.atOrientadores = new HashMap<>((Map<String, Map<String, Long>>) object);

    }

    //Consulta
    //Estudantes com Propostas Atribuidas
    public int distAlunosRamos(String ramo) {
        int num = 0;
        for (Map.Entry<Long, Alunos> atprop : alunos.entrySet()) {
            if (atprop.getValue().getRamo().equals(ramo)) {
                num++;
            }
        }
        return num;
    }

    //Num de propostas atribuidas e não atribuidas
    public int nPropAtNAt(String s){
        int num = 0;

        switch (s) {
            case "Não atribuido" -> {
                for (Alunos aluno : alunos.values()) {
                    if (!atPropostas.containsValue(aluno.getNumero())) {
                        num++;
                    }
                }
            }
            case "Atribuido" -> {
                for (Map.Entry<String, Long> atprop : atPropostas.entrySet()) {
                    num++;
                }
            }
        }
        return num;
    }


    //Top 5 docentes
    public LinkedHashMap<String, Integer> getTop5Docentes() {
        LinkedHashMap<String, Integer> docentes = new LinkedHashMap<>();

        for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
            docentes.put(atO.getKey(),atO.getValue().size());
        }

        return docentes;
    }


    //Top 5 empresas
    public LinkedHashMap<String, Integer> getTop5EmpEstagio() {
        Map<String, Integer> empEstagio = new HashMap<>();
        for (Proposta proposta : propostas.values()) {
            if (!empEstagio.containsKey(proposta.getIdEmpresa()) && !proposta.getIdEmpresa().equals("Sem empresa")) {
                empEstagio.put(proposta.getIdEmpresa(),0);
            }
        }
        for (Proposta proposta : propostas.values()) {
            for (Map.Entry<String, Integer> emp: empEstagio.entrySet()) {
                if (emp.getKey().equals(proposta.getIdEmpresa())){
                    emp.setValue(emp.getValue()+1);
                }
            }
        }
        LinkedHashMap<String,Integer> resEmpEstagio = empEstagio.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),(entry1, entry2) -> entry2,LinkedHashMap::new));
        return resEmpEstagio;
    }
    public String alunosAtribuidos(){

        StringBuilder sb = new StringBuilder("Alunos Atribuidos:\n");
        if (atPropostas == null || atPropostas.size() == 0)
            sb.append("Não existem alunos atribuidos no sistema!");
        else
            for (Map.Entry<String, Long> atprop : atPropostas.entrySet()){
                sb.append(String.format("\t- Número: %d, Nome: %s, Proposta atribuida: %s\n",
                        atprop.getValue(),alunos.get(atprop.getValue()).getNome(),atprop.getKey()));
            }
        return sb.toString();
    }
    //Estudantes sem Propostas Atribuidas
    public String alunosNAtribuidos(){

        StringBuilder sb = new StringBuilder("Alunos não atribuidos a uma proposta:\n");
        if (atPropostas == null || atPropostas.size() == 0)
            sb.append("Todos os alunos estão atribuidos a uma proposta!");
        else{
            for(Alunos aluno : alunos.values()){
                if(!atPropostas.containsValue(aluno.getNumero())){
                    sb.append(String.format("\t- Número: %d, Nome: %s\n", aluno.getNumero(),aluno.getNome()));
                }
            }
        }
        return sb.toString();
    }
    //conjunto de propostas disponíveis.
    public String propDisponiveis(){

        StringBuilder sb = new StringBuilder("Propostas Disponiveis:\n");
        if (atPropostas == null || atPropostas.size() == 0)
            sb.append("Não existem propostas disponiveis no sistema!");
        else {
            for(Map.Entry<String, Proposta> prop : propostas.entrySet()){
                if(!atPropostas.containsKey(prop.getKey()))
                    sb.append(String.format("\t- Código: %s, Titulo: %s, Ramo: %s\n",prop.getKey(),prop.getValue().getTitulo(),prop.getValue().getRamo()));
            }
        }
        return sb.toString();
    }
    public String propAtribuidas(){
        StringBuilder sb = new StringBuilder("Propostas Atribuidas:\n");
        if (atPropostas == null || atPropostas.size() == 0)
            sb.append("Não existem propostas atribuidas no sistema!");
        else {
            for(Map.Entry<String, Long> atP : atPropostas.entrySet()){
                sb.append(String.format("\t- Código: %s, Titulo: %s, Ramo: %s\n",atP.getKey(),propostas.get(atP.getKey()).getTitulo(),propostas.get(atP.getKey()).getRamo()));
            }
        }
        return sb.toString();
    }
    public String orientacoesPorDocente(){
        StringBuilder sb = new StringBuilder("Número de orientações por docentes:\n");
        if (atOrientadores == null || atOrientadores.size() == 0)
            sb.append("Não existem propostas atribuidas a orientadores no sistema!");
        else{
            for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
                int nOrientacoes = atO.getValue().size();
                sb.append(String.format("\t- Docente: %s -> está a orientar %d propostas\n",docentes.get(atO.getKey()).getNomeDocente(),nOrientacoes));
            }
        }
        return sb.toString();
    }
    public float mediaOrientacoes(){
        float media=0;
        int count = 0;
        int soma = 0;

        if (atOrientadores == null || atOrientadores.size() == 0)
            media = -1;
        else{
            for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()){
                soma += atO.getValue().size();
                count++;
            }
        }
        if(media == 0){
            media += soma/count;
        }
        return media;
    }
    public int minimoOrientacoes(){
        int min = 0;
        int count = 0;

        for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()) {
            if(count == 0)
                min = atO.getValue().size();
            else if(min > atO.getValue().size())
                min = atO.getValue().size();
        }


        if(min == 0)
            min = -1;
        return min;
    }
    public int maximoOrientacoes(){
        int max = 0;
        int count = 0;

        for(Map.Entry<String, Map<String, Long>> atO : atOrientadores.entrySet()) {
            if(count == 0)
                max = atO.getValue().size();
            else if(max > atO.getValue().size())
                max = atO.getValue().size();
        }


        if(max == 0)
            max = -1;
        return max;
    }
    public int numeroOrientacoesDocente(String docente){

        if(!atOrientadores.containsKey(docente))
            return -1;

        return atOrientadores.get(docente).size();
    }
    public Erros fecharFase(GPEState state) {
        return switch (state){
            case CONFIGURACAO -> fecharConfiguracao();
            case OPCOES_CANDIDATURA -> fecharCandidaturas();
            case ATRIBUICAO_PROPOSTAS -> fecharAtPropostas();
            case ATRIBUICAO_ORIENTADORES -> fecharAtOrientadores();
            default -> Erros.NONE;
        };
    }
    private Erros fecharConfiguracao(){
        if(alunos.isEmpty() || propostas.isEmpty())
            return Erros.CONFIGURACAO_FECHARFASE_SEMDADOS;
        if(docentes.isEmpty())
            return Erros.CONFIGURACAO_FECHARFASE_SEMDADOS_DOCENTES;

        int alDA,alSI,alRAS,propsDA,propsSI,propsRAS;

        alDA = alunosDA(); alSI = alunosSI(); alRAS = alunosRAS();
        propsDA = propostasDA(); propsSI = propostasSI(); propsRAS = propostasRAS();

        if(alDA != 0 && propsDA < alDA)
            return Erros.CONFIGURACAO_DA;
        if(alSI != 0 && propsSI < alSI)
            return Erros.CONFIGURACAO_SI;
        if(alRAS != 0 && propsRAS < alRAS)
            return Erros.CONFIGURACAO_RAS;

        faseFechada.replace(GPEState.CONFIGURACAO,true);
        return Erros.NONE;
    }
    private int propostasRAS() {
        int count = 0;
        for(Proposta p : propostas.values())
            if(p.getRamo().contains("RAS"))
                count++;
        return count;
    }
    private int propostasSI() {
        int count = 0;
        for(Proposta p : propostas.values())
            if(p.getRamo().contains("SI"))
                count++;
        return count;
    }
    private int propostasDA() {
        int count = 0;
        for(Proposta p : propostas.values())
            if(p.getRamo().contains("DA"))
                count++;
        return count;
    }
    private int alunosRAS() {
        int count = 0;
        for(Alunos a : alunos.values())
            if(a.getRamo().equalsIgnoreCase("RAS"))
                count++;
        return count;
    }
    private int alunosSI() {
        int count = 0;
        for(Alunos a : alunos.values())
            if(a.getRamo().equalsIgnoreCase("SI"))
                count++;
        return count;
    }
    private int alunosDA(){
        int count = 0;
        for(Alunos a : alunos.values())
            if(a.getRamo().equalsIgnoreCase("DA"))
                count++;
        return count;
    }
    private Erros fecharCandidaturas() {
        if(!isFechado(GPEState.CONFIGURACAO))
            return Erros.CONFIGURACAO_NFECHADA;

        if(candidaturas.isEmpty())
            return Erros.CANDIDATURAS_FECHARFASE_SEMDADOS;

        faseFechada.replace(GPEState.OPCOES_CANDIDATURA,true);
        return Erros.NONE;
    }
    private Erros fecharAtPropostas() {
        if(!isFechado(GPEState.OPCOES_CANDIDATURA))
            return Erros.CANDIDATURAS_NFECHADA;

        if(atPropostas.isEmpty())
            return Erros.ATRIBUICAO_FECHARFASE_SEMDADOS;

        for(Map.Entry<Long, List<String>> c : candidaturas.entrySet()){
            if(!atPropostas.containsValue(c.getKey()))
                return Erros.ATRIBUICAO_ALUNOSDIF_CANDIDATURAS;
        }
        faseFechada.replace(GPEState.ATRIBUICAO_PROPOSTAS,true);
        return Erros.NONE;
    }
    private Erros fecharAtOrientadores() {
        if(!isFechado(GPEState.ATRIBUICAO_PROPOSTAS))
            return Erros.ATRIBUICAO_NFECHADA;

        if(atOrientadores.isEmpty())
            return Erros.ATORIENTADORES_FECHARFASE_SEMDADOS;

        if(!atOrientadores.containsValue(atPropostas))
            return Erros.ATRIBUICAO_ATPROPDIF;

        faseFechada.replace(GPEState.ATRIBUICAO_ORIENTADORES,true);
        return Erros.NONE;
    }

    public boolean isFechado(GPEState state) {
        if(faseFechada.containsKey(state))
            return faseFechada.get(state);
        return false;
    }
    public int nAlunos(){
        if(alunos.isEmpty())
            return 0;
        return alunos.values().size();
    }
    public int nDocentes(){
        if(docentes.isEmpty())
            return 0;
        return docentes.values().size();
    }
    public int nPropostas(){
        if(propostas.isEmpty())
            return 0;
        return propostas.values().size();
    }
    public int nCandidaturas(){
        if(candidaturas.isEmpty())
            return 0;
        return candidaturas.values().size();
    }
    public int nAtPropostas(){
        if(atPropostas.isEmpty())
            return 0;
        return atPropostas.values().size();
    }
    public int nAtOrientadores(){
        if(atOrientadores.isEmpty())
            return 0;
        Map<String,Long> aux = new HashMap<>();
        for(Map<String,Long> atO : atOrientadores.values())
            aux.putAll(atO);
        return aux.size();
    }
}