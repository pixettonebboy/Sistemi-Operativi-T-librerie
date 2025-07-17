
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Random;

public class Monitor {
	private final int ped=0; //indice utente pediatrico
	private final int adu =1; //indica utente adulto
	private final int rosso = 0;
	private final int giallo = 1;
	private final int verde =2;

	private int MAXS; // capacità sala aspetto
	private int NA; // numero ambulatori

	private Lock lock = new ReentrantLock();

	// stato della risorsa:
	private int TOTS;// persone in sala
	private int LiberiA; //ambulatori liberi

	// condition:
	private Condition[] codaSala = new Condition[2];
	private Condition[]codaAmb = new Condition[3];

	private int[] sospSala = new int[2];
	private int [] sospAmb = new int[3];

	private Random R;

	public Monitor(int maxs, int na, Random r) {
		int i;

		this.MAXS = maxs;
		this.NA = na;
		this.TOTS=0; //sala vuota
		this.LiberiA=NA; //ambulatori liberi
		this.R=r;
		for (i = 0; i < 2; i++) {
			codaSala[i]=lock.newCondition();
			sospSala[i]=0;
		}
		for ( i=0; i<3; i++)
		{	codaAmb[i] = lock.newCondition();
		sospAmb[i]=0;
		}
	}

	//entrata in sala di un minorenne con accompagnatore:
	public int entraSala_M() throws InterruptedException {	
		int cod;
		lock.lock();
		while (TOTS + 2 > MAXS ||  sospSala[adu] > 0) {
			sospSala[ped]++;
			codaSala[ped].await();
			sospSala[ped]--;
		}

		TOTS+=2;  //entrano 2 persone: utente e accompagnatore
		cod=R.nextInt(3); //codice assegnato dal triage
		// verifica
		System.out.println("\nUtente Pediatrico con codice "+cod+" e accompagnatore entrano in sala d'aspetto.");
		stampa_stato();
		lock.unlock();
		return cod;
	}

	//entrata in sala di un adulto:
	public int entraSala_A() throws InterruptedException {
		int cod;
		lock.lock();
		while (TOTS == MAXS) {
			sospSala[adu]++;
			codaSala[adu].await();
			sospSala[adu]--;
		}
		TOTS++;  //entra 1 persona adulta
		cod=R.nextInt(3); //codice assegnato dal triage
		// verifica
		System.out.println("\nUtente Adulto con codice "+cod+" entra in sala d'aspetto.");
		stampa_stato();
		lock.unlock();
		return cod;
	}

	//accesso ad un ambulatorio:
	public void accessoAMB(int codice, int tipo) throws InterruptedException {
		lock.lock();
		while ( LiberiA== 0 || piuprio(codice))
		{	sospAmb[codice]++;
		codaAmb[codice].await();
		sospAmb[codice]--;
		}
		LiberiA--; // occupo un ambulatorio
		if (tipo==ped)
			TOTS-=2;
		else 
			TOTS--;
		segnala_uscitaSala(tipo);
		// verifica
		if (tipo ==ped)
			System.out.println("\nMinorenne e accompagnatore entrano in ambulatorio con codice " + codice);
		else 
			System.out.println("\nAdulto entra in ambulatorio con codice " + codice);
		stampa_stato();
		lock.unlock();
	}

	//uscita dal pronto soccorso:
	public void escePS(int tipo) {
		lock.lock();
		LiberiA++;
		segnala_uscitaAmb();
		// verifica
		if (tipo==adu)
			System.out.println("\nutente adulto esce dal PS");
		else
			System.out.println("\nutente pediatrico esce dal PS");
		stampa_stato();
		lock.unlock();

	}
	
	boolean piuprio(int cod) // verifica se ci sono processi in attesa con codice più prioritario
	{	
		for (int i=0; i<cod; i++) //ciclo sul codice
			if (sospAmb[i]>0)
				return true;
		return false;	
	}	

	void segnala_uscitaAmb() //in uscita dal PS: viene liberato un ambulatorio
	{	for (int i=rosso; i<=verde; i++)
			if (sospAmb[i]>0)
			{	codaAmb[i].signal();
				return;
			}
	}

	void segnala_uscitaSala(int tipo) //in uscita dalla sala d'attesa..
	{	if (tipo==ped) 	//libera 2 posti
		{	if (sospSala[adu]>0 && (TOTS + 2 <= MAXS)) //precedenza agli adulti: ne risveglio 2
			{	codaSala[adu].signal();
				codaSala[adu].signal();
			}
			else if ( (sospSala[ped]>0) && (sospSala[adu]==0))
				codaSala[ped].signal();
		} else	//adulto libera un posto
		{	if (sospSala[adu]>0) //precedenza agli adulti
				codaSala[adu].signal();
			else if (sospSala[ped]>0)
				codaSala[ped].signal();
		}
		return;
	}

	private void stampa_stato() {
		System.out.println("In sala:" + TOTS + "; Ambulatori Occupati=" + (NA-LiberiA));
		System.out.println("sospSala[adu]=" + sospSala[adu] + "; sospSala[ped]=" + sospSala[ped]); 
		System.out.println("sospAmb[rosso]=" + sospAmb[rosso]+"; sospAmb[giallo]="+sospAmb[giallo]+"; sospAmb[verde]="+sospAmb[verde]);

	}
}