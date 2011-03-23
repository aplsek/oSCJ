/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   miniCDx is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   miniCDx is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with miniCDx.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010 
 *   @authors  Daniel Tang, Ales Plsek
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.SCJAllowed;
import bench.BenchMem;

import cdx.unannotated.NanoClock;
import edu.purdue.scj.VMSupport;

import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;

@SCJAllowed(members=true)
@Scope("cdx.Level0Safelet")
@RunsIn("cdx.CollisionDetectorHandler")
public class CollisionDetectorHandler extends PeriodicEventHandler {
    private final TransientDetectorScopeEntry cd = new TransientDetectorScopeEntry(
            new StateTable(), Constants.GOOD_VOXEL_SIZE);
    public final NoiseGenerator noiseGenerator = new NoiseGenerator();

    public boolean stop = false;

    @SCJRestricted(INITIALIZATION)
    public CollisionDetectorHandler() {

        // these very large limits are reported to work with stack traces... of
        // errors encountered...
        // most likely they are unnecessarily large
        super(null, null, new StorageParameters(Constants.TRANSIENT_DETECTOR_SCOPE_SIZE,0,0));
    }

    public void runDetectorInScope(final TransientDetectorScopeEntry cd) {
        Benchmarker.set(14);

        final RawFrame f = cdx.ImmortalEntry.frameBuffer.getFrame();
        if (f == null) {
            ImmortalEntry.frameNotReadyCount++;
            System.out.println("Frame not ready");
            Benchmarker.done(14);
            return;
        }

        if ((cdx.ImmortalEntry.framesProcessed + cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES) {
            stop = true;
            Benchmarker.done(14);
            return;
        } // should not be needed, anyway

        noiseGenerator.generateNoiseIfEnabled();
        Benchmarker.set(Benchmarker.RAPITA_SETFRAME);
        cd.setFrame(f);
        Benchmarker.done(Benchmarker.RAPITA_SETFRAME);
        
        final long timeBefore = NanoClock.now();
        cd.run();
        final long timeAfter = NanoClock.now();
        //final long heapFreeAfter = Runtime.getRuntime().freeMemory();

        
        
        
        if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
            ImmortalEntry.timesBefore[ImmortalEntry.recordedRuns] = timeBefore;
            ImmortalEntry.timesAfter[ImmortalEntry.recordedRuns] = timeAfter;
            //ImmortalEntry.heapFreeBefore[ImmortalEntry.recordedRuns] = heapFreeBefore;
            //ImmortalEntry.heapFreeAfter[ImmortalEntry.recordedRuns] = heapFreeAfter;
            ImmortalEntry.recordedRuns++;
        }

        cdx.ImmortalEntry.framesProcessed++;

        if ((cdx.ImmortalEntry.framesProcessed + cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES)
            stop = true;
        Benchmarker.done(14);
    }

    public void handleAsyncEvent() {
        
        //BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
            
        try {
            if (!stop) {
                long now = NanoClock.now();
                ImmortalEntry.detectorReleaseTimes[ImmortalEntry.recordedDetectorReleaseTimes] = now;
                ImmortalEntry.detectorReportedMiss[ImmortalEntry.recordedDetectorReleaseTimes] = false;
                ImmortalEntry.recordedDetectorReleaseTimes++;

                runDetectorInScope(cd);
            } else {
                Mission.getCurrentMission().requestSequenceTermination();
            }
        } catch (Throwable e) {
            System.out.println("Exception thrown by runDetectorInScope: "
                    + e.getMessage());
            e.printStackTrace();
        }
        
       // BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
    }

    
    
    
    
    
    
    public void cleanUp() {
    }



    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }

}
