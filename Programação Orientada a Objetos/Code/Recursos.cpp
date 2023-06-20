#include "Recursos.h"

using namespace std;

std::string Recursos::getAsString() const {
    std::ostringstream os;

    os<<tipo<<" = ";

    return os.str();
}

float Recursos::getQuantidade() const {
    return getQuantidade();
}

void Recursos::removeQuantidade(float q) {
    removeQuantidade(q);
}

float Recursos::getPreco() const {
    return getPreco();
}


//Dinheiro
std::string Dinheiro::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Dinheiro::removeQuantidade(float q) {
    quantidade-=q;
}


//Ferro

Ferro &Ferro::operator+=(int q){
    quantidade+=q;
    return *this;
}

std::string Ferro::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Ferro::removeQuantidade(float q) {
    quantidade-=q;
}


//Carvão


Carvao &Carvao::operator+=(int q){
    quantidade+=q;
    return *this;
}

std::string Carvao::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Carvao::removeQuantidade(float q) {
    quantidade-=q;
}

//Barras

Barras &Barras::operator+=(int q){
    quantidade+=q;
    return *this;
}

std::string Barras::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Barras::removeQuantidade(float q) {
    quantidade-=q;
}

//Madeira


Madeira &Madeira::operator+=(int q){
    quantidade+=q;
    return *this;
}

std::string Madeira::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Madeira::removeQuantidade(float q) {
    quantidade-=q;
}

//Vigas


Vigas &Vigas::operator+=(int q){
    quantidade+=q;
    return *this;
}

std::string Vigas::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Vigas::removeQuantidade(float q){
    quantidade-=q;
}

//Eletricidade


Eletricidade &Eletricidade::operator+=(int q){
    quantidade+=q;
    return *this;
}

std::string Eletricidade::getAsString() const{
    ostringstream os;

    os<<Recursos::getAsString()<<quantidade<<"  ";

    return os.str();
}

void Eletricidade::removeQuantidade(float q) {
    quantidade-=q;
}

