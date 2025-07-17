package sol15lug2020;

import java.util.concurrent.locks.*;

public class Monitor {
	private final static int SUP = 0;
	private final static int MED = 1;
	
	private int max_gc, max_mg, max;
	private int[] in = new int[2];
	private int in_tot;
	private boolean[] in_igiene = new boolean[2];
	
	private Lock lock = new ReentrantLock();
	private Condition[] clienti = new Condition[2];
	private Condition []addetto= new Condition[2];
	private int[] SospC = new int[2];
	private int[] SospA = new int[2];
	
	public Monitor (int max_gc, int max_mg, int max) {
		this.max = max;
		this.max_gc = max_gc;
		this.max_mg = max_mg;
		this.in_tot = 0;

		for (int i=0; i<2; i++) {
			this.clienti[i] = lock.newCondition();
			this.addetto[i] = lock.newCondition();
			this.SospC[i] = 0;
			this.SospA[i] = 0;
			this.in[i] = 0;
			this.in_igiene[i] = false;
		}
	}
	
	public void accesso (int negozio) throws InterruptedException {
		lock.lock();
		try {
			if (negozio == SUP) { // cliente entra nel supermercato
				while (in[SUP] == max_gc || in_tot == max || in_igiene[SUP] || ((max_mg - in[MED]) > (max_gc - in[SUP]) && SospC[MED] > 0) || SospA[SUP] > 0) {
					SospC[SUP]++;
					clienti[SUP].await();
					SospC[SUP]--;
				}
				in[SUP]++;
				in_tot++;
				System.out.println("Cliente entra nel supermercato [clienti del SUPERMERCATO: "+in[SUP]+" clienti di MEDIAGOODS "+in[MED]+" Numero TOTALE di clienti: "+in_tot+"]");
			}
			else if (negozio == MED) { // cliente entra nel negozio di media
				while (in[MED] == max_gc || in_tot == max || in_igiene[MED] || ((max_gc - in[SUP]) > (max_mg - in[MED]) && SospC[SUP] > 0) || SospA[MED] > 0) {
					SospC[MED]++;
					clienti[MED].await();
					SospC[MED]--;
				}
				in[MED]++;
				in_tot++;
				System.out.println("Cliente entra nel negozio di Elettrodomestici [clienti del SUPERMERCATO: "+in[SUP]+" clienti di MEDIAGOODS "+in[MED]+" Numero TOTALE di clienti: "+in_tot+"]");
			}
		} finally {lock.unlock();}
	}
	
	public void uscita (int negozio) throws InterruptedException {
		lock.lock();
		try {
			in[negozio]--;
			in_tot--;
			if (negozio == MED)
				System.out.println("Cliente esce dal negozio di Elettrodomestici [clienti del SUPERMERCATO: "+in[SUP]+" clienti di MEDIAGOODS "+in[MED]+" Numero TOTALE di clienti: "+in_tot+"]");
			else
				System.out.println("Cliente esce dal supermercato [clienti del SUPERMERCATO: "+in[SUP]+" clienti di MEDIAGOODS "+in[MED]+" Numero TOTALE di clienti: "+in_tot+"]");
			if (in[negozio] == 0 && SospA[negozio] > 0)
				addetto[negozio].signal();
			else if ((max_gc - in[SUP]) > (max_mg - in[MED]) && SospC[SUP] > 0)
				clienti[SUP].signal();
			else if ((max_mg - in[MED]) > (max_gc - in[SUP]) && SospC[MED] > 0)
				clienti[MED].signal();
			
		} finally {lock.unlock();}
	}
	
	public void inizio_igiene (int negozio) throws InterruptedException {
		lock.lock();
		try {
			while (in[negozio] != 0) {
				SospA[negozio]++;
				addetto[negozio].await();
				SospA[negozio]--;
			}
			
			in_igiene[negozio] = true;
			if (negozio==0)
				System.out.println("Addetto: inizio pulizia del supermercato GoodCoop");
			else
				System.out.println("Addetto: inizio pulizia del negozio MediaGoods");
				
		} finally {lock.unlock();}
	}
	
	public void fine_igiene (int negozio) throws InterruptedException {
		lock.lock();
		try {
			in_igiene[negozio] = false;
			System.out.println("Addetto: fine pulizia");
			if (SospC[negozio] > 0)
				clienti[negozio].signalAll();
		} finally {lock.unlock();}
	}
	
}
