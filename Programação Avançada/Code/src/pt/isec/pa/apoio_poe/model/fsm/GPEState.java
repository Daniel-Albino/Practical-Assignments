package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;


import java.io.Serializable;
import java.util.Map;

public enum GPEState implements Serializable {
    INICIO,CONFIGURACAO,GESTAO_ALUNOS,GESTAO_DOCENTES,GESTAO_PROPOSTAS,OPCOES_CANDIDATURA,
    ATRIBUICAO_PROPOSTAS,ATRIBUICAO_AUTOMATICA_PROPOSTAS,ATRIBUICAO_ORIENTADORES,CONSULTA;

    IGPEState createState(GPEContext context, DataManager data){
        return switch (this){
            case INICIO -> new Inicio(context,data);
            case CONFIGURACAO -> new Configuracao(context,data);
            case GESTAO_ALUNOS -> new GestaoAlunos(context,data);
            case GESTAO_DOCENTES -> new GestaoDocentes(context,data);
            case GESTAO_PROPOSTAS -> new GestaoPropostas(context,data);
            case OPCOES_CANDIDATURA -> new OpcoesCandidatura(context,data);
            case ATRIBUICAO_PROPOSTAS -> new AtribuicaoPropostas(context,data);
            case ATRIBUICAO_AUTOMATICA_PROPOSTAS -> new AtribuicaoAutomaticaPropostas(context,data);
            case ATRIBUICAO_ORIENTADORES -> new AribuicaoOrientadores(context,data);
            case CONSULTA -> new Consulta(context,data);
        };
    }

}
