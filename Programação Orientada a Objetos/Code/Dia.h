#ifndef POO_DIA_H
#define POO_DIA_H


class Dia {
    int dia;
public:
    Dia(int dia=1) : dia(dia) {}
    int getDia(){return dia;}
    Dia operator++();
    ~Dia() = default;
};

#endif //POO_DIA_H
