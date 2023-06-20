#include "Edificios.h"

using namespace std;

Edificios::Edificios(std::string tipo,std::string p,int d) : tipo(tipo), produz(p), diasConstruido(d) {}

const std::string &Edificios::getEdificio() const {
    return tipo;
}

void Edificios::DesEdificio() {
    if(ligado){
        ligado = false;
        cout<<"\n\tAviso: O edificio desligado com sucesso!"<<endl;
    }
    else
        cout<<"Aviso: O edificio já se encontra desligado!"<<endl;
}

void Edificios::LigaEdificio() {
    if(!ligado){
        ligado = true;
        cout<<"\n\tAviso: O edificio ligado com sucesso!"<<endl;
    }
    else
        cout<<"Aviso: O edificio já se encontra ligado!"<<endl;
}

bool Edificios::isLigado(){
    if(ligado)
        return true;
    return false;
}

void Edificios::removeArmazem(int x) {
    if(getArmazem() >= 0){
        removeArmazem(x);
    }
}


bool MinaFerro::adicionaArmazem(int x) {
    if((armazem + x) <= armazena){
        armazem += x;
        return true;
    }
    return false;
}

int MinaFerro::getProducao() const {
    return producao;
}
bool MinaFerro::addNivel(){
    if(nivel<5) {
        nivel += 1;
        addArmazenamento();
        return true;
    }
    return false;
}

bool MinaCarvao::adicionaArmazem(int x) {
    if((armazem + x) <= armazena){
        armazem += x;
        return true;
    }
    return false;
}

int MinaCarvao::getProducao() const {
    return producao;
}

bool MinaCarvao::addNivel() {
    if(nivel<5) {
        nivel += 1;
        addArmazenamento();
        return true;
    }
    return false;
}


bool CentralEletrica::adicionaArmazem(int x) {
    if((armazem + x) <= armazena){
        armazem += x;
        return true;
    }
    return false;
}

bool CentralEletrica::addNivel() {
    if(nivel<5) {
        nivel += 1;
        addArmazenamento();
        return true;
    }
    return false;
}

bool Bateria::adicionaArmazem(int x) {
    if((armazem + x) <= armazena){
        armazem += x;
        return true;
    }
    return false;
}

bool Bateria::addNivel() {
    if(nivel<5) {
        nivel += 1;
        cout<<"nivel: "<<nivel<<endl;
        addArmazenamento();
        return true;
    }
    return false;
}

bool Fundicao::adicionaArmazem(int x) {
    if((armazem + x) <= armazena){
        armazem += x;
        return true;
    }
    return false;
}

bool Fundicao::addNivel() {
    if(nivel<5) {
        nivel += 1;
        addArmazenamento();
        return true;
    }
    return false;
}

bool EdificioX::adicionaArmazem(int x) {
    if((armazem + x) <= armazena){
        armazem += x;
        return true;
    }
    return false;
}

bool EdificioX::addNivel() {
    if(nivel<5) {
        nivel += 1;
        addArmazenamento();
        return true;
    }
    return false;
}
