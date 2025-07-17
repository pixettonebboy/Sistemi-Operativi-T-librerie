package esame220629;

import java.util.Random;

public class Guide extends Thread {
	private Monitor M;
	private Random R;
	
	public Guide(Monitor monitor, Random r)
	{
		this.M = monitor;
		this.R = r;
	}
	

	
	public void run() {
		while(true) {
			M.EntraGuida();
			
			try {
				Thread.sleep(R.nextInt(3000));
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
			
			M.EsciGuida();
		
			try {
				Thread.sleep(R.nextInt(3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

