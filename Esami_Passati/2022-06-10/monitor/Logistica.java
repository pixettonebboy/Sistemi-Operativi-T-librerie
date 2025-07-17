import java.util.Random;

public class Logistica {
	public static void main(String[] args) {
		final int MaxD = 30;
		final int NF = 8;
		final int MaxNc = 24; //massimo valore di NC
		int numC = 20; //numero di camion
		int numF = 100; //numero di furgoni
		Camion[] camion = new Camion[numC];
		Furgone[] furgoni = new Furgone[numF];
		Monitor m = new Monitor(MaxD, NF);
		Random r = new Random(System.currentTimeMillis());
		
		for(int i = 0; i < numC; i++)
			camion[i] = new Camion(m, i%2, r.nextInt(MaxNc)+1, r); 
			

		for(int i = 0; i < numF; i++)
			furgoni[i] = new Furgone(m, i%2, r); 

		
		for(int i = 0; i < numC; i++)
			camion[i].start();
		for(int i = 0; i < numF; i++)
			furgoni[i].start();
		
		try {
			for(int i = 0; i < numC; i++)
				camion[i].join();
			
			for(int i = 0; i < numF; i++)
				furgoni[i].join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Terminazione main");
	}
}
