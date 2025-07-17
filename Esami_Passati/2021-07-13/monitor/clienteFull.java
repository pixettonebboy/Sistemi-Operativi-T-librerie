package stabilimentoBalneare;

import java.util.Random;

public class clienteFull extends Thread{

	private monitor M;
	private Random R;
	private int tipo; //0 --> abbonato, 1--> occasionale
	private int id;
	
	public clienteFull(monitor m, Random r, int t, int i) {
		this.M=m;
		this.R=r;
		this.tipo=t;
		this.id=i;
	}
	public void run() {
		boolean OK;
		int ntimes;
		try {
			sleep(R.nextInt(10*1000));
			M.entraFULL(tipo,id); // entra nello stabilimento occupando un ombrellone
			ntimes=R.nextInt(10); // al massimo 10 iterazioni..
			System.out.println("Cliente FULL "+id+" di tipo "+tipo+" vuole andare "+ntimes+" volte in piscina\n");
			for (int i=0; i<ntimes; i++) 
			{	sleep(R.nextInt(10000)); //permanenza in spiaggia
				OK=R.nextBoolean();
				if (OK) //decide di andare in piscina
				{	M.entraP_FULL(tipo, id);
					sleep(R.nextInt(10000)); //permanenza in piscina
					M.esceP_FULL(tipo, id);
				}
			}
			M.esceFULL(tipo, id);	//esce dallo stabilimento
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
