#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

void wait_child();


int main(int argc, char *argv[]) {
	int pip[2], p1,p2, fd;
	char c;


	// controllo parametri
	if (argc!=5) {
		perror("Numero di parametri errato\n");
		exit(-1);
	}
	if (argv[1][0]!='/'){
		printf("Il primo argomento deve essere un nome assoluto di file\n");
		exit(-2);
	}
	if (strlen(argv[2])!=1 || strlen(argv[3])!=1 ){
	    printf("Il secondo e terzo argomento devono essere caratteri\n");
        exit(-3);
    }


    //esecuzione
	if(pipe(pip) < 0 ){
        perror("Errore pipe\n");
        exit(1);
    }

	p1=fork();
	if (p1==0) {
		//CODICE P1
		close(pip[1]);
		close(0);
		dup(pip[0]);

		execlp("grep", "grep", argv[4], (char*) 0);
		perror("P1: Errore nella execlp\n");
		exit(EXIT_FAILURE);

	}else if(p1>0){
		p2=fork();
		if (p2==0) {
			//CODICE P2
			close(pip[0]);
			if ((getpid()%2)!=0){
				printf("P2: Pid dispari!\n");
			}

			if((fd = open(argv[1], O_RDONLY)) < 0){
		        perror("Errore open\n");
		        exit(-1);
		    }
		    while(read(fd, &c, sizeof(char))>0){
		    	if (c==argv[2][0] && (getpid()%2)==0) {
		    		write(pip[1], argv[3], sizeof(char) );
		    	}else
		    		write(pip[1], &c, sizeof(char) );
		    }
			exit(EXIT_SUCCESS);
			
		}else if(p2>0){
			//CODICE P0
			close(pip[0]);
			close(pip[1]);
			

    		wait_child();
    		wait_child();
			
		}else{
			printf("P0: Impossibile creare processo figlio P2\n");
			exit(3);
		}
		
	}else{
		printf("P0: Impossibile creare processo figlio P1\n");
		exit(3);
	} 

}



void wait_child() {
	int pid_terminated,status;
	pid_terminated=wait(&status);
		if(WIFEXITED(status))
			printf("\nPADRE: terminazione volontaria del figlio %d con stato %d\n",pid_terminated,WEXITSTATUS(status));
		else if(WIFSIGNALED(status))
			printf("\nPADRE: terminazione involontaria del figlio %d a causa del segnale %d\n",pid_terminated,WTERMSIG(status));
}





