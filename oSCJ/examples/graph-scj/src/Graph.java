
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
//import java.util.*;
//import java.text.*;

public class Graph extends CyclicExecutive {

    public Graph() {
        super(null);
    }

    public static void main(final String[] args) {
        Safelet safelet = new Graph();
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
        new WordHandler(200000, "HelloWorld.\n", 1);
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
 
        private WordHandler(long psize, String name, int count) {
            super(null, null, new StorageParameters(psize, 0 , 0), name);
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

        public void handleAsyncEvent() {
	    //	    Terminal.getTerminal().write(getName());
	    long startTime=System.nanoTime();

	    Node node1 = new Node(1, 2);
	    Node node2 = new Node(2, 2);
	    Node node3 = new Node(3, 1);
	    //	    Node node4 = new Node(4, 2);
	    //	    Node node5 = new Node(5, 2);
	    //	    Node node6 = new Node(6, 2);
	    //	    Node node7 = new Node(7, 2);
	    //	    Node node8 = new Node(8, 2);
	    //	    Node node9 = new Node(9, 2);
	    //	    Node node10 = new Node(10, 2);
	    //	    Node node11 = new Node(11, 2);
	    //	    Node node12 = new Node(12, 2);
	    //	    Node node13 = new Node(13, 2);
	    //	    Node node14 = new Node(14, 2);
	    //	    Node node15 = new Node(15, 2);
	    //	    Node node16 = new Node(16, 2);
	    //	    Node node17 = new Node(17, 2);
	    //	    Node node18 = new Node(18, 2);
	    //	    Node node19 = new Node(19, 2);
	    //	    Node node20 = new Node(20, 2);
	    //	    Node node21 = new Node(21, 2);
	    //	    Node node22 = new Node(22, 2);
	    //	    Node node23 = new Node(23, 2);
	    //	    Node node24 = new Node(24, 2);
	    //	    Node node25 = new Node(25, 2);
	    //	    Node node26 = new Node(26, 2);
	    //	    Node node27 = new Node(27, 2);
	    //	    Node node28 = new Node(28, 2);
	    //	    Node node29 = new Node(29, 2);
	    //	    Node node30 = new Node(30, 2);
	    //	    Node node31 = new Node(31, 2);
	    //  Node node32 = new Node(32, 2);                                                     
	    //  Node node33 = new Node(33, 2);                                                                            
	    //  Node node34 = new Node(34, 2);                                                                             
	    //  Node node35 = new Node(35, 2);                                                                            
	    //  Node node36 = new Node(36, 2);                                                                          
	    //  Node node37 = new Node(37, 2);                                                       
	    //  Node node38 = new Node(38, 2);                          
	    //  Node node39 = new Node(39, 2);      
	    //  Node node40 = new Node(40, 2);                    

	    node1.neighbors[0] = node2;
	    node1.neighbors[1] = node3;
	    node2.neighbors[0] = node1;
	    node2.neighbors[1] = node3;
	    node3.neighbors[0] = node2;
	    //node4.neighbors[0] = node2;
	    //node4.neighbors[1] = node3;
	    //node5.neighbors[0] = node3;
	    //node5.neighbors[1] = node4;
	    //node6.neighbors[0] = node4;
	    //node6.neighbors[1] = node5;
	    //node7.neighbors[0] = node5;
	    //node7.neighbors[1] = node6;
	    //node8.neighbors[0] = node6;
	    //node8.neighbors[1] = node7;
	    //node9.neighbors[0] = node7;
	    //node9.neighbors[1] = node8;
	    //node10.neighbors[0] = node8;
	    //node10.neighbors[1] = node9;
	    //node11.neighbors[0] = node9;
	    //node11.neighbors[1] = node10;
	    //node12.neighbors[0] = node10;
	    //node12.neighbors[1] = node11;
	    //node13.neighbors[0] = node11;
	    //node13.neighbors[1] = node12;
	    // node14.neighbors[0] = node12;
	    //node14.neighbors[1] = node13;
	    //node15.neighbors[0] = node13;
	    //node15.neighbors[1] = node14;
	    //node16.neighbors[0] = node14;
	    //node16.neighbors[1] = node15;
	    //node17.neighbors[0] = node15;
	    //node17.neighbors[1] = node16;
	    //node18.neighbors[0] = node16;
	    //node18.neighbors[1] = node17;
	    //node19.neighbors[0] = node17;
	    //node19.neighbors[1] = node18;
	    //node20.neighbors[0] = node18;
	    //node20.neighbors[1] = node19;
	    //node21.neighbors[0] = node19;
	    //node21.neighbors[1] = node20;
	    //node22.neighbors[0] = node20;
	    //node22.neighbors[1] = node21;
	    //node23.neighbors[0] = node21;
	    //node23.neighbors[1] = node22;
	    //node24.neighbors[0] = node22;
	    //node24.neighbors[1] = node23;
	    //node25.neighbors[0] = node23;
	    //node25.neighbors[1] = node24;
	    //node26.neighbors[0] = node24;
	    //node26.neighbors[1] = node25;
	    //node27.neighbors[0] = node25;
	    //node27.neighbors[1] = node26;
	    //node28.neighbors[0] = node26;
	    //node28.neighbors[1] = node27;
	    //node29.neighbors[0] = node27;
	    //node29.neighbors[1] = node28;
	    //node30.neighbors[0] = node28;
	    //node30.neighbors[1] = node29;
	    //node31.neighbors[0] = node29;
	    //node31.neighbors[1] = node30;
	    //node32.neighbors[0] = node30;                                                                                 
	    //node32.neighbors[1] = node31;                                                                                     
	    //node33.neighbors[0] = node31;                                                                                 
	    //node33.neighbors[1] = node32;                                                                                     
	    //node34.neighbors[0] = node32;                                                                                      
	    //node34.neighbors[1] = node33;                 
	    //node35.neighbors[0] = node33;                                                                                                
	    //node35.neighbors[1] = node34;                                                                                                
	    //node36.neighbors[0] = node34;                                                                                    
	    //node36.neighbors[1] = node35;                                                                                   
	    //node37.neighbors[0] = node35;                                                                                   
	    //node37.neighbors[1] = node36;                                                                                   
	    //node38.neighbors[0] = node36;                                                                                     
	    //node38.neighbors[1] = node37;                                                                              
	    //node39.neighbors[0] = node37;                                                                                     
	    //node39.neighbors[1] = node38;                                                                                 
	    //node40.neighbors[0] = node38;                                                                                                 
	    //node40.neighbors[1] = node39;     
	    for(int i=0; i< node1.count; i++)
                {   System.out.println("node"+node1.id+"<--"+"node"+node1.neighbors[i].id); }
            for(int i=0; i< node2.count; i++)
                {   System.out.println("node"+node2.id+"<--"+"node"+node2.neighbors[i].id); }
            for(int i=0; i< node3.count; i++)
                {   System.out.println("node"+node3.id+"<--"+"node"+node3.neighbors[i].id); }
            //for(int i=0; i< node4.count; i++)
            //    {   System.out.println("node"+node4.id+"<--"+"node"+node4.neighbors[i].id); }
            //for(int i=0; i< node5.count; i++)
	    //                {   System.out.println("node"+node5.id+"<--"+"node"+node5.neighbors[i].id); }
            //for(int i=0; i< node6.count; i++)
            //    {   System.out.println("node"+node6.id+"<--"+"node"+node6.neighbors[i].id); }
            //for(int i=0; i< node7.count; i++)
	    //   {   System.out.println("node"+node7.id+"<--"+"node"+node7.neighbors[i].id); }
            //for(int i=0; i< node8.count; i++)
            //    {   System.out.println("node"+node8.id+"<--"+"node"+node8.neighbors[i].id); }
            //for(int i=0; i< node9.count; i++)
            //    {   System.out.println("node"+node9.id+"<--"+"node"+node9.neighbors[i].id); }
            //for(int i=0; i< node10.count; i++)
            //    {   System.out.println("node"+node10.id+"<--"+"node"+node10.neighbors[i].id); }
            //for(int i=0; i< node11.count; i++)
	    //   {   System.out.println("node"+node11.id+"<--"+"node"+node11.neighbors[i].id); }
	    //for(int i=0; i< node12.count; i++)
	    //  {   System.out.println("node"+node12.id+"<--"+"node"+node12.neighbors[i].id); }
            //for(int i=0; i< node13.count; i++)
            //    {   System.out.println("node"+node13.id+"<--"+"node"+node13.neighbors[i].id); }
            //for(int i=0; i< node14.count; i++)
	    //   {   System.out.println("node"+node14.id+"<--"+"node"+node14.neighbors[i].id); }
            //for(int i=0; i< node15.count; i++)
            //    {   System.out.println("node"+node15.id+"<--"+"node"+node15.neighbors[i].id); }
	    // for(int i=0; i< node16.count; i++)
	    //   {   System.out.println("node"+node16.id+"<--"+"node"+node16.neighbors[i].id); }
            //for(int i=0; i< node17.count; i++)
            //    {   System.out.println("node"+node17.id+"<--"+"node"+node17.neighbors[i].id); }
            //for(int i=0; i< node18.count; i++)
	    //   {   System.out.println("node"+node18.id+"<--"+"node"+node18.neighbors[i].id); }
            //for(int i=0; i< node19.count; i++)
            //    {   System.out.println("node"+node19.id+"<--"+"node"+node19.neighbors[i].id); }
            //for(int i=0; i< node20.count; i++)
            //    {   System.out.println("node"+node20.id+"<--"+"node"+node20.neighbors[i].id); }
	    //for(int i=0; i< node21.count; i++)
	    //	{   System.out.println("node"+node21.id+"<--"+"node"+node21.neighbors[i].id); }
	    //for(int i=0; i< node22.count; i++)
	    //	{   System.out.println("node"+node22.id+"<--"+"node"+node22.neighbors[i].id); }
	    //for(int i=0; i< node23.count; i++)
	    //	{   System.out.println("node"+node23.id+"<--"+"node"+node23.neighbors[i].id); }
	    //for(int i=0; i< node24.count; i++)
	    //	{   System.out.println("node"+node24.id+"<--"+"node"+node24.neighbors[i].id); }
	    //for(int i=0; i< node25.count; i++)
	    //	{   System.out.println("node"+node25.id+"<--"+"node"+node25.neighbors[i].id); }
	    //for(int i=0; i< node26.count; i++)
	    //	{   System.out.println("node"+node26.id+"<--"+"node"+node26.neighbors[i].id); }
	    //for(int i=0; i< node27.count; i++)
	    //	{   System.out.println("node"+node27.id+"<--"+"node"+node27.neighbors[i].id); }
	    //for(int i=0; i< node28.count; i++)
	    //	{   System.out.println("node"+node28.id+"<--"+"node"+node28.neighbors[i].id); }
	    //for(int i=0; i< node29.count; i++)
	    //	{   System.out.println("node"+node29.id+"<--"+"node"+node29.neighbors[i].id); }
	    //for(int i=0; i< node30.count; i++)
	    //	{   System.out.println("node"+node30.id+"<--"+"node"+node30.neighbors[i].id); }
	    //for(int i=0; i< node31.count; i++)
	    //	{   System.out.println("node"+node31.id+"<--"+"node"+node31.neighbors[i].id); }
	    //for(int i=0; i< node32.count; i++)                                                                            
	    // {   System.out.println("node"+node32.id+"<--"+"node"+node32.neighbors[i].id); }                                     
	    //for(int i=0; i< node33.count; i++)                                                                                  
	    //         {   System.out.println("node"+node33.id+"<--"+"node"+node33.neighbors[i].id); }                     
	    //for(int i=0; i< node34.count; i++)                                                                                  
	    //  {   System.out.println("node"+node34.id+"<--"+"node"+node34.neighbors[i].id); }                                 
	    //for(int i=0; i< node35.count; i++)                                                                                            
	    //          {   System.out.println("node"+node35.id+"<--"+"node"+node35.neighbors[i].id); }                                 
	    //for(int i=0; i< node36.count; i++)                                                                                  
	    //          {   System.out.println("node"+node36.id+"<--"+"node"+node36.neighbors[i].id); }                         
	    //for(int i=0; i< node37.count; i++)                                                                               
	    //          {   System.out.println("node"+node37.id+"<--"+"node"+node37.neighbors[i].id); }                    
	    //for(int i=0; i< node38.count; i++)                                                                                
	    //  {   System.out.println("node"+node38.id+"<--"+"node"+node38.neighbors[i].id); }                                 
	    //for(int i=0; i< node39.count; i++)                                                                          
	    //  {   System.out.println("node"+node39.id+"<--"+"node"+node39.neighbors[i].id); }                               
	    //for(int i=0; i< node40.count; i++)                                                                                       
	    //          {   System.out.println("node"+node40.id+"<--"+"node"+node40.neighbors[i].id); }           
	    long estimatedTime=System.nanoTime()-startTime;
	    Terminal.getTerminal().write("Running Time:"+estimatedTime+"us.\n");
               if (count_-- == 0)
               getCurrentMission().requestSequenceTermination();
        }
        public void cleanUp(){
	}
         public StorageParameters getThreadConfigurationParameters() {
            return null;
        }
    }

}



