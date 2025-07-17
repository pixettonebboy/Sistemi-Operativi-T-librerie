import java.util.concurrent.locks.*;

public class Monitor {
	private final int N = 0; //nord
	private final int S = 1; //sud
	private final int A = 0; //furgone aziendale
	private final int E = 1; //furgone esterno
	private final int MaxD, NF; //NF è fisso
	private int num = 0; //numero pacchi attualmente presenti nel centro
	private int[] scarichi = new int[2]; //numero di camion scaricati per ogni direzione
	private Lock lock = new ReentrantLock();
	private Condition[] codaCamion = new Condition[2];
	private Condition[] codaFurgoni = new Condition[2];
	private int[] sospCamion = new int[2];
	private int[] sospFurgoni = new int[2];
	
	public Monitor(int MaxD, int NF) {
		this.MaxD = MaxD;
		this.NF = NF;
		for(int i = 0; i < 2; i++) {
			scarichi[i] = 0;
			sospFurgoni[i] = 0;
			sospCamion[i] = 0;
			codaCamion[i] = lock.newCondition();
			codaFurgoni[i] = lock.newCondition();
		}
	}
	
	public void camion(int dir, int nc) throws InterruptedException {
		lock.lock();
		try {
			//un camion si sospende se non può scaricare i pacchi
			//o  se c'è un camion più prioritario in attesa
			while(num + nc > MaxD || (scarichi[altraDir(dir)] < scarichi[dir] && sospCamion[altraDir(dir)] > 0)) {
				sospCamion[dir]++;
				codaCamion[dir].await();
				sospCamion[dir]--;
			}
			num += nc;
			scarichi[dir]++;
			System.out.println("Un camion con direzione "+dir+" ha appena scaricato "+nc+" pacchi. Tot pacchi: "+num);
			if(num >= NF) {
				if(sospFurgoni[A] > 0) codaFurgoni[A].signalAll(); 
				if(sospFurgoni[E] > 0) codaFurgoni[E].signalAll();
			}

			//inoltre risveglio i camion con direzione opposta nel caso in cui sia appena cambiata la priorità:
			if(scarichi[dir] >= scarichi[altraDir(dir)]+1 && sospCamion[altraDir(dir)] > 0)
				codaCamion[altraDir(dir)].signalAll(); 
				
		} finally {lock.unlock();}
	}
	
	public void furgone(int tipo) throws InterruptedException {
		lock.lock();
		try {
			
			while(num < NF || (tipo == E && sospFurgoni[A] > 0)) {
				sospFurgoni[tipo]++;
				codaFurgoni[tipo].await();
				sospFurgoni[tipo]--;
			}
				num -= NF;
				System.out.println("Un furgone ha appena prelevato "+NF+" pacchi. Tot pacchi: "+num);
				if(scarichi[N] > scarichi[S]) { //S prioritario
					if(sospCamion[S] > 0) codaCamion[S].signalAll();
					if(sospCamion[N] > 0) codaCamion[N].signalAll();
				}
				else { //N prioritario
					if(sospCamion[N] > 0) codaCamion[N].signalAll();
					if(sospCamion[S] > 0) codaCamion[S].signalAll();
					
				}
			}
		} finally {lock.unlock();}
	}
	
	
	
	private int altraDir(int dir) {
		if(dir == N) return S;
		else return N;
	}
}

