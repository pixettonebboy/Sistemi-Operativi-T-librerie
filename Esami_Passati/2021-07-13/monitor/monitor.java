package stabilimentoBalneare;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class monitor {
	private int N_OMB;
	private int N_P;
	private final int abbonato = 0;
	private final int occasionale = 1;
	// variabili di stato:
	private int O_liberi; //ombrelloni liberi
	private int PP_liberi; //posti liberi in piscina
	//variabili per la sincronizzazione:
	private Lock lock = new ReentrantLock();
	
	private Condition[] codaFULL_S = new Condition[2]; //code full per l'accesso allo stabilimento
	private Condition[]codaFULL_P = new Condition[2];  //code full per l'accesso alla piscina
	private Condition codaSWO = lock.newCondition(); //coda Swim Only per l'accesso alla piscina
	//contatori thread sospesi:
	private int[] sospFULL_S = new int[2];
	private int[] sospFULL_P = new int[2];
	private int sospSWO=0;
	
	public monitor(int nOmb, int nP) {
		this.N_OMB=nOmb;
		this.N_P=nP;
		O_liberi=N_OMB;
		PP_liberi=N_P;
		for (int i = 0; i < 2; i++) {
			codaFULL_S[i]=lock.newCondition();
			codaFULL_P[i]=lock.newCondition();
			sospFULL_S[i]=0;
			sospFULL_P[i]=0;
		}
		
		
	}

	public void entraSWO(int id) throws InterruptedException { // swim only entra in piscina
		lock.lock();
		while ( PP_liberi==0)
		{	sospSWO++;
			codaSWO.await();
			sospSWO--;
		}
		PP_liberi--;
		// verifica:
		System.out.println("Cliente SWO "+id+" entrato in piscina");		
		stampa_stato();
		
		lock.unlock();
	}
		
	public void esceSWO(int id) throws InterruptedException {  //swim_only esce dalla piscina
		lock.lock();
		PP_liberi++;
		if (sospSWO>0)
			codaSWO.signal();
		else if (sospFULL_P[abbonato]>0 && sospSWO==0)
			codaFULL_P[abbonato].signal();
		else if (sospFULL_P[occasionale]>0 && sospSWO==0 && sospFULL_P[abbonato]==0)
			codaFULL_P[occasionale].signal();
		// verifica:
		System.out.println("Cliente SWO "+id+" uscito dalla piscina");		
		stampa_stato();
		lock.unlock();
		
	}

	public void entraFULL(int tipo, int id) throws InterruptedException{
		lock.lock();
		while ( (O_liberi==0) || (tipo== occasionale) && sospFULL_S[abbonato]>0)
		{	sospFULL_S[tipo]++;
			codaFULL_S[tipo].await();
			sospFULL_S[tipo]--;
		}
		O_liberi--;
		// verifica:
		System.out.println("Cliente FULL "+id+" di tipo "+tipo+"  entrato nello stabilimento.");		
		stampa_stato();
		lock.unlock();
	}

	public void entraP_FULL(int tipo, int id) throws InterruptedException {
		lock.lock();
		while ( (PP_liberi==0) ||(sospSWO>0) || (tipo== occasionale) && sospFULL_P[abbonato]>0)
		{	sospFULL_P[tipo]++;
			codaFULL_P[tipo].await();
			sospFULL_P[tipo]--;
		}
		PP_liberi--;
		// verifica:
		System.out.println("Cliente FULL "+id+" di tipo "+tipo+"  entrato in piscina.");		
		stampa_stato();
		lock.unlock();
		
	}

	public void esceP_FULL(int tipo, int id) throws InterruptedException {
		lock.lock();
		PP_liberi++;
		if (sospSWO>0)
			codaSWO.signal();
		else if (sospFULL_P[abbonato]>0 && sospSWO==0)
			codaFULL_P[abbonato].signal();
		else if (sospFULL_P[occasionale]>0 && sospSWO==0 && sospFULL_P[abbonato]==0)
			codaFULL_P[occasionale].signal();
		// verifica:
		System.out.println("Cliente FULL "+id+" di tipo "+tipo+" uscito dalla piscina.");		
		stampa_stato();
		
		lock.unlock();
		
	}

	public void esceFULL(int tipo, int id) throws InterruptedException {
		lock.lock();
		O_liberi++;
		if (sospFULL_S[abbonato]>0)
			codaFULL_S[abbonato].signal();
		else if (sospFULL_S[occasionale]>0 && sospFULL_S[abbonato]==0)
			codaFULL_S[occasionale].signal();
		// verifica:
		System.out.println("Cliente FULL "+id+" di tipo "+tipo+"  uscito dallo stabilimento.");		
		stampa_stato();
		lock.unlock();
		
	}

	private void stampa_stato() {
		System.out.println("Ombrelloni occupati:" + (N_OMB -  O_liberi) + "; In piscina=" + (N_P - PP_liberi));
		System.out.println("ATTESA PISCINA: \nsospSWO=" + sospSWO + "; sospFULL_P[abbo]=" + sospFULL_P[abbonato]+"; sospFULL_P[occ]=" + sospFULL_P[occasionale]); 
		System.out.println("ATTESA STABILIMENTO: \nsospFULL_S[abbo]=" + sospFULL_S[abbonato]+"; sospFULL_S[occ]=" + sospFULL_S[occasionale]+"\n");
	}
	
}
