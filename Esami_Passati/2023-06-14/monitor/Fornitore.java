import java.util.Random;

public class Fornitore extends Thread {
	private String ID;
	private Monitor M;
	private Random R;
	
	
	public Fornitore(String id, Monitor m, Random r) {
		this.ID=id;
		this.M=m;
		this.R=r;
		
	}

	public void run() {
		int formato;
		//System.out.println("Parte il thread Fornitore " + ID + "...");
		while (true)
		{	formato=R.nextInt(2); // determina il formato del prossimo pacco
			try{
				sleep(R.nextInt(3000));
				this.M.donazione(formato, ID);
				sleep(R.nextInt(100));
		}catch (InterruptedException e){ e.printStackTrace();}
		}
	}
}