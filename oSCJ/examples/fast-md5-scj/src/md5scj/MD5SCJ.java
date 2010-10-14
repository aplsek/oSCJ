package md5scj;

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
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;

import javax.realtime.RelativeTime;

import com.twmacinta.util.MyMD5Input;

import bench.Benchmark;
import bench.Constants;

public class MD5SCJ extends CyclicExecutive {

	public MD5SCJ() {
		super(null);
	}

	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
		CyclicSchedule schedule = new CyclicSchedule(frames);
		frames[0] = new CyclicSchedule.Frame(new RelativeTime(Constants.PERIOD,
				0), handlers);
		return schedule;
	}

	public void initialize() {
		new WordHandler(Constants.PRIVATE_MEMORY, Constants.RUNS);
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
		System.out.println("SCJ MD5 Benchmark - setup DONE.");
	}

	public void tearDown() {
		Benchmark.dumpResults();
	}

	public void cleanUp() {
	}

	public class WordHandler extends PeriodicEventHandler {

		private int count_;

		private WordHandler(long psize, int count) {
			super(null, null, null, psize);
			count_ = count;
		}

		/**
		 * 
		 * Testing Enter Private Memory
		 * 
		 */
		public void handleEvent() {

			long start = System.nanoTime();
			//doMD5work();
			long end = System.nanoTime();
			Benchmark.set(start, end);

			if (count_-- == 0)
				getCurrentMission().requestSequenceTermination();

		}

		private  void doMD5work() {
			for (String in : Constants.input) {
				MyMD5Input myMD = new MyMD5Input();
				myMD.run(in);
				myMD.finalHash(in);
			}
		}

		public void cleanUp() {
		}

		public StorageParameters getThreadConfigurationParameters() {
			return null;
		}

	}
}
