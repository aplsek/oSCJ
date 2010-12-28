package example;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


@Scope("example.MyApp")
@RunsIn("example.MyPEH") 
public class MyPEH extends PeriodicEventHandler {

        private int count_;
 
        static int pos, MAX = 20;
        static long[] times = new long[MAX];
        static PriorityParameters pri = new PriorityParameters(13);
        static PeriodicParameters per;
        
        static {
        	per = new PeriodicParameters(new RelativeTime(0,0),new RelativeTime(500,0));
        }
        
        public MyPEH(long psize, String name, int count) {
        	super(pri, per, null, psize, name);
        	count_ = count;
        }

        long mem1 = 0;
        long mem2 = 0;
        
        /**
         * 
         * Testing Enter Private Memory
         * 
         */
        public void handleEvent() {
        	mem1 = RealtimeThread.getCurrentMemoryArea().memoryConsumed();
        	//System.out.println("Mem consumed: " + mem);
        	
        	//times[pos++] = Clock.getRealtimeClock().getTime().getMilliseconds();  //TODO
        	
        	pos++;
        	if (pos == MAX)
        		Mission.getCurrentMission().requestSequenceTermination();
        	
        }

        
        
        public void cleanUp() {
        	System.out.println("Mem consumed: " + mem1);
        	System.out.println("Mem consumed: " + mem2);
        	
        	for(int i=0;i < 10;i++) {
        		System.out.println("Time: i:" + i + ", time: " + times[i]);
        	}
        }

    
        public StorageParameters getThreadConfigurationParameters() {
        	return null;
        }
}
