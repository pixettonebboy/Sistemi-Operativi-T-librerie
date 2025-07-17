import java.util.Random;
public class Main {
			static int AreeAP = 10;
			static int ClientiMS = 10;
			static int ClientiFS = 10;
			
			final static int MS =0;
			final static int FS =1;
			
			public static void main(String[] args) {
				Random r = new Random(System.currentTimeMillis());
				Monitor monitor = new Monitor(AreeAP, r);
				Cliente[] CFS = new Cliente[ClientiFS];
				Cliente[] CMS = new Cliente[ClientiMS];
				Addetto A = new Addetto(monitor, r);
				
				
				
				for(int i=0;i<ClientiMS;i++) {
					CMS[i] = new Cliente(monitor, MS, r);
				}
				
				for(int i=0;i<ClientiFS;i++) {
					CFS[i] = new Cliente(monitor, FS, r);
				}
				for(int i=0;i<ClientiMS;i++) {
					CMS[i].start();
				}
				
				for(int i=0;i<ClientiFS;i++) {
					CFS[i].start();
				}
				A.start();
				}
			}


