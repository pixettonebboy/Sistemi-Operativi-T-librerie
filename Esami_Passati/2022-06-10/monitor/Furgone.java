import java.util.Random;

public class Furgone extends Thread {
	private Monitor m;
	private int tipo; //0 per aziendale, 1 per esterno
	private Random r;
	
	public Furgone(Monitor m, int tipo, Random r) {
		this.m = m;
		this.tipo = tipo;
		this.r = r;
	}
	
	public void run() {
		try {
			sleep(r.nextInt(3)*1000);
			m.furgone(tipo);
			sleep(r.nextInt(5)*1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
