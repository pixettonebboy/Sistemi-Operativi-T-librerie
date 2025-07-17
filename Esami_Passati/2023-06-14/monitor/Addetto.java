

import java.util.Random;

public class Addetto extends Thread {
	
	private Monitor M;
	private Random R;
	
	public Addetto(Monitor m, Random r) {
		this.M=m;
		this.R=r;

	}

	public void run() {
		
		//System.out.println("Parte il thread Addetto " + ID + "...");
		while (true)
		{	
			try{
				sleep(R.nextInt(30000));
				this.M.chiusura();
				sleep(R.nextInt(1000));
				this.M.apertura();
				sleep(R.nextInt(1000));
				
				
		}catch (InterruptedException e){ e.printStackTrace();}
		}
		
	}
}