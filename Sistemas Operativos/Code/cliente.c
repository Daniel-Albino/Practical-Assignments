#include "cliente.h"


int main(int argc, char *argv[]){
    if(argc < 2){
        printf("Deve indicar o seu nome (./cliente o_seu_nome).\n");
        exit(0);
    }

    //Sinais
    struct sigaction sa;
    sa.sa_sigaction = terminaCliente;
    sa.sa_flags = SA_SIGINFO;
    
    sigaction(SIGINT,&sa,NULL);

    struct sigaction sr;

    sr.sa_sigaction = funcSinal;
    sr.sa_flags = SA_SIGINFO;

    sigaction(SIGUSR1,&sr,NULL);

    CliMed c;
    strcpy(c.clioumed,"cli");
    strcpy(c.nome,argv[1]);
    strcpy(c.especialidade,"NA");
    c.pid = getpid();
    c.sair = 0;
    c.espera = 1;

    pthread_t th[3];
    DTh dados[2];
    pthread_mutex_t mutex;
    pthread_mutex_init(&mutex,NULL);
    dados[0].inicio = 0;
    dados[0].m = &mutex;
    dados[1].inicio = 0;
    dados[1].m = &mutex;

    if(pthread_create(&th[0],NULL,&teclado,&dados[0])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    if(pthread_create(&th[1],NULL,&clientemedico,&dados[1])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    if(pthread_create(&th[2],NULL,&medicocliente,&dados[1])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    

    
    int fd_envio,fd_resposta;

    sprintf(CLIENTE_FIFO_FINAL,CLIENTE_FIFO,getpid());

    if(mkfifo(CLIENTE_FIFO_FINAL,0666) == -1){
        if(errno == EEXIST){
            printf("ERRO: Fifo já em execucao!\n");
            kill(getpid(),SIGINT);
        }
        printf("ERRO: Erro ao abrir fifo!\n");
        kill(getpid(),SIGINT);
    }

    printf("Bem vindo ao serviço MEDICALso Sr./Sra. %s!\n",argv[1]);
    

    if(mkfifo(SERVER_FIFO,0666) == -1){
        do{
            printf("Indique o seu sintoma (para sair escrever 'sair'): ");
            fflush(stdin);
            fgets(c.sintoma,49,stdin);
            
            if(strcmp(c.sintoma,"sair")==0){
                c.sair=1;
                fd_envio = open(SERVER_FIFO,O_WRONLY);
                int size = write(fd_envio,&c,sizeof(c));
                close(fd_envio);
                kill(getpid(),SIGINT);
            }

            fd_envio = open(SERVER_FIFO,O_WRONLY);
            int size = write(fd_envio,&c,sizeof(c));
            close(fd_envio);
            
            fd_resposta = open(CLIENTE_FIFO_FINAL,O_RDONLY);
            int size2 = read(fd_resposta,&c,sizeof(c));
            close(fd_resposta);
            printf("\nResposta do classificador - [%s]\n",c.resposta);
            memset(c.resposta,0,sizeof(c.resposta));
            break;

        }while(1);
    }else{
        unlink(SERVER_FIFO);
        printf("\nO sistema MEDICALso ainda não está em funcionamento!\nVolte mais tarde!\n");
        kill(getpid(),SIGINT);
    }

    dados[0].inicio = 1;
    dados[1].inicio = 1;
    dados[1].id=0;

    DTh d;
    Con con;
    do{
        sprintf(CLIENTE_FIFO_FINAL,CLIENTE_FIFO,getpid());
        int fdServidor = open(CLIENTE_FIFO_FINAL,O_RDONLY | O_NONBLOCK);
        if(fdServidor==-1){
            printf("Erro no fifo %s",CLIENTE_FIFO_FINAL);
            exit(-1);
        }
        int sizeServidor = read(fdServidor,&c,sizeof(c));
        if(sizeServidor>0){
            printf("%d",con.id);
            d.id=con.id;
            dados[1].id=d.id;
            dados[1].inicio=2;
            dados[0].inicio=2;
            pthread_kill(th[0],SIGUSR1);
        }
        close(fdServidor);
    }while(1);

    c.sair=1;
    fd_envio = open(SERVER_FIFO,O_WRONLY);
    int size = write(fd_envio,&c,sizeof(c));
    close(fd_envio);
    kill(getpid(),SIGINT);
}

void terminaCliente(int s, siginfo_t *i, void *v){
    printf("\nVoce saiu do sistema MEDICALso!\n");
    fflush(stdout);
    unlink(CLIENTE_FIFO_FINAL);
    exit(1);
}

void funcSinal(int signum, siginfo_t * info, void *secret){
}

void Timeout(){
    CliMed c;
    strcpy(c.clioumed,"cli");
    c.pid = getpid();
    c.sair = 1;
    int fd_envio = open(SERVER_FIFO,O_WRONLY);
    int size = write(fd_envio,&c,sizeof(c));
    close(fd_envio); 
    kill(getpid(),SIGINT);
}


void *teclado(void *dados){
    DTh* pdados = (DTh*) dados;
    char sair[10];
    do{
        if(pdados->inicio==1){
            printf("\nEspere para ser atendido. Se pretender sair escreva 'sair': ");
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
        
        if(pdados->inicio==2){
            printf("\n>>");
            scanf("%s",d.clitomed);
            sprintf(CLIENTE_MEDICO_FIFO_FINAL,CLIENTE_MEDICO_FIFO,pdados->id);
            int fdEnvia = open(CLIENTE_MEDICO_FIFO_FINAL,O_WRONLY | O_NONBLOCK);
            if(fdEnvia==-1){
                printf("Erro no fifo %s",CLIENTE_MEDICO_FIFO_FINAL);
                exit(-1);
            }
            int size = write(fdEnvia,&d,sizeof(d));
            close(fdEnvia);
        }
    }while(1);
    kill(getpid(),SIGINT);
}

void *medicocliente(void *dados){
    DTh* pdados = (DTh*) dados;
    DTh d;
    Con c;
    c.id=0;
    char sair[10];
    do{
        
        if(pdados->inicio==2){
            sprintf(MEDICO_CLIENTE_FIFO_FINAL,MEDICO_CLIENTE_FIFO,d.id);
            printf("%s",MEDICO_CLIENTE_FIFO_FINAL);
            int fdEnvia = open(MEDICO_CLIENTE_FIFO_FINAL,O_RDONLY | O_NONBLOCK);
            if(fdEnvia==-1){
                printf("Erro no fifo %s",MEDICO_CLIENTE_FIFO_FINAL);
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
