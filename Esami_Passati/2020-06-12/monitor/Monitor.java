package sol12giu2020.light;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private final int IN=0;
	private final int OUT=1;
	private final int AUTO=0;
	private final int AMB=1;
	private final int AMBS=2;
			
	private Lock lock = new ReentrantLock();
	
	private Condition []codaIN;
	private Condition []codaOUT;
	private int []sospIN=new int[3];
	private int []sospOUT=new int[2]; 
	
	private int []inCamera=new int[2];
	private int [][]inRampa=new int[2][2]; //[tipo][direzione]
	
	
	private int N; // capacità camera calda
	
	
	public Monitor(int N) {
		int i,j;
		
		this.N = N;
		codaIN= new Condition[3];
		for(i=0; i<3; i++) {
			codaIN[i]=lock.newCondition();
			sospIN[i]=0;
		}
		codaOUT= new Condition[2];
		for(i=0; i<2; i++) {
			codaOUT[i]=lock.newCondition();
			sospOUT[i]=0;
		}
		for(i=0; i<2; i++)
			inCamera[i]=0;
		for (i=0;i<2;i++)
			for(j=0;j<2;j++)
				inRampa[i][j]=0;
	}
	
	public void rampa_in(int tipo, int dir) throws InterruptedException {
		
	lock.lock();
	try {
		if(dir==IN) // verso pronto soccorso
			{	while (	!condizione(tipo,IN)) {
					sospIN[tipo]++;
					codaIN[tipo].await();
					sospIN[tipo]--;
				}
				if (tipo==AMBS) tipo=AMB;
				inRampa[tipo][IN]++;
			}
			else if (dir==OUT)
			{	if (tipo==AMBS) tipo=AMB; // in uscita la sirena non conta
				while (	!condizione(tipo,OUT)) {
					sospOUT[tipo]++;
					codaOUT[tipo].await();
					sospOUT[tipo]--;
				}
				inRampa[tipo][OUT]++;
				inCamera[tipo]--;
				// liberato 1 posto: segnala in ingresso:
				if (sospIN[AMBS]>0 && tipo==AUTO)
					codaIN[AMBS].signal();
				else if (sospIN[AMBS]==0 && sospIN[AMB]>0 && tipo==AUTO)
					codaIN[AMB].signal();
				else if (sospIN[AMBS]==0 && sospIN[AMB]==0 && sospIN[AUTO]>0)
					codaIN[AUTO].signal();
			}
				System.out.println("VEICOLO IN RAMPA, tipo " + tipo +", direzione "+ dir);
		
   } finally { lock.unlock();}
		
  }
	
	public void rampa_out(int tipo, int dir) {
		
		lock.lock();
		if (tipo==AMBS) tipo=AMB;// in uscita dalla rampa la sirena non conta
		if (dir==IN)
		{	inRampa[tipo][IN]--;
			inCamera[tipo]++;
			// eventuale risveglio ambulanze in uscita:
			if ((tipo==AMB || tipo==AMBS)&& (inRampa[tipo][IN]==0))
				if (sospOUT[AMB]>0)
					codaOUT[AMB].signalAll();
		}
		else if (dir==OUT)
		{	inRampa[tipo][OUT]--;
			// eventuale risveglio ambulanze in ingresso:
					if ((tipo==AMB )&& (inRampa[tipo][OUT]==0))
					{	if (sospIN[AMBS]>0)
							codaIN[AMBS].signalAll();
						if (sospIN[AMB]>0)
							codaIN[AMB].signalAll();
					}
		}
		System.out.println("VEICOLO USCITO da RAMPA, tipo " + tipo +", direzione "+ dir);

		lock.unlock();
	}
	private boolean condizione(int tipo,int dir)
	{	
		int totIN=inCamera[AUTO]+inCamera[AMB]+inRampa[AUTO][IN]+inRampa[AMB][IN];
		int totAuto=inCamera[AUTO]+inRampa[AUTO][IN];
	
		if (dir==IN)
		{ 	if(totIN==N)
				return false;
			if (tipo==AUTO && totAuto+1>N/2)
				return false;
			if ((tipo==AMB || tipo==AMBS) && inRampa[AMB][OUT]>0)
				return false;
			if (sospOUT[AMB]>0 || sospOUT[AUTO]>0)
				return false;
			if (tipo==AUTO && (sospIN[AMB]>0 || sospIN[AMBS]>0))
				return false;
			if (tipo==AMB && sospIN[AMBS]>0)
				return false;
		}
		else //dir ==OUT
		{	if ((tipo==AMB) && inRampa[AMB][IN]>0)
				return false;
			if (tipo==AUTO && sospOUT[AMB]>0 )
				return false;
		}
		return true;
	}	
	/*public String toString() {
		
		return "\nSituazione Monitor:\nAuto alla Camera: " + autoCamera + "\nAmbulanze alla camera: " + ambulanzaCamera +
				"\nAuto in attesa di entrare: " + sospAutoIngresso + "\nAmbulanze con sirena in attesa di entrare: " + sospAmbulanzaONIngresso +
				"\nAmbulanze senza sirenza in attesa di entrare: " + sospAmbulanzaOFFIngresso + "\nAmbulanze che stanno entrando: " + ambulanzaIngresso +
				"\nAmbulanze che stanno uscendo: " + ambulanzaUscita + 
				"\nAuto totali servite: " + autoServite +
				"\nAmbulanze totali servite: " + ambulanzeServite + "\n\n";
		
	}
	*/
}