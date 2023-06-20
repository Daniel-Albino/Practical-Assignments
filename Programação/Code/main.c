#include <stdio.h>
#include <stdlib.h>
#include "tabuleiro.h"
#include "menu.h"
#include "ficheiro.h"
#include "Automatico.h"


int main(){
    int op1 = 0,op2 = 0;
    Itab x;
    pItab lista = NULL;
    x.jogadorA = 0;

    initRandom();

    menu1(&x,op1,lista);

    if(x.jogadorA == 1)
        menu3(&x,op2);
    else
        menu2(&x,op2,lista);

    free(x.tabuleiro);

    return 0;
}







