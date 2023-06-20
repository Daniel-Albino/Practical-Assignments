#ifndef POO_2_INTERFACE_H
#define POO_2_INTERFACE_H

#include <iostream>
#include <string>

class InfoIlha{
    int linha;
    int coluna;
public:
    InfoIlha(int l, int c);
    int getLinha() const;
    int getColuna() const;

    ~InfoIlha() = default;
};
void infoInicial();
bool verificaOpcao(std::string o);
InfoIlha setLinhasColunas();
bool verificaLinha(int x);
bool verificaColuna(int x);


#endif //POO_2_INTERFACE_H
