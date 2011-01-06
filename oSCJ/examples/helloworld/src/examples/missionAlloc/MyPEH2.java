package examples.missionAlloc;

import java.util.Arrays;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import javax.realtime.Clock;
import javax.realtime.MemoryArea;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


@Scope("examples.missionAlloc.MyApp2")
@RunsIn("examples.missionAlloc.MyPEH2") 
public class MyPEH2 extends PeriodicEventHandler {

        static PriorityParameters pri;
        static PeriodicParameters per;
        static StorageParameters stor;
        
        static {
        	pri = new PriorityParameters(13);
        	per = new PeriodicParameters(new RelativeTime(0,0),new RelativeTime(500,0));
        	stor = new StorageParameters(1000L,1000L,1000L);
        }
        
        public MyPEH2(long[] t) {
        	super(pri, per, stor);
        	times = t;
        }

        
        
        int pos;
        long times[];
        public void handleAsyncEvent() {
        	times[pos++] = Clock.getRealtimeClock().getTime().getMilliseconds();  
        	if (pos == 1000)
        		Mission.getCurrentMission().requestSequenceTermination();
        	
        	Arrays.sort(times);
        }
        
        public void cleanUp() {
        }

    
        public StorageParameters getThreadConfigurationParameters() {
        	return null;
        }
}
