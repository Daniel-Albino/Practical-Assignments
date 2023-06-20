#ifndef POO_2_EDIFICIOS_H
#define POO_2_EDIFICIOS_H
#include <iostream>
#include <string>
#include <sstream>

class Edificios {
    std::string tipo;
    bool ligado = false;
    std::string produz;
    int diasConstruido;
public:
    Edificios(std::string tipo,std::string p,int d=0);

    const std::string &getEdificio() const;

    void DesEdificio();
    void LigaEdificio();
    const std::string &getProduz() const {return produz;}

    virtual int getPreco() const{return getPreco();}
    virtual int getPrecoVigas() const {return getPrecoVigas();}
    virtual int getProducao() const {return getProducao();}
    virtual bool adicionaArmazem(int x) {return adicionaArmazem(x);}
    virtual void removeArmazem(int x);
    virtual int getArmazem() const {return getArmazem();}
    virtual float getDesabar() const {return getDesabar();}
    virtual bool addNivel() {return addNivel();}
    virtual int getNivel() const {return getNivel();}
    virtual int getPrecoNivel() const {return getPrecoNivel();}
    virtual void addArmazenamento() {addArmazenamento();}

    int getDiasConstruido() const {return diasConstruido;}
    void addDiasConstruido() {diasConstruido+=1;}
    bool isLigado();

    virtual ~Edificios() = default;
};

class MinaFerro : public Edificios{
    int preco;
    int precoVigas;
    int producao;
    float desabar;
    int armazena;
    int armazem;
    int nivel;
    int precoLevelUp; //+1 viga
public:
    MinaFerro(int pre = 100,int preV = 10,int pro = 2,float d= 0.15,int a = 100,int n=1,int arm = 0,int pl = 15)
        : Edificios("mnF","ferro"),preco(pre),precoVigas(preV),producao(pro),desabar(d),armazena(a),nivel(n), armazem(arm),precoLevelUp(pl){}

    int getPreco() const override{return preco;}
    int getPrecoVigas() const override {return precoVigas;}
    int getProducao() const override;
    bool adicionaArmazem(int x) override;
    void removeArmazem(int x) override {armazem-=x;}
    float getDesabar() const override{return desabar;}
    int getArmazem() const override{return armazem;}
    int getNivel() const override{return nivel;}
    int getPrecoNivel() const override {return precoLevelUp;}
    bool addNivel() override;
    void addArmazenamento() override {armazena+=10;}
    ~MinaFerro() = default;
};

class MinaCarvao : public Edificios{
    int preco; //preco em dinheiro
    int precoVigas;
    int producao;
    float desabar;
    int armazena;
    int armazem;
    int nivel;
    int precoLevelUp; //+1 viga
public:
    MinaCarvao(int pre = 100,int preV = 10,int pro = 2,float d= 0.10,int a = 100,int n=1,int arm = 0,int pl = 10)
            : Edificios("mnC","carvao"),preco(pre),precoVigas(preV),producao(pro),desabar(d),armazena(a),nivel(n),armazem(arm),precoLevelUp(pl){}
    int getPreco() const override{return preco;}
    int getPrecoVigas() const override {return precoVigas;}
    int getProducao() const override;
    bool adicionaArmazem(int x) override;
    void removeArmazem(int x) override {armazem-=x;}
    float getDesabar() const override{return desabar;}
    int getArmazem() const override{return armazem;}
    int getNivel() const override{return nivel;}
    int getPrecoNivel() const override {return precoLevelUp;}
    bool addNivel() override;
    void addArmazenamento() override {armazena+=10;}

    ~MinaCarvao() = default;
};

class CentralEletrica : public Edificios{
    int preco;
    int producao;
    int armazena;
    int armazem;
    int nivel;
    int precoLevelUp;
public:
    CentralEletrica(int pre = 15,int pro = 2,int a = 100,int n=1,int arm = 0,int pl = 20)
            : Edificios("elec","eletricidade carvao"),preco(pre),producao(pro),armazena(a),nivel(n),armazem(arm),precoLevelUp(pl){}
    int getPreco() const override{return preco;}
    int getProducao() const override {return producao;}
    bool adicionaArmazem(int x) override;
    void removeArmazem(int x) override {armazem-=x;}
    int getArmazem() const override{ return armazem;}
    int getNivel() const override{return nivel;}
    int getPrecoNivel() const override {return precoLevelUp;}
    bool addNivel() override;
    void addArmazenamento() override {armazena+=10;}

    ~CentralEletrica() = default;
};

class Bateria : public Edificios{
    int preco; //+10 vigas
    int armazena;
    int armazem;
    int nivel;
    int precoLevelUp;
public:
    Bateria(int pre = 10,int a = 100,int n=1,int arm = 0,int pl = 15)
            : Edificios("bat","NA"),preco(pre),armazena(a),nivel(n),armazem(arm),precoLevelUp(pl){}
    int getPreco() const override{return preco;}
    bool adicionaArmazem(int x) override;
    void removeArmazem(int x) override {armazem-=x;}
    int getArmazem() const override{ return armazem;}
    int getNivel() const override{return nivel;}
    int getPrecoNivel() const override {return precoLevelUp;}
    bool addNivel() override;
    void addArmazenamento() override {armazena+=10;}

    ~Bateria() = default;
};

class Fundicao : public Edificios{
    int preco;
    int producao;
    int armazena;
    int armazem;
    int nivel;
    int precoLevelUp;
public:
    Fundicao(int pre = 10,int pro = 1,float d= 0.10,int a = 100,int n=1,int arm = 0,int pl = 25)
            : Edificios("fun","barras de aço"),preco(pre),producao(pro),armazena(a),nivel(n),armazem(arm),precoLevelUp(pl){}
    int getPreco() const override{return preco;}
    int getProducao() const override {return producao;}
    bool adicionaArmazem(int x) override;
    void removeArmazem(int x) override {armazem-=x;}
    int getArmazem() const override{return armazem;}
    int getNivel() const override{return nivel;}
    int getPrecoNivel() const override {return precoLevelUp;}
    bool addNivel() override;
    void addArmazenamento() override {armazena+=10;}

    ~Fundicao() = default;
};

class EdificioX : public Edificios{
    int preco;
    int producao;
    int armazem;
    int armazena;
    int nivel;
    int precoLevelUp;
public:
    EdificioX(int pre = 20, int pro = 1,int a = 50,int n=1,int arm = 0,int pl = 10)
    : Edificios("EdX","vigas de madeira"), preco(pre),producao(pro),armazena(a),nivel(n),armazem(arm),precoLevelUp(pl){}
    int getPreco() const override{return preco;}
    int getProducao() const override {return producao;}
    bool adicionaArmazem(int x) override;
    void removeArmazem(int x) override {armazem-=x;}
    int getArmazem() const override{return armazem;}
    int getNivel() const override{return nivel;}
    int getPrecoNivel() const override {return precoLevelUp;}
    bool addNivel() override;
    void addArmazenamento() override {armazena+=10;}

    ~EdificioX() = default;
};

#endif //POO_2_EDIFICIOS_H
