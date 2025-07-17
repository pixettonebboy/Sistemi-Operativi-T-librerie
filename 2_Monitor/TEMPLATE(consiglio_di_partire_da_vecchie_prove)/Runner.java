package modelloJava;

import java.util.Random;

public class Runner implements Runnable {//implementa Runnable, quindi può essere passato a un oggetto Thread.

	private Integer id;	//identificatore del thread.
	private Monitor mon;//riferimento al monitor condiviso, passato dal main
	
	// costruttore: Inizializza l'ID del thread e salva il riferimento al Monitor
	public Runner(Monitor mon, int id){
		this.id = id; 
		this.mon = mon;
	}


	// Utilizzato per debug, verrà stampato nell'output dal Main - - - - - - -
	private String statoRunner; //Memorizza una stringa che descrive lo stato attuale del runner.
	
	//Imposta lo stato e stampa i concorrenti.
	public void SetStato(String stato){
		SetStato(stato, true);
	}

	//Se stampa == true, stampa la lista dei thread correnti dal main.
	public void SetStato(String stato, boolean stampa){
		this.statoRunner = stato;
		if (stampa)
			Main.StampaConcorrenti();
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	

	@Override
	public void run() {
		try {
			
			// Attesa iniziale random tra 200 e 1199 ms.
			Random rand = new Random();
			Thread.sleep(200 + rand.nextInt(1000));
		
			// richiesta ingresso
			//mon.EntraInSalaAttesa(this);
			
			// tempo random di utilizzo sportello
			//Thread.sleep(200 + rand.nextInt(1000));
			
			
		} catch (InterruptedException e) {   e.printStackTrace();   }//In caso di interruzione del thread, stampa l'eccezione.
	}
	
	
	//Ritorna una stringa leggibile per identificare il runner con il suo stato.
	//Esempio: XXX03 || Stato: CIAO
	@Override
	public String toString() {
		return "XXX" + String.format("%02d", id) + " || Stato: " + statoRunner;
	}
	
}
