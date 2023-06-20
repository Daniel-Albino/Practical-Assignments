#ifndef TRABALHO_PRATICO_PROGRAMACAO_MENU_H
#define TRABALHO_PRATICO_PROGRAMACAO_MENU_H
#include "validacao.h"
#include "tabuleiro.h"
#include "ficheiro.h"

void menu1 (pItab x, int op1,pItab lista);

void menu2 (pItab x, int op2,pItab lista);

void verificajogo (pItab x);

int verificaLCP (pItab x,int op2);

int verifica_fim(pItab x);

#endif //TRABALHO_PRATICO_PROGRAMACAO_MENU_H
