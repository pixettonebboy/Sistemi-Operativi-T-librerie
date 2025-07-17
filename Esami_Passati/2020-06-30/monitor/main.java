package sol30giu2020;

public class main {


	    
	    public static void main(String[] args) {
	        System.out.println("Ponte mobile: prova scritta del 30 giugno 2020");
	       	int i;
	        final int NPRI=30; //numero effettivo auto private
	    	final int NPUB=50;  //numero effettivo auto pubbliche
	    	final int PRI=0; //tipo auto privata
	    	final int PUB=1;  //tipo auto pubblica
	    	final int NB=50;  //numero effettivo barche
	        Monitor M=new Monitor();
			
			Auto []APRI=new Auto[NPRI];
			Auto []APUB=new Auto[NPUB];
			Barca []B=new Barca[NB]; 
				
			
			
			for(i=0;i<NPRI ;i++)
				APRI[i]=new Auto(M,PRI);
			for(i=0;i<NPUB ;i++)
				APUB[i]=new Auto(M,PUB);
			for(i=0;i<NB ;i++)
					B[i]=new Barca(M);
					
			for(i=0;i<NPRI ;i++)
				APRI[i].start();
			for(i=0;i<NPUB ;i++)
				APUB[i].start();
	
			for(i=0;i<NB ;i++)
					B[i].start();
			
			
			

			 
	    
	    }
	}
