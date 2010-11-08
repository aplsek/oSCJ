package heap;

import immortal.Benchmarker;
import immortal.Constants;
import immortal.ImmortalEntry;
import immortal.NanoClock;

import java.io.PrintStream;

import bench.BenchMem;

/** Real-time Java runner for the collision detector. */
public class Main {

	public static boolean PRINT_RESULTS = true;
	public static Object junk;	
    
	public static void main(final String[] w) throws Throwable {
	    Benchmarker.initialize();
	    Benchmarker.set(Benchmarker.RAPITA_BENCHMARK);
	    parse(w);
	    
	    //System.out.println("start22222 ammount of mem is : " + Runtime.getRuntime().totalMemory() );
	    //System.out.println("start22222 ammount of mem is : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
       
	    BenchMem.start = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
       
	    NanoClock.init();
	    ImmortalEntry immortalEntry=new ImmortalEntry();
	    immortalEntry.run();
	    Benchmarker.set(Benchmarker.RAPITA_DONE);
	    dumpResults();
	   //System.out.println("END: ammount of mem is : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())); 
	}
	
	public static void dumpResults() {

		if (PRINT_RESULTS) {
			System.out
			.println("Dumping output [ timeBefore timeAfter heapFreeBefore heapFreeAfter detectedCollisions ] for "
					+ ImmortalEntry.recordedRuns
					+ " recorded detector runs, in ns");
		}
		
		PrintStream out = System.out;
		
        out.println("Program Starts: " 
                + NanoClock.asString(ImmortalEntry.progStartTime));
        out.println("Program Ends: " 
                + NanoClock.asString(ImmortalEntry.progEndTime));
        out.println("Number of Planes: " 
                + Constants.NUMBER_OF_PLANES);
        out.println("Detector Period: " 
                + Constants.DETECTOR_PERIOD);
        out.println("Generated frames: " 
                + Constants.MAX_FRAMES);
		out.println("Received (and measured) frames: " 
		        + ImmortalEntry.recordedRuns);
		out.println("Frame not ready event count (in detector): " 
		        + ImmortalEntry.frameNotReadyCount);
		out.println("Frames dropped due to full buffer in detector: " 
		        + ImmortalEntry.droppedFrames);
		out.println("Frames processed by detector: "
		        + ImmortalEntry.framesProcessed);
		out.println("Detector stop indicator set: "
		        + ImmortalEntry.persistentDetectorScopeEntry.stop);
		out.println("Reported missed detector periods (reported by waitForNextPeriod): "
		        + ImmortalEntry.reportedMissedPeriods);
		out.println("Detector first release was scheduled for: "
		        + NanoClock.asString(ImmortalEntry.detectorFirstRelease));
		// heap measurements
		Simulator.dumpStats();


		out.println("=====DETECTOR-STATS-START-BELOW====");	
		for (int i = 0; i < ImmortalEntry.recordedRuns; i++) {
		    out.print(NanoClock.asString(ImmortalEntry.timesBefore[i]));
			out.print(" "); 
			out.print(NanoClock.asString(ImmortalEntry.timesAfter[i])); 
			out.print(" ");
			out.print(ImmortalEntry.heapFreeBefore[i]);
			out.print(" ");
			out.print(ImmortalEntry.heapFreeAfter[i]);
			out.print(" ");
			out.print(ImmortalEntry.detectedCollisions[i]);
			out.print(" ");
			out.print(ImmortalEntry.suspectedCollisions[i]);
			out.print(" 0 0 0 "); 
            out.print(i);
            out.println();
		}
		out.println("=====DETECTOR-STATS-END-ABOVE====");
		
		// detector release times
		out.println("=====DETECTOR-RELEASE-STATS-START-BELOW====");	
		for (int i = 0; i < ImmortalEntry.recordedDetectorReleaseTimes; i++) {
		    out.print(NanoClock.asString(ImmortalEntry.detectorWaitTimes[i]));
		    out.print(" ");
		    out.print(NanoClock.asString(ImmortalEntry.detectorReleaseTimes[i]));
		    out.print(" ");
		    out.print(NanoClock.asString(i * immortal.Constants.DETECTOR_PERIOD * Constants.NANOS_PER_MILLIS
		            + ImmortalEntry.detectorReleaseTimes[0]));
		    out.print(" ");
		    out.print(ImmortalEntry.detectorReportedMiss[i] ? "1" : "0");
		    out.print(" ");
		    out.print(i);
		    out.println();
		}
		out.println("=====DETECTOR-RELEASE-STATS-END-ABOVE====");	
		
		BenchMem.dumpMemoryUsage(out);
	}

