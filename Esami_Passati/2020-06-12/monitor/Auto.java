package sol12giu2020.light;


public class Auto extends Thread {
	private final int IN=0;
	private final int OUT=1;
	private final int AUTO=0;
	private Monitor M;
	private int tipo=AUTO;	//0: auto, 1:ambulanza, 2: ambulanza con sirena
	
	
	public Auto(Monitor M) {
		
		this.M = M;
		
	}
	
	public void run() {
		
		try {
			
			
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
