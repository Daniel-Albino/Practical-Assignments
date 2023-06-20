#include "medico.h"

int n=20;

int main(int argc, char *argv[]){
    if(argc < 3){
        if(argc == 1) {
            printf("Deve indicar o seu nome e a sua especialidade (./medico o_seu_nome a_sua_especialidade).\n");
        }
        if(argc == 2){
            printf("Deve indicar a sua especialidade (./medico %s a_sua_especialidade).\n",argv[1]);
        }
        exit(0);
    }
    //Sinais
    struct sigaction sa;
    sa.sa_sigaction = terminaMedico;
    sa.sa_flags = SA_SIGINFO;

    sigaction(SIGINT,&sa,NULL);

    CliMed m;
    strcpy(m.clioumed,"med");
    strcpy(m.sintoma,"NA");
    strcpy(m.resposta,"NA");
    strcpy(m.nome,argv[1]);
    strcpy(m.especialidade,argv[2]);
    m.pid = getpid();
    m.sair = 0;
    m.espera = 1;

    pthread_t th[3];
    DTh dados[2];
    pthread_mutex_t mutex;
    pthread_mutex_init(&mutex,NULL);
    dados[0].inicio = -1;
    dados[0].m = &mutex;
    dados[1].inicio = -1;
    dados[1].m = &mutex;

    if(pthread_create(&th[0],NULL,&teclado,&dados[0])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    if(pthread_create(&th[0],NULL,&clientemedico,&dados[1])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    if(pthread_create(&th[0],NULL,&medicocliente,&dados[1])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    

    int fd_envio,fdRecebe;

    if(access(SERVER_FIFO,F_OK) != 0){
		printf("[ERRO] O Servidor ainda não está em funcionamento!\n");
		exit(1);
	}

    printf("Bem vindo Sr./Sra. %s.\n",argv[1]);

    sprintf(MEDICO_FIFO_FINAL,MEDICO_FIFO,getpid());

    if(mkfifo(MEDICO_FIFO_FINAL,0666) == -1){
        if(errno == EEXIST){
            printf("ERRO: Fifo já em execucao!\n");
            kill(getpid(),SIGINT);
        }
        printf("ERRO: Erro ao abrir fifo!\n");
        kill(getpid(),SIGINT);
    }


    fd_envio = open(SERVER_FIFO,O_WRONLY);
    int size = write(fd_envio,&m,sizeof(m));
    close(fd_envio);

    
    M a;
    struct sigaction sa2;
    sa2.sa_sigaction = handler_sigalarm;
    sa2.sa_flags = SA_RESTART|SA_SIGINFO;
    sigaction(SIGALRM,&sa2,NULL);


    struct sigaction sr;

    sr.sa_sigaction = funcSinal;
    sr.sa_flags = SA_SIGINFO;

    sigaction(SIGUSR1,&sr,NULL);
    
    dados[0].inicio = 0;
    dados[1].inicio = 1;
    dados[1].id=0;

    DTh d;
    Con c;
    do{
        sprintf(MEDICO_FIFO_FINAL,MEDICO_FIFO,getpid());
        int fdServidor = open(MEDICO_FIFO_FINAL,O_RDONLY | O_NONBLOCK);
        if(fdServidor==-1){
            printf("Erro no fifo %s",MEDICO_FIFO_FINAL);
            exit(-1);
        }
        int sizeServidor = read(fdServidor,&c,sizeof(c));
        if(sizeServidor>0){
            d.id=c.id;
            dados[1].id=d.id;
            dados[1].inicio=2;
            dados[0].inicio=1;
            n=-1;
            pthread_kill(th[0],SIGUSR1);
        }
        close(fdServidor);
    }while(1);

    
    Timeout();
    kill(getpid(),SIGINT);
    return 0;
}

void terminaMedico(int s, siginfo_t *i, void *v){
    Timeout();
    unlink(MEDICO_FIFO_FINAL);
    printf("\nVoce saiu do sistema MEDICALso!\n");
    fflush(stdout);
    exit(1);
}

void funcSinal(int signum, siginfo_t * info, void *secret){
}

void handler_sigalarm(int s, siginfo_t *i, void *v){
    M *pa = (M*) v;
    if(strcmp(pa->aqui,"aqui")!=0){
        Timeout();
    }
}

void Timeout(){
    CliMed m;
    strcpy(m.clioumed,"med");
    m.pid = getpid();
    m.sair = 1;
    int fd_envio = open(SERVER_FIFO,O_WRONLY);
    int size = write(fd_envio,&m,sizeof(m));
    close(fd_envio); 
    kill(getpid(),SIGINT);
}

void *teclado(void *dados){
    DTh* pdados = (DTh*) dados;
    char sair[10];
    do{
        if(pdados->inicio==0){
            alarm(n);
            printf("Aguarde ate que um utente lhe seja atribuido!\n\tCaso pretenda desistir da consulta escreva 'sair'!\n");
            fflush(stdout);
            scanf("%s",sair);
        }
    }while(strcmp(sair,"sair")!=0);
    kill(getpid(),SIGINT);
}

void *clientemedico(void *dados){
    DTh* pdados = (DTh*) dados;
    DTh d;
    char sair[10];
    do{
        if(pdados->inicio == 2){
            sprintf(CLIENTE_MEDICO_FIFO_FINAL,CLIENTE_MEDICO_FIFO,pdados->id);
            int fdEnvia = open(CLIENTE_MEDICO_FIFO_FINAL,O_RDONLY | O_NONBLOCK);
            if(fdEnvia==-1){
                printf("Erro no fifo %s",CLIENTE_MEDICO_FIFO_FINAL);
                exit(-1);
            }
            int size = read(fdEnvia,&d,sizeof(d));
            if(size>0){
                printf("\nMedico: %s",d.medtocli);
            }
            close(fdEnvia);
        }
    }while(1);
    kill(getpid(),SIGINT);
    
}

void *medicocliente(void *dados){
    DTh* pdados = (DTh*) dados;
    DTh d;
    char sair[10];

    do{
        if(pdados->inicio == 2){
            sprintf(MEDICO_CLIENTE_FIFO_FINAL,MEDICO_CLIENTE_FIFO,pdados->id);
            printf("\n%s\n",MEDICO_CLIENTE_FIFO_FINAL);
            printf("\n>>");
            scanf("%s",d.clitomed);
            printf("%s",MEDICO_CLIENTE_FIFO_FINAL);
            int fdEnvia = open(MEDICO_CLIENTE_FIFO_FINAL,O_WRONLY | O_NONBLOCK);
                if(fdEnvia==-1){
                    printf("Erro no fifo %s",MEDICO_CLIENTE_FIFO_FINAL);
                    exit(-1);
                }
                int size = write(fdEnvia,&d,sizeof(d));
                close(fdEnvia);
        }
    }while(1);

}