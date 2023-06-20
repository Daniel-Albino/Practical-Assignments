#ifndef POO_2_ZONA_H
#define POO_2_ZONA_H
#include <iostream>
#include <string>
#include <vector>

class Zona {
protected:
    std::string tipo;
public:
    Zona(std::string t) : tipo(t){};
    const std::string &getZona() const {return tipo;}
    virtual int getArvores();
    virtual void removeArvores(int q){ removeArvores(q);}
    virtual void adicionaArvores(int q){ adicionaArvores(q);}
    virtual int getQuantidade(){return getQuantidade();}
    virtual void adicionaQuantidade(int q) { adicionaQuantidade(q);}
    virtual void removeQuantidade(int q){removeQuantidade(q);}
    virtual float getProducao() const {return getProducao();}
    virtual bool isDemissao() const {return isDemissao();}
    virtual int getLimiteArvores() const {return getLimiteArvores();}
    virtual bool getConstrucao() const {return getConstrucao();}
    virtual void addConstrucao() {addConstrucao();}
    virtual void removeConstrucao() {removeConstrucao();}

    virtual ~Zona() = default;
};

class Deserto : public Zona{
    bool demissao;
    float producao;
    bool construcao = false;
public:
    Deserto(bool d = true,float p = 0.5) : Zona("dsr"), demissao(d),producao(p){}
    bool isDemissao() const override{return demissao;}
    float getProducao() const override{return producao;}
    bool getConstrucao() const override{return construcao;}
    void addConstrucao() override;
    void removeConstrucao() override;

    ~Deserto() = default;
};

class Pastagem : public Zona{
    bool demissao;
    float producao;
    bool construcao = false;
public:
    Pastagem(bool d = false, float p = 1) : Zona("pas"), demissao(d),producao(p){}
    bool isDemissao() const override{return demissao;}
    float getProducao() const override{return producao;}
    bool getConstrucao() const override{return construcao;}
    void addConstrucao() override;
    void removeConstrucao() override;

    ~Pastagem() = default;
};

class Floresta : public Zona{
    int arvores;
    int limiteArvores;
    int quantidade;
    bool demissao;
    int producao;
    bool construcao = false;
public:
    Floresta(int lA=100,int q=0,bool d = true,int p=1);
    int getArvores() override{return arvores;}
    float getProducao() const override{return producao;}
    void removeArvores(int q) override;
    void adicionaArvores(int q);
    int getLimiteArvores() const override{return limiteArvores;}
    int getQuantidade() override{return quantidade;}
    void adicionaQuantidade(int q) override{quantidade+=q;}
    void removeQuantidade(int q) override{quantidade-=1;}
    bool isDemissao() const override{return demissao;}
    bool getConstrucao() const override{return construcao;}
    void addConstrucao() override;
    void removeConstrucao() override;

    ~Floresta() = default;
};

class Montanha : public Zona{
    bool demissao;
    float producao;
    bool construcao = false;
public:
    Montanha(float p = 2, bool d = true) : Zona("mnt"), producao(p),demissao(d){}
    float getProducao() const {return producao;}
    bool isDemissao() const override{return demissao;}
    bool getConstrucao() const override{return construcao;}
    void addConstrucao() override;
    void removeConstrucao() override;

    ~Montanha() = default;
};

class Pantano : public Zona{
    float producao;
    bool demissao;
    bool construcao = false;
public:
    Pantano(float p = 1,float d = true) : Zona("pnt"), producao(p),demissao(d){}
    float getProducao() const {return producao;}
    bool isDemissao() const override{return demissao;}
    bool getConstrucao() const override{return construcao;}
    void addConstrucao() override;
    void removeConstrucao() override;

    ~Pantano() = default;
};

class ZonaX : public Zona{
    float producao;
    bool demissao;
    bool construcao = false;
public:
    ZonaX(float p = 1,float d = true) : Zona("znz"),producao(p), demissao(d) {};
    float getProducao() const {return producao;}
    bool isDemissao() const override{return demissao;}
    bool getConstrucao() const override{return construcao;}
    void addConstrucao() override;
    void removeConstrucao() override;

    ~ZonaX() = default;
};

int getNArvores();
#endif //POO_2_ZONA_H
