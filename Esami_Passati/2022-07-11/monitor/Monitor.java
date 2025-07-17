package esame11lug2022;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private final static int IN = 0;
	private final static int OUT = 1;
	private final static int BP = 0;
	private final static int BG = 1;
	private final static int std =0;
	private final static int maxi=1;
	
	private int M; //numero posti maxi
	private int N; // numero posti normali
	private int Mocc; // maxi occupati
	private int Nocc; // standard occupati
	
	private Lock L = new ReentrantLock();
	private int [] piccoleC = new int  [2]; //barche piccole nel canale in entrambe le direzioni 0 entrata, 1 uscita
	private int [] grandiC = new int [2]; //barche grandi nel canale in entrambe le direzioni 0 entrata, 1 uscita
	private int [] motovedettaC=new int [2]; //motovedetta nel canale 0 entrata, 1 uscita
	
	private Condition [] codaBP = new Condition[2];
	private Condition [] codaBG = new Condition[2];
	private Condition [] codaMV = new Condition[2];
	private int [] BPsosp = new int[2];
	private int [] BGsosp = new int[2];
	private int [] MVsosp = new int[2];
	
	public Monitor(int maxm, int maxn) {
		this.M=maxm;
		this.N=maxn;
		Mocc=0;
		Nocc=0;
		for(int i =0; i<2; i++){
			this.piccoleC[i]=0;
			this.grandiC[i]=0;
			this.motovedettaC[i]=0;
			this.codaBP[i]=L.newCondition();
			this.codaBG[i]=L.newCondition();
			this.codaMV[i]=L.newCondition();
			this.BPsosp[i]=0;
			this.BGsosp[i]=0;
			this.MVsosp[i]=0;
			
		}
	}
	private int inCanale()
	{ return piccoleC[IN]+piccoleC[OUT]+grandiC[IN]+grandiC[OUT]+ motovedettaC[IN]+ motovedettaC[OUT];
	}
	
	public void entraMV(int dir) throws InterruptedException {
		L.lock();
		while (inCanale()!=0 || 
				(dir==IN && MVsosp[OUT]>0) )
		{
			this.MVsosp[dir]++;
			this.codaMV[dir].await();
			this.MVsosp[dir]--;
		}
		
		this.motovedettaC[dir]++;
		System.out.println("Motovedetta" + Thread.currentThread().getId() + " è entrata nel canale in direzione "+dir);
		L.unlock();
	}

	public void esceMV(int dir) {
		L.lock();
		this.motovedettaC[dir]--;
		// todo SIGNAL!!!
		if (MVsosp[OUT]>0)
			codaMV[OUT].signal();
		else if (MVsosp[IN]>0) //risveglio motovedette IN
			codaMV[IN].signal();
		else { // risveglio barche in ordine di priorità
			if (BGsosp[OUT]>0)
				codaBG[OUT].signalAll();
			if (BGsosp[OUT]>0)
				codaBG[OUT].signalAll();
			if (BGsosp[IN]>0)
				codaBG[IN].signalAll();
			if (BGsosp[IN]>0)
				codaBG[IN].signalAll();
		}
		System.out.println("Motovedetta" + Thread.currentThread().getId() + " è uscita dal  canale in direzione "+dir);
		L.unlock();
	}

	

	public int entraB (int tipo, int dir, int posto) throws InterruptedException {
		int ris=-1;
		L.lock();
		if ((dir == IN) && (tipo==BP) ) // barca piccola in direzione IN
		{	while (motovedettaC[IN]+motovedettaC[OUT]>0 ||  // c'è la MV nel canale
				   grandiC[OUT]>0 || //ci sono barche in dir opposta
				   (Nocc==N && Mocc==M) || // tutti i posti  sono occupati
				   MVsosp[OUT]+MVsosp[IN]+BGsosp[OUT]+BPsosp[OUT]+BGsosp[IN]>0 ) // priorità
			{	BPsosp[IN]++;
				codaBP[IN].await();
				BPsosp[IN]--;
			}
			piccoleC[IN]++;
			if (Nocc<N) // occupa un posto nel porto
			{	Nocc++;
				ris=std;;
			}
			else 
			{	Mocc++;
				ris=maxi;
			}
			System.out.println("Barca Piccola" + Thread.currentThread().getId() + " è entrata nel canale in direzione IN");
		}
		if ((dir == IN) && (tipo==BG) ) // barca grande in direzione IN
		{	while (motovedettaC[IN]+motovedettaC[OUT]>0 ||  // c'è la MV nel canale
				   piccoleC[OUT]+grandiC[OUT]>0 || //ci sono barche in dir opposta
				   Mocc==M || // tutti i posti maxi sono occupati
				   MVsosp[OUT]+MVsosp[IN]+BGsosp[OUT]+BPsosp[OUT]>0 ) // priorità
			{	BGsosp[IN]++;
				codaBG[IN].await();
				BGsosp[IN]--;
			}
			grandiC[IN]++;
			Mocc++; // occupa un posto nel porto
			ris=maxi;
			System.out.println("Barca Grande" + Thread.currentThread().getId() + " è entrata nel canale in direzione IN");
		}
		if ((dir == OUT) && (tipo==BP) ) // barca piccola in direzione OUT
		{	while (motovedettaC[IN]+motovedettaC[OUT]>0 ||  // c'è la MV nel canale
				   grandiC[IN]>0 || //ci sono barche grandi in dir opposta
				   MVsosp[OUT]+MVsosp[IN]+BGsosp[OUT]>0 ) // priorità
			{	BPsosp[OUT]++;
				codaBP[OUT].await();
				BPsosp[OUT]--;
			}
			piccoleC[OUT]++;
			// libera il posto: 
			if (posto==std) 
				   Nocc--;
			else   Mocc--;
			// signal a  piccole IN:
			if (BPsosp[IN]>0 && grandiC[OUT]==0) 
				codaBP[IN].signal();
			System.out.println("Barca Piccola" + Thread.currentThread().getId() + " è entrata nel canale in direzione OUT");
		}
		if ((dir == OUT) && (tipo==BG) ) // barca grande in direzione OUT
		{	while (motovedettaC[IN]+motovedettaC[OUT]>0 ||  // c'è la MV nel canale
				   piccoleC[IN]+grandiC[IN]>0 || //ci sono barche in dir opposta
				   MVsosp[OUT]+MVsosp[IN]>0 ) // priorità
			{	BGsosp[OUT]++;
				codaBG[OUT].await();
				BGsosp[OUT]--;
			}
			grandiC[OUT]++;
			Mocc--;
			//non segnalo nessuno in ingresso perchè non potrebbe entrare
			System.out.println("Barca Grande" + Thread.currentThread().getId() + " è entrata nel canale in direzione OUT");
		}
		L.unlock();	
		return ris;
		
	}

	public void esceB(int tipo, int dir) {
		L.lock();
		if (tipo==BG)
		{	grandiC[dir]--;
			System.out.println("Barca Grande" + Thread.currentThread().getId() + " è uscita dal  canale in direzione "+dir);
		}
		else	
		{	piccoleC[dir]--;
			System.out.println("Barca Piccola" + Thread.currentThread().getId() + " è uscita dal  canale in direzione "+dir);
		}
		if (MVsosp[OUT]>0 && inCanale()==0)
			codaMV[OUT].signal();
		else if (MVsosp[IN]>0 && inCanale()==0) //risveglio motovedette IN
			codaMV[IN].signal();
		else if (dir==OUT)
			{	 // risveglio barche IN
				if (BGsosp[IN]>0 && grandiC[OUT]+piccoleC[OUT]==0 && Mocc<M)
					codaBG[IN].signalAll();
				if (BPsosp[IN]>0 && grandiC[OUT]==0 && Mocc+Nocc<M+N)
					codaBP[IN].signalAll();
			}
		else if (dir==IN )//dir IN
			{	 // risveglio barche OUT
				if (BGsosp[OUT]>0 && grandiC[IN]+piccoleC[IN]==0)
					codaBG[OUT].signalAll();
				if (BPsosp[OUT]>0 && grandiC[IN]==0)
					codaBP[OUT].signalAll();
		}
		L.unlock();
		
	}

}
