#include "Comandos.h"
#include "utils.h"
#include "Ficheiros.h"

void comandoCons(vector<string> comando,InfoIlha II, Mapa **ilha,vector<Recursos*> recursos,vector<string> *config){

    int l,c;
    string modoPagamento;

    if(!verificaStoi(comando[2],comando[3],&l,&c))
        return;

    l--; c--; //decrementa uma posição pois o array vai de 0 - 2 e não de 1 - 3 por exemplo

    if(verificaTipoEdificio(comando[1])){
        if (Linhas(l, II)){
            if (Colunas(c, II)){
                if (!ilha[l][c].construcao()){
                    ilha[l][c].adicionaEdificios(comando[1],*config);
                    if(comando[1] == "bat"){
                        if(verificaRecursosEdificiosBateria(&ilha[l][c],recursos)){
                            cout << "\n\tAviso: O edificio " << comando[1] << " criado com sucesso!" << endl;
                            ilha[l][c].addConstrucao();
                        }else{
                            ilha[l][c].removeEdificio();
                            cout << "\n\tNão foi possivel construir um edificio por falta de recursos!" << endl;
                        }
                    }else{
                        if(comando[1] == "elec" || comando[1] == "fun" || comando[1] == "EdX")
                            modoPagamento="dinheiro";
                        else
                            modoPagamento = metodoPagamentoEdificios();
                        if (verificaRecursosEdificios(&ilha[l][c], recursos, modoPagamento)) {
                            cout << "\n\tAviso: O edificio " << comando[1] << " criado com sucesso!" << endl;
                            ilha[l][c].addConstrucao();
                        } else {
                            ilha[l][c].removeEdificio();
                            cout << "\n\tNão foi possivel construir um edificio por falta de recursos!" << endl;
                        }
                    }
            } else
                cout << "Aviso: Já existe um edificio nessa zona com o nome " << ilha[l][c].getE() << "!" << endl;
        } else {
            cout << "Aviso: Introduza uma coluna válida (entre 1 - " << II.getColuna() << ")!" << endl;
        }
        }else{
            cout<<"Aviso: Introduza uma linha válida (entre 1 - "<<II.getLinha()<<")!"<<endl;
        }
    }else{
        string resposta;
        do{
            cout << "O nome do edifico está incorreto!\nPretende ver quais os nomes dos edificios? (s/n)\n\tR: ";
            getline(cin, resposta);
            if (resposta == "s" || resposta == "S")
                infoEspecifica("edificios.txt");
        }while(!verificaResposta(resposta));
    }
}

bool verificaResposta(string resposta){
    if(resposta != "s" || resposta != "S" || resposta != "N" || resposta != "n")
        return true;
    return false;
}

void comandoDes(vector<string> comando,InfoIlha II, Mapa **ilha){
    int l,c;
    if(!verificaStoi(comando[1],comando[2],&l,&c))
        return;
    l--; c--; //decrementa uma posição pois o array vai de 0 - 2 e não de 1 - 3 por exemplo
    if (Linhas(l, II)) {
        if (Colunas(c, II)) {
            if(ilha[l][c].getE() != "    ") {
                ilha[l][c].desE();
            } else
                cout<<"Aviso: Não existe nenhum edificio construido nessa zona!"<<endl;

        } else
            cout << "Aviso: Introduza uma coluna válida (entre 1 - " << II.getColuna() << ")!" << endl;
    }else
        cout << "Aviso: Introduza uma linha válida (entre 1 - " << II.getLinha() << ")!" << endl;

}

void comandoLiga(vector<string> comando,InfoIlha II, Mapa **ilha){
    int l,c;
    if(!verificaStoi(comando[1],comando[2],&l,&c))
        return;
    l--; c--; //decrementa uma posição pois o array vai de 0 - 2 e não de 1 - 3 por exemplo
    if (Linhas(l, II)) {
        if (Colunas(c, II)) {
            if(ilha[l][c].getE() != "    ") {
                ilha[l][c].ligaE();
            } else
                cout<<"Aviso: Não existe nenhum edificio construido nessa zona!"<<endl;
        } else
            cout << "Aviso: Introduza uma coluna válida (entre 1 - " << II.getColuna() << ")!" << endl;
    }else
        cout << "Aviso: Introduza uma linha válida (entre 1 - " << II.getLinha() << ")!" << endl;

}

