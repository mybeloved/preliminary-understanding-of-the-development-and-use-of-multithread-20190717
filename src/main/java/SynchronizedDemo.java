/**
 * @Program: preliminary-understanding-of-the-development-and-use-of-multithread-20190717
 * @Description:
 * @Author: whx
 * @Create: 2019-07-17 16:16
 **/
public class SynchronizedDemo implements Runnable {
	private int x = 100;
	
	private synchronized void m1() {
		x = 1000;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("x=" + x);
	}
	
	private synchronized void m2() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		x = 2000;
	}
	
	public static void main(String[] args) {
		SynchronizedDemo sd = new SynchronizedDemo();
		new Thread(sd::m1).start();
		new Thread(sd::m2).start();
		sd.m2();
		System.out.println("Main x=" + sd.x);
	}
	
	@Override
	public void run() {
		m1();
	}
}