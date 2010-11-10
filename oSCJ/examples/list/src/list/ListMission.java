package list;

/**
 *  This file is part of oSCJ.
 *
 *   oSCJ is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   oSCJ is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with oSCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010 
 *   @authors  Lei Zhao, Ales Plsek
 */

import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import javax.realtime.RelativeTime;



import bench.Benchmark;
import bench.Constants;

public class ListMission extends CyclicExecutive {

	public ListMission() {
		super(null);
		
		System.out.println("SCJ List Benchmark - constructor");
	}

	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		System.out.println("List -getSchedule called");
		
		CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
		CyclicSchedule schedule = new CyclicSchedule(frames);
		frames[0] = new CyclicSchedule.Frame(new RelativeTime(Constants.PERIOD,
				0), handlers);
		return schedule;
	}

	public void initialize() {
		
		
		
		System.out.println("SCJ List Benchmark - initialize");
		
	    new WordHandler(Constants.PRIVATE_MEMORY, Constants.RUNS);
	    System.out.println("SCJ List Benchmark - initialize done");
	}

	/**
	 * A method to query the maximum amount of memory needed by this mission.
	 * 
	 * @return the amount of memory needed
	 */
	public long missionMemorySize() {
		return Constants.MISSION_MEMORY;
	}

	public void setUp() {
		Benchmark.init();
	}

	public void tearDown() {
		System.out.println("SCJ List Benchmark - teardown...");
		Benchmark.dumpResults();
	}

	public void cleanUp() {
	    	System.out.println("SCJ List Benchmark - clean up.");
	}



	public class WordHandler extends PeriodicEventHandler {

		private int count_;

		private List list;
		
		
		private WordHandler(long psize, int count) {
			super(null, null, null, psize);
			count_ = count;
			
			System.out.println("ListHandler created");
			
			list = new List(Constants.MAX);
		}

		/**
		 * 
		 * Testing Enter Private Memory
		 * 
		 */
		public void handleEvent() {

			long start = System.nanoTime();
			list.invert();
			long end = System.nanoTime();
			Benchmark.set(start, end);

			if (count_-- == 0)
				getCurrentMission().requestSequenceTermination();

		}

		public void cleanUp() {
		}

		public StorageParameters getThreadConfigurationParameters() {
			return null;
		}

	}
}
