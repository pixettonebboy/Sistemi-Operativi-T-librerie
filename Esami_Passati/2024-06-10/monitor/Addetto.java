
import java.util.Random;

public class Addetto extends Thread {
	private Monitor M;
	private Random R;
	
	public Addetto(Monitor monitor, Random r)
	{
		this.M = monitor;
		this.R = r;
	}
	
	public void run() {
		try{
			
			while(true) {
				M.EntraAddetto();
					
				sleep(8 * 1000); // presidia
				
				M.EsceAddetto();
					
				sleep(4 * 1000); // va in pausa
			}
		
		} catch (InterruptedException e) { }
	}

	
	}

