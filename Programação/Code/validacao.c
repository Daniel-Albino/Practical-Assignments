#include <stdio.h>
#include "validacao.h"
#include "ficheiro.h"

//Escolha das jogadas

char **escolheJogada(pItab x,int op2,int *v,pItab lista){
    int posl,posc,lc,k,t;
    char c;
    char **aux = x->tabuleiro;
    *v = 1;
    if(op2 == 1){
        do{
            printf("\n\tIndique a linha: ");
            scanf("%d",&posl);
            printf("\n\tIndique a coluna: ");
            scanf("%d",&posc);

        }while(validacao_entrada(x,op2,posl,posc,lc,k) == 1);
        c = verificajogada(x,op2,posl,posc);
        if(c == '0'){
            puts("\nJogada inválida!\n");
            *v = 0;
            return x->tabuleiro;
        }else{
            aux[(posl-1)][(posc-1)] = c;
            return aux;
        }
    }
    if(op2 == 2){
        do{
            printf("\n\tIndique a linha: ");
            scanf("%d",&posl);
            printf("\n\tIndique a coluna: ");
            scanf("%d",&posc);

        }while(validacao_entrada(x,op2,posl,posc,lc,k) == 1);
        c = verificajogada(x,op2,posl,posc);
        if(c == '0'){
            puts("\nJogada inválida!\n");
            *v = 0;
            return x->tabuleiro;
        }else{
            aux[(posl - 1)][(posc - 1)] = c;
            return aux;
        }
    }
    if(op2 == 3){
        do{
            printf("\n\tIndique a linha: ");
            scanf("%d",&posl);
            printf("\n\tIndique a coluna: ");
            scanf("%d",&posc);
        }while(validacao_entrada(x,op2,posl,posc,lc,k) == 1);
        c = verificajogada(x,op2,posl,posc);
        if(c == '0'){
            puts("\nJogada inválida!\n");
            *v = 0;
            return x->tabuleiro;
        }else{
            aux[(posl - 1)][(posc - 1)] = c;
            return aux;
        }
    }
    if(op2 == 4){
        do{
            printf("Pretende aumentar a linha (1) ou a coluna (2): ");
            scanf("%d",&lc);
        }while(validacao_entrada(x,op2,posl,posc,lc,k) == 1);
        aux = aumentaTabela(x, lc);
        return aux;
    }

    if(op2 == 5) {
        do {
            printf("Indique quantas jogadas quer ver: ");
            scanf("%d", &k);
        } while (validacao_entrada(x, op2, posl, posc, lc, k) == 1);
        mostra_todos(lista,x,k);
    }

    if(op2 == 0){
        x->termina = 1;
    }
    return x->tabuleiro;
}




int validacao_entrada(pItab x,int op2, int posl, int posc, int lc,int k){
    if(op2 == 1) {
        if ((posl < 1 || posl > x->coluna * x->linha) != 0 && (posc < 1 || posc > x->coluna * x->linha)|| x->tabuleiro[(posl - 1)][(posc - 1)] != '_' || x->tabuleiro[posl - 1][posc - 1] == 'P') {
            printf("\nAtenção!\n\tIntroduziu uma linha ou uma coluna invalidas!\n\tVolte a indicar a linha e a coluna!\n");
            return 1;
        }
    }

    if(op2 == 2){
        if((posl < 1 || posl > x->coluna * x->linha) != 0 && (posc < 1 || posc > x->coluna * x->linha)|| x->tabuleiro[(posl - 1)][(posc - 1)] == '_' || x->tabuleiro[(posl - 1)][(posc - 1)] == 'P') {
            printf("\nAtenção!\n\tIntroduziu uma linha ou uma coluna invalidas!\n\tVolte a indicar a linha e a coluna!\n");
            return 1;
        }
    }

    if(op2 == 3){
        if((posl < 1 || posl > x->coluna * x->linha) != 0 && (posc < 1 || posc > x->coluna * x->linha)|| x->tabuleiro[(posl - 1)][(posc - 1)] != '_') {
            printf("\nAtenção!\n\tIntroduziu uma linha ou uma coluna invalidas!\n\tVolte a indicar a linha e a coluna!\n");
            return 1;
        }
    }

    if(op2 == 4){
        if(lc != 1 && lc != 2) {
            printf("\nAtenção!\n\tDeve colocar 1 ou 2 para escolher a opção!\n");
            return 1;
        }
    }

    if(op2 == 5){
        if(k > x->n_jogadas){
            printf("\nAtenção!\n\tDeve indicar um valor inferior a %d!\n",x->n_jogadas);
            return 1;
        }
    }

    return 0;
}

//Verifica a jogada
char verificajogada(pItab x,int op2,int posl,int posc){
    if(op2 == 1)
        return 'G';
    else if(op2 == 2) {
        if (x->tabuleiro[(posl-1)][(posc-1)] == 'Y')
            return 'R';
        else if(x->tabuleiro[(posl-1)][(posc-1)] == 'G')
            return 'Y';
    }
    if (op2 == 3){
        if(x->jogador == 1)
            x->pedra1 += 1;
        else
            x->pedra2 += 1;
        return 'P';
    }

    return '0';
}

//Verifica se existe uma linha completa por 'G'/'Y'/'R'
int linha (pItab x){
    int l,c;

    for(l=0; l<x->linha; l++)
        if(x->tabuleiro[l][0] != '_'){
            for(c=0; c<x->coluna-1 && x->tabuleiro[l][c] == x->tabuleiro[l][c+1]; c++);
            if(c == x->coluna-1)
                return 1;
        }
    return 0;
}

//Verifica se existe uma coluna completa por 'G'/'Y'/'R'
int coluna (pItab x){
    int l,c;

    for(c=0; c<x->coluna; c++)
        if(x->tabuleiro[0][c] != '_'){
            for(l=0; l<x->linha-1 && x->tabuleiro[l][c] == x->tabuleiro[l+1][c]; l++);
            if(l == x->linha-1)
                return 1;
        }
    return 0;
}

//Verifica se existe uma diagonal completa por 'G'/'Y'/'R'
int diagonal(pItab x) {
    int l, c;

    if (x->linha == x->coluna) {
        if (x->tabuleiro[0][0] != '_') {
            for (l = c = 0; l < x->linha - 1 && x->tabuleiro[l][c] == x->tabuleiro[l + 1][c + 1]; l++, c++);
            if (l == x->linha - 1)
                return 1;
        } else if (x->tabuleiro[x->linha - 1][0] != '_') {
            for (c = 0, l = x->linha - 1; c < x->coluna - 1 && x->tabuleiro[l][c] == x->tabuleiro[l - 1][c + 1]; l--, c++);
            if (l == 0)
                return 1;
        }
    }
    return 0;
}
