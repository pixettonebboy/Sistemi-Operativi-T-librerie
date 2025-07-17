package esame11lug2022;

import java.util.Random;

public class Barca extends Thread{
	private String ID;
	private Monitor M;
	private int Tipo;
	private Random R;
	private final int IN=0;
	private final int OUT=1;
	
	public Barca(int t, String id, Monitor m, Random r) {
		this.Tipo=t;
		this.ID=id;
		this.M=m;
		this.R=r;
	}
	public void run() {
		int posto;
		try{
		posto=this.M.entraB(Tipo, IN, 0);
		sleep(R.nextInt(100));
		this.M.esceB(Tipo, IN);
		sleep(R.nextInt(1000));
		this.M.entraB(Tipo, OUT, posto);
		sleep(R.nextInt(100));
		this.M.esceB(Tipo, OUT);
		}catch (InterruptedException e){ e.printStackTrace();}
	}
}
