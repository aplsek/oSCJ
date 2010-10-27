package list;

import bench.Benchmark;
import bench.Constants;

/**
 * 
 * Created by Ales Plsek, October 2010.
 * @author plsek
 *
 */

public class MyMain {
	
	 private static void main(String[] arg) {
		 Benchmark.init();
		 
		 List list = new List(Constants.MAX);
		 
		 for(int i = 0; i < Constants.RUNS;i++) {
			 long start = System.nanoTime();
			 list.invert();
			 long  end =   System.nanoTime();
	         Benchmark.set(start, end);
		 }
		 
		 Benchmark.dumpResults();
	 }
	 
}
