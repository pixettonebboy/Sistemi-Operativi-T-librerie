package sol15lug2020;

import java.util.Random;

public class Addetto extends Thread {
	private Monitor m;
	private Random r = new Random();
	int negozio, t;
	
	public Addetto (Monitor m) {
		this.m = m;
	}
	
	public void run() {
		try {
			while (true) {
				t=r.nextInt(100);
				Thread.sleep(t*10);
				this.negozio = r.nextInt(2);
				m.inizio_igiene(negozio);
				t=r.nextInt(200);
				Thread.sleep(t*10);
				m.fine_igiene(negozio);
			}
		} catch (InterruptedException e) {}
	}
}
