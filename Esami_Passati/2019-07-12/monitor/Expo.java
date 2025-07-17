import java.util.Random;


public class Expo {

	public static void main(String[] args) {
		
		final int MAXF = 4; 
		final int MAXM = 5;
		final int NMP = 5;
		final int TOTVIS = 20;
		
		Monitor m = new Monitor(MAXF, MAXM, NMP);
		Random r = new Random(System.currentTimeMillis());				
		Visitatore []v = new Visitatore[TOTVIS];
		
		for (int i = 0; i < TOTVIS; i++)
			v[i] = new Visitatore(r, m);	
		
		for (int i = 0; i < TOTVIS; i++)
			v[i].start();
		}
}
