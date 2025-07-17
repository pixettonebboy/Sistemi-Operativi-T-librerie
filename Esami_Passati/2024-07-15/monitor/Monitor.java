import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private final int MAX = 1000; //capacità in ml
	private final int PICCOLO = 0;
	private final int GRANDE = 1;
	private final int capG=50; // capacità bicchiere grande
	private final int capP=33; // capacità bicchiere piccolo
	
	private Lock lock = new ReentrantLock();
	private Condition codaBirra[] = new Condition[2];
	private Condition codaAddetto;
	private int sospBirra[] = new int[2];
	private int attesaAddetto;
	private boolean occupato;
	private int inFusto; //quantità nel fusto (in ml)
	private boolean inSostituzione;
	
	public Monitor() {
		for(int i = 0; i < 2; i++) {
				codaBirra[i] = lock.newCondition();
				sospBirra[i] = 0;
			}
		occupato = false;
		inFusto= MAX;
		codaAddetto = lock.newCondition();
		attesaAddetto = 0;
		inSostituzione = false;
		
	}
	
	public void InizioPrelievo(int id, int formato) throws InterruptedException {
		lock.lock();
		try {
			while(  (formato==PICCOLO && inFusto<capP)||
					(formato==GRANDE && inFusto<capG) ||
					 occupato==true || 
					 inSostituzione==true || 
					(formato==GRANDE && sospBirra[PICCOLO]>0)  ) {
				sospBirra[formato]++;
				codaBirra[formato].await();
				sospBirra[formato]--;
			}
			System.out.println("cliente "+id+" inizia prelievo di BIRRA con formato "+ formato);
			occupato= true;
			int quantSpillata = (formato == 0 ? capP : capG);
			inFusto -= quantSpillata;
			
		} finally { lock.unlock(); }
	}
	
	public void FinePrelievo(int id, int formato) {
		lock.lock();
		try {
			System.out.println("cliente "+id+" finisce prelievo di birra con formato "+ formato);
			System.out.println("cliente "+id+": birra rimasta nel distributore: "+inFusto);
			occupato = false;
			//risvegli:
			
			if(attesaAddetto > 0 && inFusto<capP) 
				codaAddetto.signal();
			else if(sospBirra[PICCOLO] > 0 ) 
				codaBirra[PICCOLO].signal();
			else if(sospBirra[GRANDE] > 0 ) 
				codaBirra[GRANDE].signal();
			
		} finally { lock.unlock(); }
			
	}
	
	public void InizioSostituzione() throws InterruptedException {
		
		lock.lock();
		
		try {
			while(occupato|| inFusto>=capP) {
				attesaAddetto++;
				codaAddetto.await();
				attesaAddetto--;
			}
			
			System.out.println("L'addetto sta per sostituire il fusto di birra.. ");
				
			inSostituzione = true;
			
			
		} finally { lock.unlock(); }
	}
	
	public void FineSostituzione() {
		lock.lock();
		try {
			inFusto=MAX;
			System.out.println("...terminata sostituzione del fusto di birra. ");
			inSostituzione = false;
			//risveglio clienti sospesi

			if(sospBirra[PICCOLO] > 0) 
				codaBirra[PICCOLO].signal();
			else if(sospBirra[GRANDE] > 0 ) 
				codaBirra[GRANDE].signal();
			
		} finally { lock.unlock(); }
	}
	
	
}