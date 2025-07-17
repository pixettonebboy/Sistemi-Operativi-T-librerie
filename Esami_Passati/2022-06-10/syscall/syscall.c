#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

void wait_child();
void handlerP0(int);

int pid1, pid2;

int main(int argc, char **argv){
    int S, I, N, pip[2];
    
    if ( argc != 5 ) {
        printf("Errore troppi pochi parametri -> esame Fin S I N\n");
        exit(EXIT_FAILURE);
    }
    
    if ( argv[1][0] != '/' ) {
        printf("Fin non è un path assoluto\n");
        exit(EXIT_FAILURE);
    }
    
    if ((S = atoi(argv[2])) <= 0) {
        printf("S non è un intero positivo\n");
        exit(EXIT_FAILURE);
    }
    
    if ((I = atoi(argv[3])) <= 0) {
        printf("I non è un intero positivo\n");
        exit(EXIT_FAILURE);
    }
    
    if ((N = atoi(argv[4])) <= 0) {
        printf("N non è un intero positivo\n");
        exit(EXIT_FAILURE);
    }
    
    if (pipe(pip) < 0) {
        perror("pipe error");
        exit(EXIT_FAILURE);
    }
    
    signal(SIGALRM, handlerP0);
    alarm(S);
    
    pid1 = fork();
    if ( pid1 == 0 ) {
        // CODICE P1
        close(pip[1]);
        
        char stringa[250] = "";
        int i = 0;
        char c;
        
        while(read(pip[0],&c,sizeof(char)) > 0) {
            
            if ( c == '\n' ) {
                if ( i > N )  {
                    stringa[i] = '\0';
                    printf("%s\n", stringa);
                }
                i = 0;
            } else {
                stringa[i++] = c;
            }
        }
        
        close(pip[0]);
        exit(EXIT_SUCCESS);
    } else if ( pid1 > 0 ) {
        pid2 = fork();
        if ( pid2 == 0) {
            //CODICE P2
            close(pip[0]);
            close(1);
            dup(pip[1]);
            
            execlp("cut","cut","-d"," ","-f",argv[3],argv[1],(char*) 0);
            perror("Errore nella execlp");
            close(pip[1]);
            exit(EXIT_FAILURE);
        } else if ( pid2 > 0 ) {
            // CODICE P0
            close(pip[0]);
            close(pip[1]);
            wait_child();
            wait_child();
        } else {
            perror("Fork error P2");
            exit(EXIT_FAILURE);
        }
    } else {
        perror("Fork error P1");
        exit(EXIT_FAILURE);
    }
        
} /* fine main */

void handlerP0(int sig) {
    printf("Timeout scaduto\n");
    kill(pid1,SIGKILL);
    kill(pid2,SIGKILL);
    exit(EXIT_SUCCESS);
}

void wait_child() {
    int pid_terminated,status;
    pid_terminated=wait(&status);
    if(WIFEXITED(status))
        printf("PADRE: terminazione volontaria del figlio %d con stato %d\n",
                pid_terminated, WEXITSTATUS(status));
    else if(WIFSIGNALED(status))
        printf("PADRE: terminazione involontaria del figlio %d a causa del segnale %d\n",
                pid_terminated,WTERMSIG(status));
}






