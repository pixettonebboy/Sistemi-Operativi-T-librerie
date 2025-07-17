package esame11lug2022;
import java.util.Random;

public class Main {

	public static final int MAXN=10;
	public static final int MAXM=7;
	public static final int NP =12;
	public static final int NG = 7;
	public static final int NM =3;
	public static final int PICCOLA=0;
	public static final int GRANDE=1;
	
	
	public static void main (String [] args){
		Monitor M = new Monitor (MAXM, MAXN);
		MotoVedetta [] motovedette = new MotoVedetta[NM];
		Barca [] BG = new Barca[NG];
		Barca [] BP = new Barca[NP];
		Random r=new Random(System.currentTimeMillis());
		
		for (int i=0; i< NM; i++){
			motovedette[i]=new MotoVedetta("MV" +i, M, r);
		}
		
		for (int i=0; i< NG; i++){
			BG[i]=new Barca(GRANDE, "BG" +i, M, r);
		}
		for (int i=0; i< NP; i++){
			BP[i]=new Barca(PICCOLA, "BP" +i, M, r);
		}
		
	
		for (int i=0; i< NM; i++){
			motovedette[i].start();
		}
		
		for (int i=0; i< NG; i++){
			BG[i].start();
		}
		for (int i=0; i< NP; i++){
			BP[i].start();
		}
	}
	
}