	private static void parse(final String[] v) {
		for (int i = 1; i < v.length; i++) {
			if (v[i].equals("PERSISTENT_DETECTOR_SCOPE_SIZE")) { /* flags with parameters */
				Constants.PERSISTENT_DETECTOR_SCOPE_SIZE = Long
				.parseLong(v[i + 1]);
				i++;
			} else if (v[i].equals("PERIOD")) {
				Constants.DETECTOR_PERIOD = Long.parseLong(v[i + 1]);
				i++;
			} else if (v[i].equals("TRANSIENT_DETECTOR_SCOPE_SIZE")) {
				Constants.TRANSIENT_DETECTOR_SCOPE_SIZE = Long
				.parseLong(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_PRIORITY")) {
				//Constants.DETECTOR_PRIORITY = Integer.parseInt(v[i + 1]);
				//Constants.DETECTOR_STARTUP_PRIORITY = Constants.DETECTOR_PRIORITY - 1;
				i++;
			} else if (v[i].equals("SIMULATOR_PRIORITY")) {
				Constants.SIMULATOR_PRIORITY = Integer.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("NOISE_RATE")) {
				Constants.NOISE_RATE = Integer.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("MAX_FRAMES")) {
				Constants.MAX_FRAMES = Integer.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("TIME_SCALE")) {
				Constants.TIME_SCALE = Integer.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("BUFFER_FRAMES")) {
				Constants.BUFFER_FRAMES = Integer.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("FPS")) {
				Constants.FPS = Integer.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_NOISE_REACHABLE_POINTERS")) {
				Constants.DETECTOR_NOISE_REACHABLE_POINTERS = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_NOISE_ALLOCATION_SIZE")) {
				Constants.DETECTOR_NOISE_ALLOCATION_SIZE = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_NOISE_ALLOCATE_POINTERS")) {
				Constants.DETECTOR_NOISE_ALLOCATE_POINTERS = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_NOISE_MIN_ALLOCATION_SIZE")) {
				Constants.DETECTOR_NOISE_MIN_ALLOCATION_SIZE = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_NOISE_MAX_ALLOCATION_SIZE")) {
				Constants.DETECTOR_NOISE_MAX_ALLOCATION_SIZE = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_NOISE_ALLOCATION_SIZE_INCREMENT")) {
				Constants.DETECTOR_NOISE_ALLOCATION_SIZE_INCREMENT = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("DETECTOR_STARTUP_OFFSET_MILLIS")) {
				Constants.DETECTOR_STARTUP_OFFSET_MILLIS = Integer
				.parseInt(v[i + 1]);
				i++;
			} else if (v[i].equals("SPEC_NOISE_ARGS")) {
				Constants.SPEC_NOISE_ARGS = v[i + 1];
				i++;
            } else if (v[i].equals("PRINT_RESULTS")) {
                PRINT_RESULTS=true;
            } else if (v[i].equals("DETECTOR_STATS")) {
                Constants.DETECTOR_STATS="true";
            } else if (v[i].equals("DETECTOR_RELEASE_STATS")) {
                Constants.DETECTOR_RELEASE_STATS="true";
			} else if (v[i].equals("SYNCHRONOUS_DETECTOR")) { /*
			 * flags without
			 * a parameter
			 */
				Constants.SYNCHRONOUS_DETECTOR = true;
			} else if (v[i].equals("PRESIMULATE")) {
				Constants.PRESIMULATE = true;
			} else if (v[i].equals("FRAMES_BINARY_DUMP")) {
				Constants.FRAMES_BINARY_DUMP = true;
			} else if (v[i].equals("DEBUG_DETECTOR")) {
				Constants.DEBUG_DETECTOR = true;
			} else if (v[i].equals("DUMP_RECEIVED_FRAMES")) {
				Constants.DUMP_RECEIVED_FRAMES = true;
			} else if (v[i].equals("DUMP_SENT_FRAMES")) {
				Constants.DUMP_SENT_FRAMES = true;
			} else if (v[i].equals("USE_SPEC_NOISE")) {
				Constants.USE_SPEC_NOISE = true;
			} else if (v[i].equals("SIMULATE_ONLY")) {
				Constants.SIMULATE_ONLY = true;
			} else if (v[i].equals("DETECTOR_NOISE")) {
				Constants.DETECTOR_NOISE = true;
			} else if (v[i].equals("DETECTOR_NOISE_VARIABLE_ALLOCATION_SIZE")) {
				Constants.DETECTOR_NOISE_VARIABLE_ALLOCATION_SIZE = true;
			} else if (v[i].equals("NUMBER_OF_PLANES")) {
				Constants.NUMBER_OF_PLANES = Integer.parseInt(v[i + 1]);
				i++;
			}

			else
				throw new Error("Unrecognized option: " + v[i]);
		}
	}
}
