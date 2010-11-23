package comp.cdx;

public class Main implements Runnable {

	@SuppressWarnings("static-access")
	public void run() {
		System.out.println("Main thread in sleeping state...");
		try {
			Thread.currentThread().sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// BENCH RESULTS DUMP
		Results.dumpResults();
	}

}
