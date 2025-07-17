

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private final static int SING = 0;
	private final static int FAM = 1;

	private int PMAX; //capienza deposito
	private int LIBERI; //posti per pacco singolo liberi in deposito (il pacco famiglia vale 3)
	private int NSING; // numero pacchi singoli in deposito
	private int NFAM; //numero pacchi famiglia in deposito
	private boolean APERTO; //true se il palazzetto è aperto
	private Lock L = new ReentrantLock();
	private Condition [] codaF = new Condition[2]; // una condition per formato di pacco, in cui accodare i fornitori
	private Condition [] codaC = new Condition[2];  //una condition per i cittadini per ogni formato di pacco
	private int [] sospF = new int[2];
	private int [] sospC = new int[2];


	public Monitor(int maxP) {
		this.PMAX=maxP;
		this.NSING=0;
		this.NFAM=0;
		this.LIBERI=maxP;
		this.APERTO=true; // il palazzetto è inizialmente aperto


		for(int i =0; i<2; i++){
			this.codaF[i]=L.newCondition();
			this.codaC[i]=L.newCondition();
			this.sospF[i]=0;
			this.sospC[i]=0;

		}
	}


	public void richiesta(int formato, String iD)  throws InterruptedException { //cittadino chiede un pacco del formato dato
		L.lock();	
		try {
			if (formato==FAM )	
			{
				while ( NFAM==0 || !APERTO)
				{	sospC[FAM]++;
				codaC[FAM].await();
				sospC[FAM]--;	
				}
				NFAM--;
				LIBERI+=3;
				//liberati 3 posti: segnalo i fornitori
				if (sospF[SING]>0)
					codaF[SING].signalAll();
				if (sospF[FAM]>0)
					codaF[FAM].signal();  
			}
			else if (formato==SING)	
			{
				while ( NSING==0 || sospC[FAM]>0 || !APERTO  )
				{	sospC[SING]++;
				codaC[SING].await();
				sospC[SING]--;	
				}
				NSING--;
				LIBERI++;
				//è stato liberato un posto: segnalo un fornitore
				if (sospF[SING]>0)
					codaF[SING].signal();
				else if (sospF[FAM]>0)
					codaF[FAM].signal();
			}

			System.out.println("Cittadino" + iD + " ha ottenuto un pacco di formato "+formato + stato());
		} finally {L.unlock(); }

	}


	public void donazione(int formato, String iD) throws InterruptedException  {
		L.lock();	
		try {
			if (formato==FAM)
			{	while ( LIBERI<3  ||  sospF[SING]>0 || !APERTO)
			{	sospF[FAM]++;
			codaF[FAM].await();
			sospF[FAM]--;	
			}
			NFAM++;
			LIBERI-=3;
			//c'è un nuovo pacco famiglia: segnalo un cittadino in attesa del pacco famiglia
			if (sospC[FAM]>0)
				codaC[FAM].signal();

			}
			else if (formato==SING)	
			{	while ( LIBERI<1 || !APERTO )
				{	sospF[SING]++;
					codaF[SING].await();
					sospF[SING]++;	
				}
				NSING++;
				LIBERI--;
				//c'è un nuovo pacco singolo: segnalo un cittadino in attesa del pacco singolo
				if (sospC[SING]>0 && sospC[FAM]==0)
					codaC[SING].signal();
				}
				System.out.println("Fornitore" + iD +  " ha depositato un pacco di formato "+formato+ stato());
		} finally {L.unlock(); }
	}


	public void chiusura() {
		L.lock();

		APERTO=false;
		System.out.println("ALLERTA METEO! Il palazzetto è chiuso.."+ stato());

		L.unlock();
	}


	public void apertura() {
		L.lock();	
		APERTO=true;
		// segnalo Cittadini e Fornitori: 
		if (sospC[FAM]>0)
			codaC[FAM].signalAll();
		if (sospC[SING]>0)
			codaC[SING].signalAll();
		if (sospF[SING]>0)
			codaF[SING].signalAll();
		if (sospF[FAM]>0)
			codaF[FAM].signalAll();
		System.out.println("FINE ALLERTA METEO: il palazzetto è aperto!"+ stato());

		L.unlock();
	}

	private String stato() { //per DEBUG
		return "[APERTO="+APERTO+" LIBERI="+LIBERI+" N_Singoli="+NSING+ " N_Famiglia="+NFAM+ " sospF[SING]="+sospF[SING]+" sospF[FAM]="+sospF[FAM]+" sospC[SING]="+sospC[SING]+" sospC[FAM]="+sospC[FAM]+"]";
	}


}
