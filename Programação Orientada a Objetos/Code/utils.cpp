#include "utils.h"


float getRealUniform(){
    static default_random_engine e(time(0));
    static uniform_real_distribution<float> d(0,1);
    return d(e);
}

std::vector<Zona*> escolheZonas(InfoIlha II){
    std::vector<Zona*> v;
    unsigned seed = time(0);
    srand(seed);
    //Ajuda a função rand a gerar numeros aleatórios diferentes em cada execução!

    int i,j= 1+rand()%6;

    for(i=0;i<II.getLinha()*II.getColuna();i++,j++){
        if(j==1)
            v.push_back(new Montanha());
        else if(j==2)
            v.push_back(new Deserto());
        else if(j==3)
            v.push_back(new Pastagem());
        else if(j==4)
            v.push_back(new Floresta());
        else if(j==5)
            v.push_back(new Pantano());
        else if(j==6)
            v.push_back(new ZonaX());
        else if(j==7){
            j=0;i--;
        }
    }

    unsigned y = 0; //seed

    shuffle(v.begin(), v.end(), default_random_engine(y));

    return v;
}


bool verificaTipoEdificio(string e){
    if(e == "mnF")
        return true;
    if(e == "mnC")
        return true;
    if(e == "elec")
        return true;
    if(e == "bat")
        return true;
    if(e == "fun")
        return true;
    if(e == "EdX")
        return true;
    return false;
}

bool Linhas(int x,InfoIlha II){
    if(x<= II.getLinha() && x>=0)
        return true;
    return false;
}

bool Colunas(int x,InfoIlha II){
    if(x<= II.getColuna() && x>=0)
        return true;
    return false;
}

bool verificaTrabalhador(std::string t){
    if(t == "oper")
        return true;
    if(t == "len")
        return true;
    if(t == "miner")
        return true;
    return false;
}

string nomeTrabalhador(string t){ //Função que dá o nome de O -> Operario / L -> Lenhador / M -> Mineiro
    ostringstream os;
    if(t == "oper"){
        os<<"O";
        return os.str();
    }
    if(t == "len"){
        os<<"L";
        return os.str();
    }
    if(t == "miner"){
        os<<"M";
        return os.str();
    }
    return "    ";
}

bool verificaStoi(string strL,string strC, int *l,int *c){
    try {
        *l = stoi(strL);
    }
    catch(std::invalid_argument e) {
        cout << "Aviso: Deve introduzir um numero para que seja identificada a linha!"<<endl;
        return false;
    }

    try {
        *c = stoi(strC);
    }
    catch(std::invalid_argument e) {
        cout << "Aviso: Deve introduzir um numero para que seja identificada a coluna!"<<endl;
        return false;
    }

    return true;
}

bool verificaRecursosEdificios(Mapa *ilha, vector<Recursos*> recursos,string modoPagamento) {
    int aux;
    for (int i = 0; i < recursos.size(); i++) {
        if (recursos[i]->getTipo() == "dinheiro") {
            if (modoPagamento == "dinheiro") {
                if (ilha->getE() == "elec" || ilha->getE() == "fun " || ilha->getE() == "EdX ")
                    aux = ilha->precoEd();
                if (ilha->getE() == "mnF " || ilha->getE() == "mnC ")
                    aux = ilha->precoMinas("dinheiro");
                if (aux <= recursos[i]->getQuantidade()) {
                    recursos[i]->removeQuantidade(aux);
                    return true;
                }
            }
        }
        if (recursos[i]->getTipo() == "vigas de madeira") {
            if (modoPagamento == "vigas")
                aux = ilha->precoMinas("vigas");
            if (aux <= recursos[i]->getQuantidade()) {
                recursos[i]->removeQuantidade(aux);
                return true;
            }
        }
    }
    return false;
}