void comandoList(vector<string> comando,InfoIlha II, Mapa **ilha){
    int l,c,i,j=0,nm=0,no=0,nl=0;
    if(!verificaStoi(comando[1],comando[2],&l,&c))
        return;
    l--; c--; //decrementa uma posição pois o array vai de 0 - 2 e não de 1 - 3 por exemplo
    cout<<"\n\n";

    if (Linhas(l, II)) {
        if (Colunas(c, II)) {
            cout<<"\n";
            cout<<"Informação da zona: "<<endl;
            for(i=0;i<47;i++)
                cout<<"*";
            cout<<"\n";
/*========================================Zonas========================================*/
            string z = zona(ilha[l][c].getZ());
            cout<<"*Zona -> "<<z;
            if(z.size() == 6)
                cout<<"                               *"<<endl;
            if(z.size() == 7)
                cout<<"                              *"<<endl;
            if(z.size() == 8)
                cout<<"                             *"<<endl;

/*========================================Edificios========================================*/
            if(!ilha[l][c].construcao()) {
                cout << "*Edificio -> Não existe";
                cout << "                       *"<<endl;
            }else {
                if (!ilha[l][c].getDesLig()) {
                    cout << "*Edificio -> " << ilha[l][c].getNE();
                    if(ilha[l][c].getNE().size()==7)
                        cout<< " (desligado)              *" << endl;
                    if(ilha[l][c].getNE().size()==10)
                        cout<< " (desligado)             *" << endl;
                    if (ilha[l][c].getNE().size() == 13)
                        cout << " (desligado)        *" << endl;
                    if (ilha[l][c].getNE().size() == 15)
                        cout << " (desligado)       *" << endl;
                    if(ilha[l][c].getNE().size()==17)
                        cout<< " (desligado)     *" << endl;

                }
                if (ilha[l][c].getDesLig()) {
                    cout << "*Edificio -> " << ilha[l][c].getNE();
                    if(ilha[l][c].getNE().size()==7)
                        cout<< " (ligado)                 *" << endl;
                    if(ilha[l][c].getNE().size()==10)
                        cout<< " (ligado)                *" << endl;
                    if (ilha[l][c].getNE().size() == 13)
                        cout<< " (ligado)           *" << endl;
                    if (ilha[l][c].getNE().size() == 15)
                        cout<< " (ligado)          *" << endl;
                    if(ilha[l][c].getNE().size()==17)
                        cout<< " (ligado)        *" << endl;
                }
                cout<<"*\tNivel -> "<<ilha[l][c].getNivel();
                for(i = 0;i<32;i++)
                    cout<<" ";
                cout<<"*"<<endl;
            }

/*========================================Trabalhadores========================================*/
            cout<<"*Trabalhadores:                               *"<<endl;
            for(i=0;i<ilha[l][c].getNT();i++,j++){
                if(ilha[l][c].getT(j) == "miner")
                    nm++;
                if(ilha[l][c].getT(j) == "oper")
                    no++;
                if(ilha[l][c].getT(j) == "len")
                    nl++;
            }
            cout<<"*\tMineiros -> "<<nm;
            if(nm<10)
                cout<<"                             *"<<endl;
            if(nm>10 && nm<100)
                cout<<"                            *"<<endl;
            if(nm>100)
                cout<<"                           *"<<endl;
            cout<<"*\tOperarios -> "<<no;
            if(no<10)
                cout<<"                            *"<<endl;
            if(no>10 && no<100)
                cout<<"                           *"<<endl;
            if(no>100)
                cout<<"                          *"<<endl;
            cout<<"*\tLenhadores -> "<<nl;
            if(nl<10)
                cout<<"                           *"<<endl;
            if(nl>10 && nl<100)
                cout<<"                          *"<<endl;
            if(nl>100)
                cout<<"                         *"<<endl;
            for (i = 0; i < 47; i++)
                cout << "*";

            cout<<"\n";
        }
        else
            cout << "Aviso: Introduza uma coluna válida (entre 1 - " << II.getColuna() << ")!" << endl;
    }
    else
        cout << "Aviso: Introduza uma linha válida (entre 1 - " << II.getLinha() << ")!" << endl;
    cout<<endl;
}

string zona(string z){
    if(z == "mnt ")
        return "Montanha";
    if(z == "pnt ")
        return "Pantano";
    if(z == "pas ")
        return "Pastagem";
    if(z == "dsr ")
        return "Deserto";
    if(z == "flr ")
        return "Floresta";
    if(z == "znz ")
        return "Zona-X";
    return "    ";
}

