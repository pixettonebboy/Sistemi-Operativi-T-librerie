package sol12giu2020.light;


public class Ambulanza extends Thread {
	
		private final int IN=0;
		private final int OUT=1;
		private final int AMB=1;
		private final int AMBS=2;
		private Monitor M;
		private int tipo;	//0: auto, 1:ambulanza, 2: ambulanza con sirena
		private boolean sirena;
		
		public  Ambulanza (boolean Sirena, Monitor M) {
			
			this.M = M;
			this.sirena=Sirena;
			
		}
		
		public void run() {
			
			try {
				if (sirena)
					tipo=AMBS;
				else
					tipo=AMB;
				
				sleep(1000);
				
				M.rampa_in(tipo, IN);
				
				sleep(2000);	
				
				M.rampa_out(tipo, IN);		
				
				sleep(3000);		
				
				M.rampa_in(tipo, OUT);
				
				sleep(2000);
				
				M.rampa_out(tipo, OUT);			
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

