#include "Interface.h"
#include "Jogo.h"
#include "Ficheiros.h"

using namespace std;

InfoIlha::InfoIlha(int l, int c) : linha(l), coluna(c) {}

int InfoIlha::getLinha() const {
    return linha;
}

int InfoIlha::getColuna() const {
    return coluna;
}

void infoInicial(){
    string o;
    cout<<"*====================Simulador de construção e desenvolvimento====================*"<<endl;
    do {
        o.clear();
        cout << "\nIndique o que pretende fazer:" << endl;
        cout << "\t1. Jogar." << endl;
        cout << "\t2. Informação." << endl;
        cout << "\t3. Sair." << endl;
        do {
            cout << "\t\tOpção: ";
            getline(cin, o);
        } while (!verificaOpcao(o));
        if (o == "2" || o == "Informação" || o == "Informacão" || o == "Informaçao" || o == "informacao" ||
            o == "informação" || o == "informacão" || o == "informaçao")
            infoJogo("infoJogo.txt");
        if (o == "3" || o == "Sair" || o == "sair")
            return;
    }while(o != "1" && o != "Jogar" && o != "jogar");
    inicioJogo();
}

bool verificaOpcao(string o){
    if(o == "1" || o == "Jogar" || o == "jogar" || o == "2" || o == "Informação" || o == "Informacão" || o == "Informaçao" || o == "informacao" || o == "informação" || o == "informacão" || o == "informaçao" || o == "3" || o == "Sair" || o == "sair")
        return true;
    return false;
}

bool verificaLinha(int x) {
    if(x >= 3)
        if(x <= 8)
            return true;
    cout << "\nValor inválido! Deve introduzir um valor inteiro entre 3 a 8!"<<endl;
    return false;
}

bool verificaColuna(int x) {
    if(x >= 3)
        if(x <= 16)
            return true;

    cout << "\nValor inválido! Deve introduzir um valor inteiro entre 3 a 16!"<<endl;
    return false;
}

InfoIlha setLinhasColunas(){
    int l,c;
    cout <<"Introduza as dimensões para a ilha: "<<endl;
    do{
        cout <<"Linhas: ";
        cin >> l;
    }while(!verificaLinha(l));
    do {
        cout << "Colunas: ";
        cin >> c;
    }while(!verificaColuna(c));
    InfoIlha II(l,c);
    return II;
}