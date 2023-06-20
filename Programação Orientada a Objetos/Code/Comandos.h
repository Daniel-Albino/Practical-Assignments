#ifndef POO_COMANDOS_H
#define POO_COMANDOS_H
#include <iostream>
#include <vector>
#include "Jogo.h"
#include "Dia.h"
#include "Recursos.h"

using namespace std;

void comandoCons(vector<string> comando,InfoIlha II, Mapa **ilha,vector<Recursos*> recursos,vector<string> *config);
bool verificaResposta(string resposta);
void comandoDes(vector<string> comando,InfoIlha II, Mapa **ilha);
void comandoLiga(vector<string> comando,InfoIlha II, Mapa **ilha);
void comandoList(vector<string> comando,InfoIlha II, Mapa **ilha);
string zona(string z);
void comandoCont(vector<string> comando,InfoIlha II, Mapa **ilha,Dia *dia,vector<Recursos*> recursos,vector<string> *config);
void comandoMove(vector<string> comando,InfoIlha II, Mapa **ilha);
void comandoVende(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos);
void comandoNext(InfoIlha II, Mapa **ilha, vector<Recursos*> recursos,int da,int ds);
void comandoLevelUp(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos);
vector<string> comandoConfig(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos);
void comandoDebug(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos);
#endif //POO_COMANDOS_H
