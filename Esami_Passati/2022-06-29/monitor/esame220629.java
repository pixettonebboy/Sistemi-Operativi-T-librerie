package esame220629;

import java.util.Random;

public class esame220629 {
	
		static int CAPIENZA_MASSIMA = 10;
		static int VISITATORI_ADULTI = 10;
		static int VISITATORI_BAMBINI = 10;
		static int GUIDE = 3;
		final static int BAM =0;
		final static int ADU =0;
		
		public static void main(String[] args) {
			Random r = new Random(System.currentTimeMillis());
			Monitor monitor = new Monitor(CAPIENZA_MASSIMA, r);
			Visitatori[] visitatoriAdulti = new Visitatori[VISITATORI_ADULTI];
			Visitatori[] visitatoriBambini = new Visitatori[VISITATORI_BAMBINI];
			Guide[] guide = new Guide[GUIDE];
			
			
			for(int i=0;i<GUIDE;i++) {
				guide[i] = new Guide(monitor, r);
				guide[i].start();
			}
			
			for(int i=0;i<VISITATORI_ADULTI;i++) {
				visitatoriAdulti[i] = new Visitatori(monitor, ADU,r);
			}
			
			for(int i=0;i<VISITATORI_BAMBINI;i++) {
				visitatoriBambini[i] = new Visitatori(monitor, BAM,r);
			}
			for(int i=0;i<VISITATORI_ADULTI;i++) {
				visitatoriAdulti[i].start();
			}
			
			for(int i=0;i<VISITATORI_BAMBINI;i++) {
				visitatoriBambini[i].start();
			}
		}
	}
