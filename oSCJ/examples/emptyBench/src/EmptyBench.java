
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

import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;

import bench.BenchMem;
import bench.Constants;

public class EmptyBench extends CyclicExecutive {

    public EmptyBench() {
        super(null);
    }

    public static void main(final String[] args) {
        Safelet safelet = new EmptyBench();
        safelet.setUp();
        safelet.getSequencer().start();
        safelet.tearDown();
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0), handlers);
        return schedule;
    }

    public void initialize() {
        new WordHandler(Constants.TRANSIENT_DETECTOR_SCOPE_SIZE, null, 1);
    }

    /**
     * A method to query the maximum amount of memory needed by this mission.
     * 
     * @return the amount of memory needed
     */
    // @Override
    public long missionMemorySize() {
        return Constants.PERSISTENT_DETECTOR_SCOPE_SIZE;
    }

    public void setUp() {     
    }

    public void cleanUp() {
    }
    
    
    
    public void tearDown() {
        BenchMem.dumpMemoryUsage();
    }


    
    public class WordHandler extends PeriodicEventHandler {

        private WordHandler(long psize, String name, int count) {
	    super(null, null, new StorageParameters(psize, 0 , 0), name);
        }

        public void handleAsyncEvent() {
            for (int i=0; i < BenchMem.maxTraces;i++)
                BenchMem.setMemUsage(RealtimeThread.getCurrentMemoryArea().memoryConsumed());
            
            getCurrentMission().requestSequenceTermination();
        }
        
        public void cleanUp() {
        }

	//	        public void register() {
	// }
        
        public StorageParameters getThreadConfigurationParameters() {
            return null;
        }
    }
}
