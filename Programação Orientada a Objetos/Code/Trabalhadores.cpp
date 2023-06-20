#include "Trabalhadores.h"
#include "utils.h"


unsigned int Trabalhadores::n = 1;

Trabalhadores::Trabalhadores(std::string t,Dia d){
        tipo = t;
        DiasTrabalhar = 0;
        numero = n++;
        id += std::to_string(d.getDia());
        id += ".";
        id += std::to_string(numero);
}

const std::string &Trabalhadores::getTipo() const {
    return tipo;
}

const std::string &Trabalhadores::getId() const {
    return id;
}

int Trabalhadores::getPreco() const {
    return getPreco();
}

bool Trabalhadores::depedimento(int d) {
    float x = getRealUniform();
    if(getTipo() != "len") {
        if (diaDesp() <= DiasTrabalhar) {
            if (getCansar() >= x)
                return true;
        }
    }else{
        if(getCansar() != 0) {
            if (diaDesp() <= DiasTrabalhar) {
                if (getCansar() >= x)
                    return true;
            }
        }
    }
    return false;
}

void Trabalhadores::adicionaDiasTrabalho() {
    if(getTipo() == "len") {
        DiasTrabalhoLen();
        DiasTrabalhar++;
    }else
        DiasTrabalhar++;
}

void Lenhador::DiasTrabalhoLen() {
    if(diasTrabalho>=0) {
        if (diasTrabalho == 1)
            Descanso = true;
        diasTrabalho--;
    }
    if(diasTrabalho<0) {
        diasTrabalho = 4;
        Descanso = false;
    }
}


bool Lenhador::diaDescanso() {
    if(Descanso == true)
        return true;
    return false;
}