bool verificaRecursosEdificiosBateria(Mapa *ilha,vector<Recursos*> recursos){
    //função para a bateria que e construida com 10 de dinheiro e 10 vigas
    int d,v;
    for(int i = 0;i< recursos.size();i++) {
        if (recursos[i]->getTipo() == "dinheiro") {
            if(ilha->precoEd()<=recursos[i]->getQuantidade()) {
                d = i;
            }
            else
                return false;
        }
        if (recursos[i]->getTipo() == "vigas de madeira") {
            if(ilha->precoEd()<=recursos[i]->getQuantidade()) {
                v = i;
            }
            else{
                return false;
            }
        }
    }
    recursos[d]->removeQuantidade(ilha->precoEd());
    recursos[v]->removeQuantidade(ilha->precoEd());
    return true;
}

string metodoPagamentoEdificios(){
    string modoPagamento;
    do {
        cout << "\n\tPretende pagar o edificio em dinheiro ou em vigas de madeira?\n\tR: ";
        getline(cin, modoPagamento);
    }while(!verificaRespostaPagamentoEdificios(&modoPagamento));
    return modoPagamento;
}

bool verificaRespostaPagamentoEdificios(string *modoPagamento){
    if(*modoPagamento == "dinheiro")
        return true;
    if(*modoPagamento == "Dinheiro"){
        *modoPagamento = "dinheiro";
        return true;
    }
    if(*modoPagamento == "vigas")
        return true;
    if(*modoPagamento == "Vigas"){
        *modoPagamento = "vigas";
        return true;
    }
    if(*modoPagamento == "vigas de madeira"){
        *modoPagamento = "vigas";
        return true;
    }
    if(*modoPagamento == "Vigas de madeira"){
        *modoPagamento = "vigas";
        return true;
    }
    cout<<"Deve escolher entre as seguintes palavras:"<<endl;
    cout<<"\ta) dinheiro\tb) Dinheiro\tc) vigas\td) Vigas\te) vigas de madeira\tf) Vigas de madeira"<<endl;
    return false;
}

bool verificaRecursosTrabalhadores(Mapa *ilha,vector<Recursos*>recursos,string id){
    int aux = ilha->precoTrab(id);
    if(aux != 0) {
        if (aux < recursos[0]->getQuantidade()) {
            recursos[0]->removeQuantidade(aux);
            return true;
        }
    }
    return false;
}

bool verificaVende(string *comando){
    if(*comando == "ferro")
        return true;
    if(*comando == "carvao")
        return true;
    if(*comando == "barras"){
        *comando = "barras de aço";
        return true;
    }
    if(*comando == "vigas"){
        *comando = "vigas de madeira";
        return true;
    }
    if(*comando == "madeira")
        return true;
    if(*comando == "eletricidade")
        return true;
    return false;
}

