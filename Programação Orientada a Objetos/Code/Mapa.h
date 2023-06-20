#ifndef POO_2_MAPA_H
#define POO_2_MAPA_H
#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include "Zona.h"
#include "Edificios.h"
#include "Recursos.h"
#include "Trabalhadores.h"
#include "Interface.h"


class Mapa{
    Zona *z = nullptr;
    Edificios *e = nullptr;
    std::vector<Trabalhadores*> t;
public:

    Mapa();
    ~Mapa();

    /*Zonas*/
    std::string getZ() const;
    void setZ(Zona *Zo);
    int getArvores();
    void removeArvores(int q);
    void adicionaArvores(int q);
    int getQuantidadeFlr();
    void addQuantidadeFlr(int q);
    void removeQuantidadeFlr(int q);
    bool construcao();
    void addConstrucao();
    void removeConstrucao();

    /*Edificios*/
    std::string getE() const;
    std::string getNE() const;
    void desE();
    void ligaE();
    bool getDesLig();
    void adicionaEdificios(std::string ed,std::vector<std::string> config);
    int precoEd();
    int precoMinas(std::string pagamento);
    void removeEdificio();
    std::string getProduz() const;
    int getProducao();
    void adicionaArmazem(int x);
    void removeArmazem(int x);
    int getDiasConstruido();
    void addDiasEdificioConstruido();
    void zonaPnt(std::vector<Recursos*> recursos);
    int getNivel();
    int getPrecoLevelUp();
    bool LevelUp(std::vector<Recursos*>recursos,InfoIlha II,Mapa **i);
    float getDesabar();
    void Desabar(std::vector<Recursos*> recursos);

    /*Trabalhadores*/
    void adicionaTrabalhador(std::string trabalhador,Dia d,std::vector<std::string> config);
    std::string getId();
    std::string getT(int x) const;
    int getNT();
    bool verificaID(std::string id);
    Trabalhadores* removeTrabalhador(std::string id);
    void moveTrabalhador(Trabalhadores *trab);
    int precoTrab(std::string id);
    void despedimento(int d);
    void adicionaNDT();
    void setCansar(float x,std::string id);
    int getDiasTrabalhar(int x);
    void removeT(std::string id);
    bool procuraT(std::string trab,int *q);


    /*Verificações*/

    bool verificaTrabalhadorNaZona(std::string tipoProducao);
    bool verificaEspaço(float *x);
    bool procuraTrabalhador(std::string trab,float *q);
    int quatidadeRecursosZona();
    bool quantidadeDisponivel();

};
bool VStoi(std::string n,int *x);

#endif //POO_2_MAPA_H
