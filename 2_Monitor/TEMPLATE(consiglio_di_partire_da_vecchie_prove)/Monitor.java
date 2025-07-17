package modelloJava;

import java.util.concurrent.locks.*;

public class Monitor {
	
	// Oggetti Monitor
	
		/*Istanzia un ReentrantLock che gestisce l'accesso mutualmente esclusivo alle variabili condivise del monitor.
		Questo lock deve essere usato ogni volta che si accede/modifica lo stato condiviso.*/
	private Lock lock = new ReentrantLock();
	
	// Variabili di stato
		//qui andranno dichiarate le variabili che rappresentano lo stato condiviso tra i thread

	
	public Monitor() {
		// ciao COSTRUTTORE AL MOMENTO VUOTO
	}
	
	
	// GETTER  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
	
	@Override
	public String toString() {
		return "";	//Il metodo toString() è sovrascritto ma non restituisce nulla di utile al momento.
	}
	
	
	
	
	// METODI ENTRY = = = = = = = = = = = = = = = = = = = = = = = = = = = =
	
	/* MODELLO:
	 
	public void Metodo() throws InterruptedException {
		lock.lock();	//Acquisizione del lock
		try{
			
			if (true)
				con_postoAttesaLiberato.await();	//Eventuale attesa su una Condition
			
		} finally{   lock.unlock();   }	//Rilascio del lock nel finally
	}
	*/
	
}
