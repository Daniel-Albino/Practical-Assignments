#include <stdio.h>
#include <stdlib.h>
#include "ficheiro.h"
#include "tabuleiro.h"


//Lista ligada
pItab adiciona_lista(pItab lista, pItab x){

    char **tab = NULL;
    int i;
    int l,c;

    //Cria um novo tabuleiro
    tab =(char **) malloc(x->linha * sizeof(char *));
    if(tab == NULL){
        printf("\nErro a alocar memória!\n");
        return lista;
    }
    for (i=0; i<x->linha; i++) {
        tab[i] = (char *) malloc(x->coluna * sizeof(char));
        if (tab[i] == NULL) {
            printf("\nErro a alocar memória!\n");
            return lista;
        }
    }

    //Copia o tabuleiro para o novo tabuleiro
    for (l = 0; l < x->linha; l++) {
        for (c = 0; c < x->coluna; c++) {
            tab[l][c] = x->tabuleiro[l][c];
        }
    }


    pItab novo_tab = malloc(sizeof (Itab));
    if(novo_tab == NULL){
        fprintf(stderr,"Erro a alocar memória!");
        return lista;
    }

    *novo_tab = *x;
    novo_tab->tabuleiro = tab;  //copia para a lista ligada o novo tabuleiro

    if(lista == NULL){
        lista = novo_tab;
        novo_tab->prox = NULL;
    } else {
        pItab aux = lista;
        while(aux->prox != NULL)
            aux = aux->prox;
        aux->prox = novo_tab;
        novo_tab->prox = NULL;
    }


    return lista;

}

void mostra_lista(Itab p){
    printf("\nJogador = %d\t Nº Jogadas = %d\n",p.jogador,p.n_jogadas);
    escreveTabela(&p);
}

void mostra_todos(pItab lista,pItab x,int k){
    int conta = 0,y = (x->n_jogadas - k);

    if(!lista){
        puts("\nA lista está vazia!");
        return;
    }

    pItab aux = lista;

    while(aux != NULL && conta < x->n_jogadas){
        if(conta < y)
            aux = aux->prox;
        else {
            mostra_lista(*aux);
            aux = aux->prox;
        }
        conta ++;
    }
}

//Ficheiro texto
void gravar_texto(pItab lista, char *filename){
    FILE *f = fopen(filename,"wt");
    if(!f){
        fprintf(stderr,"Erro a abrir o ficheiro %s!\n",filename);
        return;
    }

    for(pItab aux = lista;aux != NULL;aux = aux->prox) {
        fprintf(f, "\nJogador = %d,Número de jogadas = %d\nTabuleiro:", aux->jogador, aux->n_jogadas);
        for (int l = 0; l < lista->linha; l++) {
            fprintf(f,"\n");
            for (int c = 0; c < lista->coluna; c++) {
                fprintf(f," %c ",aux->tabuleiro[l][c]);
            }
        }
    }

    fclose(f);
}