import java.util.Random;


public class Visitatore extends Thread{
	private Random r;
	private Monitor m;
	private int biglietto; //0=MEX - 1=FEX
	
	public Visitatore(Random r, Monitor m) {
		this.r = r;
		this.m = m;
		biglietto = ((r.nextBoolean()) ? 0 : 1);
	}
	

	public void run() {
		try{
			System.out.println(getName() + " VISITATORE " + biglietto);
			m.entra(biglietto);
			System.out.println(getName() + " ENTRATO " + biglietto);
			sleep(r.nextInt(10) * 1000);
			m.esce(biglietto);
			System.out.println(getName() + " USCITO " + biglietto);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
