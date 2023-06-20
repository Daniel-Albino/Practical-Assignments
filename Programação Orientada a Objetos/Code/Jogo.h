#ifndef POO_2_JOGO_H
#define POO_2_JOGO_H

#include <iostream>
#include <string>
#include <vector>
#include <bits/stdc++.h>
#include "Interface.h"
#include "Mapa.h"
#include "Dia.h"

using namespace std;

void inicioJogo();

std::vector<Recursos*> Recurso(std::vector<Recursos*> r);

void limpaCin();

void Zonas(Mapa **ilha,InfoIlha II,std::vector<Zona*>v);

void imprimeIlha(InfoIlha II,Mapa **ilha, Dia *d,std::vector<Recursos*> recursos,vector<string> *config);

void comandos(InfoIlha II,Mapa **ilha,Dia *d,std::vector<Recursos*> recursos,vector<string> *config);

bool verificaComando(std::string comando);

std::vector<std::string> divideComando(std::string c);

void executaComando(std::vector<std::string> comando, InfoIlha II,Mapa **ilha,Dia *d,std::vector<Recursos*> recursos,vector<string> *config);

#endif //POO_2_JOGO_H
