#include "Zona.h"
#include <bits/stdc++.h>

using namespace std;

int getNArvores(){
    static default_random_engine e(time(0));
    static uniform_int_distribution<int> d(20,40);
    return d(e);
}

Floresta::Floresta (int lA,int q,bool d,int p) : Zona("flr"),limiteArvores(lA),quantidade(q),demissao(d),producao(p) {
    arvores = getNArvores();
}

void Floresta::removeArvores(int q) {
    arvores-=q;
}

void Floresta::adicionaArvores(int q){
    if(getLimiteArvores()>=getArvores()+q){
        arvores+=q;
    }
}

void Floresta::addConstrucao() {
    if(!construcao)
        construcao = true;
}

void Floresta::removeConstrucao() {
    if(construcao)
        construcao = false;
}

int Zona::getArvores(){
    if(getZona() == "flr")
        return getArvores();
    return -1;
}

void Deserto::addConstrucao() {
    if(!construcao)
        construcao = true;
}

void Deserto::removeConstrucao() {
    if(construcao)
        construcao = false;
}

void Pantano::addConstrucao() {
    if(!construcao)
        construcao = true;
}

void Pantano::removeConstrucao() {
    if(construcao)
        construcao = false;
}

void Montanha::addConstrucao() {
    if(!construcao)
        construcao = true;
}

void Montanha::removeConstrucao() {
    if(construcao)
        construcao = false;
}

void ZonaX::addConstrucao() {
    if(!construcao)
        construcao = true;
}

void ZonaX::removeConstrucao() {
    if(construcao)
        construcao = false;
}

void Pastagem::addConstrucao() {
    if(!construcao)
        construcao = true;
}

void Pastagem::removeConstrucao() {
    if(construcao)
        construcao = false;
}