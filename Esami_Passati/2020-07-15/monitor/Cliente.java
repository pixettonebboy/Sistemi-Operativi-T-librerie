package sol15lug2020;

import java.util.Random;

public class Cliente extends Thread {
	private Monitor m;
	private Random r = new Random();
	int negozio, t;
	
	public Cliente (Monitor m) {
		this.m = m;
		this.negozio = r.nextInt(2);		//scelta del negozio 0:supermercato, 1 media
	}
	
	public void run () {
		try {
			m.accesso(negozio);
			t=r.nextInt(200);
			Thread.sleep(t*10);
			m.uscita(negozio);
		} catch (InterruptedException e) {}
	}
}
