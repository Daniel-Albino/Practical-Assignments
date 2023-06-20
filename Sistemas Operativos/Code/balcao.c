#include "balcao.h"

int main(int argc, char *argv[]) {

    //Sinais
    struct sigaction sa;
    sa.sa_sigaction = terminaBalcao;
    sa.sa_flags =  SA_SIGINFO;

    struct timeval tC,tM;
    fd_set read_Cli,read_Med;
    int nfd_Cli,nfd_Med;

    int x=0,y,j=0;
    int size = 0,sizem = 0;
    Balc b;
    CliMed cm;
    pICli listaUtentes = NULL;
    pIMed listaMedicos = NULL;

    pthread_t th[2];
    DTh dados[1];

    b.MaxClientes = atoi(getenv(argv[1]));
    if(b.MaxClientes == 0){
        printf("Erro!");
        exit(1);
    }

    b.MaxMedicos = atoi(getenv(argv[2]));
    if(b.MaxMedicos == 0){
        printf("Erro!");
        exit(1);
    }

    pthread_mutex_t mutex;
    pthread_mutex_init(&mutex,NULL);

    dados[0].stop = 1;
    dados[0].listaClientes = listaUtentes;
    dados[0].listaMedicos = listaMedicos;
    dados[0].m = &mutex;
    dados[0].freq = 30;
    dados[0].maxcli = b.MaxClientes;
    dados[0].maxmed = b.MaxMedicos;

    
    if(pthread_create(&th[0],NULL,&teclado,&dados[0])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }
    if(pthread_create(&th[1],NULL,&temporizador,&dados[0])!=0){
        printf("\nErro a criar a thread!\n");
        exit(-1);
    }

    
    sigaction(SIGINT,&sa,NULL);

    if(mkfifo(SERVER_FIFO,0666) == -1){
        if(errno == EEXIST){
            printf("ERRO: Fifo já em execucao!\n");
            kill(getpid(),SIGINT);
        }
        printf("ERRO: Erro ao abrir fifo!\n");
        kill(getpid(),SIGINT);
    }
    

    int fdRecebe = open(SERVER_FIFO,O_RDWR);
    if(fdRecebe == -1){
		printf("Erro!\n");
		exit(1);
	}
    int idU,idM;
    do{
        size = read(fdRecebe,&cm,sizeof(cm));
        if(size>1){
                pthread_mutex_lock(&mutex);
                if(strcmp(cm.clioumed,"cli") == 0){
                    if(cm.sair == 1){
                        listaUtentes=eliminaCliente(listaUtentes,cm.pid);
                        dados[0].listaClientes = listaUtentes;
                    }else{
                        if(dados[0].maxcli<0){
                            cm.sair=1;
                            sprintf(CLIENTE_FIFO_FINAL,CLIENTE_FIFO,cm.pid);
                            int fdEnvio = open(CLIENTE_FIFO_FINAL,O_WRONLY);
                            int size2 = write(fdEnvio,&cm,sizeof(cm));
                            close(fdEnvio);
                        }else{
                            classifica(&cm);
                            cm.sair=0;
                            sprintf(CLIENTE_FIFO_FINAL,CLIENTE_FIFO,cm.pid);
                            int fdEnvio = open(CLIENTE_FIFO_FINAL,O_WRONLY);
                            int size2 = write(fdEnvio,&cm,sizeof(cm));
                            close(fdEnvio);
                            listaUtentes = adicionaUtente(listaUtentes,&cm);
                            dados[0].listaClientes = listaUtentes;
                            printf("Novo Utente:\n\tNome: %s, classificação: %s",cm.nome,cm.resposta);
                            fflush(stdout);
                        }
                    }
                }
                if(strcmp(cm.clioumed,"med") == 0){
                    if(cm.sair == 1){
                        listaMedicos=eliminaMedico(listaMedicos,cm.pid);
                        dados[0].listaMedicos = listaMedicos;
                    }else{
                        listaMedicos=adicionaMedico(listaMedicos,&cm);
                        dados[0].listaMedicos = listaMedicos;
                        printf("Novo Medico:\n\tNome: %s, especialidade: %s",cm.nome,cm.especialidade);
                        fflush(stdout);
                    }
                }
                pthread_mutex_unlock(&mutex);
                
                int v = ProcuraConsulta(listaUtentes,listaMedicos,&idU,&idM);
                if(v==1){
                    j++;
                    Con ccm;
                    ccm.id = j;

                    sprintf(CLIENTE_MEDICO_FIFO_FINAL,CLIENTE_MEDICO_FIFO,ccm.id);
                    if(mkfifo(CLIENTE_MEDICO_FIFO_FINAL,0666) == -1){
                        if(errno == EEXIST){
                            printf("ERRO: Fifo já em execucao!\n");
                            kill(getpid(),SIGINT);
                        }
                    printf("ERRO: Erro ao abrir fifo!\n");
                    kill(getpid(),SIGINT);
                    }
                    sprintf(MEDICO_CLIENTE_FIFO_FINAL,MEDICO_CLIENTE_FIFO,ccm.id);
                    if(mkfifo(MEDICO_CLIENTE_FIFO_FINAL,0666) == -1){
                        if(errno == EEXIST){
                            printf("ERRO: Fifo já em execucao!\n");
                            kill(getpid(),SIGINT);
                        }
                    printf("ERRO: Erro ao abrir fifo!\n");
                    kill(getpid(),SIGINT);
                    }

                    sleep(2);
                    union sigval val;
                    sigqueue(idU,SIGUSR1,val);
                    sigqueue(idM,SIGUSR1,val);
                
                    sprintf(CLIENTE_FIFO_FINAL,CLIENTE_FIFO,idU);
                    int fdEnvioC = open(CLIENTE_FIFO_FINAL,O_WRONLY);
                    int sizepc = write(fdEnvioC,&ccm,sizeof(ccm));
                    close(fdEnvioC);

                    sprintf(MEDICO_FIFO_FINAL,MEDICO_FIFO,idM);
                    int fdEnvioM = open(MEDICO_FIFO_FINAL,O_WRONLY);
                    int sizepm = write(fdEnvioM,&ccm,sizeof(ccm));
                    close(fdEnvioM);


                    
                }
                v=0;
            }
    }while(x!=1);
    kill(getpid(),SIGINT);
}


