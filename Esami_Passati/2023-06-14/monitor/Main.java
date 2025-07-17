
import java.util.Random;

public class Main {

	public static final int Pmax=20;
	
	public static final int NC=50; //numero totale cittadini
	public static final int NF=10;  // numero totale fornitori
	
	
	
	public static void main (String [] args){
		
		Cittadino [] C = new Cittadino[NC];
		Fornitore [] F = new Fornitore[NF];
		
		Random r=new Random(System.currentTimeMillis());
		Monitor M = new Monitor (Pmax);
		Addetto A = new Addetto(M, r);
		
		for (int i=0; i< NC; i++){
			
			C[i]=new Cittadino(r.nextInt(2), "CIT"+i ,M, r);
		}
		for (int i=0; i< NF; i++){
			
			F[i]=new Fornitore("FORN"+i ,M, r);
		}
		
		for (int i=0; i< NC; i++){
			
			C[i].start();
		}
		
		for (int i=0; i< NF; i++){
			
			F[i].start();
		}
		A.start();
		System.out.println("Main: ho creato " + NC + " cittadini e  "+NF+ " fornitori e un addetto.");
	}
	
	
	
}
