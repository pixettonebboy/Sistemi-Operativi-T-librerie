package sol30giu2020;

import java.util.Random;

public class Auto extends Thread
{	private Monitor m;
	private int tipo;
	private Random r;
	int tempo;
	
	public  Auto(Monitor M, int TIPO){
		this.m =M;
		this.tipo=TIPO;
		this.r=new Random();
	}
	
	public void run()
	{ 	
		try{ 
			tempo=r.nextInt(10);
			sleep(tempo*1000);
			
			m.EntraA(tipo);
			
			tempo=r.nextInt(10);
			sleep(tempo*100);
		  
			m.EsceA(tipo);
		  
		}
		catch(InterruptedException e){}

	}
}