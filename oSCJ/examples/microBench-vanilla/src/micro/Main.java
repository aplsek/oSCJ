package micro;
import bench.Benchmark;
import com.fiji.fivm.ThreadPriority;

public class Main {

	static long startTime = 0;
	
	
	public static void main(final String[] w) {
		System.out.println("running");
		
		MicroBenchVanilla vanilla = new MicroBenchVanilla();
		Benchmark.init();
		
		//Thread.setPriority(ThreadPriority.FIFO_MAX);
		
		//try {
		//	Thread.sleep(5000);
		//} catch (InterruptedException e) {
		//	// TODO Auto-generated catch block
		///	e.printStackTrace();
		//}
		
		
		startTime = System.nanoTime();
		
		System.out.println("startime is: " + startTime);
		 
		for (int i=0 ; i < Constants.RUNS;i ++) {
			boolean missed=waitForNextPeriod();
			vanilla.handleEvent();
		}
		
		Benchmark.dumpResults();
	}
	
	
	
	public static boolean waitForNextPeriod() {

		long period = Constants.PERIOD;

		if (period < 0) {
			return true;
		}

		long now = System.nanoTime();
		long tosleep = period
				- ((now - startTime) % period);

		try {
			long tmilis = tosleep / 1000000;
			int tnanos = (int) (tosleep - tmilis * 1000000);
			Thread.sleep(tmilis, tnanos);
		} catch (InterruptedException iex) {
		}

		return true;
	}
	
}
