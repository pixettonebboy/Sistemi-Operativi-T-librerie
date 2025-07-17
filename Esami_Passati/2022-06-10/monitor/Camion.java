import java.util.Random;

public class Camion extends Thread {
	private Monitor m;
	private int dir; //0 per Nord, 1 per Sud
	private int nc;
	private Random r;
	
	public Camion(Monitor m, int dir, int nc, Random r) {
		this.m = m;
		this.dir = dir;
		this.nc = nc;
		this.r = r;
	}
	
	public void run() {
		try {
			sleep(r.nextInt(5)*1000);
			m.camion(dir, nc);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
