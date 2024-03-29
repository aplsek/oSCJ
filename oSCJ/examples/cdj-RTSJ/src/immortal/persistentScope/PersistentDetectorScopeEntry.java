package immortal.persistentScope;

import immortal.Constants;
import immortal.Benchmarker;
import immortal.ImmortalEntry;
import immortal.NanoClock;
import immortal.RawFrame;
import immortal.persistentScope.transientScope.TransientDetectorScopeEntry;

import javax.realtime.LTMemory;
import javax.realtime.NoHeapRealtimeThread;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;

//import com.fiji.fivm.*;

import bench.BenchMem;
/** 
 * This thread is the periodic real-time threads that periodically wakes-up to run the collision detector.
 * Its constructor runs in immortal memory. The instance lives in immortal memory. 
 * The thread itself runs in the persistent detector scope.
 */
public class PersistentDetectorScopeEntry implements Runnable /*extends RealtimeThread*/ {

    
	//public PersistentDetectorScopeEntry(final PriorityParameters p, final PeriodicParameters q, final LTMemory l) {
    //    super(p, q, null, l, null, null);
    //}

    public boolean stop = false;
    
    public void run() {
        System.out.println("Persistent, area:" + RealtimeThread.getCurrentMemoryArea());
    	
    	
    	ImmortalEntry.progStartTime = NanoClock.now();
        NoiseGenerator noiseGenerator = new NoiseGenerator();
        final LTMemory transientDetectorScope = new LTMemory(immortal.Constants.TRANSIENT_DETECTOR_SCOPE_SIZE, immortal.Constants.TRANSIENT_DETECTOR_SCOPE_SIZE);
        try {
            final TransientDetectorScopeEntry cd = new TransientDetectorScopeEntry(new StateTable(), Constants.GOOD_VOXEL_SIZE);
            //if (immortal.Constants.DEBUG_DETECTOR) {
            //    System.out.println("Detector thread is "+Thread.currentThread());
            //    System.out.println("Entering detector loop, detector thread priority is "+
            //                       +Thread.currentThread().getPriority()+
            //                       " (NORM_PRIORITY is "+Thread.NORM_PRIORITY+
            //                       ", MIN_PRIORITY is "+Thread.MIN_PRIORITY+
            //                       ", MAX_PRIORITY is "+Thread.MAX_PRIORITY+")");  
            //}
            
            while (!stop) {
                //if (ImmortalEntry.recordedDetectorReleaseTimes > 0) {
                //    ImmortalEntry.detectorWaitTimes[ ImmortalEntry.recordedDetectorReleaseTimes - 1 ] = NanoClock.now();
                //}
                boolean missed=!RealtimeThread.waitForNextPeriod();
                ImmortalEntry.detectorReleaseTimes[ ImmortalEntry.recordedDetectorReleaseTimes ] = NanoClock.now();
                ImmortalEntry.detectorReportedMiss [ ImmortalEntry.recordedDetectorReleaseTimes ] = missed;
                ImmortalEntry.recordedDetectorReleaseTimes++;
                
                long timeBefore = NanoClock.now();
                runDetectorInScope(cd, transientDetectorScope, noiseGenerator);
                long timeAfter = NanoClock.now();
                
                if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
                    ImmortalEntry.timesBefore[ ImmortalEntry.recordedRuns ] = timeBefore;
                    ImmortalEntry.timesAfter[ ImmortalEntry.recordedRuns ] = timeAfter;
                    ImmortalEntry.recordedRuns ++;
                }
                if ( (immortal.ImmortalEntry.framesProcessed + immortal.ImmortalEntry.droppedFrames) == immortal.Constants.MAX_FRAMES) {
                    stop = true;
                }
            }
            ImmortalEntry.detectorWaitTimes[ ImmortalEntry.recordedDetectorReleaseTimes-1 ] = NanoClock.now();
        } catch (final Throwable t) {
            throw new Error(t);
        }
        ImmortalEntry.progEndTime = NanoClock.now();
    }

    public void runDetectorInScope(final TransientDetectorScopeEntry cd, final LTMemory transientDetectorScope, final NoiseGenerator noiseGenerator) {
    	
    	final RawFrame f = immortal.ImmortalEntry.frameBuffer.getFrame();
        if (f == null) {
            ImmortalEntry.frameNotReadyCount++;
            System.out.println("Frame not ready");
            return;
        }
                
        cd.setFrame(f);
               
        transientDetectorScope.enter(cd);               
       
        immortal.ImmortalEntry.framesProcessed++;
    }

    public void start() {
        ImmortalEntry.detectorThreadStart = NanoClock.now();
       // super.start();
    }
}