void classifica(pCliMed c){
        Balc b;
        pipe(b.balc_to_clas);
        pipe(b.clas_to_balc);
        b.fk1 = fork();
        if(b.fk1<0){
            printf("ERRO: Erro no fork!\n");
            return;
        }
        else if(b.fk1 == 0){ //Filho
            close(STDIN_FILENO); //Fecha o stdin
            dup(b.balc_to_clas[0]); //liga o balc_to_clas ao stdin
            close(b.balc_to_clas[0]);
            close(b.balc_to_clas[1]);
            
            close(STDOUT_FILENO); //Fecha o stdout
            dup(b.clas_to_balc[1]); //liga o clas_to_balc ao stdout
            close(b.clas_to_balc[1]);
            close(b.clas_to_balc[0]);
            
            execl("classificador","classificador",NULL);
        }
        else if(b.fk1 > 0){//Pai
        close(b.balc_to_clas[0]);
        close(b.clas_to_balc[1]);

        write(b.balc_to_clas[1],c->sintoma,sizeof(c->sintoma));
        read(b.clas_to_balc[0],c->resposta,49);
        close(b.clas_to_balc[0]);
        if(c->resposta[strlen(c->resposta)-1] == '\n')
            c->resposta[strlen(c->resposta)-1] = '\0';
        }
}

void terminaBalcao(int s, siginfo_t *i, void *v){
    unlink(SERVER_FIFO);
    printf("\nO balcao encerrou!\n");
    exit(1);
}

void closeserver(DTh *dados){
    int c,i;
    libertaClientes(dados->listaClientes);
    libertaMedicos(dados->listaMedicos);
    kill(getpid(),SIGINT);
}


void *teclado(void *dados){
	DTh *pdados = (DTh*) dados;
	char varSair[100];
	do{
        fflush(stdin);
		printf("\nIntroduza o comando (para sair introduza 'encerra'): ");
		fgets(varSair,49,stdin);
        
		if(strcmp(varSair,"encerra\n") == 0){
			pdados->stop=0;
		}else{
			comandos(varSair,pdados);
		}
	}while(pdados->stop == 1);
    
	pthread_mutex_destroy(pdados->m);
    closeserver(pdados);
	pthread_exit(NULL);
}

