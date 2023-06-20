#ifndef TRABALHO_PRATICO_PROGRAMACAO_FICHEIRO_H
#define TRABALHO_PRATICO_PROGRAMACAO_FICHEIRO_H
#include "tabuleiro.h"
#include "menu.h"
#include "validacao.h"


pItab adiciona_lista(pItab lista, pItab x);

void mostra_lista(Itab p);

void mostra_todos(pItab lista,pItab x, int k);

void grava_binario(pItab lista, char *filename);

pItab ler_binario(char *filename);

void recarrega_var(pItab lista,pItab x);

void gravar_texto(pItab lista, char *filename);

#endif //TRABALHO_PRATICO_PROGRAMACAO_FICHEIRO_H
