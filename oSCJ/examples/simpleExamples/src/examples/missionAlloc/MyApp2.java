package examples.missionAlloc;

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

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.MissionManager;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;


import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import javax.safetycritical.annotate.Scope;

@Scope("exampleMissionAlloc.MyApp2") 
public class MyApp2 extends CyclicExecutive {

	static PriorityParameters p = new PriorityParameters(18); 
 	static StorageParameters s = new StorageParameters(1000L, 1000L, 1000L); 
 	static RelativeTime t = new RelativeTime(5,0);
 	
 	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) { 
		return new CyclicSchedule(new CyclicSchedule.Frame[]{new CyclicSchedule.Frame(t,handlers)});
	}

	public MyApp2() {
		super(p, s);
	}

	public void initialize() {
		new MyPEH2(new long[1000]);
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 * 
	 * @return the amount of memory needed
	 */
	public long missionMemorySize() {
		return 1420;   // MIN without printing is 430  bytes.
	}

	@SCJRestricted(INITIALIZATION) 
	public void setUp() {
		//Terminal.getTerminal().write("Mission setUp.\n");
	}

	@SCJRestricted(CLEANUP)
	public void tearDown() {
		//Terminal.getTerminal().write("Mission teardown.\n");
	}

	@SCJRestricted(CLEANUP)
	public void cleanUp() {
		
        	//System.out.println("Mission Mem consumed : " + RealtimeThread.getOuterMemoryArea(1).memoryConsumed());

        	//System.out.println("total: " +  Runtime.getRuntime().totalMemory());
        	//System.out.println("free: " + Runtime.getRuntime().freeMemory());
        	
        	//String str = RealtimeThread.getCurrentMemoryArea().toString();
        	
        	//System.out.println("mem now : " + str + "  consumed:" + RealtimeThread.getCurrentMemoryArea().memoryConsumed());
        	
		//Terminal.getTerminal().write("Mission cleanUp.\n");
		
	}

}
