#include "Jogo.h"
#include "utils.h"
#include "Ficheiros.h"
#include "Comandos.h"
#include "Recursos.h"
using namespace std;


void limpaCin(){ //função que serve para limpar o \n do cin
    std::cin.clear();
    std::cin.ignore(INT_MAX,'\n');
}

void inicioJogo(){ //Inicia o jogo
    vector<Zona*>v;
    InfoIlha II = setLinhasColunas();
    Dia d;
    vector<Recursos*> recursos;
    recursos = Recurso(recursos);

    limpaCin();

    Mapa **ilha = new Mapa *[II.getLinha()];

    for (int i = 0; i < II.getLinha(); i++) {
        ilha[i] = new Mapa[II.getColuna()];
    }

    v = escolheZonas(II);
    Zonas(ilha,II,v);

    vector<string> config;

    imprimeIlha(II,ilha,&d,recursos,&config);
}

void Zonas(Mapa **ilha,InfoIlha II,vector<Zona*>v){
    int x=0;

    for(int i=0;i<II.getLinha();i++){
        for(int j=0;j<II.getColuna();j++){
            ilha[i][j].setZ(v[x]);
            x++;
        }
    }

}

vector<Recursos*> Recurso(vector<Recursos*> r){
    r.push_back(new Dinheiro());
    r.push_back(new Ferro());
    r.push_back(new Carvao());
    r.push_back(new Barras());
    r.push_back(new Vigas());
    r.push_back(new Madeira());
    r.push_back(new Eletricidade());
    return r;
}

void imprimeIlha(InfoIlha II,Mapa **ilha, Dia *d,vector<Recursos*> recursos,vector<string> *config) { //Imprime a ilha na consola
    int i, x, z = 0, e = 0, t = 0, nt = 0, contador = 0; //linhas: z -> zonas || e -> edificos || t -> trabalhadores || nt -> numero de trabalhadores
    cout<<"Dia: "<<d->getDia()<<endl;
    for(int m=0;m<recursos.size();m++){
        cout<<recursos[m]->getAsString();
    }
    cout<<endl;

    for (x = 0; x <= II.getLinha(); x++) {
        for (i = 0; i < II.getColuna() * 5 + 1; i++)
            cout << "*";
        cout << "\n";
        if (contador < II.getLinha()) {
            for (i = 0; i < II.getColuna(); i++) //imprime as zonas
                cout << "*" << ilha[z][i].getZ();
            cout << "*\n";

            for (i = 0; i < II.getColuna(); i++)//imprime os edificios
            {
                cout << "*" << ilha[e][i].getE();
            }
            cout << "*\n";
            for (i = 0; i < II.getColuna(); i++){ //imprime os trabalhadores
                if(ilha[t][i].getNT() == 0)
                    cout << "*    ";
                else{
                    cout<<"*";
                    int j=0;
                    while(j<4) {
                        if(ilha[t][i].getT(j) != " ")
                            cout << nomeTrabalhador(ilha[t][i].getT(j));
                        j++;
                    }
                    if(ilha[t][i].getNT() == 1)
                        cout<<"   ";
                    if(ilha[t][i].getNT() == 2)
                        cout<<"  ";
                    if(ilha[t][i].getNT() == 3)
                        cout<<" ";
                }
            }
            cout<<"*"<<endl;
            for (i = 0; i < II.getColuna(); i++) { //imprime o numero de trabalhadores
                if (ilha[nt][i].getNT() < 10)
                    cout << "* " << ilha[nt][i].getNT() << "  ";
                if (ilha[nt][i].getNT() >= 10 && ilha[nt][i].getNT() < 100)
                    cout << "* " << ilha[nt][i].getNT() << " ";
                if(ilha[nt][i].getNT() >= 100)
                    cout << "* " << ilha[nt][i].getNT();
            }
            cout << "*\n";
            z++;
            nt++;
            e++;
            t++;
        }
        contador++;

    }

    comandos(II, ilha,d,recursos,config);
}

void comandos(InfoIlha II,Mapa **ilha,Dia *d,vector<Recursos*> recursos,vector<string> *config){ //Introudução dos comandos
    string c;
    vector<string> comando;
    do{
        cout << "\n\tIntroduza um comando: ";
        getline(cin,c);
        comando = divideComando(c);
        if(!verificaComando(comando[0]))
            cout<<"Introduza um comando válido"<<endl;
        else
            executaComando(comando,II,ilha,d,recursos,config);

    }while(!verificaFimJogo(II,ilha,recursos));
    for(int i=0;i<II.getLinha();i++)
        delete [] ilha[i];
    delete [] ilha;

}


vector<string> divideComando(string c){
    vector<string> command;
    istringstream commandnizer {c};
    string cd;

    while(commandnizer >> c)
        command.push_back(c);
    return command;
}

bool verificaComando(string comando){
    if(comando == "exec")
        return true;
    if(comando == "cons")
        return true;
    if(comando == "liga")
        return true;
    if(comando == "des")
        return true;
    if(comando == "move")
        return true;
    if(comando == "vende")
        return true;
    if(comando == "cont")
        return true;
    if(comando == "list")
        return true;
    if(comando == "next")
        return true;
    if(comando == "config")
        return true;
    if(comando == "debcash")
        return true;
    if(comando == "debed")
        return true;
    if(comando == "debkill")
        return true;
    if(comando == "levelup")
        return true;
    return false;
}

