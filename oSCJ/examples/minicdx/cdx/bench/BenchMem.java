package bench;
import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import cdx.Constants;
import cdx.ImmortalEntry;


public class BenchMem {

    static private int              OVERRUN = 2;
    
    static public int              tracePoints  = 7;  // number of trace points per iteration
    static public int              maxDetectorRuns = Constants.MAX_FRAMES;
    
    static public int              traces = 0;
    static public int              maxTraces  = maxDetectorRuns * tracePoints + OVERRUN;
    
    static public long[]           privateMemUsage = new long[maxTraces];
    static public long[]           missionMemUsage = new long[maxTraces];
    static public long[]           immortalMemUsage = new long[maxTraces];
    
    static public long[]           limit = new long[maxTraces];
    
    
    static public int              missionIdnex = 0;
    
    /*
    public BenchMem() {
        maxDetectorRuns = Constants.MAX_FRAMES;
        maxTraces = maxDetectorRuns * tracePoints;
        memoryUsage = new long[maxTraces];
        traces = 0;
    }*/
    
    public static boolean err = false;
    
    public static ImmortalMemory immortal = ImmortalMemory.instance();
    
    public static void setMemUsage(long mem) {
        if (traces < maxTraces ) {
          long missionMem = RealtimeThread.getOuterMemoryArea(1).memoryConsumed();
          privateMemUsage[traces] = mem ;
          missionMemUsage[traces] =   missionMem;
          immortalMemUsage[traces] =  immortal.memoryConsumed();
           
          if (mem == 0)
              limit[traces] = Constants.PERSISTENT_DETECTOR_SCOPE_SIZE;
          else
              limit[traces] = Constants.PERSISTENT_DETECTOR_SCOPE_SIZE + Constants.TRANSIENT_DETECTOR_SCOPE_SIZE;
          //System.out.println("Mem usage total: " + (Runtime.getRuntime().totalMemory() -
          //    Runtime.getRuntime().freeMemory()) );
          
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
        
        if (Constants.PRINT_RESULTS) {
            System.out
                .println("Dumping output [ iterationNumber trace-point-number privateMemoryUsage missionMemoryUsage total-iter-Number] for "
                        + ImmortalEntry.recordedRuns + " recorded detector runs, in ns");
        }
        
        System.out.println("=====MEMORY-BENCH-STATS-START-BELOW====");
        
        for (int i = 0 ; i < traces - OVERRUN ; i++) {
            System.out.print(i);
            //System.out.print(space);
            //System.out.print(i * Constants.DETECTOR_PERIOD * 1000000L + ImmortalEntry.detectorReleaseTimes[0]);
            System.out.print(space);
            System.out.print(privateMemUsage[i]);
            System.out.print(space);
            System.out.print(missionMemUsage[i]);
            System.out.print(space);
            System.out.print(immortalMemUsage[i]);
            System.out.print(space);
            System.out.print(limit[i]);
            System.out.println(space);
        }
        System.out.println("=====MEMORY-BENCH-STATS-END-ABOVE====");
        
    }
}
