#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "tabuleiro.h"

void initRandom(){
    srand(time(NULL));
}

int intUniformRnd(int a, int b){
    return a + rand()%(b-a+1);
}

//Inicializa todas as variáveis necessárias para começar um novo jogo
void inicioJogo(pItab x){
    x->linha = x->coluna = intUniformRnd(3,5);
    x->jogador = 1;
    x->ganhou = 0;
    x->n_jogadas = 0;
    x->termina = 0;
    x->tabuleiro = NULL;
    x->pedra2 = x->pedra1 = 0;
    x->lc1 = x->lc2 = 0;
}

//Inicializa o tabuleiro
char **inicializa_tab(pItab x) {
    int i, j;
    char **aux = NULL;

    aux =(char **) malloc(x->linha * sizeof(char *));
    if(aux == NULL){
        printf("\nErro a alocar memória!\n");
        return x->tabuleiro;
    }
    for (i=0; i<x->coluna; i++) {
        aux[i] = (char *) malloc(x->coluna * sizeof(char));
        if (aux[i] == NULL) {
            printf("\nErro a alocar memória!\n");
            return x->tabuleiro;
        }
    }
    for (i = 0; i < x->linha; i++) {
        for (j = 0; j < x->coluna; j++)
            aux[i][j] = '_';
    }
    return aux;

}

//Escreve o tabuleiro na consola
void escreveTabela(pItab x){
    int c, l;

    printf("\n");
    for(l=0; l<x->linha; l++){
        for(c=0; c<x->coluna; c++)
            printf(" %c ", x->tabuleiro[l][c]);
        printf("\n");
    }
}

//Aumenta as linhas e as colunas ao tabuleiro
char **aumentaTabela(pItab y,int c){
    int i,j;
    char **aux = NULL;

    if(y->jogador == 1)
        y->lc1 += 1;
    else
        y->lc2 += 1;

    aux = y->tabuleiro;
    if(c == 1) {    //Linhas
        aux = (char **)realloc(y->tabuleiro,(y->linha + 1) * sizeof(char*));
        if(aux == NULL){
            printf("\nErro a alocar memória!\n");
            return y->tabuleiro;
        }

        aux[y->linha] = (char *) malloc(y->coluna * sizeof(char));
        if(aux[y->linha] == NULL){
            printf("\nErro a alocar memória!\n");
            return y->tabuleiro;
        }

        for( j = 0;j<y->coluna;j++)
            aux[y->linha][j] = '_';
        y->linha += 1;

        return aux;
    }
    if(c == 2){ //Colunas
        for(i = 0;i<y->linha;i++) {
            aux[i] = (char *) realloc(y->tabuleiro[i], (y->coluna + 1) * sizeof(char));
            if (aux[i] == NULL) {
                printf("\nErro a alocar memória!\n");
                return y->tabuleiro;
            }
        }
        for( j = 0;j<y->linha;j++)
            aux[j][y->coluna] = '_';
        y->coluna += 1;

        return aux;
    }

    return aux;
}

//Final do jogo
void finalJogo(pItab x){
    if(x->termina == 1)
        printf("\nO jogo terminou!\n");
    if (x->ganhou == 1)
        printf("\nGanhou o jogador %d!\n", x->jogador);

}