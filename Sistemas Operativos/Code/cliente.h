#ifndef TP_SO_CLIENTE_H
#define TP_SO_CLIENTE_H

#include <stdio.h>
#include <unistd.h>
#include <sys/stat.h>
#include <signal.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <limits.h>
#include <sys/types.h>
#include <pthread.h>

#define SERVER_FIFO "servidor"
#define CLIENTE_FIFO "cliente%d"
char CLIENTE_FIFO_FINAL[100];

#define CLIENTE_MEDICO_FIFO "clientemedico%d"
char CLIENTE_MEDICO_FIFO_FINAL[100];

#define MEDICO_CLIENTE_FIFO "medicocliente%d"
char MEDICO_CLIENTE_FIFO_FINAL[100];

typedef struct ClienteMedico CliMed, *pCliMed;

struct ClienteMedico{
    char clioumed[10];
    char sintoma[50],resposta[50]; //sintomas e resposta do classificador
    char especialidade[50];
    char nome[100];
    int sair;
    int espera;
    pid_t pid; //pid do cliente
};

typedef struct TDADOS DTh, *pDTh;

struct TDADOS{
    char clitomed[500];
    char medtocli[500];
    int id;
    int inicio;
    pthread_mutex_t *m;
};

typedef struct Consulta Con, *pCon;

struct Consulta{
    int id;
};

void terminaCliente(int s, siginfo_t *i, void *v);

void funcSinal(int signum, siginfo_t * info, void *secret);

void *clientemedico(void *dados);

void *medicocliente(void *dados);

void *teclado(void *dados);

#endif //TP_SO_CLIENTE_H
