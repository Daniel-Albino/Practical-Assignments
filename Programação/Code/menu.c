#include <stdio.h>
#include "menu.h"

//Inicio do jogo (Novo jogo ou carregar jogo)
void menu1 (pItab x, int op1,pItab lista){
    int o;
    puts("\n-----Jogo do semaforo-----");
    puts("\t1. Novo jogo.");
    puts("-------------------------");
    do{
        printf("Opção: ");
        scanf("%d",&op1);
        if(op1 != 1)
            printf("\nAtenção! Deve colocar 1 para escolher a opção!\n");
    }while(op1 != 1);

    switch (op1)
    {
        case 1:
            do {
                printf("2 Jogadores ou 1 Jogador?\n\tOpção: ");
                scanf("%d", &o);
                if(o != 1 && o != 2)
                    printf("\nAtenção! Introduziu uma opção inválida. Deve indicar o número de jogadores.\n");
            }while(o != 1 && o != 2);
            if(o == 1)
                x->jogadorA = 1;
            inicioJogo(x);
            printf("\nA tabela terá as seguintes dimensões -> ");
            printf("Linhas: %d\tColunas: %d\n",x->linha,x->coluna);
            x->tabuleiro = inicializa_tab(x);
            escreveTabela(x);
            break;
    }
}

//Função que apresenta as possibilidades que o jogador tem
void menu2 (pItab x, int op2,pItab lista){

    int v;
    char nomefich[100];


    do{
        do{
            if (x->n_jogadas == 0){
                printf("\nO jogador 1 irá colocar uma peça verde.");
                op2 = 1;
            }else {
                printf("\nÉ a vez do jogador %d\n", x->jogador);
                printf("\n----- Menu -----\n");
                printf("O que pretende fazer?\n");
                printf("\t1. Colocar uma peça verde.\n");
                printf("\t2. Mudar a cor.\n");
                printf("\t3. Adicionar uma pedra.\n");
                printf("\t4. Adiciona uma linha ou coluna.\n");
                printf("\t5. Ver as jogadas anteriores.\n");
                printf("\t0. Terminar o jogo.\n");
                puts("-------------------------");


                do {
                    printf("Opção: ");
                    scanf("%d", &op2);
                    if (op2 != 1 && op2 != 2 && op2 != 3 && op2 != 4 && op2 != 5 && op2 != 0)
                        printf("\nAtenção! Deve colocar uma opção valida!!\n");
                } while ((op2 != 1 && op2 != 2 && op2 != 3 && op2 != 4 && op2 != 5 && op2 != 0) || verificaLCP(x,op2) == 1);
            }

        x->tabuleiro = escolheJogada(x, op2, &v,lista);

        }while (v == 0);


        if(op2 != 5 && op2 != 0){
            escreveTabela(x);

            x->n_jogadas += 1;

            lista = adiciona_lista(lista,x);

            verificajogo(x);
        }


    }while(verifica_fim(x) == 0);

    if(x->termina == 0){
        printf("\nO jogo vai criar um ficheiro de texto para que possa ver as jogadas efetuadas!\n");
        printf("\tPor favor introduza um nome para o ficheiro: ");
        scanf("%s",nomefich);
        gravar_texto(lista,nomefich);
    }

    finalJogo(x);

}




//Função que verifica se o jogador ganhou / muda de jogador
void verificajogo (pItab x){
    if(linha(x) == 1 || coluna(x) == 1 || diagonal(x)==1)
        x->ganhou = 1;
    else if (x->jogador == 1)
            x->jogador = 2;
    else if(x->jogador == 2)
            x->jogador = 1;
}

//Verifica se o jogador 1 ou 2 já usou uma pedra/acrescentou uma linha ou uma coluna
int verificaLCP (pItab x, int op2){
    if(op2 == 3) {
        if (x->jogador == 1){
            if(x->pedra1 == 1){
                puts("\n O jogador 1 esgotou as hipóteses de usar uma pedra!");
                return 1;
            }
        }
        else
            if(x->pedra2 == 1) {
                puts("\n O jogador 2 esgotou as hipóteses de usar uma pedra!");
                return 1;
            }
    }
    if(op2 == 4) {
        if (x->jogador == 1) {
            if (x->lc1 == 2){
                puts("\n O jogador 1 esgotou as hipóteses de aumentar uma linha ou uma coluna!");
                return 1;
            }
        }
        else
            if (x->lc2 == 2){
                puts("\n O jogador 2 esgotou as hipóteses de aumentar uma linha ou uma coluna!");
                return 1;
            }
    }

    return 0;
}

int verifica_fim(pItab x){
    if(x->ganhou == 1)
        return 1;
    if(x->termina == 1)
        return 1;

    return 0;
}