void comandoCont(vector<string> comando,InfoIlha II, Mapa **ilha,Dia *dia,vector<Recursos*> recursos,vector<string> *config){
    int l,c;
    if(verificaTrabalhador(comando[1])){
        for(l=0;l<II.getLinha();l++) {
            for (c = 0; c < II.getColuna(); c++){
                if (ilha[l][c].getZ() == "pas "){
                    ilha[l][c].adicionaTrabalhador(comando[1],dia->getDia(),*config);
                    if(verificaRecursosTrabalhadores(&ilha[l][c],recursos,ilha[l][c].getId())) {
                        cout << "Trabalhador " << comando[1] << " foi contratado com sucesso e o seu id é "<< ilha[l][c].getId() << "!" << endl;
                        return; //Quando encontra a 1ª zona = pas salta do ciclo para não contratar mais do que 1 trabalhador!
                    }else{
                        ilha[l][c].removeTrabalhador(ilha[l][c].getId());
                        cout<<"Aviso: Não foi possivel contratar um "<<comando[1]<<", pois não tem dinheiro suficiente!"<<endl;
                        return;
                    }
                }
            }
        }
    }else{
        string resposta;
        do{
            cout << "O nome do trabalhador está incorreto!\nPretende ver quais os nomes dos trabalhadores? (s/n)\n\tR: ";
            getline(cin, resposta);
            if (resposta == "s" || resposta == "S")
                infoEspecifica("trabalhadores.txt");
        }while(!verificaResposta(resposta));
    }
}

void comandoMove(vector<string> comando,InfoIlha II, Mapa **ilha){
    int l,c,i,j;
    if(!verificaStoi(comando[2],comando[3],&l,&c))
        return;
    l--;c--;
    if(Linhas(l,II)){
        if(Colunas(c,II)){
            for(i=0;i<II.getLinha();i++) {
                for (j = 0; j < II.getColuna(); j++){
                    if(ilha[i][j].verificaID(comando[1])) {
                        if(!ilha[l][c].verificaID(comando[1])) {
                            Trabalhadores *t = ilha[i][j].removeTrabalhador(comando[1]);
                            ilha[l][c].moveTrabalhador(t);
                            if(ilha[l][c].getZ() == "mnt "){
                                ilha[l][c].setCansar(0.05,comando[1]);
                            }
                            cout << "Trabalhador movido com sucesso!" << endl;
                            return;
                        }else{
                            cout<<"Aviso: O trabalhador já se encontra na zona para onde pretende mover!"<<endl;
                            return;
                        }
                    }
                }
            }
        }else{
            cout<<"Aviso: Introduza uma coluna válida (entre 1 - "<<II.getColuna()<<")!"<<endl;
        }
    }else{
        cout<<"Aviso: Introduza uma linha válida (entre 1 - "<<II.getLinha()<<")!"<<endl;
    }
    cout<<"Não foi possivel mover o trabalhador!"<<endl;
}

void comandoVende(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos){
    if(comando[1] == "dinheiro"){
        cout<<"Não é possivel vender dinheiro!"<<endl;
        return;
    }
    if(verificaVende(&comando[1])){
        float quantidade;
        int aux = 0;
        try{
            quantidade = stof(comando[2]); //numero decimais!
        }
        catch (invalid_argument e){
            cout<<"Aviso: Deve introduzir um número!"<<endl;
            return;
        }
        for(int i = 0;i<recursos.size();i++) {
            if (recursos[i]->getTipo() == comando[1]){
                if(recursos[i]->getQuantidade() >= quantidade)
                    aux = i;
                else {
                    cout << "Aviso: Não tem " << recursos[i]->getTipo() << " suficiente(s) para vender!" << endl;
                    return;
                }
            }
        }
        float x = quantidade*recursos[aux]->getPreco();
        recursos[0]->adicionaQuantidade(x);
        recursos[aux]->removeQuantidade(quantidade);

        removeRecursosEdificios(comando[1],II,quantidade,ilha);
        return;
    }else{
        int l,c;
        if(verificaStoi(comando[1],comando[2],&l,&c)){
            l--;c--;
            if(ilha[l][c].construcao()) {
                int pED,auxREd;
                pED = ilha[l][c].precoEd();
                auxREd = ilha[l][c].quatidadeRecursosZona();

                if(ilha[l][c].getE() == "mnF "){
                    recursos[1]->removeQuantidade(auxREd);
                    ilha[l][c].removeConstrucao();
                }
                if(ilha[l][c].getE() == "mnC "){
                    recursos[2]->removeQuantidade(auxREd);
                    ilha[l][c].removeConstrucao();
                }
                if(ilha[l][c].getE() == "fun "){
                    recursos[3]->removeQuantidade(auxREd);
                    ilha[l][c].removeConstrucao();
                }
                if(ilha[l][c].getE() == "elec"){
                    recursos[6]->removeQuantidade(auxREd);
                    ilha[l][c].removeConstrucao();
                }
                if(ilha[l][c].getE() == "EdX"){
                    recursos[6]->removeQuantidade(auxREd);
                    ilha[l][c].removeConstrucao();
                }

                ilha[l][c].removeEdificio();
                recursos[0]->adicionaQuantidade(pED);
                cout<<"Edificio vendido com sucesso!"<<endl;
                return;
            }else{
                cout<<"Aviso: Não existe nenhum edificio nessa zona!"<<endl;
                return;
            }
        }
    }
    cout<<"Aviso: Introduziu mal o comando!\n\tComando: vende <tipo> <quantidade> ou vende <linha> <coluna>!"<<endl;
}

