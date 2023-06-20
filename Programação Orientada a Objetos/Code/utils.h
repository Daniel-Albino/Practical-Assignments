#ifndef POO_2_UTILS_H
#define POO_2_UTILS_H

#include <iostream>
#include <random>
#include <ctime>
#include <vector>
#include <bits/stdc++.h>
#include "Jogo.h"

using namespace std;

float getRealUniform();
vector<Zona*> escolheZonas(InfoIlha II);
bool verificaTipoEdificio(string e);
bool Linhas(int x,InfoIlha II);
bool Colunas(int x,InfoIlha II);
bool verificaTrabalhador(string t);
string nomeTrabalhador(string t);
bool verificaStoi(string strL,string strC, int *l,int *c);
bool verificaRecursosEdificios(Mapa *ilha, vector<Recursos*> recursos,string modoPagamento);
string metodoPagamentoEdificios();
bool verificaRespostaPagamentoEdificios(string *modoPagamento);
bool verificaRecursosEdificiosBateria(Mapa *ilha,vector<Recursos*> recursos);
bool verificaRecursosTrabalhadores(Mapa *ilha,vector<Recursos*>recursos,string id);
bool verificaVende(string *comando);
bool verificaEdificioPe(int l,int c,Mapa **ilha,InfoIlha II,string Ed,int *L1,int *C1,int *L2,int *C2);
void removeRecursosEdificios(string comando, InfoIlha II, int quantidade, Mapa **ilha);
bool verificaFimJogo(InfoIlha II,Mapa **ilha,vector<Recursos*>recursos);
#endif //POO_2_UTILS_H
