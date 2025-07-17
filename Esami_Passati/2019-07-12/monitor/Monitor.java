import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Monitor {
	private int MAX_F;
	private int MAX_M;
	private int NMP;
	
	private Lock lock = new ReentrantLock();
	private Condition mex = lock.newCondition();	//coda ingresso mex
	private Condition fex = lock.newCondition();	//coda ingresso fex
	
	private int sospM;
	private int sospF;
	
	private int nMonopattino;	//numero di monopattini attualmente occupati
	private int nFEX;	//numero di persone nel FEX
	private int nMEX;	//numero di persone nel MEX
	
	public Monitor(int maxf, int maxm, int nmp){
		MAX_F = maxf;
		MAX_M = maxm;
		if(nmp > maxf + maxm)
			throw new IllegalArgumentException();
		NMP = nmp;
		
		sospM = 0;
		sospF = 0;
		nMonopattino = 0;
		nFEX = 0;
		nMEX = 0;
	}
	
	
	public void entra(int biglietto) throws InterruptedException {
		lock.lock();
		try {
			if(biglietto == 0){ //MEX
				while(MAX_M == nMEX || nMonopattino == NMP || (sospF > 0 && (MAX_F-nFEX) < (MAX_M-nMEX))){
					sospM++;
					mex.await();
					sospM--;
				}
				nMEX++;
				nMonopattino++;
				
			} else { //FEX
				while(MAX_F == nFEX || nMonopattino == NMP || (sospM > 0 && (MAX_M-nMEX) < (MAX_F-nFEX))){
					sospF++;
					fex.await();
					sospF--;
				}
				nFEX++;
				nMonopattino++;
				
			}
			System.out.println("Stato attuale -> MEX: " + nMEX + " FEX: " + nFEX + " Monopattini: " + nMonopattino);
		} finally {
			lock.unlock();
		}
	}

	public void esce(int biglietto) {
		lock.lock();
		try {
			nMonopattino--;
			if(biglietto == 0) //MEX
				nMEX--;
			else  //FEX
				nFEX--;
				
			if((MAX_M-nMEX) < (MAX_F-nFEX)) //prio a FEX
			{	if (sospF>0) 
					fex.signal();
				else if (sospM>0)
					mex.signal();
			}
			else // prio a MEX
			{	if (sospM>0) 
					mex.signal();
				else if (sospF>0)
					fex.signal();
			}
			System.out.println("Stato attuale -> MEX: " + nMEX + " FEX: " + nFEX + " Monopattini: " + nMonopattino);
		} finally {
			lock.unlock();
		}		
	}

}