void comandos(char *varSair,DTh *pdados){
    char c[50];
    int x,v=0;
    if(strcmp(varSair,"utentes\n") == 0){
        mostraInfoCliente(pdados->listaClientes);
        return;
    }
    if(strcmp(varSair,"especialistas\n") == 0){
        mostraInfoMedico(pdados->listaMedicos);
        return;
    }
    v = divideComando(varSair,c,&x);
    if(v==0){
        if(strcmp(c,"delut") == 0){
            if(x>0){
                if(pdados->listaClientes == NULL){
                    printf("\nAviso: Não existem Clientes, por isso não é possivel eliminar nenhum cliente\n");
                    return;
                }
                union sigval val;
                sigqueue(x,SIGINT,val);
                pdados->listaClientes = eliminaCliente(pdados->listaClientes,x);
                return;
            }
        }
        if(strcmp(c,"delesp") == 0){
            if(x>0){
                if(pdados->listaMedicos == NULL){
                    printf("\nAviso: Não existem Medicos, por isso não é possivel eliminar nenhum medico\n");
                    return;
                }
                union sigval val;
                sigqueue(x,SIGINT,val);
                pdados->listaMedicos = eliminaMedico(pdados->listaMedicos,x);
                return;
            }
        }
        if(strcmp(c,"freq") == 0){
            if(x>0){
                pdados->freq = x;
                return;
            }
        }
    }
    printf("\nComando invalido!\n");
    return;

}

int divideComando(char *varSair,char *c,int *x){
    int i,j=0,y=0;
    char aux[10];
    for(i=0;varSair[i]== ' ';i++);

    for(;i<strlen(varSair);i++){
        if(varSair[i] != ' '){
            c[j] = varSair[i];
            j++;
        }else{
            break;
        }
    }

    for(;varSair[i]== ' ';i++);

    if(i==strlen(varSair)-1)
        return -1;

    for(;i<strlen(varSair);i++,y++){
        aux[y] = varSair[i];
    }


    *x = atoi(aux);
    if(*x == 0){
        printf("Deve introduzir um numero!\n");
        *x=-1;
        return -1;
    }
    return 0;

}

void *temporizador(void *dados){
	pDTh pdados = (DTh*) dados;
	do{
		pthread_mutex_lock(pdados->m);

        mostraInfoCliente(pdados->listaClientes);
        mostraInfoMedico(pdados->listaMedicos);

		pthread_mutex_unlock(pdados->m);
		sleep(pdados->freq);
	}while(pdados->stop);
}

pICli adicionaUtente(pICli listaUtentes, pCliMed c){
    pICli novo, aux;
    novo = malloc(sizeof(CliMed));
    if(novo == NULL){
        //Erro ao adicionar um novo cliente!
        printf("Erro ao adicionar um utente!\n");
        return listaUtentes;
    }
    preencheUtente(novo,c);
    if(listaUtentes==NULL){
        listaUtentes = novo;
    }else{
        aux=listaUtentes;
        while(aux->prox != NULL){
            aux = aux->prox;
        }
        aux->prox = novo;
    }
    return listaUtentes;
}

void preencheUtente(pICli novo,pCliMed c){
    strcpy(novo->sintoma,c->sintoma);
    strcpy(novo->especialidade,c->resposta);
    novo->pid = c->pid;
    strcpy(novo->nome,c->nome);
    novo->prox = NULL;
    novo->espera = c->espera;
}

void mostraInfoCliente(pICli listaUtentes){
    if(listaUtentes == NULL){
            printf("\nAinda não existe Clientes!\n");
            return;
    }
    int espera;
    printf("\nUtentes: \n");
    while(listaUtentes!=NULL){
        espera = listaUtentes->espera;
        if(espera == 0){
            printf("\nO Utente %s está numa consulta!\n",listaUtentes->nome);
            listaUtentes = listaUtentes->prox;
        }
        if(espera==1){
            printf("\n\tNome: ");
            puts(listaUtentes->nome);
            printf("\t\tSintoma: ");
            puts(listaUtentes->sintoma);
            printf("\t\tEspecialidade: ");
            puts(listaUtentes->especialidade);
            printf("\n\t\tPID: ");
            printf("%d\n",listaUtentes->pid);
            fflush(stdout);
            listaUtentes = listaUtentes->prox;
        }
    }
}

