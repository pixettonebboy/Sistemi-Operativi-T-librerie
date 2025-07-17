package sol30giu2020;

import java.util.concurrent.locks.*;

public class Monitor
{ 

//Costanti:
private final int MAX=10;
private final int PRI=0; //tipo auto privata
private final int PUB=1;  //tipo auto pubblica

//Dati:
private int veicoli; //num veicoli sul ponte
private int navi;	// num navi nel tratto





private Lock lock= new ReentrantLock();
private Condition []codaA= new Condition[2];
private int []sospA=new int[2]; // auto sospese

private Condition codaB=lock.newCondition();  // coda imbarcazioni
private int sospB; // navi sospese;

private boolean abbassato;

//Costruttore:
public Monitor( ) {
	int i;
	veicoli=0;
	navi=0;
	abbassato=true;
	
	for(i=0; i<2; i++) {
		codaA[i]=lock.newCondition();
		sospA[i]=0;
	}
	sospB=0;
	System.out.print("\nIl ponte è inizalmente abbassato: le auto possono passare..\n");
}

//metodi "entry":

public void EntraB()throws InterruptedException
{	lock.lock();
	try
  { 	
  		
  		while (veicoli>0)  // ci sono veicoli sul ponte
  		{ 		sospB++;
  				System.out.print("Barca aspetta: "+ veicoli+" veicoli; "+navi+" navi\n");
       			codaB.await();
       			sospB--;
       	}
       	if (abbassato)
       	{	abbassato=false;
       		System.out.print("\nIl ponte si alza per far passare le barche!\n");
       	}
       	navi++; 
       	System.out.print("Barca entra: "+ veicoli+" veicoli; "+navi+" navi\n");
	} finally{ lock.unlock();}
	return;
}

public void EntraA(int tipo)throws InterruptedException
{	lock.lock();
	try
	{ 	
  			while ( navi>0 || //stanno passando delle navi
  					veicoli==MAX ||
  					sospB>0 ||
  					(tipo==PRI && sospA[PUB]>0))
  			{ 		sospA[tipo]++;
  					codaA[tipo].await();
       				sospA[tipo]--;
  			}
  			if (!abbassato)
  	       	{	abbassato=true;
  	       		System.out.print("\nIl ponte si abbassa per far passare le auto!\n");
  	       	}
  			veicoli++; 
  			System.out.print("auto di tipo "+tipo+" entra:  "+ veicoli+" veicoli; "+navi+" navi\n");
  	} finally{ lock.unlock();}
	return;
}

public void EsceB()throws InterruptedException
{ 	lock.lock();
	try
	{	navi--;
		System.out.print("Barca esce "+ navi +" navi..\n");
		if (navi==0 && sospB==0)
		{	if (sospA[PUB]>0)	
				codaA[PUB].signalAll();
			if (sospA[PRI]>0)	
				codaA[PRI].signalAll();
		}
	} finally{  lock.unlock();}
}


public void EsceA(int tipo)throws InterruptedException
{ 	lock.lock();
	try
	{	
		veicoli--;
		System.out.print("auto di tipo "+tipo+" esce:  "+ veicoli+" veicoli; "+navi+" navi\n");
		  
		if (sospB>0 && veicoli==0) //ponte vuoto e barche in coda
			codaB.signalAll();
		else if (sospB==0) //ponte non vuoto e non ci sono barche in attesa
				if (sospA[PUB]>0) 
					codaA[PUB].signal();
				else if  (sospA[PRI]>0)
					codaA[PRI].signal();
	} finally{  lock.unlock();}
}






}

