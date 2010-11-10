package bench;

import java.io.PrintStream;

import immortal.Constants;
import immortal.ImmortalEntry;


public class BenchMem {

    static public long             start = 0;
    
    static public final int        tracePoints  = 7;  // number of trace points per iteration
    static public final int        maxDetectorRuns = Constants.MAX_FRAMES;
    
    static public int              traces = 0;
    static public final int        maxTraces  = maxDetectorRuns * tracePoints + 2;
    
    static public long[]           memoryUsage          = new long[maxTraces];
    static public long[]           freeMem              = new long[maxTraces];
    static public long[]           privateMemoryUsage   = new long[maxTraces];
    
    static public long[]           missionMemUsage      = new long[maxTraces];
    static public int              missionIdnex = 0;
    static private Runtime         runtime = Runtime.getRuntime(); 
    
    /*
    public BenchMem() {
        maxDetectorRuns = Constants.MAX_FRAMES;
        maxTraces = maxDetectorRuns * tracePoints;
        memoryUsage = new long[maxTraces];
        traces = 0;
    }*/
    
    public static boolean err = false;
    
    
    /**
     * For regular JAva:
     * 
     */
    public static void memUsage() {
        
        if (traces < maxTraces ) {
            freeMem[traces]     =  runtime.freeMemory();
            memoryUsage[traces] =  runtime.totalMemory() - freeMem[traces];
            traces++;
		} else {
            //System.err.println("Memory Benchmark ERROR: Too many trace points!");
            traces++;
            err = true;
        }		 
    }
    
    /**
     * for SCJ 
     *  - stores Mission and Private Memory Areas usage 
     * 
     * @param mem
     */
    public static void setMemUsage(long mem) {
       
		if (traces < maxTraces ) {
		    //long missionMem = RealtimeThread.getOuterMemoryArea(0).memoryConsumed();
		    privateMemoryUsage[traces] = mem ;
		    //missionMemUsage[traces] = missionMem;
		    traces++;
        } else {
            //System.err.println("Memory Benchmark ERROR: Too many trace points!");
            traces++;
            err = true;
        }
		
    }
    
    public static void setMissionUsage(long mem) {
		
        if (missionIdnex < maxDetectorRuns ) {
            missionMemUsage[missionIdnex] = mem;
            missionIdnex++;
        } else {
            //System.err.println("Memory Benchmark ERROR: Too many trace points!");
            missionIdnex++;
            err = true;
        } 
    }
    
    public static void dumpMemoryUsage(PrintStream out) {
        if (err) {
            System.err.println("Memory Benchmark ERROR: Too many trace points! :" + traces);
            return;
        }
        
        out.println("Dumping output [Iteration PrivateMemoryUsage MissionMemoryUsage 0 TOTALmemory FreeSpace] for "
                        + ImmortalEntry.recordedRuns + " recorded detector runs");
        
        out.println("=====MEMORY-BENCH-STATS-START-BELOW====");
        
        for (int i = 0 ; i < traces ; i++) {
            out.print(i);  // total iter number
            //out.print(space);
            //out.print(i * Constants.DETECTOR_PERIOD * 1000000L + ImmortalEntry.detectorReleaseTimes[0]);
            out.print(" ");
            out.print(privateMemoryUsage[i]);
            out.print(" ");
            out.print(missionMemUsage[i]);
            out.print(" ");
            out.print(0);
            out.print(" ");
            out.print(memoryUsage[i]);
            out.print(" ");
            out.print(freeMem[i]);
            out.println(" ");
        }
        out.println("=====MEMORY-BENCH-STATS-END-ABOVE====");
        out.println("Dumping output [ Iteration PrivateMemoryUsage MissionMemoryUsage 0 TOTALmemory FreeSpace] for "
                + ImmortalEntry.recordedRuns + " recorded detector runs");
    }
}
