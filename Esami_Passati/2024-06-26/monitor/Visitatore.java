package sot26giu24A;

import java.util.Random;
public class Visitatore extends Thread{

	private Monitor M;
	private Random R;

	public Visitatore(Monitor monitor, Random r) {
		this.M=monitor;
		this.R=r;
	}

	public void run() {

		try{ 
			sleep(R.nextInt(500));
			M.EntraSalaVis();
			sleep(R.nextInt(3000));
			M.EsceSalaVis();

		} catch (InterruptedException e) { }
	}

}

