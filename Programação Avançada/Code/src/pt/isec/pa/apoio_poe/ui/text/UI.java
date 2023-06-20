package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.fsm.GPEContext;
import pt.isec.pa.apoio_poe.utils.PAInput;
import pt.isec.pa.apoio_poe.utils.TratamentoErros;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UI {
    GPEContext fsm;

    public UI(GPEContext fsm) {
        this.fsm = fsm;
    }

    boolean finish = false;

    public void start() {
        while (!finish) {
            switch (fsm.getState()) {
                case INICIO -> inicioUI();
                case CONFIGURACAO -> configuracaoUI();
                case GESTAO_ALUNOS -> gestaoAlunosUI();
                case GESTAO_DOCENTES -> gestaoDocentesUI();
                case GESTAO_PROPOSTAS -> gestaoPropostasUI();
                case OPCOES_CANDIDATURA -> OpcoesCandidaturaUI();
                case ATRIBUICAO_PROPOSTAS -> AtPropostasUI();
                case ATRIBUICAO_AUTOMATICA_PROPOSTAS -> AtPropostasAutomaticasUI();
                case ATRIBUICAO_ORIENTADORES -> AtOrientadoresUI();
                case CONSULTA -> ConsultaUI();
            }
        }
    }

    private void inicioUI() {
        System.out.println("\n" + fsm.getState());
        switch (PAInput.chooseOption("Escolha uma opção:", "Iniciar", "Load", "Sair")) {
            case 1 -> fsm.iniciar();
            case 2 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.load(new File(filename));
            }
            case 3 -> finish = true;
        }
    }

    private void configuracaoUI() {
        System.out.println("\n" + fsm.getState());
        if(fsm.isFechado()) {
            configuracaoFechadaUI();
            return;
        }
        switch (PAInput.chooseOption("Escolha uma opção:", "Gerir alunos", "Gerir docentes", "Gerir propostas", "Fechar fase", "Fase seguinte", "Save", "Sair")) {
            case 1 -> fsm.gerirAlunos();
            case 2 -> fsm.gerirDocentes();
            case 3 -> fsm.gerirPropostas();
            case 4 -> TratamentoErros.trataErros(fsm.fecharFase());
            case 5 -> fsm.seguinte();
            case 6 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
            case 7 -> terminarUI();
        }
    }

    private void configuracaoFechadaUI() {
        System.out.println("Atenção: Estado fechado");
        switch (PAInput.chooseOption("Escolha uma opção:", "Gerir alunos", "Gerir docentes", "Gerir propostas", "Fase seguinte", "Sair")) {
            case 1 -> fsm.gerirAlunos();
            case 2 -> fsm.gerirDocentes();
            case 3 -> fsm.gerirPropostas();
            case 4 -> fsm.seguinte();
            case 5 -> terminarUI();
        }
    }

    private void terminarUI() {
        int escolha = PAInput.chooseOption("Pretende guardar os dados e o estado do programa?", "Sim", "Não", "Voltar");
        if (escolha == 1) {
            String filename = PAInput.readString("Nome ficheiro (.dat): ");
            fsm.save(new File(filename));
        }
        if (escolha == 3)
            return;
        fsm.terminar();
    }


    //Alunos:
    private void gestaoAlunosUI() {
        System.out.println("\n" + fsm.getState());
        if (fsm.isFechado()) {
            gestaoAlunosFechadaUI();
            return;
        }
        switch (PAInput.chooseOption("Escolha uma opção:", "Adicionar alunos", "Remover alunos", "Importar alunos", "Exportar alunos"
                ,"Editar alunos","Mostrar todos os alunos", "Obter dados de um aluno", "Voltar", "Save")) {
            case 1 -> adicionarAluno();
            case 2 -> removerAluno();
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheiro (.csv): ");
                fsm.importFile(new File(filename));
            }
            case 4 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 5 -> editarAlunos();
            case 6 -> System.out.println(fsm.getTodosAlunos());
            case 7 -> System.out.println(fsm.getAluno(PAInput.readLong("Número: ")));
            case 8 -> fsm.voltar();
            case 9 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
        }
    }

    private void editarAlunos(){
        boolean finish = false;
        while (!finish) {
            switch (PAInput.chooseOption("Escolha uma opção: ", "Editar nome", "Editar classificação", "Editar aptidão","Voltar")) {
                case 1 -> TratamentoErros.trataErros(fsm.setNomeAluno(PAInput.readLong("Número: "), PAInput.readString("Novo nome: ")));
                case 2 -> TratamentoErros.trataErros(fsm.setClassificacao(PAInput.readLong("Número: "), PAInput.readString("Nova classificação: ")));
                case 3 -> TratamentoErros.trataErros(fsm.setAptoEstagio(PAInput.readLong("Número: ")));
                case 4 -> finish = true;
            }
        }
    }

    private void gestaoAlunosFechadaUI() {
        System.out.println("Atenção: Estado fechado");
        switch (PAInput.chooseOption("Escolha uma opção:","Mostrar todos os alunos", "Obter dados de um aluno" ,"Exportar alunos", "Voltar")){
            case 1 -> System.out.println(fsm.getTodosAlunos());
            case 2 -> System.out.println(fsm.getAluno(PAInput.readLong("Número: ")));
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 4 -> fsm.voltar();
        }
    }

    private void adicionarAluno() {
        List<String> valuesAlunos = new ArrayList<>();
        valuesAlunos.add(PAInput.readString("Número: "));
        valuesAlunos.add(PAInput.readString("Nome: "));
        valuesAlunos.add(PAInput.readString("E-mail: "));
        valuesAlunos.add(PAInput.readString("Curso: "));
        valuesAlunos.add(PAInput.readString("Ramo: "));
        valuesAlunos.add(PAInput.readString("Classificação: "));

        if (PAInput.chooseOption("Esta apto para ir a estágio?", "Sim", "Não") == 1)
            valuesAlunos.add("true");
        else
            valuesAlunos.add("false");

        TratamentoErros.trataErros(fsm.addAlunos(valuesAlunos));
    }

    private void removerAluno() {
        TratamentoErros.trataErros(fsm.removeAlunos(PAInput.readLong("Número: ")));
    }

    //Docentes
    private void gestaoDocentesUI() {
        System.out.println("\n" + fsm.getState());
        if(fsm.isFechado()) {
            gestaoDocentesFechadaUI();
            return;
        }
        switch (PAInput.chooseOption("Escolha uma opção:", "Adicionar docentes", "Remover docentes", "Importar docentes", "Exportar docentes",
                "Editar nome do Docente","Mostrar todos os docentes", "Obter dados de um docente", "Voltar", "Save")) {
            case 1 -> adicionarDocente();
            case 2 -> TratamentoErros.trataErros(fsm.removeDocente(PAInput.readString("E-mail: ")));
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheiro (.csv): ");
                fsm.importFile(new File(filename));
            }
            case 4 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 5 -> TratamentoErros.trataErros(fsm.setNomeDocente(PAInput.readString("E-mail: "),PAInput.readString("Novo nome: ")));
            case 6 -> System.out.println(fsm.getTodosDocentes());
            case 7 -> System.out.println(fsm.getDocente(PAInput.readString("E-mail: ")));
            case 8 -> fsm.voltar();
            case 9 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }

        }
    }

    private void adicionarDocente() {
        List<String> list = new ArrayList<>();
        list.add(PAInput.readString("Nome: "));
        list.add(PAInput.readString("E-mail: "));
        TratamentoErros.trataErros(fsm.addDocente(list));
    }

    private void gestaoDocentesFechadaUI() {
        System.out.println("Atenção: Estado fechado");
        switch (PAInput.chooseOption("Escolha uma opção:", "Mostrar todos os docentes", "Obter dados de um docente","Exportar docentes", "Voltar")){
            case 1 -> System.out.println(fsm.getTodosDocentes());
            case 2 -> System.out.println(fsm.getDocente(PAInput.readString("E-mail: ")));
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 4 -> fsm.voltar();
        }
    }

    //Gestão Propostas
    private void gestaoPropostasUI() {
        System.out.println("\n" + fsm.getState());
        if(fsm.isFechado()) {
            gestaoPropostasFechadaUI();
            return;
        }
        switch (PAInput.chooseOption("Escolha uma opção:", "Adicionar proposta", "Remover proposta", "Importar propostas", "Exportar propostas",
                "Editar Propostas","Mostrar todas as propostas", "Obter dados de uma proposta", "Voltar", "Save")) {
            case 1 -> adicionarProposta();
            case 2 -> TratamentoErros.trataErros(fsm.removeProposta(PAInput.readString("Código: ")));
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheiro (.csv): ");
                fsm.importFile(new File(filename));
            }
            case 4 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 5 -> editarPropostas();
            case 6 -> System.out.println(fsm.getTodasPropostas());
            case 7 -> System.out.println(fsm.getProposta(PAInput.readString("Id proposta: ")));
            case 8 -> fsm.voltar();
            case 9 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
        }
    }

    private void gestaoPropostasFechadaUI() {
        System.out.println("Atenção: Estado fechado");
        switch (PAInput.chooseOption("Escolha uma opção:","Mostrar todas as propostas","Obter dados de uma proposta","Exportar propostas","Voltar")){
            case 1 -> System.out.println(fsm.getTodasPropostas());
            case 2 -> System.out.println(fsm.getProposta(PAInput.readString("Id proposta: ")));
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 4 -> fsm.voltar();
        }
    }

    private void adicionarProposta() {
        List<String> valuesPropostas = new ArrayList<>();
        int tipo = PAInput.chooseOption("Tipo: ", "Estágio (T1)", "Projeto (T2)", "Estágio/Projeto autoproposto (T3)");
        valuesPropostas.add(switch (tipo) {
            case 1 -> "T1";
            case 2 -> "T2";
            case 3 -> "T3";
            default -> "NA";
        });
        valuesPropostas.add(PAInput.readString("Código: "));
        if (tipo != 3) {
            valuesPropostas.add(switch (PAInput.chooseOption("Ramo: ", "DA", "RAS", "SI", "DA|RAS", "DA|SI", "RAS|SI", "DA|RAS|SI")) {
                case 1 -> "DA";
                case 2 -> "RAS";
                case 3 -> "SI";
                case 4 -> "DA|RAS";
                case 5 -> "DA|SI";
                case 6 -> "RAS|SI";
                case 7 -> "DA|RAS|SI";
                default -> ""; //o compilador dava erro se não fosse acrescentado esta linha
            });
        }

        valuesPropostas.add(PAInput.readString("Titulo: "));

        switch (tipo) {
            case 1 -> {
                valuesPropostas.add(PAInput.readString("Empresa: "));
                int Aluno = PAInput.chooseOption("Indicar a proposta a um aluno?", "Sim", "Não");
                if (Aluno == 1)
                    valuesPropostas.add(PAInput.readString("Número do Aluno: "));
                else
                    valuesPropostas.add("0");

                TratamentoErros.trataErros(fsm.addProposta(valuesPropostas));
            }
            case 2 -> {
                valuesPropostas.add(PAInput.readString("E-mail do docente: "));
                int Aluno = PAInput.chooseOption("Indicar a proposta a um aluno?", "Sim", "Não");
                if (Aluno == 1)
                    valuesPropostas.add(PAInput.readString("Número do Aluno: "));
                else
                    valuesPropostas.add("0");
                TratamentoErros.trataErros(fsm.addProposta(valuesPropostas));
            }
            case 3 -> {
                valuesPropostas.add(PAInput.readString("Número do Aluno: "));
                TratamentoErros.trataErros(fsm.addProposta(valuesPropostas));
            }
        }
    }

    private void editarPropostas(){
        boolean finish = false;
        while (!finish) {
            switch (PAInput.chooseOption("Escolha uma opção:", "Editar Titulo", "Editar Ramo", "Voltar")) {
                case 1 -> TratamentoErros.trataErros(fsm.setTitulo(PAInput.readString("Código: "), PAInput.readString("Novo titulo: ")));
                case 2 -> {
                    String id = PAInput.readString("Código: ");
                    String ramo = (switch (PAInput.chooseOption("Ramo: ", "DA", "RAS", "SI", "DA|RAS", "DA|SI", "RAS|SI", "DA|RAS|SI")) {
                        case 1 -> "DA";
                        case 2 -> "RAS";
                        case 3 -> "SI";
                        case 4 -> "DA|RAS";
                        case 5 -> "DA|SI";
                        case 6 -> "RAS|SI";
                        case 7 -> "DA|RAS|SI";
                        default -> ""; //o compilador dava erro se não fosse acrescentado esta linha
                    });
                    TratamentoErros.trataErros(fsm.setRamo(id,ramo));
                }
                case 3 -> finish = true;
            }
        }
    }

    //Candidaturas
    private void OpcoesCandidaturaUI() {
        System.out.println("\n" + fsm.getState());
        if(fsm.isFechado()) {
            gestaoOpcoesCandidaturaFechadaUI();
            return;
        }
        switch (PAInput.chooseOption("Escolha uma opção: ", "Adicionar Candidatura", "Remover Candidaturas",
                "Importar Candidaturas", "Exportar Candidaturas","Editar candidaturas", "Mostrar candidaturas", "Informação",
                "Volta para a fase anterior", "Fase seguinte", "Save","Fechar fase")) {
            case 1 -> adicionarCandidaturas();
            case 2 -> TratamentoErros.trataErros(fsm.removeCandidatura(PAInput.readLong("Número: ")));
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheiro (.csv): ");
                fsm.importFile(new File(filename));
            }
            case 4 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 5 -> {
                String val;

                List<String> values = new ArrayList<>();
                values.add(PAInput.readString("Numero: "));
                do {
                    val = PAInput.readString(" Id Proposta [\'exit\' para sair]: ");
                    if (!val.equalsIgnoreCase("exit"))
                        values.add(val);
                } while (!val.equalsIgnoreCase("exit"));

                TratamentoErros.trataErros(fsm.setCandidaturas(values));
            }
            case 6 -> System.out.println(fsm.getTodasCandidaturas());
            case 7 -> getInfoCandidaturas();
            case 8 -> fsm.voltar();
            case 9 -> fsm.seguinte();
            case 10 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
            case 11 -> TratamentoErros.trataErros(fsm.fecharFase());
        }
    }

    private void gestaoOpcoesCandidaturaFechadaUI() {
        System.out.println("Atenção: Estado fechado");
        switch (PAInput.chooseOption("Escolha uma opção: ","Mostrar candidaturas", "Informação","Exportar candidaturas",
                "Volta para a fase anterior", "Fase seguinte")){
            case 1 -> System.out.println(fsm.getTodasCandidaturas());
            case 2 -> getInfoCandidaturas();
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 4 -> fsm.voltar();
            case 5 -> fsm.seguinte();
        }
    }

    private void getInfoCandidaturas() {
        boolean v1 = false;
        boolean v2;
        while (!v1) {
            switch (PAInput.chooseOption("Escolha uma opção: ", "Obter lista de alunos", "Obter lista de propostas", "Voltar")) {
                case 1 -> {
                    v2 = false;
                    while (!v2) {
                        switch (PAInput.chooseOption("Escolha uma opção: ", "Com autoproposta", "Com candidatura já registada", "Sem candidatura já registada", "Voltar")) {
                            case 1 -> System.out.println(fsm.getInfoCandidaturas("aluno", "Com autoproposta"));
                            case 2 -> System.out.println(fsm.getInfoCandidaturas("aluno", "Com candidatura já registada"));
                            case 3 -> System.out.println(fsm.getInfoCandidaturas("aluno", "Sem candidatura já registada"));
                            case 4 -> v2 = true;
                        }
                    }
                }
                case 2 -> {
                    v2 = false;
                    while (!v2) {
                        switch (PAInput.chooseOption("Escolha uma opção: ", "Autopropostas de alunos",
                                "Propostas de docentes", "Propostas com candidaturas", "Propostas sem candidatura", "Sem filtros", "Voltar")) {
                            case 1 -> System.out.println(fsm.getInfoCandidaturas("propostas", "Autopropostas de alunos"));
                            case 2 -> System.out.println(fsm.getInfoCandidaturas("propostas", "Propostas de docentes"));
                            case 3 ->
                                    System.out.println(fsm.getInfoCandidaturas("propostas", "Propostas com candidaturas"));
                            case 4 ->
                                    System.out.println(fsm.getInfoCandidaturas("propostas", "Propostas sem candidatura"));
                            case 5 -> System.out.println(fsm.getInfoCandidaturas("propostas", ""));
                            case 6 -> v2 = true;
                        }
                    }
                }
                case 3 -> v1 = true;
            }
        }
    }

    private void adicionarCandidaturas() {

        String val;


        List<String> values = new ArrayList<>();
        values.add(PAInput.readString("Numero: "));

        do {
            val = PAInput.readString(" Id Proposta [\'exit\' para sair]: ");
            if (!val.equalsIgnoreCase("exit"))
                values.add(val);
        } while (!val.equalsIgnoreCase("exit"));

        TratamentoErros.trataErros(fsm.addCandidatura(values));
    }

    //AtPropostas
    private void AtPropostasUI() {
        System.out.println("\n" + fsm.getState());
        if(fsm.isFechado()) {
            gestaoAtPropostasFechadaUI();
            return;
        }
        switch (PAInput.chooseOption("Escolha uma opção: ", "Atribuir automáticamente as propostas","Atribuir manualmente um aluno",
                "Remove Atribuição de Proposta com o Id Proposta","Remove Atribuição de Proposta com o Nº de aluno",
                "Exportar Atribuição de Propostas","Redo","Undo", "Mostrar atribuição de propostas",
                "Volta para a fase anterior", "Fase seguinte", "Save", "Fechar fase")) {
            case 1 -> fsm.atribuirAutomaticamente();
            case 2 -> TratamentoErros.trataErros(fsm.addAtPropostasManualmente(PAInput.readString("Código da proposta: "),PAInput.readLong("Nº Aluno: ")));
            case 3 -> TratamentoErros.trataErros(fsm.removeAtPropostasProp(PAInput.readString("Código da proposta: ")));
            case 4 -> TratamentoErros.trataErros(fsm.removeAtPropostasAluno(PAInput.readLong("Nº de aluno: ")));
            case 5 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 6 -> System.out.println("Redo -> " + fsm.execute());
            case 7 -> System.out.println("Undo -> " + fsm.undo());
            case 8 -> System.out.println(fsm.getAtPropostas());
            case 9 -> fsm.voltar();
            case 10 -> fsm.seguinte();
            case 11 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
            case 12 -> TratamentoErros.trataErros(fsm.fecharFase());
        }
    }

    boolean terminaAtPropostasAutomaticas;
    private void AtPropostasAutomaticasUI() {

        System.out.println("\n" + fsm.getState());
        terminaAtPropostasAutomaticas = false;

        while(!terminaAtPropostasAutomaticas) {
            Erros erro = fsm.addAtPropostas();

            if(erro.equals(Erros.NONE))
                terminaAtPropostasAutomaticas = true;
            else {
                System.out.println("Atenção: Ouve um empate na atribuição de propostas!");
                System.out.println("\n" + fsm.getAlunosEmpatados());
                do {
                    erro = fsm.desempate(PAInput.readString("\tIndique o numero de aluno que pretende desempatar: "));
                } while (erro.equals(Erros.VALOR_INCORRETO));
            }
        }

        fsm.seguinte();
    }
    private void gestaoAtPropostasFechadaUI() {
        System.out.println("Atenção: Estado fechado");
        switch (PAInput.chooseOption("Escolha uma opção: ","Mostrar atribuição de propostas", "Volta para a fase anterior",
                "Exportar Atribuição de Propostas","Fase seguinte")){
            case 1 -> System.out.println(fsm.getAtPropostas());
            case 2 -> fsm.voltar();
            case 3 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 4 -> fsm.seguinte();
        }
    }

    //AtOrientadores
    private void AtOrientadoresUI() {
        System.out.println("\n" + fsm.getState());
        switch (PAInput.chooseOption("Escolha uma opção: ", "Atribuir automáticamente os orientadores","Adiciona orientadores manualmente",
                "Remover uma proposta","Remover todas as propostas de um docente (Não elimina as porpostas que o docente porpôs)","Editar atribuição orientador",
                "Redo","Undo"
                , "Mostrar Orientadores", "Exportar Atribuição de Orientadores",
                "Volta para a fase anterior", "Fase seguinte", "Save","Fechar fase")) {
            case 1 -> fsm.addAtOrientadores();
            case 2 -> TratamentoErros.trataErros(fsm.addManOrientadores(PAInput.readString("E-mail docente: "), PAInput.readString("Id proposta: ")));
            case 3 -> TratamentoErros.trataErros(fsm.removeOrientadorProposta(PAInput.readString("Id proposta: ")));
            case 4 -> TratamentoErros.trataErros(fsm.removeOrientadorTodasProposta(PAInput.readString("E-mail do docente: ")));
            case 5 -> TratamentoErros.trataErros(fsm.editAtOrientadores(PAInput.readString("Código da Proposta: "),PAInput.readString("E-mail novo docente: ")));
            case 6 -> fsm.redoAtOrientadores();
            case 7 -> fsm.undoAtOrientadores();
            case 8 -> System.out.println(fsm.mostraPropostasOrientadas());
            case 9 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 10 -> fsm.voltar();
            case 11 -> {
                if(!fsm.seguinte())
                    System.out.println("Impossível passar para a próxima fase!");
            }
            case 12 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
            case 13 -> TratamentoErros.trataErros(fsm.fecharFase());
        }
    }


    //Consulta
    private void ConsultaUI() {
        System.out.println("\n" + fsm.getState());
        switch (PAInput.chooseOption("Escolha uma opção: ", "Consultar alunos atribuidos", "Consultar alunos não atribuidos", "Consultar propostas disponiveis",
                "Consultar propostas atribuidas","Orientações por docente","Media de orientações","Mínimo de orientações","Máximo de orientações",
                "Número de orientações de um docente", "Exportar informação","Save", "Sair")) { //retirar a funcionalidade de voltar
            case 1 -> System.out.println(fsm.alunosAtribuidos());
            case 2 -> System.out.println(fsm.alunosNAtribuidos());
            case 3 -> System.out.println(fsm.propDisponiveis());
            case 4 -> System.out.println(fsm.orientacoesPorDocente());
            case 5 -> {
                float aux = fsm.mediaOrientacoes();
                if(aux == -1)
                    System.out.println("Não existem dados a mostrar!");
                else
                    System.out.printf("Média de orientações -> %f\n",aux);
            }
            case 6 ->{
                int aux = fsm.minimoOrientacoes();
                if(aux == -1)
                    System.out.println("Não existem dados a mostrar!");
                else
                    System.out.printf("Minimo de orientações -> %d\n",aux);
            }
            case 7 -> {
                int aux = fsm.maximoOrientacoes();
                if(aux == -1)
                    System.out.println("Não existem dados a mostrar!");
                else
                    System.out.printf("Máximo de orientações -> %d\n",aux);
            }
            case 8 ->{
                String docente = PAInput.readString("E-mail do docente: ");
                int aux = fsm.numeroOrientacoesDocente(docente);
                if(aux == -1)
                    System.out.println("Não existem dados a mostrar!");
                else
                    System.out.printf("O número de orientações do docente %s é %d\n",fsm.getNomeDocente(docente),aux);
            }
            case 9 -> {
                String filename = PAInput.readString("Nome do ficheir (.csv): ");
                fsm.exportFile(new File(filename));
            }
            case 10 -> {
                String filename = PAInput.readString("Nome ficheiro (.dat): ");
                fsm.save(new File(filename));
            }
            case 11 -> fsm.terminar();
        }
    }

}