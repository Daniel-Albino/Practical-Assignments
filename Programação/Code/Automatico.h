#ifndef TRABALHO_PRATICO_PROGRAMACAO_AUTOMATICO_H
#define TRABALHO_PRATICO_PROGRAMACAO_AUTOMATICO_H
#include "ficheiro.h"
#include "menu.h"
#include "tabuleiro.h"
#include "validacao.h"

void menu3 (pItab x, int op2);
char **jogador_automatico(pItab x,int *v,pItab lista);
int verificaLCP_JA (pItab x, int op);

#endif //TRABALHO_PRATICO_PROGRAMACAO_AUTOMATICO_H
