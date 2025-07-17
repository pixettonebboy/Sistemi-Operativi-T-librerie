#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>

int pfd[2], pid[2], N, M;


void p2_handler(int signo);
void wait_child();

int main(int argc,char**argv){
    //Controllo parametri
	if(argc!=4){
        fprintf(stderr,"Numero di argomenti errato.\nUsage:\n\t%s T Car Fin\n", argv[0]);
        exit(EXIT_FAILURE);
	}	

    //M numero positivo
    if( ( M =atoi(argv[2]))<0  ){
        fprintf(stderr,"Intero M deve essere positivo\n");
        exit(EXIT_FAILURE);
    }

    //Ftext e Fbin path assoluti
    /*if (argv[1][0]!='/' || argv[3][0]!='/'){ 
        //se il parametro passato non inizia per '/' allora non è un nome assoluto
        perror("Il primo e il terzo parametro devono essere nomi assoluti di file.\n");
        exit(EXIT_FAILURE);
    }*/



    //Esecuzione
    if(pipe(pfd)<0){
        fprintf(stderr,"Errore nella creazione della pipe\n");
        exit(EXIT_FAILURE); 
    }
        
    pid[0]=fork();
    if(pid[0]==0){//P1
        printf("P1, pid = %d\n",getpid());
        close(pfd[0]);//chiudo lato di lettura

        close(1);
        dup(pfd[1]);
        execlp("head", "head", "-n1", argv[1], (char*)0);
        perror("P1: Errore nella exec. Termino.\n");
        exit(EXIT_FAILURE);
            
    }else if(pid[0]>0){
        pid[1]=fork();
        if(pid[1]==0){//P2
            signal(SIGUSR1,p2_handler);
            close(pfd[0]);//chiudo lato di lettura
            close(pfd[1]);//chiudo lato di scrittura
            printf("P2, pid = %d\n",getpid());
            pause();
            exit(EXIT_SUCCESS); 
        }else if(pid[1]>0){//P0
            close(pfd[1]);//chiudo lato di scrittura
            char buff[20];
            int i=0,nread;
            while((nread=read(pfd[0],&buff[i],sizeof(char)))>0)
                i++;
            buff[i]='\0';
            int N=atoi(buff);

            if (N>M){
                int fdbin=open(argv[3],O_RDWR|O_CREAT|O_TRUNC,0640);
                if(fdbin<0){
                    fprintf(stderr,"P0: Errore in apertura/creazione del file %s. Termino.\n",argv[3]);
                    exit(1); 
                }
                int written=write(fdbin,&N,sizeof(int));
                if(written<0){
                    perror("P0: Errore scrittura su file binario. Termino.\n");
                    exit(EXIT_FAILURE); 
                }
                
                //solo per controllo, leggo quanto scritto su file binario e lo stampo a video:
                lseek(fdbin,0, SEEK_SET);
                int check;
                nread=read(fdbin,&check,sizeof(int));
                printf("P0, stampa di controllo. N = %d\n",N);
            
                kill(pid[1],SIGUSR1);
            }else{
                kill(pid[1],SIGKILL);
            }
            wait_child();
            wait_child();

        }else{
            perror("P0: errore nella fork per P2\n");
            exit(EXIT_FAILURE); 
        }

    }else{
        perror("P0: errore nella fork per P1\n");
        exit(EXIT_FAILURE); 
    }

}


void p2_handler(int signo){
	if (signo == SIGUSR1){
		printf("P2: ricevuto segnale SIGUSR1 da P0. Il primo numero è >= %d. Ora termino.\n", M);
		exit(EXIT_SUCCESS);
	}else {
		perror("P2: segnale inatteso.\n");
		exit(EXIT_FAILURE);
	}

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

