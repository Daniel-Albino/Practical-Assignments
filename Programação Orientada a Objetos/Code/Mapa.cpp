#include "Mapa.h"
#include "utils.h"
#include <sstream>

using namespace std;

Mapa::Mapa(){}

Mapa::~Mapa(){
    delete z;
    delete e;
    for(int i=0;i<t.size();i++)
        delete t[i];
}

void Mapa::setZ(Zona *Zo) {
    Mapa::z = Zo;
}

string Mapa::getZ() const {
    return z->getZona()+" ";
}

int Mapa::getArvores() {
    return z->getArvores();
}

void Mapa::removeArvores(int q) {
    z->removeArvores(q);
}

void Mapa::adicionaArvores(int q){
    z->adicionaArvores(q);
}

int Mapa::getQuantidadeFlr(){
    if(getZ() == "flr ")
        return z->getQuantidade();
    return -1;
}

void Mapa::addQuantidadeFlr(int q){
    if(getZ() == "flr ")
        z->adicionaQuantidade(q);
}
void Mapa::removeQuantidadeFlr(int q){
    if(getZ() == "flr ") {
        z->removeQuantidade(q);
    }
}

bool Mapa::construcao(){
    return z->getConstrucao();
}

void Mapa::addConstrucao(){
    z->addConstrucao();
}

void Mapa::removeConstrucao(){
    z->removeConstrucao();
}

string Mapa::getE() const {
    if(e == nullptr)
        return "    ";
    if(e->getEdificio().size() < 4)
        return e->getEdificio() + " ";
    return e->getEdificio();
}

std::string Mapa::getNE() const{
    if(getE() == "mnF ")
        return "Mina de ferro";
    if(getE() == "mnC ")
        return "Mina de Carvão";
    if(getE() == "elec")
        return "Central Elétrica";
    if(getE() == "bat ")
        return "Bateria";
    if(getE() == "fun ")
        return "Fundição";
    if(getE() == "EdX ")
        return "Serração";
    return "NA";
}

bool VStoi(std::string n,int *x){
    try {
        *x = stoi(n);
        return true;
    }
    catch(std::invalid_argument e) {
        return false;
    }
}

void Mapa::adicionaEdificios(string ed,vector<string> config) {
    for(int i=0;i<config.size();i++){
        int x;
        vector<string> preco = divideComando(config[i]);
        if (ed == "mnF") {
            if(preco[0] == "mnF") {
                if(VStoi(preco[1],&x)) {
                    e = new MinaFerro(x);
                    return;
                }
            }
        }
        if (ed == "mnC") {
            if(preco[0] == "mnC") {
                if(VStoi(preco[1],&x)) {
                    e = new MinaCarvao(x);
                    return;
                }
            }
        }
        if (ed == "elec") {
            if(preco[0] == "elec") {
                if(VStoi(preco[1],&x)) {
                    e = new CentralEletrica(x);
                    return;
                }
            }
        }
        if (ed == "bat") {
            if(preco[0] == "bat") {
                if(VStoi(preco[1],&x)) {
                    e = new Bateria(x);
                    return;
                }
            }
        }
        if (ed == "fun") {
            if(preco[0] == "fun") {
                if(VStoi(preco[1],&x)) {
                    e = new Fundicao(x);
                    return;
                }
            }
        }
        if (ed == "EdX") {
            if(preco[0] == "EdX") {
                if(VStoi(preco[1],&x)) {
                    e = new EdificioX(x);
                    return;
                }
            }
        }
    }

    if(ed == "mnF")
        e = new MinaFerro();
    if (ed == "mnC")
        e = new MinaCarvao();
    if (ed == "elec")
        e = new CentralEletrica();
    if (ed == "bat")
        e = new Bateria();
    if (ed == "fun")
        e = new Fundicao();
    if (ed == "EdX")
        e = new EdificioX();
}

void Mapa::desE(){
    e->DesEdificio();
}

void Mapa::ligaE(){
    e->LigaEdificio();
}

bool Mapa::getDesLig(){
    return e->isLigado();
}

int Mapa::precoEd(){
    if(getZ() == "mnt ")
        return 2*e->getPreco();
    return e->getPreco();
}

int Mapa::precoMinas(string pagamento){
    if(pagamento == "dinheiro"){
        if (getE() == "mnF " || getE() == "mnC ") {
            if (getZ() == "mnt ")
                return 2 * e->getPreco();
            else
                return e->getPreco();
        }
    }else {
        if (getE() == "mnF " || getE() == "mnC ") {
            if (getZ() == "mnt ")
                return 2 * e->getPrecoVigas();
            else
                return e->getPrecoVigas();
        }
    }
    return -1;
}

