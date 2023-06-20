#ifndef TP_SO_BALCAO_H
#define TP_SO_BALCAO_H

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

#define MEDICO_FIFO "medico%d"
char MEDICO_FIFO_FINAL[100];

#define CLIENTE_MEDICO_FIFO "clientemedico%d"
char CLIENTE_MEDICO_FIFO_FINAL[100];

#define MEDICO_CLIENTE_FIFO "medicocliente%d"
char MEDICO_CLIENTE_FIFO_FINAL[100];

typedef struct Balcao Balc, *pBalc;

struct Balcao{
    char comandos[50]; //Variavel onde é escrito os comandos do administrador
    int fk1, balc_to_clas[2],clas_to_balc[2],MaxMedicos,MaxClientes;
    //fork, files descriptors, vars ambiente
};

typedef struct ClienteMedico CliMed, *pCliMed;

struct ClienteMedico{
    char clioumed[10];
    char sintoma[50],resposta[50]; //sintomas e resposta do classificador
    char especialidade[50];
    char nome[100];
    int sair;
    int espera;
    pid_t pid; 
};


typedef struct InfoCliente ICli, *pICli;

struct InfoCliente{
    char sintoma[50],especialidade[50];
    char nome[100];
    int espera;
    pid_t pid; //pid do cliente
    pICli prox;
};


typedef struct InfoMedicos IMed, *pIMed;

struct InfoMedicos{
    char especialidade[50];
    char nome[100];
    int espera;
    pid_t pid; //pid do medico
    pIMed prox;
};

typedef struct DThreads DTh,*pDTh;

struct DThreads{
    int stop;
    int freq;
    int maxcli,maxmed;
    pICli listaClientes;
    pIMed listaMedicos;
    pthread_mutex_t *m;
};

typedef struct Consulta Con, *pCon;

struct Consulta{
    int id;
};

void classifica(pCliMed c);

void terminaBalcao(int s, siginfo_t *i, void *v);

void closeserver(DTh *dados);

void *teclado(void *dados);

void *temporizador(void *dados);

void comandos(char *varSair,DTh *pdados);

int divideComando(char *varSair,char *c,int *x);

int ProcuraConsulta(pICli listaUtentes,pIMed listaMedicos,int *idU,int *idM);

pICli adicionaUtente(pICli listaUtentes, pCliMed c);

void mostraInfoCliente(pICli listaUtentes);

void preencheUtente(pICli novo,pCliMed c);

pICli eliminaCliente(pICli listaUtentes,int pid);

void libertaClientes(pICli p);

pIMed adicionaMedico(pIMed listaMedicos, pCliMed m);

void mostraInfoMedico(pIMed listaMedicos);

void preencheMedico(pIMed novo,pCliMed m);

void libertaMedicos(pIMed p);

pIMed eliminaMedico(pIMed listaMedicos,int pid);

#endif //TP_SO_BALCAO_H