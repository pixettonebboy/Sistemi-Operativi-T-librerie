package esame11lug2022;

import java.util.Random;

public class MotoVedetta extends Thread {
	private String ID;
	private Monitor M;
	
	private Random R;
	private final int IN=0;
	private final int OUT=1;
	
	public MotoVedetta(String id, Monitor m, Random r) {
		this.ID=id;
		this.M=m;
		this.R=r;
		
	}

	public void run() {
		while (true)
		{	
			try{
				sleep(R.nextInt(3000));//permanenza in porto
				this.M.entraMV(OUT);
				sleep(R.nextInt(100));
				this.M.esceMV(OUT);
				sleep(R.nextInt(1000));//navigazione in mare
				this.M.entraMV(IN);
				sleep(R.nextInt(100));
				this.M.esceMV(IN);
				
		}catch (InterruptedException e){ e.printStackTrace();}
		}
	}
}