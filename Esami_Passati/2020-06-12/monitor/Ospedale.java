package sol12giu2020.light;

import java.util.Random;

public class Ospedale {

	private final static int N_POSTI = 12;
	private final static int N_AUTO = 40;
	private final static int N_AMBULANZE = 20;
	
	public static void main(String args[]) {
		Random r=new Random(System.currentTimeMillis());
		Monitor M = new Monitor(N_POSTI);
		Auto[] auto = new Auto[N_AUTO];
		Ambulanza[] ambulanza = new Ambulanza[N_AMBULANZE];
		boolean sirena;
		
		for (int i = 0; i < N_AUTO; i++)
			auto[i] = new Auto(M);
		
		for ( int i = 0; i < N_AMBULANZE; i++)
		{	sirena= r.nextBoolean();	
			ambulanza[i] = new Ambulanza(sirena, M);
		}
		
		System.out.print(M.toString());
		
		for (int i = 0; i < N_AUTO; i++)
			auto[i].start();
		
		for ( int i = 0; i < N_AMBULANZE; i++)
			ambulanza[i].start();
		
		
		
	}
}
