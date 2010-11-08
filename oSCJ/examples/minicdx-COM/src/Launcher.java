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

import cdx.CollisionDetector;
import cdx.Constants;
import cdx.ImmortalEntry;
import cdx.StateTable;
import cdx.TransientDetector;
import cdx.NanoClock;
import cdx.*;



import javax.realtime.*;


public class Launcher {
	public static void main(final String[] args) {
		if (args.length > 0)
			Constants.NUMBER_OF_PLANES = Integer.parseInt(args[0]);
		if (args.length > 1)
			Constants.DETECTOR_PERIOD = Integer.parseInt(args[1]);
		if (args.length > 2)
			Constants.MAX_FRAMES = Integer.parseInt(args[2]);

		// FRACTAL BINDING
		//CollisionDetector cd = new CollisionDetector();

		AbsoluteTime releaseAt = NanoClock.roundUp(Clock.getRealtimeClock().getTime().add(Constants.DETECTOR_STARTUP_OFFSET_MILLIS, 0));

		//		System.out.println("" + releaseAt + " -- period:" + Constants.DETECTOR_PERIOD + " priority " + Constants.DETECTOR_PRIORITY);
		
                CollisionDetector cd = new CollisionDetector(
     		   new PriorityParameters(Constants.DETECTOR_PRIORITY), 
		   new PeriodicParameters(releaseAt, // start
		   new RelativeTime(Constants.DETECTOR_PERIOD, 0), // period
		     		 null, //cost
				 null, // deadline
				 null, null), 
		   null);  //persistentDetectorScope);

		
		StateTable st = new StateTable();
		STInterceptor sti = new STInterceptor(st);

		TransientDetector td = new TransientDetector(Constants.GOOD_VOXEL_SIZE);
		TDInterceptor tdi = new TDInterceptor(td);

		td.bindStateTable(sti);
		
		cd.bindTransientDetector(td);

		ImmortalEntry ie = new ImmortalEntry();
		ie.run();
		
		
		// START THE THREAD
		cd.start();

		// END
		try {
			cd.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// BENCH RESULTS DUMP
		dumpResults();
	}

	public static void dumpResults() {

		String space = " ";
		String triZero = " 0 0 0 ";

		// System.out.println("Mem setup at startup was:" + memSetup);
		// System.out.println("Mem setup END Was:" + memSetupEnd);

		if (Constants.PRINT_RESULTS) {
			System.out
					.println("Dumping output [ timeBefore timeAfter heapFreeBefore heapFreeAfter detectedCollisions ] for "
							+ ImmortalEntry.recordedRuns
							+ " recorded detector runs, in ns");
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
		System.out.println("Received (and measured) frames: "
				+ ImmortalEntry.recordedRuns);
		System.out.println("Frame not ready event count (in detector): "
				+ ImmortalEntry.frameNotReadyCount);
		System.out.println("Frames dropped due to full buffer in detector: "
				+ ImmortalEntry.droppedFrames);
		System.out.println("Frames processed by detector: "
				+ ImmortalEntry.framesProcessed);
		// System.out.println("Detector stop indicator set: "
		// + ImmortalEntry.persistentDetectorScopeEntry.stop);
		System.out
				.println("Reported missed detector periods (reported by waitForNextPeriod): "
						+ ImmortalEntry.reportedMissedPeriods);
		// System.out.println("Detector first release was scheduled for: "
		// + NanoClock.asString(ImmortalEntry.detectorFirstRelease));
		// heap measurements

		// detector release times
		if (Constants.DETECTOR_RELEASE_STATS != "") {
			System.out.println("=====DETECTOR-RELEASE-STATS-START-BELOW====");
			for (int i = 0; i < ImmortalEntry.recordedDetectorReleaseTimes; i++) {
				System.out.print(ImmortalEntry.detectorReleaseTimes[i]);
				System.out.print(space);
				System.out.print(i * Constants.DETECTOR_PERIOD * 1000000L
						+ ImmortalEntry.detectorReleaseTimes[0]);
				System.out.print(space);
				System.out.print(ImmortalEntry.detectorReportedMiss[i] ? 1 : 0);
				System.out.print(space);
				System.out.println(i);
			}
			System.out.println("=====DETECTOR-RELEASE-STATS-END-ABOVE====");
		}

		// BenchMem.dumpMemoryUsage();
	}
}
