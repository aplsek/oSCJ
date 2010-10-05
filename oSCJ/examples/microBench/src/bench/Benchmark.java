package bench;

import micro.Constants;


public class Benchmark {


	static public int              maxDetectorRuns = 0;
	static public long[]            timesBefore;
	static public long[]            timesAfter;

	static public int              recordedRuns                 = 0;


	public Benchmark() {
	}
	
	public static void init() {
		maxDetectorRuns = Constants.RUNS+1;

		timesBefore = new long[maxDetectorRuns];
		timesAfter = new long[maxDetectorRuns];
		NanoClock.init();
	}

	public static void set(long start, long end) {
		timesBefore[recordedRuns] = start;
		timesAfter[recordedRuns] = end;
		recordedRuns++;
	}

	public static void stats() {
		long res = 0;
		for (int i = 0 ; i < recordedRuns ; i++) 
			res += timesAfter[i] - timesBefore[i];
		
		res = res % recordedRuns;
		
		System.out.println("Average execution time:" + res);
	}
	
	public static void dumpResults() {
		String space = " ";
		String triZero = " 0 0 0 ";

		System.out
		.println("Dumping output [ iterationNumber trace-point-number privateMemoryUsage missionMemoryUsage total-iter-Number] for "
				+ recordedRuns + " recorded detector runs, in ns");

		System.out.println("=====DETECTOR-STATS-START-BELOW====");

		for (int i = 0 ; i < recordedRuns-1 ; i++) {
			System.out.print(space);
			System.out.print(timesBefore[i]);
			System.out.print(space);
			System.out.print(timesAfter[i]);
			System.out.print(space);
			System.out.print(timesAfter[i] - timesBefore[i]);
			System.out.print(space);
			System.out.print(i);
			System.out.print("\n");
		}
		System.out.println("=====DETECTOR-STATS-END-ABOVE====");
		
		stats();
	}


}
