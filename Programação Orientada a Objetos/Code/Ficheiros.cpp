#include "Ficheiros.h"

using namespace std;


void infoJogo(std::string nfich) {

    string frase;
    ifstream ficheiro(nfich);
    cout<<endl;

    if(ficheiro) {
        while (getline(ficheiro, frase)) {
            cout<<frase<<endl;
        }
    }
    else
        cout << "ERRO a abrir o ficheiro!! Por favor verifique se o ficheiro existe ou se escreveu bem o nome do ficheiro!"<<endl;

}

void infoEspecifica(std::string nfich){

    string frase;
    ifstream ficheiro(nfich);
    cout<<endl;

    if(ficheiro) {
        while (getline(ficheiro, frase)) {
            cout<<frase<<endl;
        }
    }
    else
        cout << "ERRO a abrir o ficheiro!! Pedimos desculpa pelo sucedido! "<<endl;

}

void ficheiroExec(string nfich, InfoIlha II,Mapa **ilha,Dia *d,vector<Recursos*> recursos,vector<string> *config){

    string frase;
    ifstream ficheiro(nfich);
    vector<string> aux;


    if(ficheiro) {
        while (getline(ficheiro, frase)) {
            aux = divideComando(frase);
            if(verificaComando(aux[0])){
                executaComando(aux,II,ilha,d,recursos,config);
            }
        }
    }
    else
        cout << "ERRO a abrir o ficheiro!! Por favor verifique se o ficheiro existe ou se escreveu bem o nome do ficheiro!"<<endl;

}

