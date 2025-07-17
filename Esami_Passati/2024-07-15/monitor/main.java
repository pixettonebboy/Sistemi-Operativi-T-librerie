public class main {
	public static void main(String args[]) {
		Monitor m = new Monitor();
		int clienti = 500;
		Cliente c[] = new Cliente[clienti];
		Addetto a = new Addetto(m);
		
		for(int i = 0; i < clienti; i++) 
			c[i] = new Cliente(m, i);
			
		a.start();
		
		for(int i = 0; i < clienti; i++) 
			c[i].start();
				
	}
}
