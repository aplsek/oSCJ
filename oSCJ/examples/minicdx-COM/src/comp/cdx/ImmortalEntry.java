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
package comp.cdx;

import java.io.DataOutputStream;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

import cdx.Constants;
import cdx.RawFrame;
import workload.*;

/**
 * This thread runs only during start-up to run other threads. It runs in immortal memory, is allocated in immortal
 * memory, and it's constructor runs in immortal memory. It is a singleton, allocation from the Main class Ales: this
 * thread allocates - the scopes - the PersistentDetectorScope and TransientDetectorScope -
 */
@Component(name = "ImmortalEntry",
		provides = {
		@Interface(name = "iCdToIe", signature = comp.cdx.ICollDetectToImmEntry.class),
		@Interface(name = "iTdToIe", signature = comp.cdx.ITransDetectToImmEntry.class)})
public class ImmortalEntry implements Runnable, ICollDetectToImmEntry, ITransDetectToImmEntry {

    static public Object           initMonitor                  = new Object();
    static public boolean          detectorReady                = false;
    static public boolean          simulatorReady               = false;

    static public boolean          stop               = false;

    
    static public int              maxDetectorRuns;

    static public long             detectorFirstRelease         = -1;

    static public long[]           timesBefore;
    static public long[]           timesAfter;
    static public long[]           heapFreeBefore;
    static public long[]           heapFreeAfter;
    static public int[]            detectedCollisions;
    static public int[]            suspectedCollisions;

    static public long             detectorThreadStart;
    static public long[]           detectorReleaseTimes;
    static public boolean[]        detectorReportedMiss;

    static public int              reportedMissedPeriods        = 0;
    static public int              frameNotReadyCount           = 0;
    static public int              droppedFrames                = 0;
    static public int              framesProcessed              = 0;
    static public int              recordedRuns                 = 0;

    static public int              recordedDetectorReleaseTimes = 0;

    //static public FrameBuffer frameBuffer = new FrameBufferPLDI();
    static public FrameBuffer frameBuffer = new WorkloadStar();
	
    static public DataOutputStream binaryDumpStream             = null;

    public ImmortalEntry() {
        // super(new PriorityParameters(Constants.DETECTOR_STARTUP_PRIORITY));
    	
    	// TODO: should be implemented as a component property
        maxDetectorRuns = Constants.MAX_FRAMES;

        timesBefore = new long[maxDetectorRuns];
        timesAfter = new long[maxDetectorRuns];
        heapFreeBefore = new long[maxDetectorRuns];
        heapFreeAfter = new long[maxDetectorRuns];
        detectedCollisions = new int[maxDetectorRuns];
        suspectedCollisions = new int[maxDetectorRuns];

        detectorReleaseTimes = new long[maxDetectorRuns + 10]; // the 10 is for missed deadlines
        detectorReportedMiss = new boolean[maxDetectorRuns + 10];
    }

	public void run() {
		
	}
	
	/* 
	 * Implementations of the interface ICollDetectToImmEntry
	 */
	public void beforeRun(long time, boolean reportedMiss) {
		detectorReleaseTimes[ImmortalEntry.recordedDetectorReleaseTimes] = time;
		detectorReportedMiss[ImmortalEntry.recordedDetectorReleaseTimes] = reportedMiss;
		recordedDetectorReleaseTimes++;
	}
	public void afterRun(long timeBefore, long timeAfter) {
		if (recordedRuns < maxDetectorRuns) {
			timesBefore[ ImmortalEntry.recordedRuns ] = timeBefore;
			timesAfter[ ImmortalEntry.recordedRuns ] = timeAfter;
			//ImmortalEntry.heapFreeBefore[ ImmortalEntry.recordedRuns ] = heapFreeBefore;
			//ImmortalEntry.heapFreeAfter[ ImmortalEntry.recordedRuns ] = heapFreeAfter;
			recordedRuns++;
		}
	}
	
	/* 
	 * Implementations of the interface ITransDetectToImmEntry
	 */
	public RawFrame getFrame() {
		return frameBuffer.getFrame();
	}
	public void incrementFrameNotReadyCount() {
		frameNotReadyCount++;
	}
	public void incrementFramesProcessed() {
		framesProcessed++;
	}
	public void setNumberOfCollisions(int numberOfCollisions) {
		if (recordedRuns < maxDetectorRuns) {
			detectedCollisions[recordedRuns] = numberOfCollisions;
		}
	}
	public void setSuspectedSize(int suspectedSize) {
		if (recordedRuns < maxDetectorRuns) {
			suspectedCollisions[recordedRuns] = suspectedSize;
		}	
	}
}