void Mapa::removeEdificio() {
    delete e;
    e = nullptr;
    z->removeConstrucao();
}

std::string Mapa::getProduz() const{
    if(e == nullptr)
        return "NA";
    return e->getProduz();
}

int Mapa::getProducao() {
    if(e->getEdificio() != "bat ")
        return e->getProducao()*z->getProducao();
    return 0;
}

void Mapa::adicionaArmazem(int x) {
    e->adicionaArmazem(x);
}

void Mapa::removeArmazem(int x) {
    if(e == nullptr)
        return;
    else if(e->getArmazem()>=x){
        e->removeArmazem(x);
    }
}

int Mapa::getDiasConstruido(){
    if(e == nullptr)
        return -1;
    else
        return e->getDiasConstruido();
}

void Mapa::addDiasEdificioConstruido() {
    if(e == nullptr)
        return;
    else
        e->addDiasConstruido();
}

void Mapa::zonaPnt(vector<Recursos*> recursos){
    if(e != nullptr) {
        if (getDiasConstruido() == 10) {
            if (getE() != "bat " || getE() != "elec") {
                for (int i = 0; i < recursos.size(); i++)
                    if (recursos[i]->getTipo() == e->getProduz())
                        recursos[i]->removeQuantidade(quantidadeDisponivel());
            }
            if (getE() == "bat "){
                recursos[6]->removeQuantidade(quantidadeDisponivel());
            }
            if (getE() == "elec"){
                recursos[2]->removeQuantidade(quantidadeDisponivel());
            }
            removeEdificio();
            removeArmazem(quantidadeDisponivel());
        }

    }
    for(int i=0;i<t.size();i++) {
        if (getDiasTrabalhar(i) == 10) {
            removeTrabalhador(t[i]->getId());
        }
    }
}
int Mapa::getNivel(){
    return e->getNivel();
}

int Mapa::getPrecoLevelUp(){
    return e->getPrecoNivel();
}

bool Mapa::LevelUp(vector<Recursos*>recursos,InfoIlha II,Mapa **i){
    if(e!= nullptr) {
        if (e->getNivel() <= 4) {
            if (getE() == "mnF " || getE() == "mnC ") {
                if (recursos[4]->getQuantidade() >= 1) {
                    if (getPrecoLevelUp() <= recursos[0]->getQuantidade()) {
                        e->addNivel();
                        recursos[0]->removeQuantidade(getPrecoLevelUp());
                        recursos[4]->removeQuantidade(1);
                        removeRecursosEdificios(recursos[4]->getTipo(), II, 1, i);
                        return true;
                    }
                }
            } else if (getPrecoLevelUp() <= recursos[0]->getQuantidade()) {
                e->addNivel();
                recursos[0]->removeQuantidade(getPrecoLevelUp());
                return true;
            }
        }
    }
    return false;
}

void Mapa::adicionaTrabalhador(std::string trabalhador,Dia d,vector<string> config) {
    if(!config.empty()) {
        for (int i = 0; i < config.size(); i++) {
            vector<string> preco = divideComando(config[i]);
            int x;
            if (trabalhador == "miner") {
                if (preco[0] == "miner") {
                    if (VStoi(preco[1], &x)) {
                        t.push_back(new Mineiro(d, x));
                        return;
                    }
                }
            }
            if (trabalhador == "oper") {
                if (preco[0] == "oper") {
                    if (VStoi(preco[1], &x)) {
                        t.push_back(new Operario(d, x));
                        return;
                    }
                }
            }
            if (trabalhador == "len") {
                if (preco[0] == "len") {
                    if (VStoi(preco[1], &x)) {
                        t.push_back(new Lenhador(d, x));
                        return;
                    }
                }
            }
        }
    }else {
        if (trabalhador == "miner")
            t.push_back(new Mineiro(d));
        if (trabalhador == "oper")
            t.push_back(new Operario(d));
        if (trabalhador == "len")
            t.push_back(new Lenhador(d));
    }
}

string Mapa::getId() {
    return t[t.size()-1]->getId();
}

std::string Mapa::getT(int x) const{
    if(x < t.size())
        return t[x]->getTipo();
    return " ";
}

int Mapa::getNT(){
    return t.size();
}

bool Mapa::verificaID(string id) {
    for(int i=0;i<t.size();i++) {
        if (t[i]->getId() == id) {
            return true;
        }
    }
    return false;
}

Trabalhadores* Mapa::removeTrabalhador(string id){
    for(int i=0;i<t.size();i++)
        if(t[i]->getId() == id){
            Trabalhadores *auxT = t[i];
            t.erase(t.cbegin()+i);
            return auxT;
        }
    return nullptr;
}

