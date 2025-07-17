
import java.util.Random;

public class Cittadino extends Thread{
	private String ID;
	private Monitor M;
	private int Formato;
	private Random R;
	
	
	public Cittadino(int formato, String id, Monitor m, Random r) {
		this.Formato=formato; //pacco singolo o famiglia
		this.ID=id;
		this.M=m;
		this.R=r;
	}
	public void run() {
		//System.out.println("Parte il thread Cittadino " + ID + " che richiede un pacco di formato "+ Formato+ "...");
		try{
		sleep(R.nextInt(100));
		this.M.richiesta(Formato, ID);
		sleep(R.nextInt(100));
		}catch (InterruptedException e){ e.printStackTrace();}
	}
}
