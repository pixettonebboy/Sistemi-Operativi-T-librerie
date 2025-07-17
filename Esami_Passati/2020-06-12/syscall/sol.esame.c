#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

// soluzione compito syscall del 26 giungo 2019

int p1,p2,N;


void alrm_handler(int sig){
	printf("P2: Timeout exeeded! Killing P0\n");
	kill(getppid(),SIGKILL);
	exit(0);
}

int main(int argc, char *argv[]) {
	int pip[2], i, count, fd, oldCount, diff, nread;
	char filecontent[1025],c;


	// controllo parametri
	if (argc!=4) {
		perror("Numero di parametri errato\n");
		exit(-1);
	}
	if (argv[1][0]!='/'){
		printf("Il primo argomento deve essere un nome assoluto di file\n");
		exit(-2);
	}

	int W= atoi(argv[2]);
	if ( W <= 0  ){
        printf("Il secondo argomento deve essere un intero positivo\n");
        exit(-3);
    }

	int T= atoi(argv[3]);
	if ( T <= 0  ){
        printf("Il terzo argomento deve essere un intero positivo\n");
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
		i=0;
		oldCount=0;
		while( read(pip[0], &count, sizeof(int))> 0){
			diff = count - oldCount;
			if (diff>0)
				printf("Lettura numero %d: trovati %d caratteri aggiuntivi\n", i, diff);
			if (diff<0)
				printf("Lettura numero %d: %d caratteri cancellati\n", i, diff*(-1));
	        i++;
	        oldCount=count;
	    }
	    printf("Lettura terminata!\n");
	    exit(0);

	}else if(p1>0){
		p2=fork();
		if (p2==0) {
			//CODICE P2
			close(pip[1]);
			close(pip[0]);
			signal(SIGALRM, alrm_handler);
			alarm(T);
			while(1){
				sleep(2);
				printf("P2: mio pid %d\n", getpid());
			}
			
		}else if(p2>0){
			//CODICE P0
			close(pip[0]);
			if((fd = open(argv[1], O_RDONLY)) < 0){
		        perror("Errore open\n");
		        exit(-1);
		    }

    		while(1){
    			count=lseek(fd, 0, SEEK_END);
    			printf("P0: caratteri sono %d\n", count);
    			
		    	write(pip[1], &count, sizeof(int));
		    	sleep(W);
		    	
    			
    		}
			
		}else{
			printf("P0: Impossibile creare processo figlio P2\n");
			exit(3);
		}
		
	}else{
		printf("P0: Impossibile creare processo figlio P1\n");
		exit(3);
	} 

}





