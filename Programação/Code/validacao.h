#ifndef SEMAFORO_VALIDACAO_H
#define SEMAFORO_VALIDACAO_H
#include "tabuleiro.h"

char **escolheJogada(pItab x,int op2,int *v,pItab lista);

int validacao_entrada(pItab x,int op2, int posl, int posc, int lc,int k);

char verificajogada(pItab x,int op2,int posl,int posc);

int linha (pItab x);

int coluna (pItab x);

int diagonal(pItab x);

#endif //SEMAFORO_VALIDACAO_H
