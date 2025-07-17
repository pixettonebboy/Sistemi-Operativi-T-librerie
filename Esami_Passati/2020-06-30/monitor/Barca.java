package sol30giu2020;
import java.util.Random;

public class Barca extends Thread
{	Monitor m;
	private Random r;
	int tempo;
	
	public  Barca(Monitor M){
		this.m =M;
		this.r=new Random();
	}
	
	public void run()
	{ 	
		try{ 
		  tempo=r.nextInt(10);
		  sleep(tempo*100);
		  
		  m.EntraB();
		  
		  tempo=r.nextInt(10);
		  sleep(tempo*100);
		  
		  m.EsceB();
		  
		}
		catch(InterruptedException e){}

	}
}