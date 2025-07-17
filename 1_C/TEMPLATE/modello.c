

// COMANDI UTILI:
//
// gcc <nomeFile.c> -o <nomeOut>
// chmod +x <nomeFile>
// ./<nomeFile>


#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#include <ctype.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <signal.h>
#include <fcntl.h>
#include <unistd.h>


// Definizione funzioni libreria
int ControlloArgomenti(int argc, char* argv[]);
void AttendiTermineFiglio();
// e funzioni specifiche
void Esecuzione_Padre();
void Esecuzione_P1();
void Esecuzione_P2();

// Numero argomenti da passare + 1 extra per nome file eseguito
#define NUM_ARGOMENTI 2
// Numero di processi figli che andranno gestiti
#define NUM_FIGLI 2
// salva la lista dei PID dei processi creati
int PIDfigli[NUM_FIGLI];
// metodi figli
void (*metodiFigli[NUM_FIGLI])() = { Esecuzione_P1, Esecuzione_P2 };



//  MAIN    = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
// ------------------------------------------------------------------------------

int main(int argc, char *argv[]){

    // Controllo Argomenti
    if (ControlloArgomenti(argc, argv) == 1)
        return 1;

    
    // Creazione processi figli
    for (int i = 0; i < NUM_FIGLI; i++){
        // Fork processo
        PIDfigli[i] = fork();
        
        // errore
        if (PIDfigli[i] < 0){
            perror("\n\nErrore di creazione processo figlio!");
            exit(1);
        // chiamata metodo del processo figlio
        } else if (PIDfigli[i] == 0){
            metodiFigli[i]();
            exit(0);
        }
    }

    // Esecuzione codice padre
    
    Esecuzione_Padre();

    // Attesa terminazione di tutti i figli
    for (int i = 0; i < NUM_FIGLI; i++)
        AttendiTermineFiglio();
    
    return 0;
}




//  ESECUZIONI PROCESSI   = = = = = = = = = = = = = = = = = = = = = = = = = = = =
// ------------------------------------------------------------------------------

void Esecuzione_Padre(){
    printf("Ciao sono il padre!\n");
}

void Esecuzione_P1(){
    printf("Ciao sono P1!\n");
}

void Esecuzione_P2(){
    printf("Ciao sono P2!\n");
}




//  FUNZIONI di LIBRERIA    = = = = = = = = = = = = = = = = = = = = = = = = = = =
// ------------------------------------------------------------------------------

// Ritorna 1 se gli argomenti sono stati passati in modo errato
int ControlloArgomenti(int argc, char* argv[]){
    // Check Numero Argomenti
    if(argc != NUM_ARGOMENTI){
        printf("\n ARGCHECK ] Numero di argomenti errati.\n");
        return 1;
    }

    // Controllo conversione numero
    //int conv = atoi(argv[1]);
    //if(conv <= 0){
    //    printf(" ARGCHECK ] Argomento numerico non convertibile.\n");
    //    return 1;
    //}

    // Controllo path assoluto
    //if (argv[1][0] != '/'){
    //    printf(" ARGCHECK ] Argomento percorso non fornito come assoluto.\n");
    //    return 1;
    //}

    //esistenza file    
    //if (access("nomefile.txt", F_OK) == 0) {  //F_OK verifica solo l’esistenza del file. Puoi anche usare R_OK, W_OK, X_OK per verificare permessi di lettura, scrittura o esecuzione.
    //    printf("Il file esiste.\n");
    //} else {
    //    printf("Il file NON esiste.\n");
    //}

    return 0;
}

// Chiamato dal padre, attende la terminazione di ogni figlio
// segnalando in caso contrario il malfunzionamento accaduto.
void AttendiTermineFiglio() {
    int pid_terminated, status;
    pid_terminated = wait(&status);
    if (pid_terminated < 0)    {
        //fprintf(stderr, "P0: errore in wait. \n");
        exit(EXIT_FAILURE);
    }
    if (WIFEXITED(status)) {
        //printf("P0: terminazione volontaria del figlio %d con stato %d\n", pid_terminated, WEXITSTATUS(status));
        if (WEXITSTATUS(status) == EXIT_FAILURE) {
            fprintf(stderr, "P0: errore nella terminazione del figlio pid_terminated\n");
            exit(EXIT_FAILURE);
        }
    }
    else if (WIFSIGNALED(status)) {
        //fprintf(stderr, "P0: terminazione involontaria del figlio %d a causa del segnale %d\n", pid_terminated,WTERMSIG(status));
        exit(EXIT_FAILURE);
    }
}