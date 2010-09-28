package micro;

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

import bench.Benchmark;
import bench.NanoClock;

public class MicroBench extends CyclicExecutive {

    public MicroBench() {
        super(null);
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(Constants.PERIOD, 0), handlers);
        return schedule;
    }

    public void initialize() {
        new WordHandler(Constants.PRIVATE_MEMORY, null, Constants.RUNS);
    }

    /**
     * A method to query the maximum amount of memory needed by this mission.
     * 
     * @return the amount of memory needed
     */
    // @Override
    public long missionMemorySize() {
        return Constants.MISSION_MEMORY;
    }

    public void setUp() {  
    	Benchmark.init();
    	System.out.println("MicroBenchmark - setup DONE.");
    }

    public void tearDown() {
    	Benchmark.dumpResults();
    }

    public void cleanUp() {
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
        public void handleEvent() {
           
           long start = NanoClock.now();
           for (int i=0 ; i < Constants.MAX ; i++) {
        	   generate();
           }
           long  end =  NanoClock.now(); 
           Benchmark.set(start, end);
           
           
           if (count_-- == 0)
               getCurrentMission().requestSequenceTermination();
               
        }

        
        public void cleanUp() {
        }

    
        public StorageParameters getThreadConfigurationParameters() {
            return null;
        }

        
        //protected byte[] callsigns;
        protected float t;
        
        private void generate() {
        	
        	byte[] callsigns = new byte[Constants.NUMBER_OF_PLANES*6];
        	
        	RawFrame result=new RawFrame();
            for (byte k=0;k<Constants.NUMBER_OF_PLANES;k++) {
                callsigns[6*k]=112;
                callsigns[6*k+1]=108;
                callsigns[6*k+2]=97;
                callsigns[6*k+3]=110;
                callsigns[6*k+4]=101;
                callsigns[6*k+5]=(byte)(49+k);
            }
            float positions[] = new float[60*3];
            
            for (int k=0;k<Constants.NUMBER_OF_PLANES/2;k++) {
                positions[3*k]=(float)(100*Math.cos(t)+500+50*k);
                positions[3*k+1]=100.0f;
                positions[3*k+2]=5.0f;
                positions[Constants.NUMBER_OF_PLANES/2*3+3*k]=(float)(100*Math.sin(t)+500+50*k);
                positions[Constants.NUMBER_OF_PLANES/2*3+3*k+1]=100.0f;
                positions[Constants.NUMBER_OF_PLANES/2*3+3*k+2]=5.0f;
            }
            // increase the time
            t=t+0.25f;
            //result.copy(null,callsigns,positions);
        }
        
    }
}
