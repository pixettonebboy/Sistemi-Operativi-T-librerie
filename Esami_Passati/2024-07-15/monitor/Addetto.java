import java.util.Random;

public class Addetto extends Thread {
	private Monitor m;
	private Random r = new Random();
	
	
	public Addetto(Monitor m) {
		this.m = m;
	}
	
	public void run() {
		
		try {
			System.out.println("addetto creato");
			Thread.sleep(r.nextInt(500));
			while (true) {
				m.InizioSostituzione();
				Thread.sleep(r.nextInt(1500));
				m.FineSostituzione();
				Thread.sleep(r.nextInt(500));
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
