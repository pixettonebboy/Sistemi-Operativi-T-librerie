package modelloJava;

import java.util.ArrayList; //per gestire una lista dinamica di thread
import java.util.Random;

public class Main {

	//Lista statica per conservare tutti i thread (concorrenti) creati.
	static ArrayList<Thread> concorrenti = new ArrayList<Thread>();
	
	public static void main(String[] args) {

		System.out.println("Esame Achille Pisani");
		
		Monitor mon = new Monitor();
		Random rand = new Random();
		
		
		// CREAZIONE PROCESSI = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 
		
		//Genera un numero casuale di thread tra 5 e 24 (inclusi).
		int numThreads = 5 + rand.nextInt(20);
		
		for(int i= 0; i < numThreads; i++) {
			Runner newRunner = new Runner(mon, i);
			//setti stato iniziale processo, (stato) oppure (stato,boolean) true->stampa, false->no
			newRunner.SetStato("CIAO");
			//Lo incapsula in un oggetto Thread e lo aggiunge alla lista concorrenti.
			concorrenti.add( new Thread(newRunner) );
		}
			
		
		// START DEI PROCESSI = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
		
		//fa partire tutti i thread chiamando start
		for(int i = 0; i < concorrenti.size(); i++)
			concorrenti.get(i).start();
		
		
		// ATTESA TERMINE THREADS = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 
		
		for(int i = 0; i < concorrenti.size(); i++)
			try {
				//Aspetta che ogni thread finisca (join).
				concorrenti.get(i).join();
			} catch (InterruptedException e){  e.printStackTrace();   }//Se un thread viene interrotto, stampa la traccia dell'errore.
		
		
		// STAMPA FINALE = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 
		
		//Stampa lo stato finale del monitor
		System.out.println(mon.toString());
		
	}
	
	//Metodo ausiliario per stampare tutti i thread contenuti nella lista concorrenti.
	public static void StampaConcorrenti() {
		// Stampa della lista di tutti i Thread
		System.out.println("== TABELLA THREADS ====================");
		for(int i = 0; i < concorrenti.size(); i++)
			System.out.println("  " + concorrenti.get(i));
		System.out.println("=======================================");
	}
	
	
}
