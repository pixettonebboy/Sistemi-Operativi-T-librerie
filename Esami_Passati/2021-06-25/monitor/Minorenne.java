import java.util.Random;

public class Minorenne  extends Thread {

	private final int ped=0; //indice utente pediatrico
	
	private Monitor M;
	private Random r;
	
	public Minorenne(Monitor M, Random R) {
		this.M = M;
		this.r=R;
	}

	public void run() {
		int codice;
		try {
			sleep(r.nextInt(1000));
			codice=M.entraSala_M();
			sleep(r.nextInt(100));
			M.accessoAMB(codice, ped);	
			sleep(r.nextInt(20*1000));  //durata della visita
			M.escePS(ped);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}