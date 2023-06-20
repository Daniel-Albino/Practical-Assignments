#ifndef POO_2_FICHEIROS_H
#define POO_2_FICHEIROS_H

#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include "Jogo.h"
#include "Mapa.h"

void infoJogo(std::string nfich); //informação do jogo logo no inicio!
void infoEspecifica(std::string nfich); //informações especificas -> para ver nome dos edificios por exemplo!
void ficheiroExec(std::string nfich, InfoIlha II,Mapa **ilha,Dia *d,std::vector<Recursos*> recursos,vector<string> *config);
#endif //POO_2_FICHEIROS_H