void executaComando(vector<string> comando, InfoIlha II,Mapa **ilha,Dia *d,vector<Recursos*> recursos,vector<string> *config){
    if(comando[0] == "exec"){
        if(comando.size() == 2)
            ficheiroExec(comando[1],II,ilha,d,recursos,config);
        if(comando.size() < 2)
            cout<<"Aviso: Falta introduzir o nome do ficheiro!\nComando: exec <NomeFicheiro>"<<endl;
        if(comando.size() > 2)
            cout<<"Aviso: Parametros a mais!\nComando: exec <NomeFicheiro>"<<endl;
    }
    if(comando[0]== "cons"){
        if(comando.size() == 4)
            comandoCons(comando,II,ilha,recursos,config);
        if(comando.size() < 4)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: cons <tipo> <linha> <coluna>"<<endl;
        if(comando.size() > 4)
            cout<<"Aviso: Parametros a mais!\nComando: cons <tipo> <linha> <coluna>"<<endl;
    }
    if(comando[0] == "liga"){
        if(comando.size() == 3)
            comandoLiga(comando,II,ilha);
        if(comando.size() < 3)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: liga <linha> <coluna>"<<endl;
        if(comando.size() > 3)
            cout<<"Aviso: Parametros a mais!\nComando: liga <linha> <coluna>"<<endl;
    }
    if(comando[0]== "des"){
        if(comando.size() == 3)
            comandoDes(comando,II,ilha);
        if(comando.size() < 3)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: des <linha> <coluna>"<<endl;
        if(comando.size() > 3)
            cout<<"Aviso: Parametros a mais!\nComando: des <linha> <coluna>"<<endl;
    }
    if(comando[0] == "move"){
        if(comando.size() == 4)
            comandoMove(comando,II,ilha);
        if(comando.size() < 4)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: move <id> <linha> <coluna>"<<endl;
        if(comando.size() > 4)
            cout<<"Aviso: Parametros a mais!\nComando: move <id> <linha> <coluna>"<<endl;
    }
    if(comando[0] == "vende"){
        if(comando.size() == 3)
            comandoVende(comando,II,ilha,recursos);
        if(comando.size() < 3)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: vende <tipo> <quantidade> ou vende <linha> <coluna>"<<endl;
        if(comando.size() > 3)
            cout<<"Aviso: Parametros a mais!\nComando: vende <tipo> <quantidade> ou vende <linha> <coluna>"<<endl;
    }
    if(comando[0] == "cont"){
        if(comando.size() == 2)
            comandoCont(comando,II,ilha,d,recursos,config);
        if(comando.size() < 2)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: cont <tipo>"<<endl;
        if(comando.size() > 2)
            cout<<"Aviso: Parametros a mais!\nComando: cont <tipo>"<<endl;
    }
    if(comando[0] == "list"){
        if(comando.size() == 3)
            comandoList(comando,II,ilha);
        if(comando.size() < 3)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: list <linha> <coluna>"<<endl;
        if(comando.size() > 3)
            cout<<"Aviso: Parametros a mais!\nComando: list <linha> <coluna>"<<endl;
    }
    if(comando[0] == "next"){
        if(comando.size() == 1) {
            int danterior = d->getDia();
            comandoNext(II,ilha,recursos,danterior,d->getDia());
            d->operator++();
            comandoNext(II,ilha,recursos,danterior,d->getDia());
            imprimeIlha(II, ilha, d,recursos,config);
        }
        if(comando.size() > 1)
            cout<<"Aviso: Parametros a mais!\nComando: next"<<endl;
    }
    if(comando[0] == "config"){
        if(comando.size()==2)
            *config = comandoConfig(comando,II,ilha,recursos);
        if(comando.size()<2)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: config <nomeFicheiro>"<<endl;
        if(comando.size()>2)
            cout<<"Aviso: Parametros a mais!\nComando: config <nomeFicheiro>"<<endl;
    }
    if(comando[0] == "debcash"){
        if(comando.size()==2)
            comandoDebug(comando,II,ilha,recursos);
        if(comando.size()<2)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: debcash <valor>"<<endl;
        if(comando.size()>2)
            cout<<"Aviso: Parametros a mais!\nComando: debcash <valor>"<<endl;
    }
    if(comando[0] == "debed"){
        if(comando.size()==4)
            comandoDebug(comando,II,ilha,recursos);
        if(comando.size()<4)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: debed <tipo> <linha> <coluna>"<<endl;
        if(comando.size()>4)
            cout<<"Aviso: Parametros a mais!\nComando: debed <tipo> <linha> <coluna>"<<endl;
    }
    if(comando[0] == "debkill"){
        if(comando.size()==4)
            comandoDebug(comando,II,ilha,recursos);
        if(comando.size()<2)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: debkill <id>"<<endl;
        if(comando.size()>2)
            cout<<"Aviso: Parametros a mais!\nComando: debkill <id>"<<endl;
    }
    if(comando[0] == "levelup"){
        if(comando.size()==3)
            comandoLevelUp(comando,II,ilha,recursos);
        if(comando.size()<3)
            cout<<"Aviso: Faltam argumentos no comando!\nComando: levelup <linha> <coluna>"<<endl;
        if(comando.size()>3)
            cout<<"Aviso: Parametros a mais!\nComando: levelup <linha> <coluna>"<<endl;
    }
    return;
}