bool verificaEdificioPe(int l,int c,Mapa **ilha,InfoIlha II,string Ed,int *L1,int *C1,int *L2,int *C2){
    int l1,c1,v1=0,v2=0;
    float x;
    if(Ed == "elec") {
        l1 = l;
        c1 = c;
        if (l1 - 1 >= 0) {
            l1-=1;
            if (ilha[l1][c1].getE() == "bat ") {
                if(ilha[l1][c1].getDesLig()) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        v2 = 1;
                        *L2=l1;
                        *C2=c1;
                    }
                    v2 = 1;
                    *L2=l1;
                    *C2=c1;
                }
            }
        }
        l1 = l;c1 = c;
        if (l1 + 1 < II.getLinha()) {
            l1+=1;
            if (ilha[l1][c1].getE() == "bat ") {
                if(ilha[l1][c1].getDesLig()) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        v2 = 1;
                        *L2=l1;
                        *C2=c1;
                    }
                    v2 = 1;
                    *L2=l1;
                    *C2=c1;
                }
            }
        }
        l1 = l;c1 = c;
        if (c1 - 1 >= 0) {
            c1-=1;
            if (ilha[l1][c1].getE() == "bat ") {
                if(ilha[l1][c1].getDesLig()) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        v2 = 1;
                        *L2=l1;
                        *C2=c1;
                    }
                    v2 = 1;
                    *L2=l1;
                    *C2=c1;
                }
            }
        }
        l1 = l;c1 = c;
        if (c1 + 1 < II.getColuna()) {
            c1+=1;
            if (ilha[l1][c1].getE() == "bat ") {
                if(ilha[l1][c1].getDesLig()) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        v2 = 1;
                        *L2=l1;
                        *C2=c1;
                    }
                    v2 = 1;
                    *L2=l1;
                    *C2=c1;
                }
            }
        }
    }
    if(Ed == "fun "){
        l1 = l;
        c1 = c;
        if (l1 - 1 >= 0) {
            l1-=1;
            if (ilha[l1][c1].getE() == "mnF ") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getE() == "mnC " || ilha[l1][c1].getE() == "elec") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v2 = 1;
                    *L2 = l1;
                    *C2 = c1;
                }
            }
        }
        l1 = l;
        c1 = c;
        if (l1 + 1 < II.getLinha()) {
            l1+=1;
            if (ilha[l1][c1].getE() == "mnF ") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getE() == "mnC " || ilha[l1][c1].getE() == "elec") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v2 = 1;
                    *L2 = l1;
                    *C2 = c1;
                }
            }
        }
        l1 = l;
        c1 = c;
        if (c1 - 1 >= 0) {
            c1-=1;
            if (ilha[l1][c1].getE() == "mnF ") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getE() == "mnC " || ilha[l1][c1].getE() == "elec") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v2 = 1;
                    *L2 = l1;
                    *C2 = c1;
                }
            }
        }
        l1 = l;
        c1 = c;
        if (c1 + 1 < II.getColuna()) {
            c1+=1;
            if (ilha[l1][c1].getE() == "mnF ") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v1 = 1;
                    *L1 = l1;
                    *C1 = c1;
                }
            }
            if (ilha[l1][c1].getE() == "mnC " || ilha[l1][c1].getE() == "elec") {
                if (ilha[l1][c1].quatidadeRecursosZona() >= 1) {
                    v2 = 1;
                    *L2 = l1;
                    *C2 = c1;
                }
            }
        }
    }


    if (Ed == "EdX "){
        l1 = l;
        c1 = c;
        if(l1 - 1 >= 0) {
            l1-=1;
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        *L1=l1;
                        *C1=c1;
                        return true;
                    }
                    *L1=l1;
                    *C1=c1;
                    return true;
                }
            }
        }
        l1 = l;
        c1 = c;
        if(l1 + 1 < II.getLinha()){
            l1+=1;
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        *L1=l1;
                        *C1=c1;
                        return true;
                    }
                    *L1=l1;
                    *C1=c1;
                    return true;
                }
            }
        }
        l1 = l;
        c1 = c;
        if(c1 - 1 >= 0){
            c1-=1;
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        *L1=l1;
                        *C1=c1;
                        return true;
                    }
                    *L1=l1;
                    *C1=c1;
                    return true;
                }
            }
        }
        l1 = l;
        c1 = c;
        if(c1 + 1 < II.getColuna()){
            c1+=1;
            if (ilha[l1][c1].getZ() == "flr ") {
                if(ilha[l1][c1].getQuantidadeFlr() >= 1) {
                    if (ilha[l1][c1].procuraTrabalhador("len", &x)) {
                        *L1=l1;
                        *C1=c1;
                        return true;
                    }
                    *L1=l1;
                    *C1=c1;
                    return true;
                }
            }
        }
    }


    if(v1 == 1){
        if(v2 == 1) {
            return true;
        }
    }

    return false;
}

