package bench;

import realtime.RealtimeThread;
import immortal.Constants;
import immortal.ImmortalEntry;


public class BenchMem {

    static public long             start = 0;
    
    static public int              tracePoints  = 7;  // number of trace points per iteration
    static public int              maxDetectorRuns = Constants.MAX_FRAMES;
    
    static public int              traces = 0;
    static public int              maxTraces  = maxDetectorRuns * tracePoints + 2;
    
    static public long[]           memoryUsage = new long[maxTraces];
    static public long[]           freeMem = new long[maxTraces];
    static public long[]           privateMemoryUsage = new long[maxTraces];
    
    static public long[]           missionMemUsage = new long[maxTraces];
    static public int              missionIdnex = 0;
    
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
            memoryUsage[traces] =  Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            freeMem[traces] = Runtime.getRuntime().freeMemory();
            
            traces++;
          }
          else {
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
        }
        else {
            //System.err.println("Memory Benchmark ERROR: Too many trace points!");
            traces++;
            err = true;
        }
		
    }
    
    public static void setMissionUsage(long mem) {
        
		
		if (missionIdnex < maxDetectorRuns ) {
            missionMemUsage[missionIdnex] = mem;
            missionIdnex++;
          }
          else {
              //System.err.println("Memory Benchmark ERROR: Too many trace points!");
              missionIdnex++;
              err = true;
          } 
		 
    }
    
    public static void dumpMemoryUsage() {
        if (err) {
            System.err.println("Memory Benchmark ERROR: Too many trace points! :" + traces);
            return;
        }
        
        String space = " ";
        String triZero = " 0 0 0 ";
        
            System.out
                .println("Dumping output [ iterationNumber trace-point-number privateMemoryUsage missionMemoryUsage total-iter-Number] for "
                        + ImmortalEntry.recordedRuns + " recorded detector runs, in ns");
        
        System.out.println("=====MEMORY-BENCH-STATS-START-BELOW====");
        
        for (int i = 0 ; i < traces ; i++) {
            System.out.print(i);  // total iter number
            //System.out.print(space);
            //System.out.print(i * Constants.DETECTOR_PERIOD * 1000000L + ImmortalEntry.detectorReleaseTimes[0]);
            System.out.print(space);
            System.out.print(privateMemoryUsage[i]);
            System.out.print(space);
            System.out.print(missionMemUsage[i]);
            System.out.print(space);
            System.out.print(memoryUsage[i]);
            System.out.print(space);
            System.out.print(freeMem[i]);
            System.out.println(space);
        }
        System.out.println("=====MEMORY-BENCH-STATS-END-ABOVE====");
        
        
       
        
        
        
    }
}
