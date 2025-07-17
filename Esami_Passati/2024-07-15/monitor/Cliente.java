import java.util.Random;

public class Cliente extends Thread {
	private Monitor m;
	private Random r = new Random();
	
	private int formato; //0 piccolo, 1 grande
	private int quanteVolte;
	private int id;
	
	public Cliente(Monitor M, int id) {
		this.m = M;
		this.id=id;
		quanteVolte=r.nextInt(10)+1;
	}
	
	public void run() {
		
		
		for (int i=0; i<quanteVolte; i++)
		{	
			formato = r.nextInt(2); //decide il formato
		
		try {
			Thread.sleep(r.nextInt(300*(formato+1)));
			m.InizioPrelievo(id,  formato);
			
			Thread.sleep(r.nextInt(200));
			m.FinePrelievo(id, formato);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		}
	}
}