void removeRecursosEdificios(string comando, InfoIlha II, int quantidade, Mapa **ilha){
    float auxEd,q;
    while(quantidade != 0){
        for(int l=0;l<II.getLinha();l++){
            for(int c=0;c<II.getColuna();c++) {
                vector<string> produz = divideComando(ilha[l][c].getProduz());
                if (produz[0] == comando) {
                    if (comando == "ferro") {
                        for (int i = 0; i < II.getLinha(); i++) {
                            for (int j = 0; j < II.getColuna(); j++) {
                                if (ilha[i][j].getE() == "mnF ") {
                                    auxEd = ilha[i][j].quatidadeRecursosZona();
                                    q = quantidade - auxEd;
                                    if (q < 0) {
                                        ilha[i][j].removeArmazem(quantidade);
                                        quantidade = 0;
                                        break;
                                    } else {
                                        ilha[i][j].removeArmazem(auxEd);
                                        quantidade -= auxEd;
                                    }
                                }
                            }
                        }
                    }
                    if (comando == "carvao") {
                        for (int i = 0; i < II.getLinha(); i++) {
                            for (int j = 0; j < II.getColuna(); j++) {
                                if (ilha[i][j].getE() == "mnC " || ilha[i][j].getE() == "elec") {
                                    auxEd = ilha[i][j].quatidadeRecursosZona();
                                    q = quantidade - auxEd;
                                    if (q < 0) {
                                        ilha[i][j].removeArmazem(quantidade);
                                        quantidade = 0;
                                        break;
                                    } else {
                                        ilha[i][j].removeArmazem(auxEd);
                                        quantidade -= auxEd;
                                    }
                                }
                            }
                        }
                    }
                    if (comando == "barras") {
                        for (int i = 0; i < II.getLinha(); i++) {
                            for (int j = 0; j < II.getColuna(); j++) {
                                if (ilha[i][j].getE() == "fun ") {
                                    auxEd = ilha[i][j].quatidadeRecursosZona();
                                    q = quantidade - auxEd;
                                    if (q < 0) {
                                        ilha[i][j].removeArmazem(quantidade);
                                        quantidade = 0;
                                        break;
                                    } else {
                                        ilha[i][j].removeArmazem(auxEd);
                                        quantidade -= auxEd;
                                    }
                                }
                            }
                        }
                    }
                    if (comando == "eletricidade") {
                        for (int i = 0; i < II.getLinha(); i++) {
                            for (int j = 0; j < II.getColuna(); j++) {
                                if (ilha[i][j].getE() == "bat ") {
                                    auxEd = ilha[i][j].quatidadeRecursosZona();
                                    q = quantidade - auxEd;
                                    if (q < 0) {
                                        ilha[i][j].removeArmazem(quantidade);
                                        quantidade = 0;
                                        break;
                                    } else {
                                        ilha[i][j].removeArmazem(auxEd);
                                        quantidade -= auxEd;
                                    }
                                }
                            }
                        }
                    }
                    if(produz[0] == "vigas"){
                        for (int i = 0; i < II.getLinha(); i++) {
                            for (int j = 0; j < II.getColuna(); j++) {
                                if (ilha[i][j].getE() == "EdX ") {
                                    auxEd = ilha[i][j].quatidadeRecursosZona();
                                    q = quantidade - auxEd;
                                    if (q < 0) {
                                        ilha[i][j].removeArmazem(quantidade);
                                        quantidade = 0;
                                        break;
                                    } else {
                                        ilha[i][j].removeArmazem(auxEd);
                                        quantidade -= auxEd;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        break; //caso não exista nenhum edificio com recursos salta! (ciclo infinito se nao tiver este break!)
    }
}


bool verificaFimJogo(InfoIlha II,Mapa **ilha,vector<Recursos*>recursos){
    int x=0,contEd=0,contValorEd=0,contTLen=0;

    for(int i=0;i<II.getLinha();i++){
        for(int j=0;j<II.getColuna();j++){
            if(ilha[i][j].construcao()){
                contEd++;
                contValorEd += ilha[i][j].precoEd();
            }
            ilha[i][j].procuraT("len",&contTLen);
        }
    }


    if(contEd == 0){
        if(contTLen == 0) {
            if (recursos[x]->getQuantidade() == 0) {
                x++;
                if (recursos[x]->getQuantidade() == 0) {
                    x++;
                    if (recursos[x]->getQuantidade() == 0) {
                        x++;
                        if (recursos[x]->getQuantidade() == 0) {
                            x++;
                            if (recursos[x]->getQuantidade() == 0) {
                                x++;
                                if (recursos[x]->getQuantidade() == 0) {
                                    x++;
                                    if (recursos[x]->getQuantidade() == 0) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    return false;
}