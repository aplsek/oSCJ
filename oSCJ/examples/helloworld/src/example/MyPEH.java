package example;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import javax.realtime.Clock;
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

        static int pos, MAX = 20;
        static long[] times = new long[MAX];
        static PriorityParameters pri;
        static PeriodicParameters per;
        static StorageParameters stor;
        
        static {
        	pri = new PriorityParameters(13);
        	per = new PeriodicParameters(new RelativeTime(0,0),new RelativeTime(500,0));
        	stor = new StorageParameters(50L,0,0);
        }
        
        public MyPEH() {
        	super(pri, per, stor);
        }

        public void handleAsyncEvent() {
        	times[pos++] = Clock.getRealtimeClock().getTime().getMilliseconds();  
        	if (pos == MAX)
        		Mission.getCurrentMission().requestSequenceTermination();
        }
        
        public void cleanUp() {
        	//System.out.println("Mem consumed: " + mem1);
        	//System.out.println("Mem consumed: " + mem2);
        	
        	//for(int i=0;i < 10;i++) {
        	//	System.out.println("Time: i:" + i + ", time: " + times[i]);
        	//}
        }

    
        public StorageParameters getThreadConfigurationParameters() {
        	return null;
        }
}
