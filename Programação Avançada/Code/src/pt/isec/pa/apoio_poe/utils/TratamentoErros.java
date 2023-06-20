package pt.isec.pa.apoio_poe.utils;

import pt.isec.pa.apoio_poe.model.data.Erros;

public final class TratamentoErros {
    private TratamentoErros() {}
    public static void trataErros(Erros erro){
        System.out.println(erro);
        if(erro == null) {
            System.out.println("Erro (Null)");
            return;
        }
        switch (erro){
            case NONE -> System.out.println("Operação realizada com sucesso!");
            case VALOR_INCORRETO -> System.out.println("Atenção: Introduziu um valor incorreto!");
            case EMPATE -> System.out.println("Atenção: Ouve um empate na atribuição de propostas! ");

            case ALUNO_ADD -> System.out.println("Atenção: Não foi possível adicionar o aluno!");
            case ALUNO_EXISTE -> System.out.println("Atenção: Já existe um aluno com esse número!");
            case ALUNO_ID -> System.out.println("Atenção: O número de aluno é invalido!");
            case ALUNO_EMAIL -> System.out.println("Atenção: O e-mail do aluno está incorreto!");
            case ALUNO_CURSO -> System.out.println("Atenção: O curso do aluno está incorreto!");
            case ALUNO_RAMO -> System.out.println("Atenção: O ramo do aluno está incorreto");
            case ALUNO_CLASSIFICACAO -> System.out.println("Atenção: A classificação do aluno é invalida!");
            case ALUNO_ESTAGIO -> System.out.println("Atenção: O estagio do aluno é invalido!");
            case ALUNO_REMOVE -> System.out.println("Atenção: O aluno não foi removido!");
            case ALUNO_EDIT_NOME -> System.out.println("Atenção: O nome do aluno não foi editado!");
            case ALUNO_EDIT_CLASSIFICAO -> System.out.println("Atenção: A classificação do aluno não foi editada!");
            case ALUNO_EDIT_ESTAGIO -> System.out.println("Atenção: A opção de ir a estágio/projeto não foi editada!");

            case DOCENTE_ADD -> System.out.println("Atenção: Não foi possível adicionar o docente!");
            case DOCENTE_REMOVE -> System.out.println("Atenção: Não foi possível remover o docente!");
            case DOCENTE_EMAIL -> System.out.println("Atenção: O e-mail do docente é invalido!");
            case DOCENTES_EXISTE -> System.out.println("Atenção: O docente já existe!");
            case DOCENTE_EDIT -> System.out.println("Atenção: Não foi possível editar o docente!");

            case PROPOSTA_ADD -> System.out.println("Atenção: Não foi possível adicionar a proposta!");
            case PROPOSTA_ID -> System.out.println("Atenção: Já existe uma proposta com esse ID!");
            case PROPOSTA_RAMO -> System.out.println("Atenção: O ramo é invalido");
            case PROPOSTA_DOCENTES -> System.out.println("Atenção: O docente não existe!");
            case PROPOSTA_REMOVE_FALTA_ID -> System.out.println("Atenção: Deverá indicar o id da proposta!");
            case PROPOSTA_REMOVE -> System.out.println("Atenção: Não foi possível remover a proposta!");
            case PROPOSTA_EDIT -> System.out.println("Atenção: Não foi possível editar a proposta!");
            case PROPOSTA_EDIT_NEXISTE -> System.out.println("Atenção: Não existem nenhuma proposta com esse código!");

            case CANDIDATURAS_ADD -> System.out.println("Atenção: Não foi possível adicionar a candidatura!");
            case CANDIDATURAS_ADD_ALUNO -> System.out.println("Atenção: O aluno não existe ou não está apto para se candidatar!");
            case CANDIDATURAS_ADD_ALUNOEXISTE -> System.out.println("Atenção: O aluno já realizou uma candidatura!");

            case ATRIBUICAO_ADD -> System.out.println("Atenção: Não foi possível atribuir a proposta ao aluno!");
            case ATRIBUICAO_ADD_NALUNO -> System.out.println("Atenção: O nº de aluno é invalido!");
            case ATRIBUICAO_ADD_ALUNO_RAMO -> System.out.println("Atenção: O ramo da proposta é incompatível com o ramo do aluno!");
            case ATRIBUICAO_ADD_IDPROPOSTA -> System.out.println("Atenção: O id da proposta é invalido!");
            case ATRIBUICAO_ADD_ATALUNO -> System.out.println("Atenção: Aluno já atribuido a uma proposta!");
            case ATRIBUICAO_ADD_ATPROPOSTA -> System.out.println("Atenção: Proposta já atribuida!");

        }

    }
}
