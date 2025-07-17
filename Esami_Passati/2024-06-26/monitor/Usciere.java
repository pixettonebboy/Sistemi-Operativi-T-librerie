package sot26giu24A;


import java.util.Random;

public class Usciere extends Thread {
	private Monitor M;
	private Random R;

	public Usciere(Monitor monitor, Random r)
	{
		this.M = monitor;
		this.R = r;
	}

	public void run() {
		try{

			while(true) {
				M.InizioRiunione();
				sleep(R.nextInt(3) * 1000); // tempo di svolgimento riunione
				M.FineRiunione();
				sleep(R.nextInt(5) * 1000); // va in pausa
			}

		} catch (InterruptedException e) { }
	}


}
