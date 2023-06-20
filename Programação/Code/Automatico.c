#include <stdio.h>
#include <stdlib.h>
#include "Automatico.h"

void menu3 (pItab x, int op2){

    int v;
    char nomefich[100];
    pItab lista = NULL;

    do{

        if(x->jogador == 1){

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
                            printf("\nAtenção! Deve colocar uma opção valida!\n");
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
    }else{
            printf("\nAguarde enquanto o jogador automático jogue!\n");
            x->tabuleiro = jogador_automatico(x,&v,lista);
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

char **jogador_automatico(pItab x,int *v,pItab lista){
    int op,l,c,lc;
    char car;
    char **aux = x->tabuleiro;
    initRandom();

   do{
        op = intUniformRnd(1,4);
    }while(verificaLCP_JA(x,op));

       if (op == 1) {
           do {
               l = intUniformRnd(1, x->linha);
               c = intUniformRnd(1, x->coluna);
           } while ((l < 1 || l > x->coluna * x->linha) != 0 || x->tabuleiro[(l - 1)][(c - 1)] != '_' || x->tabuleiro[l - 1][c - 1] == 'P');
           car = verificajogada(x, op, l, c);
           if (car == '0') {
               puts("\nJogada inválida!\n");
               *v = 0;
               return x->tabuleiro;
           } else {
               aux[(l - 1)][(c - 1)] = car;
               return aux;
           }
       }
       if (op == 2) {
           do {
               l = intUniformRnd(1, x->linha);
               c = intUniformRnd(1, x->coluna);
           } while ((l < 1 || l > x->coluna * x->linha) != 0 || x->tabuleiro[(l - 1)][(c - 1)] == '_' ||
                    x->tabuleiro[(l - 1)][(c - 1)] == 'P');
           car = verificajogada(x, op, l, c);
           if (car == '0') {
               puts("\nJogada inválida!\n");
               *v = 0;
               return x->tabuleiro;
           } else {
               aux[(l - 1)][(c - 1)] = car;
               return aux;
           }
       }
       if (op == 3) {
           do {
               l = intUniformRnd(1, x->linha);
               c = intUniformRnd(1, x->coluna);
           } while ((l < 1 || l > x->coluna * x->linha) != 0 || x->tabuleiro[(l - 1)][(c - 1)] != '_');
           car = verificajogada(x, op, l, c);
           if (car == '0') {
               puts("\nJogada inválida!\n");
               *v = 0;
               return x->tabuleiro;
           } else {
               aux[(l - 1)][(c - 1)] = car;
               return aux;
           }
       }
       if (op == 4) {
           do {
               lc = intUniformRnd(1, 2);
           } while (lc != 1 && lc != 2);
           aux = aumentaTabela(x, lc);
           return aux;
       }
    return x->tabuleiro;
}

int verificaLCP_JA (pItab x, int op){
    if(op == 3) {
        if(x->pedra2 == 1) {
            return 1;
        }
    }
    if(op == 4) {
        if (x->lc2 == 2){
            return 1;
        }
    }

    return 0;
}