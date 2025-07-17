package esame220629;

import java.util.Random;

public class Visitatori extends Thread{
	private Monitor M;;
	private Random R;
	private int T;
	
	public Visitatori(Monitor monitor, int tipo, Random r) {
		this.M=monitor;
		this.T=tipo;
		this.R=r;
	}


	
	public void run() {
		M.EntraV(T);
		
		try {
			Thread.sleep(R.nextInt(3000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		M.EsceV(T);
	}
}
