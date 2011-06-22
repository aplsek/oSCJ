package bench;
import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import javax.safetycritical.annotate.SCJAllowed;
import cdx.Constants;
import cdx.ImmortalEntry;

@SCJAllowed(members=true)
public class BenchMem {

    static private int             OVERRUN = 2;
    
    static public int              tracePoints  = 7;  // number of trace points per iteration
    static public int              maxDetectorRuns = Constants.MAX_FRAMES;
    
    static public int              traces = 0;
    static public int              maxTraces  = maxDetectorRuns * tracePoints + OVERRUN;
    
    static public long[]           privateMemUsage = new long[maxTraces];
    static public long[]           missionMemUsage = new long[maxTraces];
    static public long[]           immortalMemUsage = new long[maxTraces];
    static public long[]           runtimeMem = new long[maxTraces];
    
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
    
    //public static ImmortalMemory immortal = ImmortalMemory.instance();
    
    
    static private long immortalConsumed    =    0;
    static private long heapConsumed        =    0;
    
    public static void init() {
        immortalConsumed = ImmortalMemory.instance().memoryConsumed();
        //heapConsumed = Runtime.getRuntime().totalMemory();
    }
    
    public static void setMemUsage(long mem) {
        /*
		if (traces < maxTraces ) {
          long missionMem = RealtimeThread.getOuterMemoryArea(1).memoryConsumed();
          privateMemUsage[traces] = mem ;
          missionMemUsage[traces] =   missionMem;
          
          immortalMemUsage[traces] =  immortal.memoryConsumed();
          runtimeMem[traces]  = Runtime.getRuntime().totalMemory();
          
          traces++;
        }
        else {
            //System.err.println("Memory Benchmark ERROR: Too many trace points!");
            traces++;
            err = true;
        }
		 */
    }
    
    
    
    public static void setMissionUsage(long mem) {
        
		if (missionIdnex < maxDetectorRuns ) {
            missionMemUsage[missionIdnex] = mem;
            missionIdnex++;
          }
          else {
              missionIdnex++;
              err = true;
          } 
		 
    }
    
    public static void dumpMemoryUsage() {
        /*
        if (err) {
            System.err.println("Memory Benchmark ERROR: Too many trace points! :" + traces);
            return;
        }
        
        String space = " ";
        String triZero = " 0 0 0 ";
        
        if (Constants.PRINT_RESULTS) {
            System.out
                .println("Dumping output [ iterationNumber privateMemoryUsage missionMemoryUsage immortalMem  scopeSizes runtimeMem] for "
                        + ImmortalEntry.recordedRuns + " recorded detector runs, in bytes");
        }
        
        System.out.println("=====MEMORY-BENCH-STATS-START-BELOW====");
        
      
        
        for (int i = 0 ; i < traces - OVERRUN ; i++) {
           
        	long total = privateMemUsage[i] + missionMemUsage[i] + immortalMemUsage[i] + runtimeMem[i];
        	
        	System.out.print(i);
            System.out.print(space);
            System.out.print(privateMemUsage[i]);
            System.out.print(space);
            System.out.print(missionMemUsage[i]);
            //IMMORTAL :
            System.out.print(space);
            System.out.print(immortalMemUsage[i]);
         
            System.out.print(space);
            System.out.print(total);
            System.out.print(space);
            
            // printing the size of MemoryArea Scopes - depending on if we have entered the private memory or not
            if (privateMemUsage[i] == 0)
                System.out.print(Constants.PERSISTENT_DETECTOR_SCOPE_SIZE);
            else
                System.out.print((Constants.PERSISTENT_DETECTOR_SCOPE_SIZE + Constants.TRANSIENT_DETECTOR_SCOPE_SIZE));
            
            // HEAP:
            System.out.print(space);
            System.out.print(runtimeMem[i]);
            System.out.println(space);
        }
        System.out.println("=====MEMORY-BENCH-STATS-END-ABOVE====");
        System.out
        .println("Dumping output [ iterationNumber privateMemoryUsage missionMemoryUsage immortalMem totalMemory scopeSizes runtimeMem] for "
                + ImmortalEntry.recordedRuns + " recorded detector runs, in bytes");
        
        
        System.out.println("Heap :" + heapConsumed);
        System.out.println("Imm :" + immortalConsumed);
        */
    }
}
