package sot26giu24A;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	// costanti:
	private final int IN=0;
	private final int OUT=1;
	//variabili di stato della sala:
	private boolean PresenzaUsc=false;  //Usciere Presente
	private boolean ClosingMeeting=false; // riunione terminata (settato da usciere a fine riunione)
	private int VisIN=0; //visitatori IN SALA
	private int DipIn=0; // dipendenti in SALA (comprende anche l'Usciere)
	private int N; //capacità sala
	//sincronizzazione:
	private Lock lock;
	private Condition []codaU=new Condition[2]; //attesa Usciere [IN, OUT]
	private Condition codaD; //attesa dipendenti
	private Condition codaV; //attesa visitatori
	private int []sospU=new int[2]; //usciere in attesa  [IN, OUT]
	private int sospD=0; //dipendenti in attesa 
	private int sospV=0; //visitatori in attesa 
	
	
	
	public Monitor(int N) {
		this.N = N;
		lock = new ReentrantLock();
		for (int i=0; i<2; i++)
		{	codaU[i] = lock.newCondition();
			sospU[i]=0;	
		}
		codaV = lock.newCondition();
		sospV=0;
		codaD = lock.newCondition();
		sospD=0;
}
	
	
	
	

	public void EntraSalaDip() {
		lock.lock();
		try {
			while(	!PresenzaUsc || ClosingMeeting || DipIn==N ) { //la verifica dell'usciere in coda non è necessaria
					sospD++;
					codaD.await();
					sospD--;
				}

			DipIn++;
			
			System.out.println("Dipendente " + Thread.currentThread().getId() + " è entrato in  sala");
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
		
	}

	public void EsceSalaDip() {
		lock.lock();
		DipIn--;
		if (DipIn==1 && sospU[OUT]>0) 	//se è rimasto solo l'usciere, ed è in attesa
			codaU[OUT].signal();  		//sveglio l'usciere in attesa di uscire
		else if (sospV>0 && ! ClosingMeeting)
			codaV.signal();
		System.out.println("Dipendente " + Thread.currentThread().getId() + "  è uscito dalla sala");

		lock.unlock();
	}

	public void InizioRiunione() {
		lock.lock();
		try {
			while( VisIN>0 ) { // tutti i visitatori devono essere usciti, altrimenti aspetta
					sospU[IN]++;
					codaU[IN].await();
					sospU[IN]--;
				}
			PresenzaUsc=true;
			DipIn++;
			if(sospD>0)
				codaD.signalAll(); //sveglio tutti i dipendenti in attesa
			
			System.out.println("Usciere " + Thread.currentThread().getId() + " è entrato in  sala: Riunione iniziata");
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public void FineRiunione() {
		lock.lock();
		try {
			ClosingMeeting=true; //inizia la chiusura
			System.out.println("Usciere " + Thread.currentThread().getId() + " sta chiudendo...");
			
			while(	DipIn>1 ) {  //attende che l'ultimo dipendente sia uscito
					sospU[OUT]++;
					codaU[OUT].await();
					sospU[OUT]++;
				}

			ClosingMeeting=false;
			PresenzaUsc=false;
			DipIn--;
			if (sospV>0)
				codaV.signalAll(); 
			
			System.out.println("Usciere " + Thread.currentThread().getId() + " è uscito dalla sala: riunione conclusa");
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public void EntraSalaVis() {
		lock.lock();
		try {
			while(	PresenzaUsc ||  VisIN==N || sospU[IN]>0 || sospD>0) {
					sospV++;
					codaV.await();
					sospV--;
				}

			VisIN++;
			
			System.out.println("Visitatore " + Thread.currentThread().getId() + " è entrato in  sala");
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
	}

	public void EsceSalaVis() {
		lock.lock();
		try {
			VisIN--;
			if (VisIN==0 && sospU[IN]>0) // l'ultimo visitatore sblocca l'usciere in entrata
				codaU[IN].signal();
			System.out.println("Visitatore " + Thread.currentThread().getId() + "  è uscito dalla sala");
		
		}finally {
			lock.unlock();
		}
		
	}

}
