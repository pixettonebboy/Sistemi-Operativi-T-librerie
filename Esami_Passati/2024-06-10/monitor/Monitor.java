import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private final int MONO=0;
	private final int FULL=1;
	private int N; //num totale aree pulizia
	
	private boolean []libera;//stato occupazione delle aree di pulizia
	private int APdisp;
	private boolean AddettoIN; //true se l'addetto è presente
	private boolean Tocc; //true se il tunnel è occupato da un'automobile
	
	
	private Lock lock;
	
	private Condition []codaT=new Condition[2]; //attesa per il tunnel [MONO, FULL]
	private Condition []codaAP=new Condition[2]; //attesa per le Ap [MONO, FULL]
	private Condition codaA; //attesa addetto
	private int []sospT=new int[2]; //num thread in attesa per il tunnel [MONO, FULL]
	private int []sospAP=new int[2]; //attesa per le Ap [MONO, FULL]
	private int sospA=0; //attesa addetto
	
	
	
	private Random R;
	
	
	public Monitor(int N, Random r) {
		libera=new boolean[N];
		this.N = N;
		this.R=r;
		APdisp=N;
		AddettoIN=false;
		Tocc=false;
		for (int i=0; i<N; i++)
			libera[i]=true;
		lock = new ReentrantLock();
		for (int i=0; i<2; i++)
		{	codaT[i] = lock.newCondition();
			codaAP[i] = lock.newCondition();
			sospT[i]=0;
			sospAP[i]=0;
		}
		codaA = lock.newCondition();
		sospA=0;
}
	
	
	public void EntraAddetto() {
		lock.lock();
		try {
			AddettoIN=true;
			System.out.println("L'Addetto ha finito la pausa e presidia il tunnel");
			if (sospT[FULL]>0)
				codaT[FULL].signal();
			else if (sospT[MONO]>0)
				codaT[MONO].signal();
		}finally {
			lock.unlock();
		}
	}



	public void EsceAddetto() {
		lock.lock();
		try {
			
			while(Tocc==true) {
				sospA++;
				codaA.await();
				sospA--;
			}
			AddettoIN=false;
			System.out.println("L'Addetto inizia a riposarsi");
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	
	public void EntraT(int tipo) {
		lock.lock();
		try {
			while(	Tocc || !AddettoIN || (tipo==MONO && sospT[FULL]>0 )) {
					sospT[tipo]++;
					codaT[tipo].await();
					sospT[tipo]--;
				}

			Tocc=true;
			if (tipo==FULL)
				System.out.println("auto " + Thread.currentThread().getId() + " FULL è entrata nel tunnel");
			else // Mono
				System.out.println("auto " + Thread.currentThread().getId() + " MONO è entrata nel tunnel");
			Tocc=true;
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
	}

	public void EsceT(int t) {
		lock.lock();
		try {
			Tocc=false;
			if (sospA>0)
				codaA.signal();
			else if (sospT[FULL]>0)
			codaT[FULL].signal();
			else if (sospT[MONO]>0)
				codaT[MONO].signal();
			if (t==FULL)
				System.out.println("auto " + Thread.currentThread().getId() + " FULL è uscita dal tunnel");
			else // bambino
				System.out.println("auto " + Thread.currentThread().getId() + " MONO è uscita dal tunnel");

		}finally {
			lock.unlock();
		}
		
	}
	
	public int acqAP(int tipo) {
		lock.lock();
		int ris=-1;
		
		try {
			while(	APdisp==0 || (tipo==MONO && sospT[FULL]>0 )) {
					sospAP[tipo]++;
					codaAP[tipo].await();
					sospAP[tipo]--;
				}
			APdisp--;
			//ricerca dell'area da assegnare al cliente:
			for (int i=0; i<N; i++)
				if (libera[i])
				{	libera[i]=false;
					ris=i;
					break;
				}
			if (tipo==FULL)
				System.out.println("auto " + Thread.currentThread().getId() + " FULL ha ottenuto l'area n. " + ris + " e inizia a pulire..");
			else // Mono
				System.out.println("auto " + Thread.currentThread().getId() + " MONO ha ottenuto l'area n. " + ris+ " e inizia a pulire..");	
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		
		}
		return ris;
		
	}
	
	public void liberaAP(int i, int tipo){
		lock.lock();
		try {
			APdisp++;
			libera[i]=true;
			if (sospAP[FULL]>0)
				codaAP[FULL].signal();
			else if (sospAP[MONO]>0)
				codaAP[MONO].signal();
			if (tipo==FULL)
				System.out.println("auto " + Thread.currentThread().getId() + " FULL ha liberato l'area n. " + i + " e va via..");
			else 
				System.out.println("auto " + Thread.currentThread().getId() + " MONO ha liberato l'area n. " + i+ " e va via..");	
		}finally {
			lock.unlock();
		}
	}	
}
