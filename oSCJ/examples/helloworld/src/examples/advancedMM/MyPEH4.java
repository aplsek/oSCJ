package examples.advancedMM;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import javax.realtime.MemoryArea;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


@Scope("examples.missionAlloc.MyApp4")
@RunsIn("examples.missionAlloc.MyPEH4") 
public class MyPEH4 extends PeriodicEventHandler {

        static PriorityParameters pri;
        static PeriodicParameters per;
        static StorageParameters stor;
        
        static {
        	pri = new PriorityParameters(13);
        	per = new PeriodicParameters(new RelativeTime(0,0),new RelativeTime(500,0));
        	stor = new StorageParameters(1000L,1000L,1000L);
        }
        
        public MyPEH4() {
        	super(pri, per, stor);
        }

        Tick tock;
        
        public void handleAsyncEvent() {
        	try {
        		ManagedMemory m = (ManagedMemory) MemoryArea.getMemoryArea(this);
			Tick time = (Tick) m.newInstance(Tick.class);
			m.executeInArea(new Runnable() {
	        		 public void run() { 
	        			 MyPEH4.this.tock = new Tick();
	        		 }
	        	});
        	
        	} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        }
        
        public void cleanUp() {
        }

    
        public StorageParameters getThreadConfigurationParameters() {
        	return null;
        }
}

class Tick {
	
}
