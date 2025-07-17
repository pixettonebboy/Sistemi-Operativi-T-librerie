package sol15lug2020;


public class main {

	public static void main(String[] args) {
		final int MAX_GC = 10;
		final int MAX_MG = 12;
		final int MAX = 15;
		
		int n_clienti = 200;
		
		Cliente[] C = new Cliente[n_clienti];
		Monitor M = new Monitor(MAX_GC, MAX_MG, MAX);
		Addetto A = new Addetto(M);
		
		for (int i= 0; i<n_clienti; i++)
			C[i] = new Cliente(M);
		
		A.start();
		for (int i=0; i<n_clienti; i++)
			C[i].start();
	}

}
