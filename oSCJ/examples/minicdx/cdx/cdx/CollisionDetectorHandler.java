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

@SCJAllowed(members=true)
@javax.safetycritical.annotate.Scope("cdx.Level0Safelet")
@javax.safetycritical.annotate.RunsIn("cdx.CollisionDetectorHandler")
public class CollisionDetectorHandler extends PeriodicEventHandler {
    private final TransientDetectorScopeEntry cd = new TransientDetectorScopeEntry(
            new StateTable(), Constants.GOOD_VOXEL_SIZE);
    public final NoiseGenerator noiseGenerator = new NoiseGenerator();

    public boolean stop = false;

    public CollisionDetectorHandler() {

        // these very large limits are reported to work with stack traces... of
        // errors encountered...
        // most likely they are unnecessarily large
        super(null, null, null, Constants.TRANSIENT_DETECTOR_SCOPE_SIZE);
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

        final long heapFreeBefore = Runtime.getRuntime().freeMemory();
        final long timeBefore = NanoClock.now();

        noiseGenerator.generateNoiseIfEnabled();
        Benchmarker.set(Benchmarker.RAPITA_SETFRAME);
        //BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
        cd.setFrame(f);
        Benchmarker.done(Benchmarker.RAPITA_SETFRAME);
        // actually runs the detection logic in the given scope
        
        //BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
        cd.run();
        //BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
        
        
        
        final long timeAfter = NanoClock.now();
        final long heapFreeAfter = Runtime.getRuntime().freeMemory();

        if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
            ImmortalEntry.timesBefore[ImmortalEntry.recordedRuns] = timeBefore;
            ImmortalEntry.timesAfter[ImmortalEntry.recordedRuns] = timeAfter;
            ImmortalEntry.heapFreeBefore[ImmortalEntry.recordedRuns] = heapFreeBefore;
            ImmortalEntry.heapFreeAfter[ImmortalEntry.recordedRuns] = heapFreeAfter;
            ImmortalEntry.recordedRuns++;
        }

        cdx.ImmortalEntry.framesProcessed++;

        if ((cdx.ImmortalEntry.framesProcessed + cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES)
            stop = true;
        Benchmarker.done(14);
    }

    public void handleEvent() {
        
        // BenchMem.setMissionUsage(RealtimeThread.getOuterMemoryArea(0).memoryConsumed());
        // start of the cycle
        BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
        /*
        long total = Runtime.getRuntime().totalMemory();
        long free  = Runtime.getRuntime().freeMemory();
        MemoryArea immortal = ImmortalMemory.instance();
        Terminal.getTerminal().write("\n\t total : " + total + "\n");
        Terminal.getTerminal().write("\t free : " + free + " \n");
        Terminal.getTerminal().write("\t real : " + (total - free) + " \n");
        Terminal.getTerminal().write("\t immortal before : " + immortal.memoryConsumed() + " \n");
        */
   
        
        
            
            
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
        
        
        MemoryArea immortal = ImmortalMemory.instance();
        long im = immortal.memoryConsumed();
        long curr = RealtimeThread.getCurrentMemoryArea().memoryConsumed();
        long im_VM =  VMSupport.memoryConsumed( VMSupport.getImmortalArea());
        long curr_VM = VMSupport.memoryConsumed(VMSupport.getCurrentArea());
        
        //Terminal.getTerminal().write("------- mem test  \n");
        //Terminal.getTerminal().write("IMMORTAL consumed [oSCJ]: " + im + "\n");
        //Terminal.getTerminal().write("current consumed [oSCJ]: " + curr + "\n"); 
        //Terminal.getTerminal().write("immortal [VMSupport]: " +  im_VM + "\n"); 
        //Terminal.getTerminal().write("immortal size [VMSupport]: " +  VMSupport.getScopeSize(VMSupport.getImmortalArea()) + "\n"); 
        //Terminal.getTerminal().write("current consumed [VMSupport]: " +  curr_VM + "\n"); 
        //Terminal.getTerminal().write("current size [VMSupport]: " +   VMSupport.getScopeSize(VMSupport.getCurrentArea()) + "\n");
        //Terminal.getTerminal().write("\n"); 
        
        
        // end of the cycle
        BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
    }

    public void cleanUp() {
    }

    public void register() {
    }

    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }

}
