
import java.util.Random;

public class Adulto  extends Thread {	
	private final int adu =1; //indica utente adulto
	private Monitor M;
	private Random r;
	
	public Adulto(Monitor M, Random R) {
		this.M = M;
		this.r=R;
	}

	public void run() {
		int codice;
		try {
			sleep(r.nextInt(1000));
			codice=M.entraSala_A();
			sleep(r.nextInt(500)); 
			M.accessoAMB(codice,adu);	
			sleep(r.nextInt(20*1000));  //durata della visita
			M.escePS(adu);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}