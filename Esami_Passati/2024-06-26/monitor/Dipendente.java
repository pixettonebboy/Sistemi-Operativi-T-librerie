package sot26giu24A;

import java.util.Random;
public class Dipendente extends Thread{
	private Monitor M;
	private Random R;
	private int QuanteR; // numero di riunioni a cui intende partecipare

	public Dipendente(Monitor monitor, Random r) {
		this.M=monitor;
		this.R=r;
		this.QuanteR=R.nextInt(3)+1; // al massimo 3 riunioni
	}

	public void run() {

		try{ sleep(R.nextInt(500));
		for (int i=0; i<QuanteR; i++)
		{	M.EntraSalaDip();
			sleep(R.nextInt(3000));
			M.EsceSalaDip();
			sleep(R.nextInt(5000));
		}

		} catch (InterruptedException e) { }
	}

}


