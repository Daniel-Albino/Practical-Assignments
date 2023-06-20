#ifndef TABULEIRO_H
#define TABULEIRO_H

typedef struct tabuleiro Itab,*pItab;

struct tabuleiro {
    int coluna,linha;
    int ganhou,n_jogadas,termina,jogador,jogadorA,pedra1,pedra2,lc1,lc2;
    char **tabuleiro;
    pItab prox;
};

void initRandom();

int intUniformRnd(int a, int b);

void inicioJogo(pItab x);

char **inicializa_tab(pItab x);

void escreveTabela(pItab x);

char **aumentaTabela(pItab y,int c);

void finalJogo(pItab x);

#endif /* TABULEIRO_H */