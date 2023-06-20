#ifndef POO_2_TRABALHADORES_H
#define POO_2_TRABALHADORES_H
#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include "Dia.h"


class Trabalhadores {
    std::string tipo;
    static unsigned int n;
    unsigned int numero;
    std::string id;
    int DiasTrabalhar;
public:
    Trabalhadores(std::string t,Dia d);
    const std::string &getTipo() const;
    const std::string &getId() const;
    virtual int getPreco() const;
    virtual float getCansar() const{return getCansar();}
    virtual int diaDesp() const {return diaDesp();}
    bool depedimento(int d);
    int diasTrabalhar() const{return DiasTrabalhar;}
    void adicionaDiasTrabalho();
    virtual void DiasTrabalhoLen() {DiasTrabalhoLen();}
    virtual void setCansar(float x) {setCansar(x);}
    virtual bool diaDescanso(){return diaDescanso();}
    Trabalhadores operator++();

    virtual ~Trabalhadores() = default;
};

class Operario : public Trabalhadores{
    int preco;
    float cansar;
    int diaDespedimento;
public:
    Operario(Dia d,int p = 15, float c = 0.05,int diaD = 10) : Trabalhadores("oper",d), preco(p), cansar(c),diaDespedimento(diaD) {};
    void setCansar(float c) override {Operario::cansar += c;}
    int getPreco() const override{return preco;}
    float getCansar() const override {return cansar;}
    int diaDesp() const override {return diaDespedimento;}

    ~Operario() = default;
};

class Lenhador : public Trabalhadores{
    int preco;
    int diasTrabalho;
    float cansar;
    bool Descanso;
    int diaDespedimento;
public:
    Lenhador(Dia d,int p = 15,int dT = 4,int c=0,int diaD = 0) : Trabalhadores("len",d), preco(p), diasTrabalho(dT),cansar(c),diaDespedimento(diaD),Descanso(false) {};
    void setCansar(float c) override {Lenhador::cansar += c;}
    int getPreco() const override{return preco;}
    float getCansar() const override {return cansar;}
    bool diaDescanso() override;
    void DiasTrabalhoLen() override;
    int diaDesp() const override {return diaDespedimento;}

    ~Lenhador() = default;
};

class Mineiro : public Trabalhadores{
    int preco;
    float cansar;
    int diaDespedimento;
public:
    Mineiro(Dia d,int p = 15,float c = 0.10,int diaD = 2) : Trabalhadores("miner",d), preco(p), cansar(c), diaDespedimento(diaD) {};
    void setCansar(float c) override {Mineiro::cansar += c;}
    int getPreco() const override{return preco;}
    float getCansar() const override {return cansar;}
    int diaDesp() const override {return diaDespedimento;}

    ~Mineiro() = default;
};

#endif //POO_2_TRABALHADORES_H
