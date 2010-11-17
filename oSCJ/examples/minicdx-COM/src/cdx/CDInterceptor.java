package cdx;

import javax.realtime.LTMemory;
import javax.realtime.RealtimeThread;

public class CDInterceptor  implements ICollisionDetector{

	ICollisionDetector iCD;
	LTMemory persistentMemory;
	
	public CDInterceptor(ICollisionDetector cd) {
		this.iCD  = cd;
		persistentMemory = new LTMemory(Constants.PERSISTENT_DETECTOR_SCOPE_SIZE);
	    System.out.println("persistentScope: " + persistentMemory);
	}
	final R r = new R();
	
	public void runCollisionDetector() {
		 
		 persistentMemory.enter(r);
		
	}
	
	class R implements Runnable {
		public void run() {
			 while (!ImmortalEntry.stop) {
				   boolean missed=!RealtimeThread.waitForNextPeriod();
				   long now = System.nanoTime();
				   ImmortalEntry.detectorReleaseTimes[ImmortalEntry.recordedDetectorReleaseTimes] = now;
				   ImmortalEntry.detectorReportedMiss[ImmortalEntry.recordedDetectorReleaseTimes] = false;
				   ImmortalEntry.recordedDetectorReleaseTimes++;
				   
				   iCD.runCollisionDetector();
			 }
		}
	}
}