void comandoNext(InfoIlha II, Mapa **ilha, vector<Recursos*> recursos,int da,int ds){

    /*Trabalhadores*/
    int l,c,auxl1,auxc1,auxl2,auxc2,y;
    float x;
    vector<string> produz;

    /*Trabalhadores*/
    for (l = 0; l < II.getLinha(); l++) {
        for (c = 0; c < II.getColuna(); c++) {
            if (da == ds) {
                ilha[l][c].adicionaNDT();
            } else {
                ilha[l][c].despedimento(ds);
            }
        }
    }

    /*Edificios e Recursos*/

    if(da==ds){
        for(l=0;l<II.getLinha();l++){
            for(c=0;c<II.getColuna();c++) {

                //Minas
                if (ilha[l][c].getE() == "mnF " || ilha[l][c].getE() == "mnC ") {
                    if (ilha[l][c].getDesLig()) {
                        if (ilha[l][c].verificaTrabalhadorNaZona(ilha[l][c].getProduz())) {
                            x = ilha[l][c].getProducao();
                            if (ilha[l][c].verificaEspaço(&x)) {
                                if(ilha[l][c].getE() == "mnF ")
                                    recursos[1]->adicionaQuantidade(x);
                                if(ilha[l][c].getE() == "mnC ")
                                    recursos[2]->adicionaQuantidade(x);
                            }
                        }
                    }
                }

                //Central Eletrica
                if(ilha[l][c].getE() == "elec"){
                    if (ilha[l][c].getDesLig()) {
                        produz = divideComando(ilha[l][c].getProduz());
                        if (ilha[l][c].verificaTrabalhadorNaZona(produz[0])){
                            if(verificaEdificioPe(l,c,ilha,II,"elec",&auxl1,&auxc1,&auxl2,&auxc2)) {
                                if (recursos[5]->getQuantidade() >= 1) {
                                    x = ilha[l][c].getProducao();
                                    if (ilha[l][c].verificaEspaço(&x)) {
                                        recursos[6]->adicionaQuantidade(x); //Eletricidade
                                        ilha[auxl1][auxc1].adicionaArmazem(x);
                                        recursos[2]->adicionaQuantidade(x); //Carvao
                                        recursos[5]->removeQuantidade(1); //Madeira
                                        ilha[auxl2][auxc2].removeQuantidadeFlr(1);
                                    }
                                }
                            }
                        }
                    }
                }

                //Fundicao
                if(ilha[l][c].getE() == "fun "){
                    if(ilha[l][c].getDesLig()){
                        if(ilha[l][c].verificaTrabalhadorNaZona("barras")){
                            if(verificaEdificioPe(l,c,ilha,II,"fun ",&auxl1,&auxc1,&auxl2,&auxc2)){
                                x=1;
                                if(ilha[l][c].verificaEspaço(&x)){
                                    recursos[3]->adicionaQuantidade(1);
                                    recursos[1]->removeQuantidade(1);
                                    ilha[auxl1][auxc1].removeArmazem(1);
                                    recursos[2]->removeQuantidade(1);
                                    ilha[auxl2][auxc2].removeArmazem(1);
                                }
                            }
                        }
                    }
                }

                //EdX
                if(ilha[l][c].getE() == "EdX "){
                    if(ilha[l][c].getDesLig()){
                        if(ilha[l][c].verificaTrabalhadorNaZona("vigas")){
                            if(verificaEdificioPe(l,c,ilha,II,"EdX ",&auxl1,&auxc1,&auxl2,&auxc2)){
                                x=1;
                                if(ilha[l][c].verificaEspaço(&x)){
                                    recursos[4]->adicionaQuantidade(x);
                                    recursos[5]->removeQuantidade(1);
                                    ilha[auxl1][auxc1].removeQuantidadeFlr(1);
                                }
                            }
                        }
                    }
                }


                //Madeira
                if(ilha[l][c].getZ()=="flr "){
                    float q=0;
                    if(ilha[l][c].procuraTrabalhador("len",&q)){
                        recursos[5]->adicionaQuantidade(q); //quantidade de lenhadores
                        ilha[l][c].addQuantidadeFlr(q);
                        ilha[l][c].removeArvores(q);
                    }
                }
                ilha[l][c].addDiasEdificioConstruido();
            }
        }
    }

    /*Zona*/
    if(da!=ds) {
        for(l=0;l<II.getLinha();l++){
            for(c=0;c<II.getColuna();c++){
                ilha[l][c].Desabar(recursos);
                if(!ilha[l][c].construcao()){
                    if(ilha[l][c].getZ() == "flr "){
                        if(ds%2 == 1){
                            ilha[l][c].adicionaArvores(2);
                        }
                    }
                }else{
                    if(ilha[l][c].getZ() == "flr "){
                        ilha[l][c].removeArvores(1);
                    }
                    if(ilha[l][c].getZ() == "mnt "){
                        if(ilha[l][c].getNT()>0){
                            float q=0.1*ilha[l][c].getNT();
                            recursos[1]->adicionaQuantidade(q);
                        }
                    }
                    if(ilha[l][c].getZ() == "znz "){
                        if(ilha[l][c].getNT()>0){
                            y=getRealUniform();
                            if(y<=0.30) {
                                float q = 0.2 * ilha[l][c].getNT();
                                recursos[4]->adicionaQuantidade(q);
                            }
                        }
                    }
                    if(ilha[l][c].getZ() == "pnt "){
                        ilha[l][c].zonaPnt(recursos);
                    }
                }
            }
        }
    }
}

