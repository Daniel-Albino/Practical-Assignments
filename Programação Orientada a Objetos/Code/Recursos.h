#ifndef POO_2_RECURSOS_H
#define POO_2_RECURSOS_H
#include <iostream>
#include <string>
#include <sstream>

class Recursos {
    std::string tipo;
public:
    Recursos(std::string t):tipo(t){}
    virtual std::string getAsString() const;
    const std::string &getTipo() const {return tipo;}
    virtual float getQuantidade() const;
    virtual void removeQuantidade(float q);
    virtual float getPreco() const;
    virtual void adicionaQuantidade(float d) {adicionaQuantidade(d);}

    virtual ~Recursos() = default;
};


class Dinheiro : public Recursos{
    float quantidade;
public:
    Dinheiro(int q = 2000): Recursos("dinheiro"), quantidade(q){}
    std::string getAsString() const override;
    float getQuantidade() const override{return quantidade;}
    void removeQuantidade(float q) override;
    void adicionaQuantidade(float d) override {quantidade += d;}

    ~Dinheiro() = default;
};

class Ferro : public Recursos{
    float preco;
    float quantidade;
public:
    Ferro(float p = 1,int q=0) : Recursos("ferro"), preco(p), quantidade(q){}
    std::string getAsString() const override;
    float getPreco() const override{return preco;}
    float getQuantidade() const override{return quantidade;}
    void adicionaQuantidade(float d) override {quantidade += d;}
    void removeQuantidade(float q) override;
    Ferro &operator+=(int q);

    ~Ferro() = default;
};

class Barras : public Recursos{
    float preco;
    float quantidade;
public:
    Barras(float p = 2,int q=2) : Recursos("barras de aço"),preco(p), quantidade(q){}
    std::string getAsString() const override;
    float getPreco() const override{return preco;};
    float getQuantidade() const override{return quantidade;}
    void adicionaQuantidade(float d) override {quantidade += d;}
    void removeQuantidade(float q) override;
    Barras &operator+=(int q);

    ~Barras() = default;
};

class Carvao : public Recursos{
    float preco;
    float quantidade;
public:
    Carvao(float p = 1,int q=0) : Recursos("carvao"),preco(p), quantidade(q){}
    std::string getAsString() const override;
    float getPreco() const override{return preco;};
    float getQuantidade() const override{return quantidade;}
    void adicionaQuantidade(float d) override {quantidade += d;}
    void removeQuantidade(float q) override;
    Carvao &operator+=(int q);

    ~Carvao() = default;
};

class Madeira : public Recursos{
    float preco;
    float quantidade;
public:
    Madeira(float p = 1,int q=0) : Recursos("madeira"),preco(p), quantidade(q){}
    std::string getAsString() const override;
    float getPreco() const override{return preco;}
    float getQuantidade() const override{return quantidade;}
    void adicionaQuantidade(float d) override {quantidade += d;}
    void removeQuantidade(float q) override;
    Madeira &operator+=(int q);

    ~Madeira()= default;
};

class Vigas : public Recursos{
    float preco;
    float quantidade;
public:
    Vigas(float p = 2,int q=20) : Recursos("vigas de madeira"),preco(p), quantidade(q){}
    std::string getAsString() const override;
    float getPreco() const override{return preco;};
    float getQuantidade() const override{return quantidade;}
    void adicionaQuantidade(float d) override {quantidade += d;}
    void removeQuantidade(float q) override;
    Vigas &operator+=(int q);

    ~Vigas() = default;
};

class Eletricidade : public Recursos{
    float preco;
    float quantidade;
public:
    Eletricidade(float p = 1.5,int q=0) : Recursos("eletricidade"),preco(p), quantidade(q){}
    std::string getAsString() const override;
    float getPreco() const override{return preco;};
    float getQuantidade() const override{return quantidade;}
    void adicionaQuantidade(float d) override {quantidade += d;}
    void removeQuantidade(float q) override;
    Eletricidade &operator+=(int q);

    ~Eletricidade() = default;
};

#endif //POO_2_RECURSOS_H