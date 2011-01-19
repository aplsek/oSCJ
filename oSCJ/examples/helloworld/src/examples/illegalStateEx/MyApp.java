package examples.illegalStateEx;

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
 *   @authors  Ales Plsek
 */

import java.util.List;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionManager;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PrivateMemory;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import javax.safetycritical.annotate.Scope;

@Scope("examples.illegalStateEx.MyApp")
public class MyApp extends CyclicExecutive {

	public PrivateMemory pri;

	static PriorityParameters p = new PriorityParameters(18);
	static StorageParameters s = new StorageParameters(1000L, 1000L, 1000L);
	static RelativeTime t = new RelativeTime(5, 0);

	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		return new CyclicSchedule(
				new CyclicSchedule.Frame[] { new CyclicSchedule.Frame(
						t, handlers) });
	}

	public MyApp() {
		super(p, s);
	}

	public void initialize() {
		new MyP1();
		new MyP2();
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 * 
	 * @return the amount of memory needed
	 */
	public long missionMemorySize() {
		return 1420; // MIN without printing is 430 bytes.
	}

	@SCJRestricted(INITIALIZATION)
	public void setUp() {
	}

	@SCJRestricted(CLEANUP)
	public void tearDown() {
	}

	@SCJRestricted(CLEANUP)
	public void cleanUp() {
	}

}

@Scope("examples.illegalStateEx.MyApp")
@RunsIn("examples.illegalStateEx.MyP1")
class MyP1 extends PeriodicEventHandler {

	static PriorityParameters pri;
	static PeriodicParameters per;
	static StorageParameters stor;

	static {
		pri = new PriorityParameters(13);
		per = new PeriodicParameters(new RelativeTime(0, 0),
				new RelativeTime(500, 0));
		stor = new StorageParameters(1000L, 1000L, 1000L);
	}

	public MyP1() {
		super(pri, per, stor);
	}

	public void handleAsyncEvent() {
		MyApp m1 = (MyApp) Mission.getCurrentMission();
		m1.pri = (PrivateMemory) MemoryArea.getMemoryArea(new int[0]);
	}

	public void cleanUp() {
	}

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}
}

@Scope("examples.illegalStateEx.MyApp")
@RunsIn("examples.illegalStateEx.MyP2")
class MyP2 extends PeriodicEventHandler {

	static PriorityParameters pri;
	static PeriodicParameters per;
	static StorageParameters stor;

	static {
		pri = new PriorityParameters(13);
		per = new PeriodicParameters(new RelativeTime(0, 0),
				new RelativeTime(500, 0));
		stor = new StorageParameters(1000L, 1000L, 1000L);
	}

	public MyP2() {
		super(pri, per, stor);
	}

	public void handleAsyncEvent() {
		try {
			MyApp m1 = (MyApp) Mission.getCurrentMission();
			m1.pri.newInstance(List.class);
			m1.pri.enterPrivateMemory(500, new Runnable() {
				public void run() {
				}
			});

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public void cleanUp() {
	}

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}
}
