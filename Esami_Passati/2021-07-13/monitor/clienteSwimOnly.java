package stabilimentoBalneare;

import java.util.Random;

public class clienteSwimOnly extends Thread {

		private monitor M;
		private Random r;
		private int id;
		


		public clienteSwimOnly(monitor M, Random R, int i) {

			this.M = M;
			this.r=R;
			this.id=i;
			

		}

		public void run() {
			
			try {
				sleep(r.nextInt(10*1000));
				M.entraSWO(id); // entra in piscina
				sleep(r.nextInt(10000)); //permanenza in piscina
				M.esceSWO(id);	//esce dalla piscina
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}