void libertaClientes(pICli p){
    pICli aux;
    while(p != NULL)
    {   
        union sigval val;
        sigqueue(p->pid,SIGINT,val);
        aux = p;
        p = p->prox;
        free(aux);
    }
}

pICli eliminaCliente(pICli listaUtentes,int pid){
    pICli atual, anterior = NULL;

    atual = listaUtentes;

    while(atual != NULL && atual->pid != pid){
        anterior = atual;
        atual = atual->prox;
    }
    if(atual == NULL){
        return listaUtentes;
    }
    if(anterior == NULL){
        listaUtentes = atual->prox;
    }else{
        anterior->prox = atual->prox;
    }
    free(atual);
    return listaUtentes;
}

int ProcuraConsulta(pICli listaUtentes,pIMed listaMedicos,int *idU,int *idM){
    char auxU[50],aux[50];
    int i;

    while(listaUtentes!=NULL){
        if(listaUtentes->espera!=0){
            while(listaMedicos!=NULL){
                strcpy(aux,listaUtentes->especialidade);
                
                for(i=0;i<strlen(aux)-2;i++){
                    auxU[i]=aux[i];
                }
                auxU[i] = '\0';
        
                
                if(strcmp(listaMedicos->especialidade,auxU) == 0){
                    *idU = listaUtentes->pid;
                    *idM = listaMedicos->pid;
                    listaMedicos->espera=0;
                    listaUtentes->espera=0;
                    printf("\nUtente %s vai ter uma consulta com o Médico %s\n",listaUtentes->nome,listaMedicos->nome);
                    fflush(stdout);
                    return 1;
                }
                listaMedicos = listaMedicos->prox;
            }
        }
        listaUtentes = listaUtentes->prox;
    }
    return -1;
}



pIMed adicionaMedico(pIMed listaMedicos, pCliMed m){
    pIMed novo, aux;
    novo = malloc(sizeof(CliMed));
    if(novo == NULL){
        //Erro ao adicionar um novo cliente!
        printf("Erro ao adicionar um medico!\n");
        return listaMedicos;
    }

    preencheMedico(novo,m);

    if(listaMedicos==NULL){
        listaMedicos = novo;
    }else{
        aux=listaMedicos;
        while(aux->prox != NULL){
            aux = aux->prox;
        }
        aux->prox = novo;
    }
    
    return listaMedicos;
}

void preencheMedico(pIMed novo,pCliMed m){
    strcpy(novo->nome,m->nome);
    strcpy(novo->especialidade,m->especialidade);
    novo->pid=m->pid;
    novo->prox = NULL;
    novo->espera = m->espera;
}

void mostraInfoMedico(pIMed listaMedicos){
    if(listaMedicos==NULL){
        printf("\nAinda não existe Medicos!\n");
        return;
    }
    printf("\nMedicos \n");
    while(listaMedicos!=NULL){
        printf("\n%d\n",listaMedicos->espera);
        if(listaMedicos->espera == 0){
            printf("\nO Medico %s está numa consulta!\n",listaMedicos->nome);
            listaMedicos = listaMedicos->prox;
        }
        if(listaMedicos!=NULL){
            printf("\n\tNome: ");
            puts(listaMedicos->nome);
            printf("\t\tEspecialidade: ");
            puts(listaMedicos->especialidade);
            printf("\n\t\tPID: ");
            printf("%d\n",listaMedicos->pid);
            fflush(stdout);
            listaMedicos = listaMedicos->prox;
        }
    }
}

void libertaMedicos(pIMed p){
    pIMed aux;
    while(p != NULL){   
        union sigval val;
        sigqueue(p->pid,SIGINT,val);
        aux = p;
        p = p->prox;
        free(aux);
    }
}

pIMed eliminaMedico(pIMed listaMedicos,int pid){
    pIMed atual, anterior = NULL;

    atual = listaMedicos;

    while(atual != NULL && atual->pid != pid){
        anterior = atual;
        atual = atual->prox;
    }
    if(atual == NULL){
        return listaMedicos;
    }
    if(anterior == NULL){
        listaMedicos = atual->prox;
    }else{
        anterior->prox = atual->prox;
    }
    free(atual);
    return listaMedicos;
}