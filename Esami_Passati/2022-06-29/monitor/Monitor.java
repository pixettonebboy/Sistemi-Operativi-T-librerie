package esame220629;


import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private int CAPIENZA_MASSIMA;
	private int NG;
	private int NV;
	
	private Lock lock;
	
	private Condition codaBIN;
	private Condition codaAIN;
	private Condition codaGOUT;
	
	
	private int sospAIN;
	private int sospBIN;
	private int sospGOUT;
	
	private Random R;
	final static int BAM =0;
	final static int ADU =0;
	
	
	public Monitor(int CAPIENZA_MASSIMA, Random r) {
		this.CAPIENZA_MASSIMA = CAPIENZA_MASSIMA;
		this.R=r;
		NG=0;
		NV=0;
		
		sospAIN = 0;
		sospBIN = 0;
		sospGOUT = 0;
		
		lock = new ReentrantLock();
		
		codaGOUT = lock.newCondition();
		codaAIN = lock.newCondition();
		codaBIN = lock.newCondition();
	}
	
	
	public void EntraGuida() {
		lock.lock();
		try {
			NG++;
			System.out.println("Guida " + Thread.currentThread().getId() + " è entrata nella grotta");
			if (sospGOUT>0)
				codaGOUT.signal();
			if (sospAIN>0)
				codaAIN.signalAll();
			if (sospBIN>0)
				codaBIN.signalAll();
		}finally {
			lock.unlock();
		}
	}



	public void EsciGuida() {
		lock.lock();
		try {
			
			while( 5*(NG-1) < NV ) {
				sospGOUT++;
				codaGOUT.await();
				sospGOUT--;
			}
			NG--;
			System.out.println("Guida " + Thread.currentThread().getId() + " è uscita dalla grotta");
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	
	public void EntraV(int t) {
		lock.lock();
		try {
			if (t==ADU)
			{	while(	NV+1 > CAPIENZA_MASSIMA ||
						5* NG < (NV+1) ) {
					sospAIN++;
					codaAIN.await();
					sospAIN--;
				}
			System.out.println("Visitatore Adulto " + Thread.currentThread().getId() + " è entrato nella grotta");
			}
			else // bambino
			{	while(	NV+1 > CAPIENZA_MASSIMA ||
						5* NG < (NV+1) ||
						sospAIN>0 ) {
					sospBIN++;
					codaBIN.await();
					sospBIN--;
				}
				System.out.println("Visitatore Bambino " + Thread.currentThread().getId() + " è entrato nella grotta");
			}
			NV++;
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
	}

	public void EsceV(int t) {
		lock.lock();
		try {
			NV--;
			if (t== ADU) 
				System.out.println("Visitatore Adulto " + Thread.currentThread().getId() + " è uscito dalla grotta");
			else 
				System.out.println("Visitatore Bambino " + Thread.currentThread().getId() + " è uscito dalla grotta");
			if(sospAIN > 0) {
				codaAIN.signal();
			}else if(sospBIN > 0) {
				codaBIN.signal();
			}
			if(sospGOUT>0 &&  (5*NG >= NV-1 )) {
				codaGOUT.signal();
			}
		}finally {
			lock.unlock();
		}
		// TODO Auto-generated method stub
		
	}
}
