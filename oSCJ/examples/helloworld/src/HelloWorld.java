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
import java.util.*;
import java.text.*;

public class HelloWorld extends CyclicExecutive {

<<<<<<< /home/wang/fivm/scj/oSCJ/examples/helloworld/src/HelloWorld.java
    public HelloWorld() {
        super(null);
    }

    public static void main(final String[] args) {
        Safelet safelet = new HelloWorld();
        safelet.setUp();
        safelet.getSequencer().start();
        safelet.tearDown();
    }

    private static void writeln(String msg) {
	// Terminal.getTerminal().writeln(msg);
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0), handlers);
        return schedule;
    }

    public void initialize() {
        new WordHandler(100000, "HelloWorld.\n", 1);
    }

    /**
     * A method to query the maximum amount of memory needed by this mission.
     * 
     * @return the amount of memory needed
     */
    // @Override
    public long missionMemorySize() {
        return 400000;
    }

    public void setUp() {    
    	//System.out.println("SET-UP, DEBUG");
    	
    	//System.out.println("current memory:" + RealtimeThread.getCurrentMemoryArea());
    	//VMSupport.setCurrentArea(VMSupport.getImmortalArea());
    	//
    	//
    	//System.out.println("current memory:" + RealtimeThread.getCurrentMemoryArea());
    	//System.out.println("current thread:" + RealtimeThread.currentThread().getClass());
    	//RealtimeThread.currentRealtimeThread().dumpInfo();
       
    	
    	
    	Terminal.getTerminal().write("setUp.\n"); 
    }

    public void tearDown() {
        Terminal.getTerminal().write("teardown.\n");
    }

    public void cleanUp() {
	    Terminal.getTerminal().write("cleanUp.\n");
    }

    
    
    public class WordHandler extends PeriodicEventHandler {

        private int count_;
 
        private WordHandler(long psize, String name, int count) {
            super(null, null, null, psize, name);
            count_ = count;
        }

        /**
         * 
         * Testing Enter Private Memory
         * 
         */

	class Node{
	    int count;
	    int id;
	    Node[] neighbors;

	    Node(int d, int nn){
		id = d;
		neighbors = new Node[nn];
		count = nn;
	    }
	}

        public void handleEvent() {
	    //	    Terminal.getTerminal().write(getName());
	    long startTime=System.nanoTime();
	    long estimatedTime=System.nanoTime()-startTime;
            Node node1 = new Node(1, 2);
	    Node node2 = new Node(2, 2);
	    Node node3 = new Node(3, 1);
	    node1.neighbors[0] = node2;
	    node1.neighbors[1] = node3;
	    node2.neighbors[0] = node1;
	    node2.neighbors[1] = node3;
	    node3.neighbors[0] = node2;
	    for(int i=0; i< node1.count; i++)
		{
		    Terminal.getTerminal().write("node"+node1.id+"<--"+"node"+node1.neighbors[i].id+".\n");
		}
	    for(int i=0; i< node2.count; i++)
		{
		    Terminal.getTerminal().write("node"+node2.id+"<--"+"node"+node2.neighbors[i].id+".\n");
		}
	    for(int i=0; i< node3.count; i++)
		{
		    Terminal.getTerminal().write("node"+node3.id+"<--"+"node"+node3.neighbors[i].id+".\n");
		}
	    Terminal.getTerminal().write("Running Time:"+estimatedTime+"us.\n");
               if (count_-- == 0)

        	Terminal.getTerminal().write(getName());
	    
        	
        	
	        //System.out.println("HelloWorld test");
        	//AbsoluteTime t = Clock.getRealtimeClock().getTime();
     		// AbsoluteTime t = Clock.getRealtimeClock().getTimePrecise();
        	//System.out.println("Time is:" + t.getMilliseconds() + "ms ," + t.getNanoseconds());

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

=======
	public HelloWorld() {
		super(null);
	}

	public static void main(final String[] args) {
		Safelet safelet = new HelloWorld();
		safelet.setUp();
		safelet.getSequencer().start();
		safelet.tearDown();
	}

	private static void writeln(String msg) {
		// Terminal.getTerminal().writeln(msg);
	}

	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
		CyclicSchedule schedule = new CyclicSchedule(frames);
		frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0),
				handlers);
		return schedule;
	}

	public void initialize() {
		new WordHandler(100000, "HelloWorld.\n", 1);
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 * 
	 * @return the amount of memory needed
	 */
	// @Override
	public long missionMemorySize() {
		return 400000;
	}

	public void setUp() {
		Terminal.getTerminal().write("setUp.\n");
	}

	public void tearDown() {
		Terminal.getTerminal().write("teardown.\n");
	}

	public void cleanUp() {
		Terminal.getTerminal().write("cleanUp.\n");
	}

	public class WordHandler extends PeriodicEventHandler {

		private int count_;

		private WordHandler(long psize, String name, int count) {
			super(null, null, new StorageParameters(psize, 0 , 0), name);
			count_ = count;
		}

		public void handleAsyncEvent() {
			Terminal.getTerminal().write(getName());

			if (count_-- == 0)
				getCurrentMission().requestSequenceTermination();
		}

		public void cleanUp() {
		}

		public StorageParameters getThreadConfigurationParameters() {
			return null;
		}
	}
>>>>>>> /tmp/HelloWorld.java~other.TSSqW4

<<<<<<< /home/wang/fivm/scj/oSCJ/examples/helloworld/src/HelloWorld.java

=======
}
>>>>>>> /tmp/HelloWorld.java~other.TSSqW4