void Mapa::moveTrabalhador(Trabalhadores *trab){
    t.push_back(trab);
}

int Mapa::precoTrab(string id) {
    for(int i = 0;i<t.size();i++)
        if(t[i]->getId() == id)
            return t[i]->getPreco();
    return 0;
}

void Mapa::despedimento(int d) {
    if(t.size() != 0) {
        for (int i = 0; i < t.size(); i++) {
            if (t[i]->getTipo() != "len") {
                if(z->isDemissao()) {
                    if (t[i]->depedimento(d)) {
                        cout<<"O trabalhador com id "<<t[i]->getId()<<" despediu-se!"<<endl;
                        t.erase(t.cbegin() + i);
                    }
                }
            }
        }
    }
}

void Mapa::adicionaNDT() {
    for(int i=0;i<t.size();i++){
        t[i]->adicionaDiasTrabalho();
    }
}

void Mapa::setCansar(float x,string id) {
    for(int i=0;i<t.size();i++){
        if(t[i]->getId() == id)
            t[i]->setCansar(x);
    }

}

int Mapa::getDiasTrabalhar(int x) {
    return t[x]->diasTrabalhar();
}

float Mapa::getDesabar() {
    if(e == nullptr)
        return -1;
    else if(e->getEdificio() == "mnF" || e->getEdificio() == "mnC")
        return e->getDesabar();
    return -1;
}

void Mapa::Desabar(vector<Recursos*> recursos){
    if(getDiasConstruido()<2)
        return;
    if (e == nullptr) {
        return;
    } else {
        float x = getRealUniform();
        float aux = getDesabar();
        if (aux == -1)
            return;
        if (x <= aux) {
            cout<<"Edificios "<<getE()<<"desabou!"<<endl;
            removeEdificio();
            for (int i = 0; i < recursos.size(); i++)
                if (recursos[i]->getTipo() == getProduz())
                    recursos[i]->removeQuantidade(quantidadeDisponivel());
            removeArmazem(quantidadeDisponivel());
        }
    }
}

/*Verificações*/

bool Mapa::verificaTrabalhadorNaZona(string tipoProducao){
    int i;
    if(e!= nullptr) {
        if (t.size() != 0) {
            if (e->isLigado()) {
                if (tipoProducao == "ferro") {
                    for (i = 0; i < getNT(); i++) {
                        if (getT(i) == "miner") {
                            return true;
                        }
                    }
                }
                if (tipoProducao == "barras") {
                    for (i = 0; i < getNT(); i++) {
                        if (getT(i) == "oper") {
                            return true;
                        }
                    }
                }
                if (tipoProducao == "carvao") {
                    for (i = 0; i < getNT(); i++) {
                        if (getT(i) == "miner")
                            return true;
                        if (getE() == "elec")
                            if (getT(i) == "oper") {
                                return true;
                            }

                    }
                }
                if (tipoProducao == "vigas") {
                    for (i = 0; i < getNT(); i++) {
                        if (getT(i) == "oper") {
                            return true;
                        }
                    }
                }
                if (tipoProducao == "eletricidade") {
                    for (i = 0; i < getNT(); i++) {
                        if (getT(i) == "oper") {
                            return true;
                        }
                    }
                }
            }
        }
    }
    return false;
}


bool Mapa::verificaEspaço(float *x){
    if(e->adicionaArmazem(*x))
        return true;
    return false;
}

bool Mapa::procuraTrabalhador(string trab,float *q){
    for(int i = 0;i<t.size();i++) {
        if (t[i]->getTipo() == trab) {
            if (trab == "len") {
                if (!t[i]->diaDescanso()) {
                    if(getArvores()>*q)
                        *q += 1;
                }
            }
        }
    }
    if(*q == 0)
        return false;
    return true;
}

bool Mapa::quantidadeDisponivel(){
    if(quatidadeRecursosZona() > 0)
        return true;
    return false;
}

int Mapa::quatidadeRecursosZona(){
    if(e== nullptr)
        return 0;
    return e->getArmazem();
}

void Mapa::removeT(string id) {
    for(int i=0;i<t.size();i++){
        if(t[i]->getId() == id){
            cout<<"O trabalhador com o id "<<t[i]->getId()<<" foi removido!"<<endl;
            t.erase(t.cbegin()+i);
        }
    }
}

bool Mapa::procuraT(string trab,int *q){
    for(int i = 0;i<t.size();i++){
        if (t[i]->getTipo() == trab) {
            *q+=1;
        }
    }
    if(*q == 0)
        return false;
    return true;

}








