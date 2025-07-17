package sot26giu24A;

import java.util.Random;
public class Main {
			static int N = 5;
			static int NVis = 20;
			static int NDip = 6;
			
			
			
			public static void main(String[] args) {
				Random r = new Random(System.currentTimeMillis());
				Monitor monitor = new Monitor(N);
				Visitatore[] V = new Visitatore[NVis];
				Dipendente[] D = new Dipendente[NDip];
				Usciere U = new Usciere(monitor, r);
				
				
				
				for(int i=0;i<NVis;i++) {
					V[i] = new Visitatore(monitor, r);
				}
				for(int i=0;i<NDip;i++) {
					D[i] = new Dipendente(monitor, r);
				}
				for(int i=0;i<NVis;i++) {
					V[i].start();
				}
				for(int i=0;i<NDip;i++) {
					D[i].start();
				}
				U.start();		
			}
}