void comandoLevelUp(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos){
    int l,c;
    if(!verificaStoi(comando[1],comando[2],&l,&c))
        return;
    l--;c--;

    if(ilha[l][c].LevelUp(recursos,II,ilha)){
        cout<<"Aviso: Edificio "<<ilha[l][c].getNE()<<" subiu para o nivel "<<ilha[l][c].getNivel()<<"!"<<endl;
        return;
    }
    cout<<"Aviso: Não foi possivel subir o nivel!"<<endl;

}

vector<string> comandoConfig(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos){

    string frase;
    vector<string> preco;
    ifstream ficheiro(comando[1]);


    if(ficheiro) {
        while (getline(ficheiro, frase)) {
            preco.push_back(frase);
        }
        return preco;
    }
    else
        cout << "ERRO a abrir o ficheiro!! Por favor verifique se o ficheiro existe ou se escreveu bem o nome do ficheiro!"<<endl;
    return preco;
}

void comandoDebug(vector<string>comando, InfoIlha II, Mapa **ilha, vector<Recursos*> recursos){
    if(comando[0] == "debcash"){
        float c;
        try {
            c = stof(comando[1]);
            recursos[0]->adicionaQuantidade(c);
        }
        catch(std::invalid_argument e) {
            cout << "Aviso: Deve introduzir um numero para que seja identificada a linha!"<<endl;
            return;
        }
    }
    if(comando[0] == "debed"){
        int l,c;
        if(!verificaStoi(comando[2],comando[3],&l,&c))
            return;
        l--;c--;
        if(verificaTipoEdificio(comando[1])) {
            if (Linhas(l, II)) {
                if (Colunas(c, II)) {
                    if (!ilha[l][c].construcao()) {
                        vector<string>o;
                        ilha[l][c].adicionaEdificios(comando[1],o);
                        cout << "\n\tAviso: O edificio " << ilha[l][c].getE() << "criado com sucesso!" << endl;
                        ilha[l][c].addConstrucao();
                    }else
                        cout << "Aviso: Já existe um edificio nessa zona com o nome " << ilha[l][c].getE() << "!" << endl;
                }
                else
                    cout << "Aviso: Introduza uma coluna válida (entre 1 - " << II.getColuna() << ")!" << endl;
            }
            else
                cout<<"Aviso: Introduza uma linha válida (entre 1 - "<<II.getLinha()<<")!"<<endl;
        }
    }
    if(comando[0] == "debkill"){
        for(int i=0;i<II.getLinha();i++)
            for(int j=0;j<II.getColuna();j++)
                ilha[i][j].removeT(comando[1]);
    }
}