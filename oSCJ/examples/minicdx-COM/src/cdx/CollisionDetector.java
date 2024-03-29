
package cdx;


import javax.realtime.*;

public class CollisionDetector implements Runnable, ICollisionDetector {

	
	   public boolean stop = false;
	   
	   public ITransientDetector iTransientDetector;
	   
	   
	   public CollisionDetector () {
		   super();
	   }

       //public CollisionDetector(final PriorityParameters p, final PeriodicParameters q, final LTMemory l) {
	   //    //super(p, q, null, l, null, null);
       //        super(p,q);
	   //}

	   
	   public void run() {
		   //System.out.println("CollisionDetector starts...");
		   int n = 0;
		   while (!stop) {
			   boolean missed=!RealtimeThread.waitForNextPeriod();
			   long now = System.nanoTime();
			   ImmortalEntry.detectorReleaseTimes[ImmortalEntry.recordedDetectorReleaseTimes] = now;
			   ImmortalEntry.detectorReportedMiss[ImmortalEntry.recordedDetectorReleaseTimes] = false;
			   ImmortalEntry.recordedDetectorReleaseTimes++;
			   
			   System.out.println("iteration: "+n++);

			   iTransientDetector.runDetectorInScope();
		   
			   if ( (ImmortalEntry.framesProcessed + ImmortalEntry.droppedFrames) == Constants.MAX_FRAMES) {
		            stop = true;
		            return;
			   }
			   // waitForNextPeriod();
		   }
	   }
	   
	   
	   public void runCollisionDetector() {
		   long timeBefore = NanoClock.now();
		   iTransientDetector.runDetectorInScope();
		   long timeAfter = NanoClock.now();
		  
		   if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
	            ImmortalEntry.timesBefore[ ImmortalEntry.recordedRuns ] = timeBefore;
	            ImmortalEntry.timesAfter[ ImmortalEntry.recordedRuns ] = timeAfter;
	            //ImmortalEntry.heapFreeBefore[ ImmortalEntry.recordedRuns ] = heapFreeBefore;
	            //ImmortalEntry.heapFreeAfter[ ImmortalEntry.recordedRuns ] = heapFreeAfter;
	            ImmortalEntry.recordedRuns ++;
	        }
		   
		   if ( (ImmortalEntry.framesProcessed + ImmortalEntry.droppedFrames) == Constants.MAX_FRAMES) {
	            ImmortalEntry.stop = true;
	            return;
		   }
	   }
	   
	   
//	   public static boolean waitForNextPeriod() {
//		   long tosleep  = 100;
//		   
//			try {
//				long tmilis = tosleep / 1000000;
//				int tnanos = (int) (tosleep - tmilis * 1000000);
//				Thread.sleep(tmilis, tnanos);
//			} catch (InterruptedException iex) {
//			}
//
//			return true;
//		}

    public void bindTransientDetector(ITransientDetector itd) {

	iTransientDetector = itd;
    }

	
}
