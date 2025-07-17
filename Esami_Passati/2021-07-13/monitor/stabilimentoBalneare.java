package stabilimentoBalneare;

import java.util.Random;

public class stabilimentoBalneare {
	private final static int N_OMB = 10; // Ombrelloni disponibili
	private  final static int N_P = 8; // capacità piscina
	private final static int nFULL = 30; //clienti FULL
	private final static int nSWONLY = 15; //clienti SWIM_ONLY
	
	public static void main(String[] args) {
		Random R=new Random(System.currentTimeMillis());
		
		monitor M = new monitor(N_OMB, N_P);
		clienteSwimOnly [] CS = new clienteSwimOnly[nSWONLY];
		clienteFull [] CF = new clienteFull[nFULL];
		int tipo;
		
		for (int i = 0; i <nFULL; i++)
		{	tipo=R.nextInt(2); //0 --> abbonato; 1 --> occasionale
			CF[i] = new clienteFull(M,R,tipo,i);
		}
		
		for ( int i = 0; i < nSWONLY; i++)
		{	
			CS[i] = new clienteSwimOnly(M,R,i);
		}
	
		System.out.println("APERTURA STABILIMENTO :ci sono "+nFULL+" clienti FULL prenotati , e "+nSWONLY+ " clienti SWIM_ONLY\n");
		System.out.println("capacità piscina: "+N_P+"; ombrelloni disponibili: "+ N_OMB +".\n\n");

		
		for (int i = 0; i <nFULL; i++)
			CF[i].start();
		
		
		for ( int i = 0; i < nSWONLY; i++)
			CS[i].start();
		
}

}
