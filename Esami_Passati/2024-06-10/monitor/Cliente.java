import java.util.Random;
public class Cliente extends Thread{
		private final int MONO=0;
		private final int FULL=1;
		private Monitor M;
		private Random R;
		private int T;
		
		public Cliente(Monitor monitor, int tipo, Random r) {
			this.M=monitor;
			this.T=tipo;
			this.R=r;
		}

		public void run() {
			int decisione, area;
			try{
				if (T==MONO)
				{	decisione=R.nextInt(2);
					if (decisione==1) //solo tunnel
					{	M.EntraT(T);
						sleep(R.nextInt(3000));
						M.EsceT(T);
					} 
					else //solo AP
					{	area=M.acqAP(T);
						sleep(R.nextInt(3000));
						M.liberaAP(area,T);
					}
				}
				else //FULL
				{	M.EntraT(T);
					sleep(R.nextInt(3000));
					M.EsceT(T);
					sleep(R.nextInt(3000));
					area=M.acqAP(T);
					sleep(R.nextInt(3000));
					M.liberaAP(area,T);
				}
					
			} catch (InterruptedException e) { }
		}

	}

