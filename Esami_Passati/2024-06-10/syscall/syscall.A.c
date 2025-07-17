#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>

int pfd[2], pid[2], N=0, fdin;


void p0_alrm_handler(int signo);
void processoP1(char* filename);
void p2_handler(int signo);
void processoP2(char* fileout);
void wait_child();

int main(int argc,char**argv){
	int T;
    //Controllo parametri
	if(argc!=4){
        fprintf(stderr,"Numero di argomenti errato.\nUsage:\n\t%s T Car Fin\n", argv[0]);
        exit(EXIT_FAILURE);
	}	

    //T numero positivo
    if( (T=atoi(argv[1]))<0  ){
        fprintf(stderr,"Intero T deve essere positivo\n");
        exit(EXIT_FAILURE);
    }

    //C unico carattere
    if(strlen(argv[2]!=1)){
        fprintf(stderr,"Il secondo parametro deve essere un singolo carattere\n");
        exit(EXIT_FAILURE);
    }

    //Fin path Assoluto 
    if (argv[3][0]!='/'){ 
        //se il parametro passato non inizia per '/' allora non è un nome assoluto
        perror("il terzo parametro deve essere il nome assoluto di un file.\n");
        exit(EXIT_FAILURE);
    }



    //Esecuzione
    if(pipe(pfd)<0){
        fprintf(stderr,"Errore nella creazione della pipe\n");
        exit(EXIT_FAILURE); 
    }
        
    pid[0]=fork();
    if(pid[0]==0){//P1
        printf("P1, pid = %d\n",getpid());
        close(pfd[1]);//chiudo lato di scrittura

        processoP1(argv[2]);
        exit(EXIT_SUCCESS); 
            
    }else if(pid[0]>0){//P0
        pid[1]=fork();
        if(pid[1]==0){//P2
            signal(SIGUSR1,p2_handler);
            close(pfd[0]);//chiudo lato di lettura
            printf("P2, pid = %d\n",getpid());
            processoP2(argv[3]);
            exit(EXIT_SUCCESS); 
        }else if(pid[1]>0){//P0
            signal(SIGALRM,p0_alrm_handler);
            alarm(T);
            close(pfd[0]);//chiudo lato di lettura
            close(pfd[1]);//chiudo lato di scrittura
            while(1){
                printf("P0: i miei figli sono %d e %d\n",pid[0],pid[1]);
                sleep(1);
            }

        }else{
            perror("P0: errore nella fork per P2\n");
            exit(EXIT_FAILURE); 
        }

    }else{
        perror("P0: errore nella fork per P1\n");
        exit(EXIT_FAILURE); 
    }

}


void p0_alrm_handler(int signo){
	int i;
	if (signo == SIGALRM){
		printf("P0: mando segnale a P2\n");
		kill(pid[1],SIGUSR1);
		// aspetto i figli e controllo il loro stato di terminazione in modo da 
    	//riscontrare eventuali loro malfunzionamenti
    	for(i=0;i<2;i++)
        	wait_child(); 
        printf("P0: termino.\n");
        exit(EXIT_SUCCESS);
	}else {
		perror("P0: segnale inatteso.\n");
		exit(EXIT_FAILURE); 
	}
}

void processoP2(char* filename){
	int seek_val,nread,written;
	char temp;
    
	fdin=open(filename,O_RDONLY);
    if(fdin<0){
        fprintf(stderr,"P2: Errore apertura file %s. Termino.\n",filename);
        exit(1); 
    }

    while(1){
	    while((nread=read(fdin,&temp,sizeof(char)))>0){
	    	written=write(pfd[1],&temp,sizeof(char));
	        if(written<0){
	            perror("P2: Errore scrittura su pipe. Termino\n");
	            exit(EXIT_FAILURE); 
	        }
	    }
        N++;
        /*Sposto il cursore a inizio file*/
        seek_val = lseek(fdin, 0, SEEK_SET);
        if ( seek_val < 0 ){
            perror("P2: errore nella lseek\n");
            exit(EXIT_FAILURE);
        }
    }
}

void p2_handler(int signo){
	if (signo == SIGUSR1){
		printf("P2: ricevuto segnale SIGUSR1 da P0. In totale ho inviato il file N=%d volte. Ora termino.\n",N);
		close(pfd[1]);
		close(fdin);
		exit(EXIT_SUCCESS);
	}else {
		perror("P2: segnale inatteso.\n");
		exit(EXIT_FAILURE);
	}

}

void processoP1(char* C){
    close(0);
    dup(pfd[0]);
    
    execlp("grep", "grep", C, (char*)0);
    perror("P1: Errore nella exec. Termino.\n");
    exit(EXIT_FAILURE);
}

void wait_child() {
    int pid_terminated,status;
    pid_terminated=wait(&status);
    if(WIFEXITED(status))
        printf("P0: terminazione volontaria del figlio %d con stato %d\n",
                pid_terminated, WEXITSTATUS(status));
    else if(WIFSIGNALED(status))
        printf("P0: terminazione involontaria del figlio %d a causa del segnale %d\n",
                pid_terminated,WTERMSIG(status));
}

