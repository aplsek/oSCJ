
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

public class HelloWorld extends CyclicExecutive {

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
        Terminal.getTerminal().writeln(msg);
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0), handlers);
        return schedule;
    }

    public void initialize() {
        new WordHandler(20000, "HelloWorld.\n", 1);
    }

    /**
     * A method to query the maximum amount of memory needed by this mission.
     * 
     * @return the amount of memory needed
     */
    // @Override
    public long missionMemorySize() {
        return 100000;
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
 
        private Foo myFoo;
        
        private WordHandler(long psize, String name, int count) {
            super(null, null, null, psize, name);
            count_ = count;
            
            myFoo = new Foo();
        }

        /**
         * 
         * Testing Enter Private Memory
         * 
         */
        public void handleEvent() {
           Terminal.getTerminal().write(getName());
          
           foobar();
           
           if (count_-- == 0)
               getCurrentMission().requestSequenceTermination();
        }

        private int foobar() {
        	foo1(new Foo());
        	
        	return 0;
        }
        
        
        public Foo foo1(Foo f) {
   		   f.crp=f;
   		 
   		   return null;
   	     }
   	
        
        public void cleanUp() {
        }

    
        public StorageParameters getThreadConfigurationParameters() {
            return null;
        }
    }



}

class Foo {
	Foo crp;
}
