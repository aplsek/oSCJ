package example;

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

import edu.purdue.scj.VMSupport;
import edu.purdue.scj.utils.Utils;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import javax.safetycritical.annotate.Scope;

@Scope("example.MyApp") 
public class MyApp extends CyclicExecutive {

	static long privateMemSize = 500;
	static String handlerName = "MyPEH";
	static int MAX_ITERATION = 1000;
	
	static PriorityParameters p = new PriorityParameters(18); 
 	static StorageParameters s = new StorageParameters(1000, 1000, 1000); 

 	static CyclicSchedule schedule; 
	static RelativeTime time = new RelativeTime(500, 0); 
	
	
	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) { 
		CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
		CyclicSchedule schedule = new CyclicSchedule(frames);
		frames[0] = new CyclicSchedule.Frame(time, handlers); 
		return schedule;
	} 

	public MyApp() {
		super(p, s);
	}

	long mem1;
	long mem2;
	long mem11;
	long mem22;
	String str ;
	long total;
	long free;
	
	public void initialize() {
		total = Runtime.getRuntime().totalMemory();
		free = Runtime.getRuntime().freeMemory();
		
		System.out.println("total = " + Runtime.getRuntime().totalMemory());
		System.out.println(" free = " + Runtime.getRuntime().freeMemory());

		long mem1 = RealtimeThread.getCurrentMemoryArea().memoryConsumed();
		long mem11 = RealtimeThread.getCurrentMemoryArea().memoryRemaining();
		new Object();
		//str = RealtimeThread.getCurrentMemoryArea().toString();
		new MyPEH(privateMemSize,handlerName,MAX_ITERATION);
		long mem2 = RealtimeThread.getCurrentMemoryArea().memoryConsumed();
		long mem22 = RealtimeThread.getCurrentMemoryArea().memoryRemaining();
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 * 
	 * @return the amount of memory needed
	 */
	public long missionMemorySize() {
		return 400000;
	}

	@SCJRestricted(INITIALIZATION) 
	public void setUp() {
		Terminal.getTerminal().write("Mission setUp.\n");
	}

	@SCJRestricted(CLEANUP)
	public void tearDown() {
		Terminal.getTerminal().write("Mission teardown.\n");
		
		
	}

	@SCJRestricted(CLEANUP)
	public void cleanUp() {
		System.out.println("Mission Mem consumed: " + mem1);
        	System.out.println("Mission Mem consumed: " + mem2);
        	System.out.println("Mission Mem remaining : " + mem11 + " --- " +  mem22);
        	System.out.println("memory: " + str);
        	System.out.println("total: " + total);
        	System.out.println("free: " + free);
        	
        	String str = RealtimeThread.getCurrentMemoryArea().toString();
        	
        	System.out.println("mem now : " + str + "  consumed:" + RealtimeThread.getCurrentMemoryArea().memoryConsumed());
        	
		Terminal.getTerminal().write("Mission cleanUp.\n");
	}

}
