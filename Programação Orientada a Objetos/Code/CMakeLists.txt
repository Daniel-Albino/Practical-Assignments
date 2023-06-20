cmake_minimum_required(VERSION 3.20)
project(POO)

set(CMAKE_CXX_STANDARD 14)

add_executable(POO_2 main.cpp Edificios.cpp Edificios.h Mapa.cpp Mapa.h Zona.cpp Zona.h Recursos.cpp Recursos.h
                Trabalhadores.cpp Trabalhadores.h Jogo.cpp Jogo.h Interface.cpp Interface.h Ficheiros.cpp
                Ficheiros.h utils.cpp utils.h Comandos.cpp Comandos.h Dia.cpp Dia.h)
