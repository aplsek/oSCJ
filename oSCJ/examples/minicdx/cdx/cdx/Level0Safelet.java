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

import immortal.Simulator;

import java.io.PrintWriter;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.SCJAllowed;
import bench.BenchMem;

import cdx.unannotated.NanoClock;
import edu.purdue.scj.VMSupport;

@SCJAllowed(members=true)
@javax.safetycritical.annotate.Scope("immortal")
public class Level0Safelet extends CyclicExecutive {
    public Level0Safelet() {
        super(null);
    }
    
    private static long memSetup;
    private static long memSetupEnd ;

    public void setUp() {
        System.out.println("set up ales..!!!!");
       
        ////////////////////////////////
        //  MEMORY BENCHMARK INIT
        BenchMem.init();
        //
        // memory statistics
        //
        System.out.println("MEMORY STATISTICS:");
        ImmortalMemory imm  = ImmortalMemory.instance();
        System.out.println("IMMORTAL size [oSCJ]: " + imm.size());
        System.out.println("IMMORTAL consumed [oSCJ]: " + imm.memoryConsumed());
        System.out.println("TOTAL VM SIZE [FijiVM]: " + ( Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
      
        
         
        
        /*  MEM TESTS : 
        System.out.println("calling get current memory.. DONE!!!!\n\n");
        
        ImmortalMemory imm  = ImmortalMemory.instance();
        
        memSetup = imm.memoryConsumed();
        
        //System.out.println("current mem : " + mem.memoryConsumed());
        System.out.println("imm mem consumed: " + imm.memoryConsumed());
        System.out.println("imm mem remaining: " + imm.memoryRemaining());
        System.out.println("imm mem size: " + imm.size());
        
        
        // direct VMSupport access:
        VMSupport.memoryConsumed(VMSupport.getCurrentArea());
        System.out.println("VMSupport- getCurrent- memory consumed: " + VMSupport.memoryConsumed(VMSupport.getCurrentArea())) ;
        System.out.println("VMSupport- getCurrent- memory size: " + VMSupport.getScopeSize(VMSupport.getCurrentArea())) ;
        
        System.out.println("VMSupport get Immortal Size: " + VMSupport.getScopeSize(VMSupport.getImmortalArea())) ;
        System.out.println("VMSupport get Immortal consumed: " + VMSupport.memoryConsumed(VMSupport.getImmortalArea())) ;
        
        
        //System.out.println("current mem : " + mem.hashCode());
        //System.out.println("current mem hash : " + imm.hashCode());
        //if (mem.equals(imm)) {
        // /   System.out.println("SET_UP = is in immortal");
        //} else
        //    System.out.println("SET_UP is NOT in immortal");
        
         System.out.println("SET_UP----------------------------------------\n\n");
          */
       
        
        Constants.PRESIMULATE = true;
        new ImmortalEntry().run();
        new Simulator().generate();
        
        //memSetupEnd = imm.memoryConsumed();
      
    }

    public void tearDown() {
        dumpResults();
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(Constants.DETECTOR_PERIOD, 0), handlers);
        CyclicSchedule schedule = new CyclicSchedule(frames);
        return schedule;
    }

    /*@javax.safetycritical.annotate.RunsIn("cdx.Level0Safelet")*/
    protected void initialize() {
        try {
            ImmortalEntry.detectorThreadStart = NanoClock.now();
            AbsoluteTime releaseAt = NanoClock.roundUp(Clock.getRealtimeClock().getTime().add(
                Constants.DETECTOR_STARTUP_OFFSET_MILLIS, 0));
            ImmortalEntry.detectorFirstRelease = NanoClock.convert(releaseAt);
            
            //System.out.println("Level0Safelet init..");
            //System.out.println("size of current: " + RealtimeThread.getCurrentMemoryArea().memoryRemaining()); 
            
            new CollisionDetectorHandler();
            // System.out.println("handler created..");
            
            if (Constants.DEBUG_DETECTOR) {
                System.out.println("Detector thread is " + Thread.currentThread());
                System.out
                    .println("Entering detector loop, detector thread priority is "
                            + +Thread.currentThread().getPriority() + " (NORM_PRIORITY is " + Thread.NORM_PRIORITY
                            + ", MIN_PRIORITY is " + Thread.MIN_PRIORITY + ", MAX_PRIORITY is " + Thread.MAX_PRIORITY
                            + ")");
            }

        } catch (Throwable e) {
            System.out.println("e: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    
    
    
    public long missionMemorySize() {
        return Constants.PERSISTENT_DETECTOR_SCOPE_SIZE;
    }
    
    
    
    public static void dumpResults() {
        
        String space = " ";
        String triZero = " 0 0 0 ";

        
       // System.out.println("Mem setup at startup was:" + memSetup);
        //System.out.println("Mem setup END Was:" + memSetupEnd);
        
        if (Constants.PRINT_RESULTS) {
            System.out
                .println("Dumping output [ timeBefore timeAfter heapFreeBefore heapFreeAfter detectedCollisions ] for "
                        + ImmortalEntry.recordedRuns + " recorded detector runs, in ns");
        }
        System.out.println("=====DETECTOR-STATS-START-BELOW====");
        for (int i = 0; i < ImmortalEntry.recordedRuns; i++) {
            System.out.print(ImmortalEntry.timesBefore[i]);
            System.out.print(space);
            System.out.print(ImmortalEntry.timesAfter[i]);
            System.out.print(space);
            System.out.print(ImmortalEntry.detectedCollisions[i]);
            System.out.print(space);
            System.out.print(ImmortalEntry.suspectedCollisions[i]);
            System.out.print(triZero);
            System.out.println(i);
        }

        System.out.println("=====DETECTOR-STATS-END-ABOVE====");

        System.out.println("Generated frames: " + Constants.MAX_FRAMES);
        System.out.println("Received (and measured) frames: " + ImmortalEntry.recordedRuns);
        System.out.println("Frame not ready event count (in detector): " + ImmortalEntry.frameNotReadyCount);
        System.out.println("Frames dropped due to full buffer in detector: " + ImmortalEntry.droppedFrames);
        System.out.println("Frames processed by detector: " + ImmortalEntry.framesProcessed);
        // System.out.println("Detector stop indicator set: "
        // + ImmortalEntry.persistentDetectorScopeEntry.stop);
        System.out.println("Reported missed detector periods (reported by waitForNextPeriod): "
                + ImmortalEntry.reportedMissedPeriods);
        System.out.println("Detector first release was scheduled for: "
                + NanoClock.asString(ImmortalEntry.detectorFirstRelease));
        // heap measurements
        Simulator.dumpStats();

        // detector release times
        if (Constants.DETECTOR_RELEASE_STATS != "") {
            System.out.println("=====DETECTOR-RELEASE-STATS-START-BELOW====");
            for (int i = 0; i < ImmortalEntry.recordedDetectorReleaseTimes; i++) {
                System.out.print(ImmortalEntry.detectorReleaseTimes[i]);
                System.out.print(space);
                System.out.print(i * Constants.DETECTOR_PERIOD * 1000000L + ImmortalEntry.detectorReleaseTimes[0]);
                System.out.print(space);
                System.out.print(ImmortalEntry.detectorReportedMiss[i] ? 1 : 0);
                System.out.print(space);
                System.out.println(i);
            }
            System.out.println("=====DETECTOR-RELEASE-STATS-END-ABOVE====");
        }
        
        
        
        BenchMem.dumpMemoryUsage();
        
        
    }

}
