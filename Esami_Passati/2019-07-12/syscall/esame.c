#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>


void handler(int signo);
void wait_child();

int fd2;
char * s;

int main(int argc, char** argv){
    char c, buf[255];
	int pid0 = getpid();
    int pid1, pid2;
    int n, count;
    int fd;
    int pip[2];
    
    signal(SIGUSR1, handler);
    
	if (argc!=4) {
		perror("Sintassi errata\n");
		exit(-1);
	}
	if (argv[1][0]!='/'){
		printf("Il primo argomento deve essere un nome assoluto di file\n");
		exit(-2);
	}
	
	n = atoi(argv[2]);
    
    s = argv[3];
    
	if ( n <= 0  ){
        printf("Il secondo argomento deve essere un intero positivo\n");
        exit(-3);
    }
     if(pipe(pip) < 0 ){
        perror("Errore pipe\n");
        exit(1);
    }
    
    pid1 = fork();
    
    if(pid1 < 0 ){
        perror("Errore fork pid1\n");
        exit(-4);
    }
    else if(pid1 == 0){
        
        close(pip[0]);
        close(1);
        dup(pip[1]);
        pause();
    }
    
   
    
    if((pid2 = fork()) < 0){
        perror("Errore fork pid2\n");
        exit(-4);
    }
    
    else if(pid2 == 0){
        //p2
    if((fd = open(argv[1], O_RDONLY)) < 0){
        perror("Errore open\n");
        exit(-1);
    }
    if((fd2 = open("temp.txt", O_WRONLY | O_CREAT , 0640))<0){
        perror("Errore open\n");
        exit(-1);
    }
    
    count = 1;
    
    while(read(fd, &c , sizeof(char))> 0){
        
        if(count % n == 0){
            write(fd2, &c, sizeof(char));
        }
        if(c == '\n') count++;
    }
    
    kill(pid1, SIGUSR1);
    exit(0);
    }
    
    close(pip[1]);
    
    while( read(pip[0], &buf, sizeof(char))> 0){
        
        printf("%c", buf);
    }
    close(pip[0]);
    
    wait_child();
    wait_child();
}

void handler (int signo){
    
    if(signo == SIGUSR1){
        printf("P1 ricevuto segnale da P2\n");
        execlp("grep", "grep", s,"temp.txt", (char * )0);
        perror("Errore execlp");
        exit(1);
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
