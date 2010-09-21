package bench;
import bench.NanoClock;
import Constants;



public class Benchmark {


	static public int              maxDetectorRuns;
	static public long[]            timesBefore;
	static public long[]            timesAfter;

	static public int              recordedRuns                 = 0;


	public Benchmark() {
		maxDetectorRuns = Constants.RUNS;

		timesBefore = new long[maxDetectorRuns];
		timesAfter = new long[maxDetectorRuns];
		
		NanoClock.init();
	}

	public static void set(long start, long end) {
		timesBefore[recordedRuns] = start;
		timesAfter[recordedRuns] = end;
		recordedRuns++;
	}

	public static void dumpResults() {
		String space = " ";
		String triZero = " 0 0 0 ";

		System.out
		.println("Dumping output [ iterationNumber trace-point-number privateMemoryUsage missionMemoryUsage total-iter-Number] for "
				+ recordedRuns + " recorded detector runs, in ns");

		System.out.println("=====DETECTOR-STATS-START-BELOW====");

		for (int i = 0 ; i < recordedRuns ; i++) {
			System.out.print(space);
			System.out.print(timesBefore[i]);
			System.out.print(space);
			System.out.print(timesAfter[i]);
			System.out.print(space);
			System.out.print(i);
		}
		System.out.println("=====DETECTOR-BENCH-STATS-END-ABOVE====");
	}
}
