import java.util.Random;

public class PSO {

		private final static int MAXS = 15; // capacità sala
		private  final static int NA = 5; // ambulatori/medici
		
		private final static int UPED = 40; //utenti pediatrici (minorenni)
		private final static int UAD = 55; //utenti adulti
		
		public static void main(String[] args) {
			Random R=new Random(System.currentTimeMillis());
			
			Monitor M = new Monitor(MAXS, NA, R);
			Adulto [] UA = new Adulto[UAD];
			Minorenne [] UM = new Minorenne[UPED];
			
			for (int i = 0; i <UAD; i++)
			{	
				UA[i] = new Adulto(M,R);
			}
			
			for ( int i = 0; i < UPED; i++)
			{	
				UM[i] = new Minorenne(M,R);
			}
		
			System.out.println("APERTURA PS :ci sono "+UAD+" utenti adulti , e "+UPED+ " utenti Pediatrici per " + MAXS +" posti in sala e "+NA+ " ambulatori/medici\n\n");

			
			for (int i = 0; i < UAD; i++)
				UA[i].start();
			
			for ( int i = 0; i < UPED; i++)
				UM[i].start();
			
			}
	}
